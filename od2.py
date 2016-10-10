# coding: utf-8
import cv2
import numpy as np

def getFileName1(num): #入ファイル名
	return "C:\\Users\\Shohei\\Documents\\sotsuken\\mark\\sonota\\tokuteijas\\" + str(num).zfill(4)+".png"


def getCascadeName(num): #カスケードファイル名
	return "C:\\Users\\Shohei\\Documents\\sotsuken\\cas\\testCascade\\cascade" + str(num).zfill(2) + ".xml"

startNum = 3
endNum = 4

print("tokuhonew")
for j in range(startNum,endNum):
	cascade = cv2.CascadeClassifier(getCascadeName(j))
	count=0
	result=0
	for  n in range(2197):
		img = cv2.imread(getFileName1(count),cv2.IMREAD_GRAYSCALE)
		minW = int(img.shape[1]/2)
		minH = int(img.shape[0]/2)
		w = img.shape[1]/3
		h = img.shape[0]/3
		ww = int(img.shape[1]*2/3)
		hh = int(img.shape[0]*2/3)
		facerect = cascade.detectMultiScale(img, scaleFactor=1.1, minNeighbors=1, minSize=(ww,hh))
		if len(facerect) > 0:
			flag = 0
			for rect in facerect:
				#if rect[0:2][0] < w and rect[0:2][1] < h and rect[2:4][0] > ww and rect[2:4][1] > hh :
					flag = 1
					cv2.rectangle(img, tuple(rect[0:2]),tuple(rect[0:2]+rect[2:4]),125, thickness=2)
			if flag == 1:
				result += 1 
		
		cv2.imshow("detected.jpg", img)
		cv2.waitKey(0)
		
		count+=1
	print(str(result/count*100)+"%")
