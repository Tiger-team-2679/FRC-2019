#include <iostream>
#include <string>

#include <opencv2/core/core.hpp>
#include <opencv2/highgui/highgui.hpp>
#include <opencv2/videoio/videoio.hpp>
#include <opencv2/imgproc/imgproc.hpp>

#include "VideoStreamer.h"
#include "camera_properties.h"

int main() {
    std::string pipe_args = "nvcamerasrc fpsRange='" + std::to_string(FPS) + " " + std::to_string(FPS) + "' ! video/x-raw(memory:NVMM)" \
                            ", width=(int)" + std::to_string(WIDTH) + \
                            ", height=(int)" + std::to_string(HEIGHT) + ",format=(string)I420, framerate=(fraction)FPS/1 !" \
                            " nvvidconv flip-method=0 ! video/x-raw, format=(string)BGRx ! videoconvert ! video/x-raw," \
                            " format=(string)BGR ! appsink ";
    cv::VideoCapture cap;
    cap.open(pipe_args);
    if(!cap.isOpened()) {
        std::cerr << "Can't create camera reader" << std::endl;
        return -1;
    }

    VideoStreamer driver_station_stream("10.10.2.61", 1234, WIDTH, HEIGHT, FPS);

    if (!driver_station_stream.isOpened()) {
        cap.release();
        std::cerr << "Can't create gstreamer writer." << std::endl;
        return -1;
    }

    cv::Mat frame;

    while (true) {
        try {
            cap >> frame;
            if (frame.empty()) {
                throw;
            }
            driver_station_stream.write(frame);
        } catch (...) {
            std::cout << "Something went wrong" << std::endl;
            break;
        }
    }
    cap.release();
}