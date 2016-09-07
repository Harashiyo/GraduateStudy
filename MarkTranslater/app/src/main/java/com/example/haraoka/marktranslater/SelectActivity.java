package com.example.haraoka.marktranslater;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.List;

public class SelectActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select);
        GridView gridView = (GridView) findViewById(R.id.gridView);
        final List<Cascade> cascades = (ArrayList<Cascade>)getIntent().getSerializableExtra("DETECTED_MARKS");
        Bitmap[] bitmaps = new Bitmap[cascades.size()];
        String[] names = new String[cascades.size()];
        for(int i = 0; i < cascades.size(); i++){
            bitmaps[i] = cascades.get(i).getBitmap();
            names[i] = cascades.get(i).getResName();
        }
        gridView.setAdapter(new MarkAdapter(this, bitmaps, names));
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplication(), TranslateActivity.class);
                intent.putExtra("DETECTED_MARK", cascades.get(position));
                startActivity(intent);
                finish();
            }
        });

    }
}
