import cv2
import numpy as np
import math
import collections

def get_contour_dis(contour, real_width, camera_view_angle, camera_view_width):
    x, y, w, h = cv2.boundingRect(contour)
    angle = camera_view_angle / 2
    tribase = real_width * camera_view_width / w / 2
    return tribase / math.tan(angle)

def get_angle_from_center(point , camera_view_angle, camera_view_width):
    dis_from_screen = (camera_view_width / 2) / math.tan(camera_view_angle / 2)
    return math.atan((point - camera_view_width / 2) / dis_from_screen)

def get_angle_2_points(point1, point2, camera_view_angle, camera_view_width):
    return get_angle_from_center(point2, camera_view_angle, camera_view_width) - get_angle_from_center(point1, camera_view_angle, camera_view_width)

def get_cargo_abs_dis(contour, real_width, camera_view_angle, camera_view_width):
    x, y, w, h = cv2.boundingRect(contour)
    angle = get_angle_2_points(x, x + w, camera_view_angle, camera_view_width) / 2
    return (real_width / 2) / math.sin(angle)

def detect_all_contours_by_color(frame, lower, upper):
    frame = cv2.cvtColor(frame, cv2.COLOR_BGR2HSV) # turn GBR TO HSV
    # set lower and upper threshold
    lower = np.array(lower) # H, S, V
    upper = np.array(upper)  # H, S, V
    # find all pixels in the hsv range
    frame = cv2.inRange(frame, lower, upper)
    # use dilate to reduce of noise
    kernel = np.ones((11,11),np.uint8)
    frame = cv2.dilate(frame, kernel, iterations = 1)
    # find all contours
    contours, hierarchy = cv2.findContours(frame, cv2.RETR_TREE, cv2.CHAIN_APPROX_SIMPLE)
    # find the biggest contour
    return contours
def detect_cargo(frame):
    return max(detect_all_contours_by_color(frame, [3,140,50], [7,255,255]), key = cv2.contourArea, default = None)

def find_duplicate(list_of_values):
    value_dict = collections.defaultdict(list)  
    for index, item in enumerate(list_of_values):
        value_dict[item].append(index)
    return [l for k, l in value_dict.items() if len(l)>=2]

def detect_2_strips(frame):
    contour_list = detect_all_contours_by_color(frame, [50, 0, 200], [75, 100, 255])
    dup = find_duplicate([round(cv2.boundingRect(c)[1], -1) for c in contour_list])
    return contour_list[dup[0][0]] , contour_list[dup[0][1]]
	

def draw_contour(frame, contour):
    x,y,w,h = cv2.boundingRect(contour)
    cv2.drawContours(frame, [contour], 0, (255,0,0), 2)
    cv2.rectangle(frame,(x,y),(x+w,y+h), (0,255,0), 2)