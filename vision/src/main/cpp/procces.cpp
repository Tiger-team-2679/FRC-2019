#include "procces.h"
#include "camera_properties.h"

float get_angle_from_center(float point, float camera_field_of_view, int camera_view_width)
{
	float dis_from_screen = (camera_view_width / 2) / tan(camera_field_of_view / 2);
	return atan((point - camera_view_width / 2) / dis_from_screen);
}

float get_angle_2_points(float p1, float p2, float camera_field_of_view, int camera_view_width)
{
	return get_angle_from_center(p2, camera_field_of_view, camera_view_width) - get_angle_from_center(p1, camera_field_of_view, camera_view_width);
}

float get_cargo_abs_dis(contour_t contour)
{
	cv::Rect rect= cv::boundingRect(contour);
	float angle = get_angle_2_points(rect.x, rect.x + rect.width, HORIZONTAL_FOV, WIDTH) / 2;
	float angle_sin = sin(angle);
	if(angle_sin == 0){
		return -1.0f;
	}
	return (CARGO_SIZE / 2) / angle_sin;
}

std::vector<contour_t> detect_all_contours_by_color(cv::Mat & frame, std::array<uint8_t, 3>& lower, std::array<uint8_t, 3>& upper)
{
	cv::cvtColor(frame, frame, cv::COLOR_BGR2HSV);
	cv::GaussianBlur(frame, frame, cv::Size(0, 0), 10, 10);
	cv::inRange(frame, lower, upper, frame);
	std::vector<contour_t> counters;
	std::vector<cv::Vec4i> hierarchy;
	cv::findContours(frame, counters, hierarchy, cv::RETR_TREE, cv::CHAIN_APPROX_SIMPLE);
	return counters;
}

contour_t detect_cargo(cv::Mat & frame)
{
	std::vector<contour_t> countours = detect_all_contours_by_color(frame, std::array<uint8_t, 3>{ 3, 140, 50 }, std::array<uint8_t, 3>{ 7, 255, 255 });
	return *std::max_element(
		countours.begin(),
		countours.end(),
		[](const contour_t& c1, const contour_t& c2) -> bool {
			return cv::contourArea(c1) < cv::contourArea(c2);
		}
	);
}



std::array<contour_t, 2> detect_2_strips(cv::Mat & frame)
{
	std::vector<contour_t> contours = detect_all_contours_by_color(frame, std::array<uint8_t, 3>{50, 0, 200}, std::array<uint8_t, 3>{75, 100, 255});
	std::vector<float> rects;
	rects.reserve(contours.size());

	for (contour_t& c: contours)
	{
		rects.push_back(cv::boundingRect(c).y);
	}

	std::optional<std::array<size_t, 2>> indexs = find_items_in_range(rects, 10.0f);

	if (!indexs.has_value()) {
		return {};
	}

	return { contours[(*indexs)[0]], contours[(*indexs)[1]] };
}

float get_strip_abs_dis(contour_t contour)
{
	cv::Rect rect = cv::boundingRect(contour);
	float angle = get_angle_from_center(rect.y, VERTICAL_FOV, HEIGHT);
	return STRIPS_PHISICAL_HEIGHT / tan(angle);
}

std::array<float, 2> get_cords(contour_t contour, float abs_dis)
{
	cv::Rect rect = cv::boundingRect(contour);
	float angle = get_angle_from_center(rect.y, HORIZONTAL_FOV, WIDTH);

	return std::array<float, 2>{sin(angle)*abs_dis, cos(angle)*abs_dis};
}
