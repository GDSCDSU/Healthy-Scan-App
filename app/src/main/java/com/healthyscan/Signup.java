package com.healthyscan;

import static com.healthyscan.signInWithGoogle.RC_SIGN_IN;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Signup extends AppCompatActivity {
    public static final int RC_SIGN_UP = 102;
    TextView login_navigateTextview;
    EditText input_username, input_email, input_password, input_confirm_password;
    Button signup_btn, SignupWithGoogle;
    FirebaseAuth firebaseAuth;
    GoogleSignInClient mGoogleSignInClient;
    FirebaseDatabase database;
    DatabaseReference reference;

    FirebaseUser mUser;
    ProgressDialog progressDialog;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    String username, email, password, confirmPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        // id's find
        login_navigateTextview = findViewById(R.id.already_acc_login_navigate);
        input_username = findViewById(R.id.input_username);
        input_email = findViewById(R.id.input_email);
        input_password = findViewById(R.id.input_password);
        input_confirm_password = findViewById(R.id.input_confirm_password);
        signup_btn = findViewById(R.id.signup_btn);
        SignupWithGoogle = findViewById(R.id.SignupWithGoogle);

        progressDialog = new ProgressDialog(this);
        firebaseAuth = FirebaseAuth.getInstance();
        mUser = firebaseAuth.getCurrentUser();
        database = FirebaseDatabase.getInstance();

        login_navigateTextview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendUserToLoginActivity();
                finish();
            }
        });


        SignupWithGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // method to handle sign with google
                signInWithGoogleMethod();

            }
        });


        signup_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateAndSignUp();
            }
        });


        // Creating GoogleSignInOptions object with default sign-in options
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                // Requesting ID token using web client ID from resources
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        // Creating GoogleSignInClient using the specified options
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }


    private void signInWithGoogleMethod() {
        // it intent to signInWithGoogle class where handle sign in with google
        Intent intent = new Intent(Signup.this, signInWithGoogle.class);
        startActivity(intent);
        finish();
    }

    //
    private void validateAndSignUp() {
        username = input_username.getText().toString();
        email = input_email.getText().toString().trim();
        password = input_password.getText().toString();
        confirmPassword = input_confirm_password.getText().toString();

        if (isValidInput()) {
            progressDialogShow();
            registerUser();
        }
    }

    private boolean isValidInput() {
        if (TextUtils.isEmpty(username)) {
            input_username.setError("Enter a username");
            input_username.requestFocus();
            return false;
        } else if (TextUtils.isEmpty(email) || !email.matches(emailPattern)) {
            input_email.setError("Enter a valid email address");
            input_email.requestFocus();
            return false;
        } else if (TextUtils.isEmpty(password) || password.length() < 6) {
            input_password.setError("Enter a password with at least 6 characters");
            input_password.requestFocus();
            return false;
        } else if (!password.equals(confirmPassword)) {
            input_confirm_password.setError("Passwords do not match");
            input_confirm_password.requestFocus();
            return false;
        }
        return true;
    }

    // Method to register a new user with Firebase authentication
    private void registerUser() {
        // Creating a new user with email and password using Firebase authentication
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                // Adding a success listener to handle successful registration
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        // Dismissing progress dialog upon successful registration
                        progressDialogDismiss();
                        // Signing out the user from Firebase authentication (optional)
                        firebaseAuth.signOut();
                        // Redirecting user to the login activity after registration
                        sendUserToLoginActivity();
                        // Saving user data (e.g., username, password) to database
                        saveUserData(username, password);
                        Toast.makeText(Signup.this, "Registration successful", Toast.LENGTH_SHORT).show();
                    }
                })
                // Adding a failure listener to handle registration failure
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialogDismiss();
                        Toast.makeText(Signup.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });
    }

    // Method to save user data to Firebase Realtime Database
    private void saveUserData(String username, String password) {
        reference = database.getReference("users");
        String userId = reference.push().getKey(); // Generate unique user ID
        userModel userData = new userModel(username, password);
        reference.child(userId).setValue(userData);
    }


    private void progressDialogShow() {
        progressDialog.setMessage("Please wait while registering");
        progressDialog.setTitle("Registration");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
    }

    private void progressDialogDismiss() {
        progressDialog.dismiss();
    }

    private void sendUserToLoginActivity() {
        Intent loginIntent = new Intent(Signup.this, Login.class);
        // Clear the back stack so that the user cannot navigate back to the Signup activity
        startActivity(loginIntent);
        finish(); // Finish the Signup activity to prevent going back to it with the back button
    }
}
