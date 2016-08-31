package com.example.haraoka.marktranslater;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;

public class TranslateActivity extends AppCompatActivity {
    //意味表示用テキスト
    TextView textTranslation;

    TextView textTitle;
    //マーク表示用イメージ
    ImageView imageMark;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_translate);
        textTranslation= (TextView) findViewById(R.id.text_translation);
        textTitle=(TextView) findViewById(R.id.text_title);
        imageMark=(ImageView)findViewById(R.id.image_mark);

        //検出されたデータを受け取る
        Intent intent = getIntent();
        int[] flag = intent.getIntArrayExtra("markflag");

        //JAS検出
        if(flag[0]==1 && flag[1]==0){
            try{
                InputStream is = getResources().getAssets().open("jas20.png");
                Bitmap bitmap = BitmapFactory.decodeStream(is);
                imageMark.setImageBitmap(bitmap);
            }catch (IOException e) {
                Log.d("Assets","Error");
            }
            textTitle.setText(getString(R.string.title_jas20));
            textTranslation.setText(getString(R.string.text_jas20));

        }

        //アルミ検出
        if(flag[0]==0 && flag[1]==1){
            try{
                InputStream is = getResources().getAssets().open("alumi.png");
                Bitmap bitmap = BitmapFactory.decodeStream(is);
                imageMark.setImageBitmap(bitmap);
            }catch (IOException e) {
                Log.d("Assets","Error");
            }
            textTitle.setText(getString(R.string.title_alumi));
            textTranslation.setText(getString(R.string.text_alumi));
        }
    }

}
