#pragma once
constexpr float PI = 3.14159265358979;

inline constexpr float rad(float degree) {
	return degree * PI / 180;
}