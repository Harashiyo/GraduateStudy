package com.example.haraoka.marktranslater;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;


/**
 * Created by sakemotoshinya on 15/12/20.
 */
public class IndicatorFragment extends Fragment {
    private static final String ARG_STRING = "string";

    private int mNum = 0;

    public IndicatorFragment() {
    }

    public static IndicatorFragment newInstance(int num) {
        IndicatorFragment fragment = new IndicatorFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_STRING, num);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mNum = getArguments().getInt(ARG_STRING);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_indicator_layout, container, false);
        ((ImageView)view.findViewById(R.id.frag_imageView)).setImageBitmap(getBitmap(mNum));
        view.setBackgroundColor(Color.rgb((int)(Math.random() * 255), (int)(Math.random() * 255), (int)(Math.random() * 255)));
        return view;
    }

    public Bitmap getBitmap(int num){
        Bitmap bitmap = null;
        switch (num) {
            case 0:
                try {
                    InputStream is = getResources().getAssets().open("description01.png");
                    bitmap = BitmapFactory.decodeStream(is);
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case 1:
                try {
                    InputStream is = getResources().getAssets().open("description02.png");
                    bitmap = BitmapFactory.decodeStream(is);
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case 2:
                try {
                    InputStream is = getResources().getAssets().open("description03.png");
                    bitmap = BitmapFactory.decodeStream(is);
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case 3:
                try {
                    InputStream is = getResources().getAssets().open("description04.png");
                    bitmap = BitmapFactory.decodeStream(is);
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case 4:
                try {
                    InputStream is = getResources().getAssets().open("description05.png");
                    bitmap = BitmapFactory.decodeStream(is);
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
        }
        return bitmap;
    }

}
