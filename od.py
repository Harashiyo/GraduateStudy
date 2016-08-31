# coding: utf-8
import cv2
import numpy as np

def getFileName(num): #入ファイル名
	return "C:\\Users\\Shohei\\Documents\\sotsuken\\mark\\tokuho\\" + str(num).zfill(4)+".png"

def getCascadeName(num): #カスケードファイル名
	return "C:\\Users\\Shohei\\Documents\\sotsuken\\cas\\testCascade\\cascade" + str(num).zfill(2) + ".xml"

for j in range(1,2):
	count=0
	cascade = cv2.CascadeClassifier(getCascadeName(j))
	for i in range(2000):
		img = cv2.imread(getFileName(i),cv2.IMREAD_GRAYSCALE)
		#min = tuple(np.array([int(img.shape[1]*2/3), int(img.shape[0]*2/3)])
		minW = int(img.shape[1]/2)
		minH = int(img.shape[0]/2)
		#minW = 1
		#minH = 1
		#(width,height)
		w = img.shape[1]/3
		h = img.shape[0]/3
		ww = img.shape[1]*2/3
		hh = img.shape[0]*2/3
		facerect = cascade.detectMultiScale(img, scaleFactor=1.1, minNeighbors=1, minSize=(minW,minH))
		if len(facerect) > 0:
			flag = 0
			for rect in facerect:
				#if rect[0:2][0] < w and rect[0:2][1] < h and rect[2:4][0] > ww and rect[2:4][1] > hh :
					flag = 1
					cv2.rectangle(img, tuple(rect[0:2]),tuple(rect[0:2]+rect[2:4]),125, thickness=2)
			if flag == 1:
				count += 1 
		
		cv2.imshow("detected.jpg", img)
		cv2.waitKey(0)
		
	print("cascade"+ str(j).zfill(2))
	print(str(count/20)+"%")