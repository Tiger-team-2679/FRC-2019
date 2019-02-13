#pragma once

#include <string>
#include <exception>
#include <iostream>
#include <mutex>
#include <thread>

#include <opencv2/videoio/videoio.hpp>

class TigerCamera {
public:
    explicit TigerCamera(cv::VideoCapture * cap);
    void on_update();
    void start();
    void stop();
    cv::Mat get_latest_frame();
private:
    cv::VideoCapture * _cap;
    cv::Mat _frame;
    std::thread _thread;
    std::mutex _mutex;
    bool _isRunning;
};