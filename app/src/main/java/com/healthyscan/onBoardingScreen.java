package com.healthyscan;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import androidx.appcompat.widget.AppCompatButton;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;


public class onBoardingScreen extends AppCompatActivity {

    ViewPager mSLideViewPager;
    LinearLayout mDotLayout;
    AppCompatButton nextbtn;

    TextView[] dots;
    ViewPagerAdapter viewPagerAdapter;
    preferenceManager prefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_boarding_screen);
        prefManager = new preferenceManager(this,"Handle_onboarding");

        nextbtn = findViewById(R.id.nextbtn);
        mSLideViewPager = findViewById(R.id.slideViewPager);
        mDotLayout = findViewById(R.id.indicator_layout);


        viewPagerAdapter = new ViewPagerAdapter(this);
        mSLideViewPager.setAdapter(viewPagerAdapter);

        nextbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getitem(0) < 3) {
                    mSLideViewPager.setCurrentItem(getitem(1), true);
                } else {
                    Intent i = new Intent(onBoardingScreen.this, Login.class);
                    startActivity(i);
                    finish();
                }
            }
        });

//        skipbtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent i = new Intent(MainActivity.this, mainscreen.class);
//                startActivity(i);
//                finish();
//            }
//        });

        Dots(0);



        mSLideViewPager.addOnPageChangeListener(viewListener);


        nextbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSLideViewPager.setCurrentItem(1,true);
            }
        });
    }



    ViewPager.OnPageChangeListener viewListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(int position) {
            Dots(position);

            nextbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (position < 2 ) {
                        mSLideViewPager.setCurrentItem(position + 1, true);
                    }
                    else {
                        prefManager.setFirstTimeLaunch(1);
                        Intent intent = new Intent(onBoardingScreen.this,Login.class);
                        startActivity(intent);
                        finish();
                    }
                }
            });
        }

        @Override
        public void onPageScrollStateChanged(int state) {
        }
    };

    private int getitem(int i) {
        return mSLideViewPager.getCurrentItem() + i;
    }

    public void Dots(int pos){

        dots = new TextView[3];
        mDotLayout.removeAllViews();

        for (int i = 0; i < dots.length; i++){
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextColor(getColor(R.color.unselect_grey));
            dots[i].setTextSize(40);                                     // unselected text (dots size) size
            mDotLayout.addView(dots[i]);

        }
        if (dots.length > 0){
            dots[pos].setTextColor(getColor(R.color.grey));
            dots[pos].setTextSize(40);
        }


    }

}