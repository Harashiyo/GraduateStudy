package com.example.haraoka.marktranslator;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

public class MainActivity extends AppCompatActivity implements CameraBridgeViewBase.CvCameraViewListener2{

    private CameraBridgeViewBase mCameraView;
    private Mat mOutputFrame;
    private Button buttonDetect;
    private int status;

    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        // OpenCV Managerによりライブラリ初期化完了後に呼ばれるコールバック
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                // 読み込みが成功したらカメラプレビューを開始
                case LoaderCallbackInterface.SUCCESS:
                    mCameraView.enableView();
                    break;
                default:
                    super.onManagerConnected(status);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //アクティビティの最初に呼ばれる
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        status=0;
        // 検出ボタンのインスタンスを変数にバインド
        buttonDetect = (Button) findViewById(R.id.button_detect);
        //リスナーの設定
        buttonDetect.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                if(status%2==0) {
                    mCameraView.disableView();
                }else{
                    onResume();
                }
                status++;
            }
        });
        // カメラビューのインスタンスを変数にバインド
        mCameraView = (CameraBridgeViewBase)findViewById(R.id.camera_view);
        // リスナーの設定
        mCameraView.setCvCameraViewListener(this);
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
    protected void onResume() {
        super.onResume();
        //TODO OpenCV Managerを使わなくても動くようにする
        /*
            ～OpenCV Managerとは～
            OpenCV4Android SDK 2.4.2からライブラリ自体は
            OpenCV Managerというアプリで管理する構成に変更されました。
            これまでのようにアプリのバイナリにライブラリをコピーするのは
            無駄だからというのが主な理由とのことです。
            デバイスのアーキテクチャをOpenCV Managerが判別して
            適切なライブラリをインストールしてくれます。
         */
        if (!OpenCVLoader.initDebug()) {//OpenCV Managerを呼び出し 非同期でライブラリの読み込み/初期化を行う
            Log.d("TAG", "Internal OpenCV library not found. Using OpenCV Manager for initialization");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_2_4_4, this, mLoaderCallback);
        } else {//OpenCV Managerが端末にインストールされていなければ、GooglePlayStoreにジャンプ
            Log.d("TAG", "OpenCV library found inside package. Using it!");
            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }
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
        // カメラプレビュー開始時に呼ばれる
        // Mat(int rows, int cols, int type)
        // rows(行): height, cols(列): width
        mOutputFrame = new Mat(height, width, CvType.CV_8UC1);
    }

    @Override
    public void onCameraViewStopped() {
        // カメラプレビュー終了時に呼ばれる
        mOutputFrame.release();
    }

    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
        // カメラプレビューが開始したら呼ばれ、ここでフレームに処理を加える
        //getContourExtraction(inputFrame.gray());//輪郭抽出
        getBinary(inputFrame.gray());//2値化
        return mOutputFrame;
    }

    //輪郭抽出
    private void getContourExtraction(Mat inputFrame){
        // Cannyフィルタをかける
        Imgproc.Canny(inputFrame, mOutputFrame, 80, 100);
        // ビット反転
        Core.bitwise_not(mOutputFrame, mOutputFrame);
    }

    //2値化
    private void getBinary(Mat inputFrame){
        //固定閾値処理
        Imgproc.threshold(inputFrame, mOutputFrame, 0.0, 255.0, Imgproc.THRESH_BINARY | Imgproc.THRESH_OTSU);
        Imgproc.cvtColor(mOutputFrame,mOutputFrame, Imgproc.COLOR_GRAY2BGRA,4);
    }

}
