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

using counter_t = std::vector<cv::Point>;

float get_angle_from_center(float point, float camera_field_of_view, int camera_view_width);

float get_angle_2_points(float p1, float p2, float camera_field_of_view, int camera_view_width);

float get_cargo_abs_dis(counter_t counters, float real_width, float camera_view_angle, int camera_view_width);

std::vector<counter_t> detect_all_contours_by_color(cv::Mat& frame, std::array<uint8_t, 3> &lower, std::array<uint8_t, 3>& upper);

counter_t detect_cargo(cv::Mat & frame);