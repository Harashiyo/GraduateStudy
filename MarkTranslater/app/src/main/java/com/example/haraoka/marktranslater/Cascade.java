package com.example.haraoka.marktranslater;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Rect;
import org.opencv.core.Size;
import org.opencv.objdetect.CascadeClassifier;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;

/**
 * Created by Shohei on 2016/08/29.
 */
public class Cascade implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final String TAG = "MT::Cascade";
    transient CascadeClassifier mCascadeClassifier;
    private byte[] mBitmapArray;
    private String mResName;
    private String mTitle;
    private String mText;

    public Cascade(String name, CascadeClassifier cc, Bitmap bmp, String title, String text){
        mResName = name;
        mCascadeClassifier = cc;
        serializeBitmap(bmp);
        mTitle = title;
        mText = text;
    }

    /**
     * マークと背景の輝度が逆転している場合も想定し，
     * 入力画像とネガポジ反転させた入力画像の２種類に対して物体検出を行う
     *
     * @param gray グレースケール化された入力画像（Mat型）
     *             この画像に対し物体検出を行う
     */
    public boolean detectMarks(Mat gray){
        Mat negMat = new Mat();
        Core.bitwise_not(gray, negMat);
        MatOfRect marks1 = new MatOfRect();
        MatOfRect marks2 = new MatOfRect();
        if (mCascadeClassifier != null) {
            mCascadeClassifier.detectMultiScale(gray, marks1, 1.1, 2, 2, new Size(gray.width()*2/3,gray.height()*2/3), new Size());
            mCascadeClassifier.detectMultiScale(negMat, marks2, 1.1, 2, 2, new Size(gray.width()*2/3,gray.height()*2/3), new Size());
        }else{
            Log.i(TAG, "CascadeClassifier is null");
        }
        Rect[] marksArray1 = marks1.toArray();
        Rect[] marksArray2 = marks2.toArray();
        if(marksArray1.length > 0 || marksArray2.length > 0){
            Log.i(TAG, getResName() + " is detected.");
            return true;
        }

        return false;
    }

    private void serializeBitmap(Bitmap bmp) {
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, bout);
        mBitmapArray = bout.toByteArray();
    }

    public void setBitmap(Bitmap bmp){
        serializeBitmap(bmp);
    }

    public Bitmap getBitmap() {
        if (mBitmapArray == null) {
            return null;
        }
        Bitmap bitmap = BitmapFactory.decodeByteArray(mBitmapArray, 0,mBitmapArray.length);
        return bitmap;
    }

    public void setCascadeClassifier(CascadeClassifier cc){
        mCascadeClassifier = cc;
    }

    public void setResName(String name){
        mResName = name;
    }

    public String getResName(){
        return mResName;
    }

    public void setTitle(String title){
        mTitle = title;
    }

    public String getTitle(){
        return mTitle;
    }

    public void setText(String text){
        mText = text;
    }

    public String getText(){
        return mText;
    }
}

