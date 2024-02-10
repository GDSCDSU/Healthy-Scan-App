package com.healthyscan;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class signInWithGoogle extends Login {
    // Request code for sign-in
    public static final int RC_SIGN_IN = 101;
    GoogleSignInClient mGoogleSignInClient;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait while Signing");
        progressDialog.setTitle("Google Sign In....");
        progressDialog.show();

        // Configuring Google sign-in options
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        // Initializing Firebase authentication
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        // Building Google sign-in client
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        // Creating and launching intent for Google sign-in
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Handling result of Google sign-in activity
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Handling sign-in failure
                Toast.makeText(this, "sign in failed" + e.getMessage(), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
                finish();
            }
        }
    }

    // Method to authenticate with Firebase using Google credentials
    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Retrieving current user
                        user = mAuth.getCurrentUser();
                        String displayName = user.getDisplayName();

                        // Saving user data into database
                        saveUserDataInToDatabase(displayName);

                        // Sign in success, update UI with the signed-in user's information
                        progressDialog.dismiss();
                        user = mAuth.getCurrentUser();
                        Toast.makeText(this, "Sign in successfully", Toast.LENGTH_SHORT).show();
                        updateUI(user);
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithCredential:failure", task.getException());
                        Toast.makeText(signInWithGoogle.this, "" + task.getException(), Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                        finish();
                    }
                });
    }

    private void updateUI(FirebaseUser user) {
        if (user != null) {
            sendToDashboard();
        }

    }

    public void sendToDashboard() {
        Intent intent = new Intent(signInWithGoogle.this, Dashboard.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    // Method to save user data into Firebase database
    private void saveUserDataInToDatabase(String username) {
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("users");
        String userId = user.getUid();
        usersRef.child(userId).child("username").setValue(username);
    }

}