import cv2
import numpy as np

h = 0
s = 0
v = 0

img = cv2.imread('subject.jpg')
hsv = cv2.cvtColor(img, cv2.COLOR_BGR2HSV)
height, width, depth = hsv.shape
for i in range(0, height):             #looping at python speed...
	for j in range(0, (width)):     #...
		h = h + hsv[i][j][0]
		s = s + hsv[i][j][1]
		v = v + hsv[i][j][1]
		
h = h/(height*width)
s = s/(height*width)
v = v/(height*width)

print("average:")
print("h - ", h)
print("s - ", s)
print("v - ", v)