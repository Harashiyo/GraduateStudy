package com.example.haraoka.marktranslater;

import android.graphics.Bitmap;

import org.opencv.objdetect.CascadeClassifier;

/**
 * Created by Shohei on 2016/08/29.
 */
public class Cascade {
    private CascadeClassifier mCascadeClassifier;
    private Bitmap mBmp;
    private String mResName;
    private String mTitle;
    private String mText;
    public Cascade(String name, CascadeClassifier cc, Bitmap bmp, String title, String text){
        mResName = name;
        mCascadeClassifier = cc;
        mBmp = bmp;
        mTitle = title;
        mText = text;
        System.out.println(text);
    }
    public void setCascadeClassifier(CascadeClassifier cc){
        mCascadeClassifier = cc;
    }

    public void setBmp(Bitmap bmp){
        mBmp = bmp;
    }

    public void setResName(String name){
        mResName = name;
    }

    public void setTitle(String title){
        mTitle = title;
    }

    public void setText(String text){
        mText = text;
    }

    public CascadeClassifier getCascadeClassifier(){
        return mCascadeClassifier;
    }

    public Bitmap getBmp(){
        return mBmp;
    }

    public String getResName(){
        return mResName;
    }

    public String getTitle(){
        return mTitle;
    }

    public String getText(){
        return mText;
    }
}
