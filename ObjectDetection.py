# coding: utf-8
import cv2
import numpy as np

def getFileName(num): #出力ファイル名
	return "pra0/" + str(num).zfill(4)+".png"

def getCascadeName(num): #カスケードファイル名
	return "cascade" + str(num).zfill(2) + ".xml"

for j in range(4,5):
	count=0
	cascade = cv2.CascadeClassifier(getCascadeName(j))
	for i in range(2000):
		img = cv2.imread(getFileName(i),cv2.IMREAD_GRAYSCALE)
		facerect = cascade.detectMultiScale(img, scaleFactor=1.1, minNeighbors=1, minSize=(1,1))
		if len(facerect) == 1:
			count += 1
		'''
			for rect in facerect:
				cv2.rectangle(img, tuple(rect[0:2]),tuple(rect[0:2]+rect[2:4]),125, thickness=2)
		else:
			print("no face")
		cv2.imshow("detected.jpg", img)
		cv2.waitKey(0)
		'''
	print("cascade"+ str(j).zfill(2))
	print(str(count/20)+"%")