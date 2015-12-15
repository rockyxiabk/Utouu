package com.rocky.utouu.guide;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.rocky.utouu.MainActivity;
import com.rocky.utouu.R;

import java.util.ArrayList;
import java.util.List;

public class GuideActivity extends AppCompatActivity {

    private ViewPager viewpager;
    private LinearLayout ll_point;
    private ImageView[] arr_point;
    private List<ImageView> list;
    private int[] images = {R.mipmap.guide_01, R.mipmap.guide_02, R.mipmap.guide_03, R.mipmap.guide_04, R.mipmap.guide_05, R.mipmap.guide_06};
    private ImageView iv_start;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide_avtivity);
        initView();
        initViewPager();
    }

    private void initViewPager() {
        list = new ArrayList<>();
        for (int i = 0; i < images.length; i++) {
            ImageView iv = new ImageView(this);
            iv.setImageResource(images[i]);
            iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
            list.add(iv);
        }
        MyPagerAdapter adapter = new MyPagerAdapter();
        viewpager.setAdapter(adapter);
        viewpager.setCurrentItem(0);
        viewpager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                for (int i = 0; i < arr_point.length; i++) {
                    arr_point[i].setEnabled(true);
                }
                arr_point[position].setEnabled(false);
                if (position == arr_point.length - 1) {
                    iv_start.setVisibility(View.VISIBLE);
                    ll_point.setVisibility(View.GONE);
                    iv_start.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(GuideActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    });
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    private void initView() {
        viewpager = ((ViewPager) findViewById(R.id.vp_guide));
        ll_point = ((LinearLayout) findViewById(R.id.ll_point));
        iv_start = ((ImageView) findViewById(R.id.guide_iv_start));
        initPoint();
    }

    private void initPoint() {
        arr_point = new ImageView[images.length];
        for (int i = 0; i < images.length; i++) {
            ImageView point = new ImageButton(this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(10, 10);
            point.setLayoutParams(params);
            point.setScaleType(ImageView.ScaleType.FIT_XY);
            point.setPadding(10, 0, 10, 0);
            point.setBackgroundResource(R.drawable.guide_point_selector);
            ll_point.addView(point);
            point.setTag(i);
            arr_point[i] = point;
        }
        arr_point[0].setEnabled(false);
    }

    class MyPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(list.get(position));
            return list.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(list.get(position));
        }
    }
}
