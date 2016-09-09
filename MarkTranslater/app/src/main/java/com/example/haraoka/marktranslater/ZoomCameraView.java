package com.example.haraoka.marktranslater;

import android.content.Context;
import android.hardware.Camera;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.widget.SeekBar;

import org.opencv.android.JavaCameraView;

/**
 * Created by Shohei on 2016/09/08.
 */
public class ZoomCameraView extends JavaCameraView {

    protected SeekBar mSeekBar;
    private double progressValue = 0;
    private ScaleGestureDetector mScaleDetector;

    public ZoomCameraView(Context context, int cameraId) {
        super(context, cameraId);
    }
    public ZoomCameraView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public double getProgressValue(){
        return progressValue;
    }

    public void setZoomControl(SeekBar seekBar) {
        mSeekBar=seekBar;
    }

    public void setSeekValue(int progress){
        mSeekBar.setProgress(progress);
    }

    protected void enableZoomControls(Camera.Parameters params) {
        final int maxZoom = params.getMaxZoom();
        mSeekBar.setMax(maxZoom);
        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
               @Override
               public void onProgressChanged(SeekBar seekBar, int progress,
                                             boolean fromUser) {
                   // TODO Auto-generated method stub
                   progressValue =progress;
                   Camera.Parameters params = mCamera.getParameters();
                   params.setZoom(progress);
                   mCamera.setParameters(params);
               }
               @Override
               public void onStartTrackingTouch(SeekBar seekBar) {
                   // TODO Auto-generated method stub

               }
               @Override
               public void onStopTrackingTouch(SeekBar seekBar) {
                   // TODO Auto-generated method stub

               }
           }
        );

        mScaleDetector = new ScaleGestureDetector(getContext(),
                new ScaleGestureDetector.OnScaleGestureListener() {
                    @Override
                    public boolean onScale(ScaleGestureDetector detector) {

                        double magnification = detector.getScaleFactor();
                        if(magnification > 1 && maxZoom > progressValue) {
                            //微調整
                            progressValue+=magnification/3;
                            setSeekValue((int)progressValue);
                        }else if(magnification < 1 && progressValue > 0){
                            progressValue-=magnification/3;
                            setSeekValue((int)progressValue);
                        }
                        invalidate();
                        return true;
                    }

                    @Override
                    public boolean onScaleBegin(ScaleGestureDetector scaleGestureDetector) {
                        return true;
                    }

                    @Override
                    public void onScaleEnd(ScaleGestureDetector scaleGestureDetector) {

                    }
                });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return mScaleDetector.onTouchEvent(event);
    }

    @Override
    protected boolean initializeCamera(int width, int height) {

        boolean ret = super.initializeCamera(width, height);

        try {
            Camera.Parameters params = mCamera.getParameters();

            if(params.isZoomSupported())
                enableZoomControls(params);

            mCamera.setParameters(params);
        }catch (Exception e){

        }


        return ret;
    }
}

