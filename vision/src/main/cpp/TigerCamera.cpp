#include "TigerCamera.h"

TigerCamera::TigerCamera(cv::VideoCapture * cap) {
    this->_cap = cap;
    this->_isRunning = false;
}

void TigerCamera::start() {
    this->_isRunning = true;
	this->_currently_used_mat = cv::Mat(HEIGHT, WIDTH, CV_8UC3);
	this->_currently_written_mat = cv::Mat(HEIGHT, WIDTH, CV_8UC3);
	this->_free_mat = cv::Mat(HEIGHT, WIDTH, CV_8UC3);
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
			this->_mutex.lock();

			this->_newExist = true;
			std::swap(this->_free_mat, this->_currently_written_mat);

			this->_mutex.unlock();
			*this->_cap >> this->_currently_written_mat;
        }
        catch (std::exception &e){
            std::cerr << "TigerCamera Exception: " << std::endl << e.what() << std::endl;
        }
    }
}

cv::Mat TigerCamera::get_latest_frame() {
    _mutex.lock();
	if (!this->_newExist)
	{
		_mutex.unlock();
		return cv::Mat(); // return empty mat
	}
	_newExist = false;
	std::swap(this->_currently_used_mat, this->_free_mat);
    _mutex.unlock();
	return this->_currently_used_mat;
}
