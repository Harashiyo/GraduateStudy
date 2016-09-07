package com.example.haraoka.marktranslater;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements CameraBridgeViewBase.CvCameraViewListener {

    private static final String TAG = "MT::MainActivity";
    private CameraBridgeViewBase mCameraView;
    private static final Scalar RED = new Scalar(255, 0, 0, 255);
    private static final Scalar BLUE = new Scalar(0, 0, 255, 255);
    private Mat mRgba;
    private Mat mGray;

    private Thread mPointThread;
    private boolean mRepeatFlag = false;
    private List<Cascade> mCascades;


    private SharedPreferences preferences;
    private Point mTopLeft;
    private Point mBottomRight;
    private Point mScreenSize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //アクティビティの最初に呼ばれる
        super.onCreate(savedInstanceState);
        //ディスプレイを消灯させない
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        //レイアウト読み込み
        setContentView(R.layout.activity_main);
        preferences = getSharedPreferences("SaveData", Context.MODE_PRIVATE);
        mCascades = new ArrayList<>();
        // ボタンの設定
        Button buttonTranslate = (Button) findViewById(R.id.button_translate);
        buttonTranslate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Mat mat = new Mat((int) (mBottomRight.y-mTopLeft.y), (int)(mBottomRight.x-mTopLeft.x), CvType.CV_8UC3);
                mGray.submat((int)mTopLeft.y,(int)mBottomRight.y,(int)mTopLeft.x,(int)mBottomRight.x).copyTo(mat);
                for(int i = 0; i < mCascades.size();i++){
                    mCascades.get(i).detectMarks(mat);
                    System.out.println(mCascades.get(i).getResName());
                    System.out.println(mCascades.get(i).getResult());
                }
                List<Cascade> detectedMarks = new ArrayList<>();
                for(int i = 0; i < mCascades.size(); i++){
                    if(mCascades.get(i).getResult()==true){
                        detectedMarks.add(mCascades.get(i));
                    }
                }

                if(detectedMarks.size() == 1){
                    Intent intent = new Intent(getApplication(), TranslateActivity.class);
                    intent.putExtra("DETECTED_MARK", detectedMarks.get(0));
                    startActivity(intent);
                }else if(detectedMarks.size() > 0){
                    Intent intent = new Intent(getApplication(), SelectActivity.class);
                    intent.putExtra("DETECTED_MARKS", (Serializable) detectedMarks);
                    startActivity(intent);
                }else{
                    Toast.makeText(MainActivity.this, "検出されませんでした", Toast.LENGTH_LONG).show();
                }
            }
        });
        Button buttonPlus = (Button) findViewById(R.id.button_puls);
        buttonPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mScreenSize.x > mBottomRight.x){
                    mTopLeft.x--;
                    mTopLeft.y--;
                    mBottomRight.x++;
                    mBottomRight.y++;
                }
            }
        });
        buttonPlus.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    mRepeatFlag = false;
                }
                return false;
            }
        });
        buttonPlus.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                mRepeatFlag = true;
                mPointThread = new Thread(repeatPlus);
                mPointThread.start();
                return false;
            }
        });
        Button buttonMinus = (Button) findViewById(R.id.button_minus);
        buttonMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mBottomRight.x - mTopLeft.x > 90) {
                    mTopLeft.x++;
                    mTopLeft.y++;
                    mBottomRight.x--;
                    mBottomRight.y--;
                }
            }
        });
        buttonPlus.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    mRepeatFlag = false;
                }
                return false;
            }
        });
        buttonMinus.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                mRepeatFlag = true;
                mPointThread = new Thread(repeatMinus);
                mPointThread.start();
                return false;
            }
        });

        // カメラビューの設定
        mCameraView = (CameraBridgeViewBase) findViewById(R.id.camera_view);
        mCameraView.setCvCameraViewListener(this);


    }

    @Override
    protected void onResume() {
        super.onResume();
        //TODO OpenCV Managerを使わなくても動くようにする
        /**
         ～OpenCV Managerとは～
         OpenCV4Android SDK 2.4.2からライブラリ自体は
         OpenCV Managerというアプリで管理する構成に変更されました。
         これまでのようにアプリのバイナリにライブラリをコピーするのは
         無駄だからというのが主な理由とのことです。
         デバイスのアーキテクチャをOpenCV Managerが判別して
         適切なライブラリをインストールしてくれます。
         */
        OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_2_4_11, this, mLoaderCallback);

    }

    @Override
    public void onPause() {
        //アクティビティが停止したら、カメラプレビューを停止
        if (mCameraView != null) {
            mCameraView.disableView();
        }
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("TOP_LEFT_X", (int) mTopLeft.x);
        editor.putInt("TOP_LEFT_Y", (int) mTopLeft.y);
        editor.putInt("BOTTOM_RIGHT_X", (int) mBottomRight.x);
        editor.putInt("BOTTOM_RIGHT_Y", (int) mBottomRight.y);
        editor.apply();
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //アクティビティが消去されたら、カメラプレビューを停止
        if (mCameraView != null) {
            mCameraView.disableView();
        }
    }

    @Override
    public void onCameraViewStarted(int width, int height) {
        /**
         * カメラプレビュー開始時に呼ばれる
         * Mat(int rows, int cols, int type)
         * rows(行): height, cols(列): width
         */
        mScreenSize = new Point(width, height);
        mTopLeft = new Point( preferences.getInt("TOP_LEFT_X",width / 4), preferences.getInt("TOP_LEFT_Y",height / 2 - width / 4));
        mBottomRight = new Point(preferences.getInt("BOTTOM_RIGHT_X",width * 3 / 4), preferences.getInt("BOTTOM_RIGHT_Y", height / 2 + width / 4));
        mGray = new Mat(height, width, CvType.CV_8UC3);
        mRgba = new Mat(height, width, CvType.CV_8UC3);
    }

    @Override
    public void onCameraViewStopped() {
        // カメラプレビュー終了時に呼ばれる
        mGray.release();
        mRgba.release();
    }

    @Override
    public Mat onCameraFrame(Mat inputFrame) {
        //mRgbaにinputFrameをコピー
        inputFrame.copyTo(mRgba);
        //mGrayにグレースケールを格納
        Imgproc.cvtColor(inputFrame, mGray, Imgproc.COLOR_RGBA2GRAY);
        //mRgbaに矩形を描画
        Core.rectangle(mRgba, mTopLeft, mBottomRight, RED, 3);

        return mRgba;
    }

    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        // OpenCV Managerによりライブラリ初期化完了後に呼ばれるコールバック
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS: {
                    Log.i(TAG, "OpenCV loaded successfully");
                    /**
                     * この辺のファイルをopencv/sdk/etc/haarmCascades もしくは、opencv/sdk/etc/lbpmCascadesにある
                     * xmlファイルと書き換えると、検出する場所が変わるっぽい
                     *
                     * 1. res/raw/に、検出したいファイルを置く。
                     * 2. InputStream isとmCascadeFileの引数をファイル名に変える
                     *
                     *
                     * Relevant Android bug here: code.google.com/p/android/issues/detail?id=204714 — the $change field will probably become transient in Android Studio 2.1, which would solve most of the issues it may produce right now.
                     * ここでは関係アンドロイドバグ：code.google.com/p/android/issues/detail?id=204714 - $changeフィールドは、おそらくそれが今生成することができる問題のほとんどを解決することになる、Androidのスタジオ2.1の一過性になります。
                     * URL:http://stackoverflow.com/questions/36549129/android-java-objmodelclass-getclass-getdeclaredfields-returns-change-as-o
                     */

                    // カスケード型分類器読み込み
                    Field[] fields = R.raw.class.getFields();
                    for (Field field : fields) {
                        try {
                            if (field.getName().equals("$change")) {
                                continue;
                            }
                            String resName = field.getName();
                            int resId = getResources().getIdentifier(resName, "raw", getPackageName());
                            InputStream is = getResources().openRawResource(resId);
                            File mCascadeDir = getDir("mCascade", Context.MODE_PRIVATE);
                            File mCascadeFile = new File(mCascadeDir, resName + ".xml");
                            FileOutputStream os = new FileOutputStream(mCascadeFile);
                            byte[] buffer = new byte[4096];
                            int bytesRead;
                            while ((bytesRead = is.read(buffer)) != -1) {
                                os.write(buffer, 0, bytesRead);
                            }
                            is.close();
                            os.close();
                            CascadeClassifier mJavaDetector = new CascadeClassifier(mCascadeFile.getAbsolutePath());
                            is = getResources().getAssets().open(resName + ".png");
                            Bitmap bm = BitmapFactory.decodeStream(is);
                            is.close();
                            resId = getResources().getIdentifier("title_" + resName, "string", getPackageName());
                            String title = getResources().getString(resId);
                            resId = getResources().getIdentifier("text_" + resName, "string", getPackageName());
                            String text = getResources().getString(resId);
                            if (mJavaDetector.empty()) {
                                Log.e(TAG, "Failed to load mCascade classifier");
                                mJavaDetector = null;
                            } else {
                                mCascades.add(new Cascade(resName, mJavaDetector, bm, title, text));
                                Log.i(TAG, "Loaded mCascade classifier from " + mCascadeFile.getAbsolutePath());
                            }
                            mCascadeDir.delete();
                        } catch (IllegalArgumentException e) {
                            e.printStackTrace();
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                            Log.e(TAG, "Failed to load mCascade. Exception thrown: " + e);
                        }
                    }
                    mCameraView.enableView();
                }
                break;
                default: {
                    super.onManagerConnected(status);
                }
                break;
            }
        }
    };

    private Runnable repeatPlus = new Runnable(){
        @Override
        public void run(){
            while(mRepeatFlag){
                try{
                    Thread.sleep(30);
                }catch(InterruptedException e){
                }
                handler.post(new Runnable(){
                    @Override
                    public void run(){
                        if (mScreenSize.x > mBottomRight.x) {
                            mTopLeft.x--;
                            mTopLeft.y--;
                            mBottomRight.x++;
                            mBottomRight.y++;
                        }
                    }
                });
            }
        }
    };

    private Runnable repeatMinus = new Runnable(){
        @Override
        public void run(){
            while(mRepeatFlag){
                try{
                    Thread.sleep(30);
                }catch(InterruptedException e){
                }
                handler.post(new Runnable(){
                    @Override
                    public void run(){
                        if (mBottomRight.x - mTopLeft.x > 90) {
                            mTopLeft.x++;
                            mTopLeft.y++;
                            mBottomRight.x--;
                            mBottomRight.y--;
                        }
                    }
                });
            }
        }
    };

    private Handler handler = new Handler(){
        public void handleMessage(Message msg){
            if(mPointThread != null){
                mPointThread.stop();
                mPointThread=null;
            }
        }
    };
}
