# coding: utf-8
import cv2
import os
import numpy as np

def getCascadeName(num,name): #カスケードファイル名
	return "C:\\Users\\Shohei\\Desktop\\CreateSample\\cascade3\\"+ name +"\\"+ getDirNum(num)+ "\\" + "cascade.xml"

def getDirNum(num):
	if num == 0:
		return "15"
	elif num == 1:
		return "25"
	elif num == 2:
		return "35"
	elif num == 3:
		return "50"

def getmark3Dir(num):
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


for o in range(7):
	fnames = os.listdir(getmark3Dir(o))
	if "Thumbs.db" in fnames: fnames.remove("Thumbs.db")
	for m in range(len(fnames)):
		f = open("C:\\Users\\Shohei\\Documents\\sotsuken\\result_lbp\\"+fnames[m]+".txt","w")
		for l in range(1,3):
			for k in range(7):
				files = os.listdir(getmark3Dir(k))
				if "Thumbs.db" in files: files.remove("Thumbs.db")	
				cascade = cv2.CascadeClassifier(getCascadeName(l,fnames[m]))
				f.write(getmark3Dir(k).replace("C:\\Users\\Shohei\\Documents\\sotsuken\\mark\\","")+" cascade"+ getDirNum(l)+"\n")
				for i in range(len(files)):
					f.write(files[i]+"\n")
					count=0
					result=0
					for n in range(2197):
						img = cv2.imread(getmark3Dir(k)+"\\"+files[i]+"\\"+str(count).zfill(4)+".png",cv2.IMREAD_GRAYSCALE)
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
					f.write(str(result/count*100)+"\n")
		f.close()
