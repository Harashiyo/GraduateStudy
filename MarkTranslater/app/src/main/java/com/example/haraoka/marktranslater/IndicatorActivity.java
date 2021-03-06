package com.example.haraoka.marktranslater;

import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.View;
import android.widget.TextView;

public class IndicatorActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener, View.OnClickListener {

    private ViewPager viewPager;
    private SparseArray<Fragment> viewSparseArray = new SparseArray<>();
    private View indicatorFloatView;
    private float indicatorSpace;
    private TextView back;
    private TextView next;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_indicator);
        initViewPager();
        setupIndicator();
    }

    private void initViewPager() {
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        FragmentManager fragmentManager = getSupportFragmentManager();
        ParallaxPagerAdapter adapter = new ParallaxPagerAdapter(fragmentManager, this, makeFragments());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(this);
    }

    private SparseArray<Fragment> makeFragments() {

        // Resources#getDrawable で
        // 指定した画像のDrawableインスタンスを取得
        viewSparseArray.put(0, IndicatorFragment.newInstance(0));
        viewSparseArray.put(1, IndicatorFragment.newInstance(1));
        viewSparseArray.put(2, IndicatorFragment.newInstance(2));
        viewSparseArray.put(3, IndicatorFragment.newInstance(3));
        viewSparseArray.put(4, IndicatorFragment.newInstance(4));
        return viewSparseArray;
    }

    private void setupIndicator() {
        float scale = getResources().getDisplayMetrics().density;
        indicatorSpace = getResources().getDimensionPixelSize(R.dimen.middle_margin);// * scale;
        indicatorFloatView = findViewById(R.id.view_float_indicator);
        back = (TextView) findViewById(R.id.text_indicator_back);
        next = (TextView) findViewById(R.id.text_indicator_next);
        back.setOnClickListener(this);
        next.setOnClickListener(this);

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        indicatorFloatView.setTranslationX((position * indicatorSpace) + (positionOffset * indicatorSpace));
    }

    @Override
    public void onPageSelected(int position) {
        if (position == 0) {
            back.setText(getString(R.string.footer_skip));
        } else {
            back.setText(getString(R.string.footer_back));
        }

        if (viewSparseArray.size() == (position + 1)) {
            next.setText(getString(R.string.footer_go));
        } else {
            next.setText(getString(R.string.footer_next));
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.text_indicator_next) {
            next();
        } else if (v.getId() == R.id.text_indicator_back) {
            back();
        }
    }

    private void next() {
        if (viewPager.getCurrentItem() == viewSparseArray.size()-1) {
            finish();
        } else {
            viewPager.setCurrentItem(viewPager.getCurrentItem() + 1, true);
        }
    }

    private void back() {
        if (viewPager.getCurrentItem() == 0) {
            viewPager.setCurrentItem(viewSparseArray.size(), true);
        } else {
            viewPager.setCurrentItem(viewPager.getCurrentItem() - 1, true);
        }
    }
}
