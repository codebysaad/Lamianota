package com.androidkudus.lamianota.ui.auth.signup;

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
import com.androidkudus.lamianota.ui.auth.signin.SigninActivity;
import com.androidkudus.lamianota.ui.main.MainActivity;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class SignupActivity extends AppCompatActivity {

    private TextInputEditText edtSignUpInEmail, edtSignUpPassword, edtSignUpPasswordConfirm;
    private String userEmail, userPassword;
    private AppCompatButton btnSignUp;
    private AppCompatTextView gotoSignIn;
    private FirebaseAuth mAuth;
    private static final String TAG = SignupActivity.class.getSimpleName();
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        edtSignUpInEmail = findViewById(R.id.value_signup_email);
        edtSignUpPassword = findViewById(R.id.value_signup_password);
        edtSignUpPasswordConfirm = findViewById(R.id.value_signup_password_confirm);
        btnSignUp = findViewById(R.id.btn_signup);
        gotoSignIn = findViewById(R.id.goto_signin);
        progressBar = findViewById(R.id.progressbar_sign_up);
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
        gotoSignIn.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(), SigninActivity.class));
            finish();
        });
        btnSignUp.setOnClickListener(v -> {
            if (validateInput()) {
                progressBar.setVisibility(View.VISIBLE);
                mAuth.createUserWithEmailAndPassword(userEmail, userPassword).addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        progressBar.setVisibility(View.GONE);
                        Log.d(TAG, getString(R.string.create_user_success));
                        Toast.makeText(getApplicationContext(), getString(R.string.create_user_success), Toast.LENGTH_SHORT).show();
                        redirectToMainActivity();
                    } else {
                        progressBar.setVisibility(View.GONE);
                        Log.w(TAG, getString(R.string.create_user_error), task.getException());
                        Toast.makeText(getApplicationContext(), getString(R.string.create_user_error) + task.getException(), Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }

    //validation input user
    private boolean validateInput() {
        boolean isValid;
        String verifEmail = Objects.requireNonNull(edtSignUpInEmail.getText()).toString().trim();
        String verifPassword = Objects.requireNonNull(edtSignUpPassword.getText()).toString().trim();
        String confirmPassword = Objects.requireNonNull(edtSignUpPasswordConfirm.getText()).toString().trim();
        if (verifEmail.isEmpty()) {
            edtSignUpInEmail.setError(getString(R.string.error_empty_field));
            isValid = false;
        } else if (!verifEmail.contains("@")) {
            edtSignUpInEmail.setError(getString(R.string.error_email_not_correct));
            isValid = false;
        } else if (verifPassword.isEmpty()) {
            Toast.makeText(this, getString(R.string.error_empty_password), Toast.LENGTH_SHORT).show();
            isValid = false;
        } else if (confirmPassword.isEmpty()) {
            Toast.makeText(this, getString(R.string.error_confirm_empty_password), Toast.LENGTH_SHORT).show();
            isValid = false;
        } else if (verifPassword.length() < 8) {
            Toast.makeText(this, getString(R.string.error_password_min_8_char), Toast.LENGTH_LONG).show();
            isValid = false;
        } else if (!verifPassword.equals(confirmPassword)) {
            Toast.makeText(this, getString(R.string.error_password_not_match), Toast.LENGTH_SHORT).show();
            isValid = false;
        } else {
            userEmail = edtSignUpInEmail.getText().toString();
            userPassword = edtSignUpPassword.getText().toString();
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
