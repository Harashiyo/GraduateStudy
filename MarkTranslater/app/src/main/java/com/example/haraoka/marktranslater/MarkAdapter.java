package com.example.haraoka.marktranslater;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


/**
 * Created by Shohei on 2016/09/07.
 */
public class MarkAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private String[] mNames;
    private Bitmap[] mBitmaps;

    public MarkAdapter(Context context, Bitmap[] bitmaps, String[] names){
        if (bitmaps.length != names.length) {
            throw new IllegalArgumentException("画像数とネーム数が違います");
        }
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
        mNames = new String[names.length];
        mBitmaps = new Bitmap[getCount()];
        for(int i=0; i < getCount(); i++){
            mNames[i] = names[i];
            mBitmaps[i] = bitmaps[i];
        }
    }

    private static class ViewHolder {
        public ImageView imageView;
        public TextView  textView;
    }

    @Override
    public int getCount() {
        return mNames.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (view == null) {
            view = mLayoutInflater.inflate(R.layout.grid_mark, null);
            holder = new ViewHolder();
            holder.imageView = (ImageView)view.findViewById(R.id.gri_image_mark);
            holder.textView = (TextView)view.findViewById(R.id.gri_text_title);
            view.setTag(holder);
        } else {
            holder = (ViewHolder)view.getTag();
        }

        holder.imageView.setImageBitmap(mBitmaps[i]);
        holder.textView.setText(mNames[i]);
        return view;
    }
}
