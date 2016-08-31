# coding: utf-8
import cv2
import numpy as np

def getFileName(): #入ファイル名
	return "F:\\GS\\image\\maru\\eco.png"

def getCascadeName(num): #カスケードファイル名
	return "C:\\Users\\Shohei\\Desktop\\tokuhoCascade\\cascade" + str(num).zfill(2) + ".xml"

for j in range(3,7):
	count=0
	cascade = cv2.CascadeClassifier(getCascadeName(j))
	img = cv2.imread(getFileName(),cv2.IMREAD_GRAYSCALE)
	#minW = 1
	#minH = 1
	#(width,height)
	facerect = cascade.detectMultiScale(img, scaleFactor=1.1, minNeighbors=1, minSize=(1,1))
	if len(facerect) > 0:
		count += 1
	
		for rect in facerect:
			cv2.rectangle(img, tuple(rect[0:2]),tuple(rect[0:2]+rect[2:4]),125, thickness=2)
	
	cv2.imshow("detected.jpg", img)
	cv2.waitKey(0)
		
	print("cascade"+ str(j).zfill(2))
	print(str(count/20)+"%")