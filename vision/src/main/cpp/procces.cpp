#include "procces.h"


float get_angle_from_center(float point, float camera_field_of_view, int camera_view_width)
{
	float dis_from_screen = (camera_view_width / 2) / tan(camera_field_of_view / 2);
	return atan((point - camera_view_width / 2) / dis_from_screen);
}

float get_angle_2_points(float p1, float p2, float camera_field_of_view, int camera_view_width)
{
	return get_angle_from_center(p2, camera_field_of_view, camera_view_width) - get_angle_from_center(p1, camera_field_of_view, camera_view_width);
}

float get_cargo_abs_dis(counter_t counter, float real_width, float camera_field_of_view, int camera_view_width)
{
	cv::Rect rect= cv::boundingRect(counter);
	float angle = get_angle_2_points(rect.x, rect.x + rect.width, camera_field_of_view, camera_view_width) / 2;
	float angle_sin = sin(angle);
	if(angle_sin == 0){
		return -1.0f;
	}
	return (real_width / 2) / angle_sin;
}

std::vector<counter_t> detect_all_contours_by_color(cv::Mat & frame, std::array<uint8_t, 3>& lower, std::array<uint8_t, 3>& upper)
{
	cv::cvtColor(frame, frame, cv::COLOR_BGR2HSV);
	cv::GaussianBlur(frame, frame, cv::Size(0, 0), 10, 10);
	cv::inRange(frame, lower, upper, frame);
	std::vector<counter_t> counters;
	std::vector<cv::Vec4i> hierarchy;
	cv::findContours(frame, counters, hierarchy, cv::RETR_TREE, cv::CHAIN_APPROX_SIMPLE);
	return counters;
}

counter_t detect_cargo(cv::Mat & frame)
{
	std::vector<counter_t> counters = detect_all_contours_by_color(frame, std::array<uint8_t, 3>{ 3, 140, 50 }, std::array<uint8_t, 3>{ 7, 255, 255 });
	return *std::max_element(
		counters.begin(),
		counters.end(),
		[](const counter_t& c1, const counter_t& c2) -> bool {
			return cv::contourArea(c1) < cv::contourArea(c2);
		}
	);
}
