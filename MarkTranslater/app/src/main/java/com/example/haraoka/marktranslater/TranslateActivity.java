package com.example.haraoka.marktranslater;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

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
        textTranslation= (TextView) findViewById(R.id.tra_text_translation);
        textTitle=(TextView) findViewById(R.id.tra_text_title);
        imageMark=(ImageView)findViewById(R.id.tra_image_mark);

        Cascade cascades = (Cascade)getIntent().getSerializableExtra("DETECTED_MARK");

        imageMark.setImageBitmap(cascades.getBitmap());
        textTitle.setText(cascades.getTitle());
        textTranslation.setText(cascades.getText());

    }
}
