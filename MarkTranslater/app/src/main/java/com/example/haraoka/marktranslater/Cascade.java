package com.example.haraoka.marktranslater;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

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
    private boolean mResult;

    public Cascade(String name, CascadeClassifier cc, Bitmap bmp, String title, String text){
        mResName = name;
        mCascadeClassifier = cc;
        serializeBitmap(bmp);
        mTitle = title;
        mText = text;
        mResult = false;
    }

    public void detectMarks(Mat gray){
        MatOfRect marks = new MatOfRect();
        if (mCascadeClassifier != null) {
            mCascadeClassifier.detectMultiScale(gray, marks, 1.1, 2, 2, new Size(0,0), new Size());
        }else{
            Log.i(TAG, "CascadeClassifier is null");
        }
        Rect[] marksArray = marks.toArray();
        if(marksArray.length > 0){
            Log.i(TAG, getResName() + " is detected.");
            mResult = true;
        }else {
            mResult = false;
        }
    }

    public boolean getResult(){
        return mResult;
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

