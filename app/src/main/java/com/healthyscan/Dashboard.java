package com.healthyscan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;

public class Dashboard extends AppCompatActivity {

    MeowBottomNavigation bottomNavigation;
    RelativeLayout body_container;
    private DrawerLayout drawerLayout;
    NavigationView navigationView;
    TextView textViewUsername, textViewEmail;

    String username, email;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        bottomNavigation = findViewById(R.id.bottomNavigation);
        body_container = findViewById(R.id.body_container);
        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navigationView);
        View headerView = navigationView.getHeaderView(0);
        textViewUsername = headerView.findViewById(R.id.textViewUsername);
        textViewEmail = headerView.findViewById(R.id.textViewEmail);


        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();


        //set home as a default in bottom navigation
        bottomNavigation.show(3, true);

        // bottom navigation icon
        bottomNavigation.add(new MeowBottomNavigation.Model(1, R.drawable.virtual_coach));
        bottomNavigation.add(new MeowBottomNavigation.Model(2, R.drawable.scan2));
        bottomNavigation.add(new MeowBottomNavigation.Model(3, R.drawable.home));
        bottomNavigation.add(new MeowBottomNavigation.Model(4, R.drawable.location_map));
        bottomNavigation.add(new MeowBottomNavigation.Model(5, R.drawable.todo_check));


        // set BackgroundColor of Relative layout in home bottom navigation
        body_container.setBackgroundColor(getColor(R.color.light_blue));

        // set email in Nav_header from firebase database
        fetchAndSetEmail();
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                handleNavigationItemSelected(item);
                return true;
            }
        });


        bottomNavigation.setOnClickMenuListener(new Function1<MeowBottomNavigation.Model, Unit>() {
            @Override
            public Unit invoke(MeowBottomNavigation.Model model) {

                //handle bottom navigation click
                int id = model.getId();
                if (id == 1) {
                    bottom_Navigation_colors();
                } else if (id == 2) {
                    loadFragments(new ScannerFragment(),false);
                    bottom_Navigation_colors();
                } else if (id == 3) {
                    loadFragments(new HomeFragment(), false);
                    bottom_Navigation_home_colors();
                } else if (id == 4) {
                    loadFragments(new MapsFragment(), false);
                    bottom_Navigation_colors();
                } else if (id == 5) {
                    bottom_Navigation_colors();
                    loadFragments(new goalsFragment(),false);
                }

                return null;
            }
        });

        loadFragments(new HomeFragment(), true);

    }

    private void fetchAndSetEmail() {
        if (user != null) {
            email = user.getEmail();
            username = user.getDisplayName();
            textViewUsername.setText(username);
            textViewEmail.setText(email);

        }
    }

    private void handleNavigationItemSelected(MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.nav_home_drawable) {
            loadFragments(new HomeFragment(), false);
        } else if (itemId == R.id.nav_settings_drawable) {
            // Handle settings click
            // Add your logic for handling settings here
        } else if (itemId == R.id.nav_logout) {
            // Handle Logout click
            logout();
            sendUserToLogin();
        }
    }

    //handle accounts logouts
    private void logout() {
        // Firebase sign out
        FirebaseAuth.getInstance().signOut();

        // Google sign out
        GoogleSignIn.getClient(this, GoogleSignInOptions.DEFAULT_SIGN_IN).signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // After signing out, redirect the user to the login screen
                        Intent intent = new Intent(Dashboard.this, Login.class);
                        startActivity(intent);
                        finish(); // Close the current activity to prevent going back to Dashboard
                    }
                });
    }


    private void sendUserToLogin() {
        Intent intent = new Intent(Dashboard.this, Login.class);
        startActivity(intent);
        finish();
    }

    private void bottom_Navigation_colors() {
        bottomNavigation.setBackgroundBottomColor(getColor(R.color.dark_blue));
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

    // handle Load Fragments in frame layout in dashboard
    private void loadFragments(Fragment fragment, boolean flag) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if (flag) {
            fragmentTransaction.add(R.id.container, fragment);
        }
        else {
            fragmentTransaction.replace(R.id.container,fragment);
        }
        fragmentTransaction.commit();
    }

    //open drawer navigation
    public void openDrawer() {
        drawerLayout.openDrawer(GravityCompat.START);
    }

}
