# coding: utf-8
import cv2
import os
import numpy as np

def getCascadeName(num,name): #カスケードファイル名
	return "C:\\Users\\Shohei\\Desktop\\CreateSample\\cascade\\"+ name +"\\"+ getDirNum(num)+ "\\" + "cascade.xml"

def getDirNum(num):
	if num == 0:
		return "15"
	elif num == 1:
		return "25"
	elif num == 2:
		return "35"
	elif num == 3:
		return "50"

def getmarkDir(num):
	return "C:\\Users\\Shohei\\Documents\\sotsuken\\mark\\kido\\"+str(num)+".png"

files = os.listdir("C:\\Users\\Shohei\\Documents\\sotsuken\\image2")
if "Thumbs.db" in files: files.remove("Thumbs.db")
for s in range(len(files)):
	fname=files[s].rstrip(".png")
	im = cv2.imread("C:\\Users\\Shohei\\Documents\\sotsuken\\image2\\"+files[s],cv2.IMREAD_GRAYSCALE)
	m=max(im.shape[1], im.shape[0])
	img = cv2.resize(im,(int(im.shape[1]/m*90),int(im.shape[0]/m*90)))
	size = tuple(np.array([img.shape[1], img.shape[0]]))
	for i in range(1,256):
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
	for l in range(4):
		cascade = cv2.CascadeClassifier(getCascadeName(l,fname))
		for m in range(1,256):
			img = cv2.imread(getmarkDir(m),cv2.IMREAD_GRAYSCALE)
			ww = int(img.shape[1]*2/3)
			hh = int(img.shape[0]*2/3)
			facerect = cascade.detectMultiScale(img, scaleFactor=1.1, minNeighbors=1, minSize=(ww,hh))
			flag = 0
			if len(facerect) > 0:	
				flag = 1		
				for rect in facerect:
					cv2.rectangle(img, tuple(rect[0:2]),tuple(rect[0:2]+rect[2:4]),125, thickness=2)
			if flag != 1:
				kido = m
		print("mark cascade"+getDirNum(l))
		print(fname)
		print(kido)


