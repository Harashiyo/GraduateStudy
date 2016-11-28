# coding: utf-8
import cv2
import numpy as np
import time

def getFileName1(): #入ファイル名
	return "C:\\Users\\Shohei\\Desktop\\steel-aluminum-mark8.jpg"

def getDirNum(num):
	if num == 0:
		return "15"
	elif num == 1:
		return "25"
	elif num == 2:
		return "35"
	elif num == 3:
		return "50"

def getCascadeName(num,name): #カスケードファイル名
	return "C:\\Users\\Shohei\\Desktop\\CreateSample\\cascade\\" + name + "\\" + getDirNum(num) + "\\cascade.xml"

startNum = 0
endNum = 4

target = "alumi"

for j in range(startNum,endNum):
	cascade = cv2.CascadeClassifier(getCascadeName(j,target))
	img = cv2.imread(getFileName1(),0)
	dst = 255 - img
	ww = int(img.shape[1]*2/3)
	hh = int(img.shape[0]*2/3)
	facerect = cascade.detectMultiScale(img, scaleFactor=1.1, minNeighbors=1, minSize=(0,0))
	if len(facerect) > 0:
		for rect in facerect:
			cv2.rectangle(img, tuple(rect[0:2]),tuple(rect[0:2]+rect[2:4]),125, thickness=2)
	#cv2.imshow("detected.jpg", img)
	#cv2.waitKey(0)
	facerect = cascade.detectMultiScale(dst, scaleFactor=1.1, minNeighbors=1, minSize=(0,0))
	if len(facerect) > 0:
		for rect in facerect:
			cv2.rectangle(dst, tuple(rect[0:2]),tuple(rect[0:2]+rect[2:4]),125, thickness=2)
	cv2.imshow("detected.jpg", dst)
	cv2.waitKey(0)