# coding: utf-8
import cv2
import os
import numpy as np

def getCascadeName(num): #カスケードファイル名
	return "C:\\Users\\Shohei\\Documents\\sotsuken\\cas\\testCascade\\cascade" + str(num).zfill(2) + ".xml"

def getMarkDir(num):
	if num ==0 :
		return "C:\\Users\\Shohei\\Documents\\sotsuken\\mark\\sonota"
	elif num == 1 :
		return "C:\\Users\\Shohei\\Documents\\sotsuken\\mark\\maru"
	elif num == 2 :
		return "C:\\Users\\Shohei\\Documents\\sotsuken\\mark\\maru2"
	elif num == 3 :
		return "C:\\Users\\Shohei\\Documents\\sotsuken\\mark\\sankaku"
	elif num == 4 :
		return "C:\\Users\\Shohei\\Documents\\sotsuken\\mark\\sankaku2"
	elif num == 5 :
		return "C:\\Users\\Shohei\\Documents\\sotsuken\\mark\\shikaku"
	elif num == 6 :
		return "C:\\Users\\Shohei\\Documents\\sotsuken\\mark\\shikaku2"


startNum = 59
endNum = 61
for k in range(7):
	files = os.listdir(getMarkDir(k))
	if "Thumbs.db" in files: files.remove("Thumbs.db")
	for j in range(startNum,endNum+1):
		cascade = cv2.CascadeClassifier(getCascadeName(j))
		print(getMarkDir(k).replace("C:\\Users\\Shohei\\Documents\\sotsuken\\mark\\","")+" cascade"+ str(j).zfill(2))
		for i in range(len(files)):
			print(files[i])
			count=0
			result=0
			for n in range(2197):
				img = cv2.imread(getMarkDir(k)+"\\"+files[i]+"\\"+str(count).zfill(4)+".png",cv2.IMREAD_GRAYSCALE)
				minW = int(img.shape[1]/2)
				minH = int(img.shape[0]/2)
				w = img.shape[1]/3
				h = img.shape[0]/3
				ww = int(img.shape[1]*2/3)
				hh = int(img.shape[0]*2/3)
				facerect = cascade.detectMultiScale(img, scaleFactor=1.1, minNeighbors=1, minSize=(ww,hh))
				#facerect = cascade.detectMultiScale(img, scaleFactor=1.1, minNeighbors=1, minSize=(0,0))
				if len(facerect) > 0:
					flag = 0
					for rect in facerect:
						#if rect[0:2][0] < w and rect[0:2][1] < h and rect[2:4][0] > ww and rect[2:4][1] > hh :
							flag = 1
							#cv2.rectangle(img, tuple(rect[0:2]),tuple(rect[0:2]+rect[2:4]),125, thickness=2)
					if flag == 1:
						result += 1 
				"""
				cv2.imshow("detected.jpg", img)
				cv2.waitKey(0)
				"""
				count+=1							
			print(str(result/count*100))
