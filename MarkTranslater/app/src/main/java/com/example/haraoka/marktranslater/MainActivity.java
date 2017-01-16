package com.example.haraoka.marktranslater;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.AppLaunchChecker;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Toast;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
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
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements CameraBridgeViewBase.CvCameraViewListener {

    private static final String TAG = "MT::MainActivity";
    private ZoomCameraView mCameraView;

    private Mat mRgba;
    private Mat mGray;

    private List<Cascade> mCascades;

    private boolean mFirstAccessFlag;

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
        //初回起動判定
        if(!AppLaunchChecker.hasStartedFromLauncher(this)){
            Intent intent = new Intent(getApplication(), IndicatorActivity.class);
            startActivity(intent);
        }
        AppLaunchChecker.onActivityCreate(this);
        //preferences = getSharedPreferences("SaveData", Context.MODE_PRIVATE);
        mCascades = new ArrayList<>();
        // ボタンの設定
        final Button buttonTranslate = (Button) findViewById(R.id.main_button_translate);
        buttonTranslate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonTranslate.setEnabled(false);
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        buttonTranslate.setEnabled(true);
                    }
                }, 500);
                Mat mat = new Mat((int) (mBottomRight.y-mTopLeft.y), (int)(mBottomRight.x-mTopLeft.x), CvType.CV_8UC3);
                Mat dst = new Mat(90,90, CvType.CV_8UC3);
                mGray.submat((int)mTopLeft.y,(int)mBottomRight.y,(int)mTopLeft.x,(int)mBottomRight.x).copyTo(mat);
                Imgproc.resize(mat, dst, new Size(90, 90));
                List<Cascade> detectedMarks = new ArrayList<>();
                for(int i = 0; i < mCascades.size();i++){
                    if(mCascades.get(i).detectMarks(dst) == true){
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
                    Mat dst2 = new Mat(90,90, CvType.CV_8UC3);
                    Imgproc.threshold(dst, dst2, 0.0, 255.0, Imgproc.THRESH_BINARY | Imgproc.THRESH_OTSU);
                    for(int i = 0; i < mCascades.size();i++){
                        if(mCascades.get(i).detectMarks(dst2) == true){
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
                        Imgproc.adaptiveThreshold(dst, dst2, 255, Imgproc.ADAPTIVE_THRESH_GAUSSIAN_C, Imgproc.THRESH_BINARY, 11, 2);
                        for(int i = 0; i < mCascades.size();i++){
                            if(mCascades.get(i).detectMarks(dst2) == true){
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
                }
            }
        });
        final Button buttonInfo = (Button) findViewById(R.id.main_button_info);
        buttonInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getApplication(), IndicatorActivity.class);
                startActivity(intent);
            }
        });

        // カメラビューの設定
        mCameraView = (ZoomCameraView) findViewById(R.id.camera_view);
        mCameraView.setZoomControl((SeekBar) findViewById(R.id.main_seek));
        mCameraView.setCvCameraViewListener(this);
        //リストmCascadesへの追加処理はonResume以降に実行される
        //そのためonResumeが実行されるごとにリストへの追加処理が実行されてしまう
        //同じマークのオブジェクトが複数追加されるのを防ぐためのフラグ
        mFirstAccessFlag = true;

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

        mTopLeft = new Point( width / 10 * 3, height / 2 - width / 10 * 2 );
        mBottomRight = new Point(width * 7 / 10, height / 2 + width / 10 * 2 );
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
        Core.rectangle(mRgba, mTopLeft, mBottomRight, new Scalar(236, 185, 53), 2);

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

                    if(mFirstAccessFlag == true) {

                        // カスケード型分類器読み込み
                        Field[] fields = R.raw.class.getFields();
                        for (Field field : fields) {
                            System.out.println(field.getName());
                        }
                        for (Field field : fields) {
                            try {
                                if (field.getName().equals("$change")||field.getName().equals("serialVersionUID")) {
                                    continue;
                                }
                                String resName = field.getName();
                                //Log.d(TAG,resName);
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
                                Log.e(TAG, "File not found : " + e);
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                                Log.e(TAG, "Failed to load mCascade. Exception thrown: " + e);
                            }
                            }


                        mFirstAccessFlag = false;
                    }
                    mCameraView.enableView();
                    mCameraView.setSeekValue(0);
                }
                break;
                default: {
                    super.onManagerConnected(status);
                }
                break;
            }
        }
    };
}
