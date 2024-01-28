package com.healthyscan;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Login extends AppCompatActivity {

    TextView not_acc_sign_up_navigate;
    Button login_btn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        not_acc_sign_up_navigate = findViewById(R.id.not_acc_sign_up_navigate);
        login_btn = findViewById(R.id.login_btn);

        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, Dashboard.class);
                startActivity(intent);
            }
        });


        not_acc_sign_up_navigate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent navigate_signup_activity = new Intent(Login.this, Signup.class);
                startActivity(navigate_signup_activity);
                finish();
            }
        });

   }
}