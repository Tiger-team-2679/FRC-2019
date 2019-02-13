#include "VideoStreamer.h"

VideoStreamer::VideoStreamer(std::string target, int port, int width, int height, int fps) {
    std::string pipe_args = "appsrc ! video/x-raw, format=(string)BGR, " \
    " width=(int)" + std::to_string(width) + "," \
    " height=(int)" + std::to_string(height) + "," \
    " framerate=(fraction)" + std::to_string(fps) + "/1" \
    " ! videoconvert ! omxh265enc bitrate=9000 ! video/x-h265, stream-format=(string)byte-stream" \
    " ! h265parse ! rtph265pay ! udpsink" \
    " host=" + target + \
    " port=" + std::to_string(port) + " ";
    std::cout << pipe_args << std::endl;
    this->pipeline_out.open(pipe_args, 0, fps, cv::Size(width, height), true);
}

VideoStreamer::~VideoStreamer() {
    this->pipeline_out.release();
}

bool VideoStreamer::isOpened() const{
    return this->pipeline_out.isOpened();
}

void VideoStreamer::write(const cv::Mat& mat) {
    this->pipeline_out.write(mat);
}
