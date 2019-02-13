#pragma once

#include <iostream>
#include <string>
#include <opencv2/opencv.hpp>

class VideoStreamer {
public :
    VideoStreamer(std::string target, int port, int width, int height, int fps);
    ~VideoStreamer();
    bool isOpened() const;
    void write(const cv::Mat& mat);
private:
    cv::VideoWriter pipeline_out;
};