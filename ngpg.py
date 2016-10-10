# coding: utf-8
import cv2
import numpy as np

def getFileName(): #入ファイル名
	return "F:\\GS\\image\\maru\\eco.png"

def getCascadeName(num): #カスケードファイル名
	return "C:\\Users\\Shohei\\Desktop\\tokuhoCascade\\cascade" + str(num).zfill(2) + ".xml"

img = cv2.imread("C:\\Users\\Shohei\\Desktop\\tokuho.png",cv2.IMREAD_GRAYSCALE)
dst = cv2.bitwise_not(img)
cv2.imshow("detected.jpg", dst)
cv2.waitKey(0)
