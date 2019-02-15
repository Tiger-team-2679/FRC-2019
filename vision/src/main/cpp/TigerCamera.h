#pragma once

#include <string>
#include <exception>
#include <iostream>
#include <mutex>
#include <thread>
#include <array>
#include <utility>

#include <opencv2/videoio/videoio.hpp>
constexpr int WIDTH = 640;
constexpr int HEIGHT = 480;
constexpr int FPS = 30;
class TigerCamera {
public:
    explicit TigerCamera(cv::VideoCapture * cap);
    void on_update();
    void start();
    void stop();
    cv::Mat get_latest_frame();
private:
    cv::VideoCapture * _cap;
    std::thread _thread;
    std::mutex _mutex;
	cv::Mat _currently_used_mat;
	cv::Mat _currently_written_mat;
	cv::Mat _free_mat;
	bool _newExist;
    bool _isRunning;
};