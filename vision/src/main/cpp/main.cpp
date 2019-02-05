#include <iostream>
#include <string>
#include <opencv2/core/core_c.h>
#include <opencv2/highgui/highgui.hpp>
#include <opencv2/videoio/videoio.hpp>

std::string get_tegra_pipeline(int width, int height, int fps) {
    return "nvcamerasrc ! video/x-raw(memory:NVMM), width=(int)" + std::to_string(width) + ", height=(int)" +
           std::to_string(height) + ", format=(string)I420, framerate=(fraction)" + std::to_string(fps) +
           "/1 ! nvvidconv flip-method=0 ! video/x-raw, format=(string)BGRx ! videoconvert ! video/x-raw, format=(string)BGR ! appsink";
}

int main() {
    // Options
    int WIDTH = 1920;
    int HEIGHT = 1080;
    int FPS = 30;

    // Define the gstream pipeline
    std::string pipeline = get_tegra_pipeline(WIDTH, HEIGHT, FPS);

    // Create OpenCV capture object, ensure it works.
    cv::VideoCapture cap(pipeline, cv::CAP_GSTREAMER);
    if (!cap.isOpened()) {
        std::cout << "Connection failed"  << std::endl;
        return -1;
    }
	
	std::cout << "Connection is good" << std::endl;

    // View video
    cv::Mat frame;
	cap >> frame;  // Get a new frame from camera

	// Display frame
	std::cout << "Saving image" << std::endl;
	imwrite( "./out.jpg", frame);
}