package com.healthyscan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.RelativeLayout;

import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;

public class Dashboard extends AppCompatActivity {

    MeowBottomNavigation bottomNavigation;
    RelativeLayout body_container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        bottomNavigation = findViewById(R.id.bottomNavigation);
        body_container = findViewById(R.id.body_container);


        bottomNavigation.show(3, true);

        // add your bottom navigation icon here
        bottomNavigation.add(new MeowBottomNavigation.Model(1,R.drawable.virtual_coach));
        bottomNavigation.add(new MeowBottomNavigation.Model(2, R.drawable.scan2));
        bottomNavigation.add(new MeowBottomNavigation.Model(3,R.drawable.home));
        bottomNavigation.add(new MeowBottomNavigation.Model(4,R.drawable.location_map));
        bottomNavigation.add(new MeowBottomNavigation.Model(5,R.drawable.todo_check));


        bottomNavigation.setOnClickMenuListener(new Function1<MeowBottomNavigation.Model, Unit>() {
            @Override
            public Unit invoke(MeowBottomNavigation.Model model) {

                int id = model.getId();
                if (id==1){
                    bottom_Navigation_colors();
                } else if (id==2) {
                    bottom_Navigation_colors();
                }
                else if (id==3) {
                    bottom_Navigation_home_colors();
                }
                else if (id==4) {
                    bottom_Navigation_colors();
                }
                else if (id==5) {
                    bottom_Navigation_colors();
                }

                return null;
            }
        });

//        bottom_navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
//            @Override
//            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//                return true;
//            }
//        });

    }

    private void bottom_Navigation_colors() {
        bottomNavigation.setBackgroundBottomColor(getColor(R.color.light_blue));
        bottomNavigation.setCircleColor(getColor(R.color.white));
        bottomNavigation.setSelectedIconColor(getColor(R.color.dark_blue));
        bottomNavigation.setDefaultIconColor(getColor(R.color.white));
        body_container.setBackgroundColor(getColor(R.color.white));


    }

    private void bottom_Navigation_home_colors() {
        bottomNavigation.setBackgroundBottomColor(getColor(R.color.white));
        bottomNavigation.setCircleColor(getColor(R.color.white));
        bottomNavigation.setSelectedIconColor(getColor(R.color.dark_blue));
        bottomNavigation.setDefaultIconColor(getColor(R.color.dark_blue));
        body_container.setBackgroundColor(getColor(R.color.light_blue));


    }
}