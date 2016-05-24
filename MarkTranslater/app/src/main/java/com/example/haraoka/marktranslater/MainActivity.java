package com.example.haraoka.marktranslater;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
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
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity implements CameraBridgeViewBase.CvCameraViewListener {

    private static final String TAG  = "MT::MainActivity";
    private CameraBridgeViewBase mCameraView;
    private Button buttonTranslate;
    private int status;
    private static final Scalar RED  = new Scalar(255, 0, 0, 255);
    private static final Scalar BLUE = new Scalar(0, 0, 255, 255);
    private Mat                    mRgba;
    private Mat                    mGray;
    private File                   mCascadeFile;
    private File faceCascadeFile;
    private CascadeClassifier      mJavaDetector;
    private CascadeClassifier      mJavaDetector2;

    public static final int        JAVA_DETECTOR       = 0;

    private float                  mRelativeFaceSize   = 0.5f;
    private int                    mAbsoluteFaceSize   = 0;
    private int                    flag[] = new int[2];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //アクティビティの最初に呼ばれる
        super.onCreate(savedInstanceState);
        //ディスプレイを消灯させない
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        //レイアウト読み込み
        setContentView(R.layout.activity_main);
        status=0;
        // 検出ボタンのインスタンスを変数にバインド
        buttonTranslate = (Button) findViewById(R.id.button_translate);
        //リスナーの設定
        buttonTranslate.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(getApplication(), TranslateActivity.class);
                intent.putExtra("markflag",flag);
                startActivity(intent);
            }
        });
        // カメラビューのインスタンスを変数にバインド
        mCameraView = (CameraBridgeViewBase)findViewById(R.id.camera_view);
        // リスナーの設定
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
        inputFrame.copyTo(mRgba);
        Imgproc.cvtColor(inputFrame, mGray, Imgproc.COLOR_RGBA2GRAY);

        if (mAbsoluteFaceSize == 0) {
            int height = mGray.rows();
            if (Math.round(height * mRelativeFaceSize) > 0) {
                mAbsoluteFaceSize = Math.round(height * mRelativeFaceSize);
            }
            //mNativeDetector.setMinFaceSize(mAbsoluteFaceSize);
        }

        MatOfRect faces = new MatOfRect();

        if (mJavaDetector != null){
            mJavaDetector.detectMultiScale(mGray, faces, 1.1, 2, 2, // TODO: objdetect.CV_HAAR_SCALE_IMAGE
                    new Size(mAbsoluteFaceSize, mAbsoluteFaceSize), new Size());
        }

        Rect[] facesArray = faces.toArray();
        for (int i = 0; i < facesArray.length; i++){
            Core.rectangle(mRgba, facesArray[i].tl(), facesArray[i].br(), RED, 3);
        }

        MatOfRect faces2 = new MatOfRect();

        if (mJavaDetector2 != null){
            mJavaDetector2.detectMultiScale(mGray, faces2, 1.1, 2, 2, // TODO: objdetect.CV_HAAR_SCALE_IMAGE
                    new Size(mAbsoluteFaceSize, mAbsoluteFaceSize), new Size());
        }

        Rect[] facesArray2 = faces2.toArray();
        for (int i = 0; i < facesArray2.length; i++){
            Core.rectangle(mRgba, facesArray2[i].tl(), facesArray2[i].br(), BLUE, 3);
        }

        if(facesArray.length>0)flag[0]=1;
        else flag[0]=0;
        if(facesArray2.length>0)flag[1]=1;
        else flag[1]=0;

        Log.i(TAG, "testflag" + flag[0]+" "+ flag[1]);

        int l=0;
        for(int j=0;j<0;j++){

            if (mJavaDetector != null){
                mJavaDetector.detectMultiScale(mGray, faces, 1.1, 2, 2, // TODO: objdetect.CV_HAAR_SCALE_IMAGE
                        new Size(mAbsoluteFaceSize, mAbsoluteFaceSize), new Size());
            }

            if(j%20==0){
                l=0;
            }
            Point test1 = new Point(l*50,j/20*50);
            Point test2 = new Point(l*50+50,j/20*50+50);
            facesArray = faces.toArray();
            for (int i = 0; i < facesArray.length; i++){
                // Core.rectangle(mRgba, test1, test2, FACE_RECT_COLOR, 3);
                //Core.rectangle(mRgba, facesArray[i].tl(), facesArray[i].br(), FACE_RECT_COLOR, 3);
            }
            l++;
        }
        return mRgba;
    }

    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        // OpenCV Managerによりライブラリ初期化完了後に呼ばれるコールバック
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS:
                {
                    Log.i(TAG, "OpenCV loaded successfully");
                    try {
                        /**
                         * この辺のファイルをopencv/sdk/etc/haarcascades もしくは、opencv/sdk/etc/lbpcascadesにある
                         * xmlファイルと書き換えると、検出する場所が変わるっぽい
                         *
                         * 1. res/raw/に、検出したいファイルを置く。
                         * 2. InputStream isとmCascadeFileの引数をファイル名に変える
                         */
                        // カスケード型分類器読み込み
                        //JAS
                        InputStream is = getResources().openRawResource(R.raw.jas20);
                        File cascadeDir = getDir("cascade", Context.MODE_PRIVATE);
                        mCascadeFile = new File(cascadeDir, "jas20.xml");
                        FileOutputStream os = new FileOutputStream(mCascadeFile);
                        byte[] buffer = new byte[4096];
                        int bytesRead;
                        while ((bytesRead = is.read(buffer)) != -1) {
                            os.write(buffer, 0, bytesRead);
                        }
                        is.close();
                        os.close();
                        //特徴を持った領域の認識には、CascadeClassifierというクラスを利用します
                        mJavaDetector = new CascadeClassifier(mCascadeFile.getAbsolutePath());
                        //mJavaDetector.load( mCascadeFile.getAbsolutePath() );
                        if (mJavaDetector.empty()) {
                            Log.e(TAG, "Failed to load cascade classifier");
                            mJavaDetector = null;
                        } else {
                            Log.i(TAG, "Loaded cascade classifier from " + mCascadeFile.getAbsolutePath());
                        }
                        cascadeDir.delete();

                        //アルミ
                        InputStream is2 = getResources().openRawResource(R.raw.alumi);
                        File cascadeDir2 = getDir("cascade", Context.MODE_PRIVATE);
                        faceCascadeFile = new File(cascadeDir2, "alumi.xml");
                        FileOutputStream os2 = new FileOutputStream(faceCascadeFile);
                        byte[] buffer2 = new byte[4096];
                        int bytesRead2;
                        while ((bytesRead2 = is2.read(buffer2)) != -1) {
                            os2.write(buffer2, 0, bytesRead2);
                        }
                        is2.close();
                        os2.close();
                        mJavaDetector2 = new CascadeClassifier(faceCascadeFile.getAbsolutePath());
                        //mJavaDetector2.load( faceCascadeFile.getAbsolutePath() );
                        if (mJavaDetector2.empty()) {
                            Log.e(TAG, "Failed to load cascade classifier");
                            mJavaDetector2 = null;
                        } else {
                            Log.i(TAG, "Loaded cascade classifier from " + faceCascadeFile.getAbsolutePath());
                        }
                        cascadeDir2.delete();


                    } catch (IOException e) {
                        e.printStackTrace();
                        Log.e(TAG, "Failed to load cascade. Exception thrown: " + e);
                    }

                    mCameraView.enableView();
                } break;
                default:
                {
                    super.onManagerConnected(status);
                } break;
            }
        }
    };

    //輪郭抽出
    private void getContourExtraction(Mat inputFrame){
        // Cannyフィルタをかける
        Imgproc.Canny(inputFrame, mGray, 80, 100);
        // ビット反転
        Core.bitwise_not(mGray, mGray);
    }

    //2値化
    private void getBinary(Mat inputFrame){
        //固定閾値処理
        Imgproc.threshold(inputFrame, mGray, 0.0, 255.0, Imgproc.THRESH_BINARY | Imgproc.THRESH_OTSU);
        Imgproc.cvtColor(mGray,mGray, Imgproc.COLOR_GRAY2BGRA,4);
    }

}
