import cv2
import numpy as np
import math

from process_cargo import *

"""
a function used to get the distance and angle from a contour
it uses info about the camera, the attribues of the contour in real world size
then it uses trigonometry to find the approximate angle and distance from the target

params:
1) contour - the countor to find the process
2) real_width - the real width of the contour (the width of the object in real life)
3) camera_view_angle - the view angle of the camera (in degrees)
4) camera_view_width - the width of the camera feed (in pixels)
return: 
1) distance from the target
2) angle from the target
"""
"""
def get_dis_angle(contour,real_width,camera_view_angle,camera_view_width):
    x,y,w,h = cv2.boundingRect(contour)
    distance_from_image_center = ((camera_view_width/2) - (x + w/2))
    angle_to_turn = 0
    distance_from_target = 0
    if distance_from_image_center != 0:
        angle_to_turn = camera_view_angle/(camera_view_width/distance_from_image_center)
        distance_from_target = abs(((distance_from_image_center*real_width)/w)/math.tan(angle_to_turn))
    return distance_from_target,angle_to_turn
"""


"""
a function used to detect the orange ball called as 'Cargo'
the function uses hsv thresholding and dilate/findContours functions 
in order to find all of the orange contours. Then it choose the biggest contour found
and returns it as the cargo.

params:
1) frame - the frame to process
return: 
1) the countor found as the cargo 
"""
"""
a function used to accept a frame and a countor, then it draws the contour's outline
and puts a rectangle around it

params:
1) frame - the frame to draw on
2) contour - the contour to draw on the frame

return: None
"""
def process():
    CAMERA_VIEW_ANGLE = math.radians(50)
    CARGO_WIDTH = 33 # CM    
    CAMERA = cv2.VideoCapture(0)
    while True: # work until break
        ret_val,img = CAMERA.read() # get frame from camera
        if ret_val: # check if frame exists
            c =  detect_cargo(img) # find the cargo's contour
            if c is not None:
                distance = get_cargo_abs_dis(c,CARGO_WIDTH, CAMERA_VIEW_ANGLE, img.shape[1]) # get distance and angle from cargo
                x, y, w, h = cv2.boundingRect(c)
                print (img.shape[0])
                angle = get_angle_2_points( x , x+ w, CAMERA_VIEW_ANGLE, img.shape[1])
                print(distance)
                #print(math.degrees(angle))
                draw_contour(img, c) # draw contour
                # TODO send contour to roborio or do something
            cv2.imshow('original', img)
            while True:
                if cv2.waitKey(1) == 27: 
                    break  # esc to quit
    cv2.destroyAllWindows()

def main():
    process()

if __name__ == '__main__':
    main()
