#include "TigerCamera.h"

TigerCamera::TigerCamera(cv::VideoCapture * cap) {
    this->_cap = cap;
    this->_isRunning = false;
}

void TigerCamera::start() {
    this->_isRunning = true;
    this->_thread = std::thread(&TigerCamera::on_update, this);
}

void TigerCamera::stop() {
    this->_isRunning = false;
    if(this->_thread.joinable()){
        this->_thread.join();
    }
}

void TigerCamera::on_update() {
    while(this->_isRunning){
        try{
            std::cout << "TigerCamera -> Started" << std::endl;
            while (this->_cap->grab()){
            }
            std::cout << "TigerCamera -> Grabbed Frame" << std::endl;
            this->_mutex.lock();
            this->_cap->retrieve(this->_frame);
            this->_mutex.unlock();
        }
        catch (std::exception &e){
            std::cerr << "TigerCamera Exception: " << std::endl << e.what() << std::endl;
        }
    }
}

cv::Mat TigerCamera::get_latest_frame() {
    cv::Mat tmp;
    _mutex.lock();
    _frame.copyTo(tmp);
    _mutex.unlock();
    return tmp;
}
