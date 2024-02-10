package com.healthyscan;


import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class Login extends AppCompatActivity {

    TextView not_acc_sign_up_navigate;
    EditText loginEmail, loginPassword;
    Button signInButton, signInWithGoogleButton;
    String emailLogin, passwordLogin;
    FirebaseAuth firebaseAuth;
    GoogleSignInClient mGoogleSignInClient;
    FirebaseUser user;
    ProgressDialog progressDialog;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        not_acc_sign_up_navigate = findViewById(R.id.not_acc_sign_up_navigate);
        loginEmail = findViewById(R.id.loginEmail);
        loginPassword = findViewById(R.id.loginPassword);
        signInButton = findViewById(R.id.signInButton);
        signInWithGoogleButton = findViewById(R.id.signInWithGoogleButton);

        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);

//       handle User is already logged in
        persistentLogin();

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //handle login logic
                validateAndLogin();
            }
        });

        not_acc_sign_up_navigate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendUserToSignup();
            }
        });


        // Initialize Google Sign-In Client
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);


        signInWithGoogleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // it intent to signInWithGoogle class where handle sign in with google
                Intent intent = new Intent(Login.this, signInWithGoogle.class);
                startActivity(intent);
                finish();
            }
        });

    }

    private void validateAndLogin() {
        emailLogin = loginEmail.getText().toString().trim();
        passwordLogin = loginPassword.getText().toString().trim();

        if (isValidCredentials()) {
            progressDialogShow();
            signInUser();
        }
    }

    private boolean isValidCredentials() {
        if (TextUtils.isEmpty(emailLogin) || !emailLogin.matches(emailPattern)) {
            loginEmail.setError("Enter a valid email address");
            loginEmail.requestFocus();
            return false;
        } else if (TextUtils.isEmpty(passwordLogin)) {
            loginPassword.setError("Enter a password");
            loginPassword.requestFocus();
            return false;
        }
        return true;
    }

    // Method to sign in an existing user with Firebase authentication
    private void signInUser() {
        // Signing in the user with email and password using Firebase authentication
        firebaseAuth.signInWithEmailAndPassword(emailLogin, passwordLogin)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        progressDialogDismiss();
                        sendUserToDashboard();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialogDismiss();
                        // Handling sign-in failure
                        handleLoginFailure(e);
                    }
                });
    }

    private void handleLoginFailure(Exception e) {
        if (e instanceof FirebaseAuthInvalidUserException) {
            // Email not found in Firebase Authentication
            showToast("Email not found, please check your email");
        } else if (e instanceof FirebaseAuthInvalidCredentialsException) {
            // Incorrect password
            showToast("Incorrect email or password, please try again");
        } else {
            // Other login failure reasons
            showToast("Login failed: " + e.getMessage());
        }
    }

    private void showToast(String message) {
        Toast.makeText(Login.this, message, Toast.LENGTH_SHORT).show();
    }

    private void sendUserToDashboard() {
        Intent intentDashboard = new Intent(Login.this, Dashboard.class);
        startActivity(intentDashboard);
        finish();
    }

    private void sendUserToSignup() {
        Intent intentSignup = new Intent(Login.this, Signup.class);
        startActivity(intentSignup);
        finish();
    }

    private void progressDialogShow() {
        progressDialog.setMessage("Please wait while logging in");
        progressDialog.setTitle("Login");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
    }

    private void progressDialogDismiss() {
        progressDialog.dismiss();
    }


    private void persistentLogin() {
        if (firebaseAuth.getCurrentUser() != null) {
            // User is already logged in, navigate to Dashboard
            sendUserToDashboard();
            finish(); // Close the current Login activity
        }
    }
}
