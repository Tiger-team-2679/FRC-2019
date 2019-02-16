#pragma once
#include <opencv2/core/core.hpp>
#include <opencv2/highgui/highgui.hpp>
#include <opencv2/videoio/videoio.hpp>
#include <opencv2/imgproc/imgproc.hpp>
#include <opencv2/core/types.hpp>

#include <algorithm>
#include <vector>
#include <array>
#include <cmath>
#include <optional>

using contour_t = std::vector<cv::Point>;

float get_angle_from_center(float point, float camera_field_of_view, int camera_view_width);

float get_angle_2_points(float p1, float p2, float camera_field_of_view, int camera_view_width);

float get_cargo_abs_dis(contour_t contour);

std::vector<contour_t> detect_all_contours_by_color(cv::Mat& frame, std::array<uint8_t, 3> &lower, std::array<uint8_t, 3>& upper);

contour_t detect_cargo(cv::Mat & frame);


std::array<contour_t, 2> detect_2_strips(cv::Mat& frame);


// ALERT: this function may chage the order of the elements in the input vector!
template<class T>
std::optional<std::array<size_t, 2>> find_items_in_range(std::vector<T> &list, T range)
{
	std::sort(list.begin(), list.end());

	for(size_t i = 0; i < list.size() - 1; i++)
	{
		if ((list[i] <= list[i+1] + range) && (list[i] >= list[i+1] - range))
		{
			return std::array{ i, i + 1 };
		}
	}
	return {};
}

float get_strip_abs_dis(contour_t contour);

std::array<float, 2> get_cords(contour_t contour, float abs_dis);