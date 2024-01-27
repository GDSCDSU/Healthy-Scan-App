package com.healthyscan;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class Signup extends AppCompatActivity {
    TextView already_acc_login_navigate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        already_acc_login_navigate = findViewById(R.id.already_acc_login_navigate);
        already_acc_login_navigate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent navigate_login_activity = new Intent(Signup.this, Login.class);
                startActivity(navigate_login_activity);
                finish();
            }
        });

    }
}