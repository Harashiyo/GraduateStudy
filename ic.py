# coding: utf-8
import cv2
import numpy as np
import random
import math
import os

def getRad(deg): #回転角度取得
	return deg / 180 * math.pi

def getBrightness(l,h): #輝度取得（乱数）
	return random.randint(l,h)

def getMatX(theta): #x軸回転行列
	return np.matrix([[1,             0,              0, 0],
					  [0, np.cos(theta), -np.sin(theta), 0],
					  [0, np.sin(theta),  np.cos(theta), 0],
					  [0,             0,              0, 1]])

def getMatY(theta): #y軸回転行列
	return np.matrix([[np.cos(theta), 0, np.sin(theta), 0],
					  [0, 1, 0, 0],
					  [-np.sin(theta), 0, np.cos(theta), 0],
					  [0, 0, 0, 1]])

def getMatZ(theta): #z軸回転行列
	return np.matrix([[np.cos(theta), -np.sin(theta), 0, 0],
					  [np.sin(theta), np.cos(theta), 0, 0],
					  [0, 0, 1, 0],
					  [0, 0, 0, 1]])

def getMatTransrate(x, y, z): #平行移動行列
	return np.matrix(  [[1,0,0,0],
						[0,1,0,0],
						[0,0,1,0],
						[x,y,z,1]]) 

def getMatExpansion(x, y, z): #拡大縮小行列
	return np.matrix(  [[x,0,0,0],
						[0,y,0,0],
						[0,0,z,0],
						[0,0,0,1]]) 

def getMatPerspectiveTransformation(d): #透視変換行列
	return np.matrix([[1, 0, 0, 0],
					  [0, 1, 0, 0],
					  [0, 0, 0, -1/d],
					  [0, 0, 0, 1]])

def getMatCompositeTransformation(theta, size): #複合行列
	m = np.dot(getMatTransrate(-size[0]/2, -size[1]/2, 0), getMatX(theta[0]))
	m = np.dot(m, getMatY(theta[1]))
	m = np.dot(m, getMatZ(theta[2]))
	return np.dot(m, getMatPerspectiveTransformation(5000))

def normarization(coordinate, size): #正規化
	h = coordinate[0, 3]
	coordinate[0, 3] = 1
	m = np.dot(getMatExpansion(h,h,1), getMatTransrate(size[0]/2, size[1]/2, 0))
	return np.dot(coordinate, m)

def getImageDir(num):
	if num ==0 :
		return "C:\\Users\\Shohei\\Documents\\sotsuken\\image\\sonota"
	elif num == 1 :
		return "C:\\Users\\Shohei\\Documents\\sotsuken\\image\\maru"
	elif num == 2 :
		return "C:\\Users\\Shohei\\Documents\\sotsuken\\image\\maru2"
	elif num == 3 :
		return "C:\\Users\\Shohei\\Documents\\sotsuken\\image\\sankaku"
	elif num == 4 :
		return "C:\\Users\\Shohei\\Documents\\sotsuken\\image\\sankaku2"
	elif num == 5 :
		return "C:\\Users\\Shohei\\Documents\\sotsuken\\image\\shikaku"
	elif num == 6 :
		return "C:\\Users\\Shohei\\Documents\\sotsuken\\image\\shikaku2"

def getMarkDir(num):
	if num ==0 :
		return "C:\\Users\\Shohei\\Documents\\sotsuken\\mark\\sonota\\"
	elif num == 1 :
		return "C:\\Users\\Shohei\\Documents\\sotsuken\\mark\\maru\\"
	elif num == 2 :
		return "C:\\Users\\Shohei\\Documents\\sotsuken\\mark\\maru2\\"
	elif num == 3 :
		return "C:\\Users\\Shohei\\Documents\\sotsuken\\mark\\sankaku\\"
	elif num == 4 :
		return "C:\\Users\\Shohei\\Documents\\sotsuken\\mark\\sankaku2\\"
	elif num == 5 :
		return "C:\\Users\\Shohei\\Documents\\sotsuken\\mark\\shikaku\\"
	elif num == 6 :
		return "C:\\Users\\Shohei\\Documents\\sotsuken\\mark\\shikaku2\\"
		
def getSign(num):
	if num == 0:
		return 1
	return -1

def getFileName(num): #出力ファイル名
	return str(num).zfill(4)+".png"

def getFilePass(d,name,num): #出力ファイル名
	#return  getMarkDir(d)+ name + "\\" + getFileName(num)
	return "C:\\Users\\Shohei\\Documents\\sotsuken\\OK\\"+getFileName(num)

lists = []
maxDeg=1
while maxDeg <= 9:
	for x in range(maxDeg):
		for y in range(maxDeg):
			for z in range(maxDeg):
				if x < (maxDeg-1) and y < (maxDeg-1) and z < (maxDeg-1):
					continue
				list = []
				for xx in range(2):
					for yy in range(2):
						for zz in range(2):
							coordinate=[getSign(xx)*x,getSign(yy)*y,getSign(zz)*z]
							if (coordinate in list)==False:
								list.append(coordinate)
								lists.append(coordinate)
	maxDeg+=1

h=0
files = os.listdir(getImageDir(h))
if "Thumbs.db" in files: files.remove("Thumbs.db")
i=0
im = cv2.imread("C:\\Users\\Shohei\\Documents\\sotsuken\\image\\maru\\tokuho.png",cv2.IMREAD_GRAYSCALE)
m=max(im.shape[1], im.shape[0])
img = cv2.resize(im,(int(im.shape[1]/m*90),int(im.shape[0]/m*90)))
size = tuple(np.array([img.shape[1], img.shape[0]]))
p1 = np.float32([[0, 0],[0, size[1]],[size[0], size[1]],[size[0],0]])
try:
	os.mkdir(getMarkDir(h) + files[i].rstrip(".png"))
except OSError:
	print(files[i] + " is already exists")
f = open("C:\\Users\\Shohei\\Documents\\sotsuken\\OK\\pos.txt","w")
cnt=0
for list in lists:
	theta = np.array([getRad(list[0]*2),getRad(list[1]*2),getRad(list[2]*2)])
	m = getMatCompositeTransformation(theta, size)
	leftTop = np.dot([0, 0, 0, 1], m)
	leftBottom = np.dot([0, size[1], 0, 1], m)
	rightBottom = np.dot([size[0], size[1], 0, 1], m)
	rightTop = np.dot([size[0], 0, 0, 1], m)
	
	leftTop = normarization(leftTop, size)
	leftBottom = normarization(leftBottom, size)
	rightBottom = normarization(rightBottom, size)
	rightTop = normarization(rightTop, size)

	p2 = np.float32([[leftTop[0,0], leftTop[0,1]],[leftBottom[0,0], leftBottom[0,1]],
		[rightBottom[0,0], rightBottom[0,1]],[rightTop[0,0],rightTop[0,1]]])
	M=cv2.getPerspectiveTransform(p1,p2)
	dst = cv2.warpPerspective(img,M,(0,0),cv2.INTER_LINEAR,cv2.BORDER_CONSTANT,1)
	leftTopY=0
	rightBottomY=0
	flag1=False
	flag2=False
	for j in range(size[1]):
		for k in range(size[0]):
			if dst[j, k] < 230 and flag1==False:
				leftTopY=j
				flag1=True
			if dst[size[1]-j-1, k] < 230 and flag2 == False:
				rightBottomY=size[1]-j-1
				flag2=True
			if flag1==True and flag2==True :
				break
		if flag1==True and flag2==True :
			break
	leftTopX=0
	rightBottomX=0
	flag1=False
	flag2=False
	for k in range(size[0]):
		for j in range(size[1]):
			if dst[j, k] < 230 and flag1==False:
				leftTopX=k
				flag1=True
			if dst[j, size[0]-k-1] < 230 and flag2==False:
				rightBottomX=size[0]-k-1
				flag2=True
			if flag1==True and flag2==True :
				break
		if flag1==True and flag2==True :
			break
	width=rightBottomX-leftTopX
	height=rightBottomY-leftTopY
	if width > height:		
		leftTopY-=int((width-height)/2)
		height=width
	elif width < height:
		leftTopX-=int((height-width)/2)
		width=height
	leftTopY-=4
	leftTopX-=4
	width+=8
	height+=8
	f.write(getFileName(cnt)+" 1 "+str(leftTopX)+" "+str(leftTopY)+" "+str(width)+" "+str(height)+"\n")
	cv2.imwrite(getFilePass(h,files[i].rstrip(".png"),cnt),dst)
	cnt=cnt+1
f.close()