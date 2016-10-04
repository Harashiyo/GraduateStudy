import cv2
import numpy as np
import random
import math
import os

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
		

def getFileName(num): #出力ファイル名
	return str(num).zfill(4)+".png"

def getFilePass(d,name,num): #出力ファイル名
	return  getMarkDir(d)+ name + "\\" + getFileName(num)

target="tokuho"
f = open("C:\\Users\\Shohei\\Documents\\sotsuken\\NG.txt","w")
for j in range(2041,2071):
	for h in range(7):
		files = os.listdir(getMarkDir(h))
		if "Thumbs.db" in files: files.remove("Thumbs.db")
		if target in files: files.remove(target)
		for i in range(len(files)):
			f.write(getMarkDir(h).replace("C:\\Users\\Shohei\\Documents\\sotsuken\\","")+"\\"+files[i]+"\\"+str(j).zfill(4)+".png\n")
f.close()

