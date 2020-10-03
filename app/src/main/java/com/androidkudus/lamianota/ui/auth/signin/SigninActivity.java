package com.androidkudus.lamianota.ui.auth.signin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.androidkudus.lamianota.R;
import com.androidkudus.lamianota.ui.auth.remember.RememberPasswordActivity;
import com.androidkudus.lamianota.ui.auth.signup.SignupActivity;
import com.androidkudus.lamianota.ui.main.MainActivity;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class SigninActivity extends AppCompatActivity {

    private TextInputEditText edtSignInEmail, edtSignInPassword;
    private String userEmail, userPassword;
    private AppCompatButton btnSign;
    private AppCompatTextView gotoSignUp, gotoRemember;
    private FirebaseAuth mAuth;
    private static final String TAG = SigninActivity.class.getSimpleName();
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        edtSignInEmail = findViewById(R.id.value_sign_email);
        edtSignInPassword = findViewById(R.id.value_sign_password);
        btnSign = findViewById(R.id.btn_signin);
        gotoSignUp = findViewById(R.id.goto_signup);
        progressBar = findViewById(R.id.progressbar_sign_in);
        gotoRemember = findViewById(R.id.tv_goto_remember);
        //Get instance firebase
        mAuth = FirebaseAuth.getInstance();
        //get State Authentication
        if (stateAuth()) {
            redirectToMainActivity();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        gotoSignUp.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(), SignupActivity.class));
            finish();
        });
        gotoRemember.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(), RememberPasswordActivity.class));
        });
        btnSign.setOnClickListener(v -> {
            if (validateInput()) {
                progressBar.setVisibility(View.VISIBLE);
                mAuth.signInWithEmailAndPassword(userEmail, userPassword).addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        progressBar.setVisibility(View.GONE);
                        Log.d(TAG, getString(R.string.auth_success));
                        Toast.makeText(getApplicationContext(), getString(R.string.auth_success), Toast.LENGTH_SHORT).show();
                        redirectToMainActivity();
                    } else {
                        progressBar.setVisibility(View.GONE);
                        Log.w(TAG, getString(R.string.auth_error), task.getException());
                        Toast.makeText(getApplicationContext(), getString(R.string.auth_error) + task.getException(), Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }

    //validation input user
    private boolean validateInput() {
        boolean isValid;
        String verifEmail = Objects.requireNonNull(edtSignInEmail.getText()).toString().trim();
        String verifPassword = Objects.requireNonNull(edtSignInPassword.getText()).toString().trim();
        if (verifEmail.isEmpty()) {
            edtSignInEmail.setError(getString(R.string.error_empty_field));
            isValid = false;
        } else if (!verifEmail.contains("@")){
            edtSignInEmail.setError(getString(R.string.error_email_not_correct));
            isValid = false;
        }
        else if (verifPassword.isEmpty()) {
            Toast.makeText(this, getString(R.string.error_empty_password), Toast.LENGTH_SHORT).show();
            isValid = false;
        } else if (verifPassword.length() < 8){
            Toast.makeText(this, getString(R.string.error_password_min_8_char), Toast.LENGTH_LONG).show();
            isValid = false;
        } else {
            userEmail = edtSignInEmail.getText().toString();
            userPassword = edtSignInPassword.getText().toString();
            isValid = true;
        }
        return isValid;
    }

    //Cek status authentication
    private boolean stateAuth() {
        boolean isLogged;
        isLogged = mAuth.getCurrentUser() != null;
        return isLogged;
    }

    //redirect to Main Activity
    private void redirectToMainActivity() {
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
        finish();
    }
}
