# coding: utf-8
import cv2
import os
import numpy as np

def getFileName(name): #入ファイル名
	return "C:\\Users\\Shohei\\Documents\\sotsuken\\mark\\sonota\\" + name

def getCascadeName(num): #カスケードファイル名
	return "C:\\Users\\Shohei\\Documents\\sotsuken\\cas\\testCascade\\cascade" + str(num).zfill(2) + ".xml"

files = os.listdir("C:\\Users\\Shohei\\Documents\\sotsuken\\mark\\maru")
files.remove("Thumbs.db")
startNum = 1
endNum = 5
for j in range(startNum,endNum):
	cascade = cv2.CascadeClassifier(getCascadeName(j))
	print("maru cascade"+ str(j).zfill(2))
	for i in range(len(files)):
		count=0
		print(files[i])
		for k in range(2000):
			img = cv2.imread("C:\\Users\\Shohei\\Documents\\sotsuken\\mark\\maru\\" + files[i] + "\\" + str(k).zfill(4)+".png",cv2.IMREAD_GRAYSCALE)
			minW = int(img.shape[1]/2)
			minH = int(img.shape[0]/2)
			w = img.shape[1]/3
			h = img.shape[0]/3
			ww = img.shape[1]*2/3
			hh = img.shape[0]*2/3
			facerect = cascade.detectMultiScale(img, scaleFactor=1.1, minNeighbors=1, minSize=(minW,minH))
			if len(facerect) > 0:
				flag = 0
				for rect in facerect:
					if rect[0:2][0] < w and rect[0:2][1] < h and rect[2:4][0] > ww and rect[2:4][1] > hh :
						flag = 1
						#cv2.rectangle(img, tuple(rect[0:2]),tuple(rect[0:2]+rect[2:4]),125, thickness=2)
				if flag == 1:
					count += 1 
		"""
		cv2.imshow("detected.jpg", img)
		cv2.waitKey(0)
		"""
		print(str(count/20)+"%")

files = os.listdir("C:\\Users\\Shohei\\Documents\\sotsuken\\mark\\maru2")
for j in range(startNum,endNum):
	cascade = cv2.CascadeClassifier(getCascadeName(j))
	print("maru2 cascade"+ str(j).zfill(2))
	for i in range(len(files)):
		count=0
		print(files[i])
		for k in range(2000):
			img = cv2.imread("C:\\Users\\Shohei\\Documents\\sotsuken\\mark\\maru2\\" + files[i] + "\\" + str(k).zfill(4)+".png",cv2.IMREAD_GRAYSCALE)
			minW = int(img.shape[1]/2)
			minH = int(img.shape[0]/2)
			w = img.shape[1]/3
			h = img.shape[0]/3
			ww = img.shape[1]*2/3
			hh = img.shape[0]*2/3
			facerect = cascade.detectMultiScale(img, scaleFactor=1.1, minNeighbors=1, minSize=(minW,minH))
			if len(facerect) > 0:
				flag = 0
				for rect in facerect:
					if rect[0:2][0] < w and rect[0:2][1] < h and rect[2:4][0] > ww and rect[2:4][1] > hh :
						flag = 1
						#cv2.rectangle(img, tuple(rect[0:2]),tuple(rect[0:2]+rect[2:4]),125, thickness=2)
				if flag == 1:
					count += 1 
		"""
		cv2.imshow("detected.jpg", img)
		cv2.waitKey(0)
		"""
		print(str(count/20)+"%")

files = os.listdir("C:\\Users\\Shohei\\Documents\\sotsuken\\mark\\sankaku")
for j in range(startNum,endNum):
	cascade = cv2.CascadeClassifier(getCascadeName(j))
	print("sankaku cascade"+ str(j).zfill(2))
	for i in range(len(files)):
		count=0
		print(files[i])
		for k in range(2000):
			img = cv2.imread("C:\\Users\\Shohei\\Documents\\sotsuken\\mark\\sankaku\\" + files[i] + "\\" + str(k).zfill(4)+".png",cv2.IMREAD_GRAYSCALE)
			minW = int(img.shape[1]/2)
			minH = int(img.shape[0]/2)
			w = img.shape[1]/3
			h = img.shape[0]/3
			ww = img.shape[1]*2/3
			hh = img.shape[0]*2/3
			facerect = cascade.detectMultiScale(img, scaleFactor=1.1, minNeighbors=1, minSize=(minW,minH))
			if len(facerect) > 0:
				flag = 0
				for rect in facerect:
					if rect[0:2][0] < w and rect[0:2][1] < h and rect[2:4][0] > ww and rect[2:4][1] > hh :
						flag = 1
						#cv2.rectangle(img, tuple(rect[0:2]),tuple(rect[0:2]+rect[2:4]),125, thickness=2)
				if flag == 1:
					count += 1 
		"""
		cv2.imshow("detected.jpg", img)
		cv2.waitKey(0)
		"""
		print(str(count/20)+"%")

files = os.listdir("C:\\Users\\Shohei\\Documents\\sotsuken\\mark\\sankaku2")
for j in range(startNum,endNum):
	cascade = cv2.CascadeClassifier(getCascadeName(j))
	print("sankaku2 cascade"+ str(j).zfill(2))
	for i in range(len(files)):
		count=0
		print(files[i])
		for k in range(2000):
			img = cv2.imread("C:\\Users\\Shohei\\Documents\\sotsuken\\mark\\sankaku2\\" + files[i] + "\\" + str(k).zfill(4)+".png",cv2.IMREAD_GRAYSCALE)
			minW = int(img.shape[1]/2)
			minH = int(img.shape[0]/2)
			w = img.shape[1]/3
			h = img.shape[0]/3
			ww = img.shape[1]*2/3
			hh = img.shape[0]*2/3
			facerect = cascade.detectMultiScale(img, scaleFactor=1.1, minNeighbors=1, minSize=(minW,minH))
			if len(facerect) > 0:
				flag = 0
				for rect in facerect:
					if rect[0:2][0] < w and rect[0:2][1] < h and rect[2:4][0] > ww and rect[2:4][1] > hh :
						flag = 1
						#cv2.rectangle(img, tuple(rect[0:2]),tuple(rect[0:2]+rect[2:4]),125, thickness=2)
				if flag == 1:
					count += 1 
		"""
		cv2.imshow("detected.jpg", img)
		cv2.waitKey(0)
		"""
		print(str(count/20)+"%")

files = os.listdir("C:\\Users\\Shohei\\Documents\\sotsuken\\mark\\shikaku")
for j in range(startNum,endNum):
	cascade = cv2.CascadeClassifier(getCascadeName(j))
	print("shikaku cascade"+ str(j).zfill(2))
	for i in range(len(files)):
		count=0
		print(files[i])
		for k in range(2000):
			img = cv2.imread("C:\\Users\\Shohei\\Documents\\sotsuken\\mark\\shikaku\\" + files[i] + "\\" + str(k).zfill(4)+".png",cv2.IMREAD_GRAYSCALE)
			minW = int(img.shape[1]/2)
			minH = int(img.shape[0]/2)
			w = img.shape[1]/3
			h = img.shape[0]/3
			ww = img.shape[1]*2/3
			hh = img.shape[0]*2/3
			facerect = cascade.detectMultiScale(img, scaleFactor=1.1, minNeighbors=1, minSize=(minW,minH))
			if len(facerect) > 0:
				flag = 0
				for rect in facerect:
					if rect[0:2][0] < w and rect[0:2][1] < h and rect[2:4][0] > ww and rect[2:4][1] > hh :
						flag = 1
						#cv2.rectangle(img, tuple(rect[0:2]),tuple(rect[0:2]+rect[2:4]),125, thickness=2)
				if flag == 1:
					count += 1 
		"""
		cv2.imshow("detected.jpg", img)
		cv2.waitKey(0)
		"""
		print(str(count/20)+"%")

files = os.listdir("C:\\Users\\Shohei\\Documents\\sotsuken\\mark\\shikaku2")
for j in range(startNum,endNum):
	cascade = cv2.CascadeClassifier(getCascadeName(j))
	print("shikaku2 cascade"+ str(j).zfill(2))
	for i in range(len(files)):
		count=0
		print(files[i])
		for k in range(2000):
			img = cv2.imread("C:\\Users\\Shohei\\Documents\\sotsuken\\mark\\shikaku2\\" + files[i] + "\\" + str(k).zfill(4)+".png",cv2.IMREAD_GRAYSCALE)
			minW = int(img.shape[1]/2)
			minH = int(img.shape[0]/2)
			w = img.shape[1]/3
			h = img.shape[0]/3
			ww = img.shape[1]*2/3
			hh = img.shape[0]*2/3
			facerect = cascade.detectMultiScale(img, scaleFactor=1.1, minNeighbors=1, minSize=(minW,minH))
			if len(facerect) > 0:
				flag = 0
				for rect in facerect:
					if rect[0:2][0] < w and rect[0:2][1] < h and rect[2:4][0] > ww and rect[2:4][1] > hh :
						flag = 1
						#cv2.rectangle(img, tuple(rect[0:2]),tuple(rect[0:2]+rect[2:4]),125, thickness=2)
				if flag == 1:
					count += 1 
		"""
		cv2.imshow("detected.jpg", img)
		cv2.waitKey(0)
		"""
		print(str(count/20)+"%")

files = os.listdir("C:\\Users\\Shohei\\Documents\\sotsuken\\mark\\sonota")
files.remove("Thumbs.db")
for j in range(startNum,endNum):
	cascade = cv2.CascadeClassifier(getCascadeName(j))
	print("sonota cascade"+ str(j).zfill(2))
	for i in range(len(files)):
		count=0
		print(files[i])
		for k in range(2000):
			img = cv2.imread("C:\\Users\\Shohei\\Documents\\sotsuken\\mark\\sonota\\" + files[i] + "\\" + str(k).zfill(4)+".png",cv2.IMREAD_GRAYSCALE)
			minW = int(img.shape[1]/2)
			minH = int(img.shape[0]/2)
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
						#cv2.rectangle(img, tuple(rect[0:2]),tuple(rect[0:2]+rect[2:4]),125, thickness=2)
				if flag == 1:
					count += 1 
		"""
		cv2.imshow("detected.jpg", img)
		cv2.waitKey(0)
		"""
		print(str(count/20)+"%")


