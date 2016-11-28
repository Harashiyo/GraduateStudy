# coding: utf-8
import cv2
import numpy as np
import random
import math
import os
name = "sq"
shape = "maru"
im = cv2.imread("C:\\Users\\Shohei\\Documents\\sotsuken\\image\\"+shape+"\\"+name+".png",cv2.IMREAD_GRAYSCALE)
m=max(im.shape[1], im.shape[0])
img = cv2.resize(im,(int(im.shape[1]/m*90),int(im.shape[0]/m*90)))
size = tuple(np.array([img.shape[1], img.shape[0]]))
for i in range(1,255):
	dst = img.copy()
	black = 0
	white = i
	for j in range(size[1]):
		for k in range(size[0]):
			if dst[j, k] < 200:
				dst[j, k] = black
			else:
				dst[j, k] = white
	cv2.imwrite("C:\\Users\\Shohei\\Documents\\sotsuken\\mark\\kido\\"+str(i)+".png",dst)