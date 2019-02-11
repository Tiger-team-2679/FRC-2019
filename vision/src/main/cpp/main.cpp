#include <iostream>
#include <string>
#include <opencv2/core/core_c.h>
#include <opencv2/highgui/highgui.hpp>
#include <opencv2/videoio/videoio.hpp>
#include <opencv2/imgproc/imgproc.hpp>

#define WIDTH 1280
#define HEIGHT 720
#define FPS 30
#define CAM_SRC "nvcamerasrc  fpsRange='FPS FPS' ! video/x-raw(memory:NVMM), width=(int)WIDTH, height=(int)HEIGHT,format=(string)I420, framerate=(fraction)FPS/1 ! nvvidconv flip-method=0 ! video/x-raw, format=(string)BGRx ! videoconvert ! video/x-raw, format=(string)BGR ! appsink"

int main() {

    // Define the gstream pipeline
    std::string pipeline = CAM_SRC;

    // Create OpenCV capture object, ensure it works.
    cv::VideoCapture cap(pipeline, cv::CAP_GSTREAMER);
    if (!cap.isOpened()) {
        std::cout << "Connection failed"  << std::endl;
        return -1;
    }

    // View video
    cv::Mat frame;
    cv::namedWindow("cam",1);
    for(;;)
    {
        cap >> frame; // get a new frame from camera
        cv::imshow("cam", frame);
        if(cv::waitKey(30) >= 0) break;
    }
    // the camera will be deinitialized automatically in VideoCapture destructor
    return 0;
}