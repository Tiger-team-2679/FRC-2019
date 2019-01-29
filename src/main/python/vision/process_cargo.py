import cv2
import numpy as np
import math

def get_dis_angle(contour,real_width,camera_view_angle,camera_view_width):
    x,y,w,h = cv2.boundingRect(contour)
    angle=camera_view_angle/2
    tribase= real_width*camera_view_width/w/2
    return tribase / math.tan(math.radians(angle))

def detect_cargo(frame):
    frame = cv2.cvtColor(frame, cv2.COLOR_BGR2HSV) # turn GBR TO HSV
    # set lower and upper threshold
    lower = np.array([3,140,50]) # H, S, V
    upper = np.array([7,255,255])  # H, S, V
    # find all pixels in the hsv range
    frame = cv2.inRange(frame, lower, upper);
    # use dilate to reduce of noise
    kernel = np.ones((11,11),np.uint8)
    frame = cv2.dilate(frame, kernel, iterations = 1)
    # find all contours
    contours, hierarchy = cv2.findContours(frame, cv2.RETR_TREE, cv2.CHAIN_APPROX_SIMPLE)
    # find the biggest contour
    return max(contours, key = cv2.contourArea, default = None)

def draw_contour(frame, contour):
    x,y,w,h = cv2.boundingRect(contour)
    cv2.drawContours(frame, [contour], 0, (255,0,0), 2)
    cv2.rectangle(frame,(x,y),(x+w,y+h), (0,255,0), 2)

