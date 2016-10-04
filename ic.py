# coding: utf-8
import cv2
import numpy as np
import random
import math

def getRad(): #回転角度取得（乱数）
	return random.uniform(-math.pi/180*12,math.pi/180*12)

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
	return np.dot(m, getMatPerspectiveTransformation(((size[0]+size[1]))))

def normarization(coordinate, size): #正規化
	h = coordinate[0, 3]
	coordinate[0, 3] = 1
	m = np.dot(getMatExpansion(h,h,1), getMatTransrate(size[0]/2, size[1]/2, 0))
	return np.dot(coordinate, m)

def getFileName(num): #出力ファイル名
	return "C:\\Users\\Shohei\\Documents\\sotsuken\\mark\\maru\\tokuho\\" + str(num).zfill(4)+".png"



img = cv2.imread("C:\\Users\\Shohei\\Desktop\\tokuho.png",cv2.IMREAD_GRAYSCALE)
size = tuple(np.array([img.shape[1], img.shape[0]]))

for i in range(2000):
	p1 = np.float32([[0, 0],[0, size[1]],[size[0], size[1]],[size[0],0]])
	'''
		画像の回転
	'''
	theta = np.array([getRad(),getRad(),getRad()])
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
	'''
		輝度変更
	'''
	
	black = getBrightness(0,155)
	white = getBrightness(black + 100, 255)

	for j in range(size[1]):
		for k in range(size[0]):
			if dst[j, k] < 200:
				dst[j, k] = black
			else:
				dst[j, k] = white
	
	cv2.imwrite(getFileName(i),dst)

	#cv2.imshow('result',dst)
	#cv2.waitKey(0)
