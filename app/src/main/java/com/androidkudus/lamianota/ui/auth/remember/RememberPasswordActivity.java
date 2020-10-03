package com.androidkudus.lamianota.ui.auth.remember;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import com.androidkudus.lamianota.R;
import com.androidkudus.lamianota.ui.auth.signin.SigninActivity;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class RememberPasswordActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private TextInputEditText edtEmailUser;
    private AppCompatButton btnSend;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;
    private String verifInputUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remember_password);
        toolbar = findViewById(R.id.toolbar_remember);
        edtEmailUser = findViewById(R.id.value_remember_email);
        btnSend = findViewById(R.id.btn_send);
        progressBar = findViewById(R.id.progressbar_remember);
        setSupportActionBar(toolbar);
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    protected void onStart() {
        super.onStart();
        toolbar.setTitle("getResources().getString(R.string.remember)");
        toolbar.setTitleTextColor(getResources().getColor(R.color.colorWhite));
        btnSend.setOnClickListener(v -> {
            if (validateInput()) {
                progressBar.setVisibility(View.VISIBLE);
                mAuth.sendPasswordResetEmail(verifInputUser).addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        progressBar.setVisibility(View.GONE);
                        Snackbar.make(v, R.string.your_request_is_success, Snackbar.LENGTH_LONG)
                                .setAction(R.string.sign_in, v1 -> {
                                    startActivity(new Intent(getApplicationContext(), SigninActivity.class));
                                    finish();
                                }).show();
                    } else {
                        progressBar.setVisibility(View.GONE);
                        Snackbar snackbar = Snackbar.make(v, R.string.your_request_is_failed, Snackbar.LENGTH_LONG);
                        snackbar.setAction(R.string.try_again, v2 -> snackbar.dismiss()).show();
                    }
                });
            }
        });
    }

    //validation input user
    private boolean validateInput() {
        boolean isValid;
        String verifEmail = Objects.requireNonNull(edtEmailUser.getText()).toString().trim();
        if (verifEmail.isEmpty()) {
            edtEmailUser.setError(getString(R.string.error_empty_field));
            isValid = false;
        } else if (!verifEmail.contains("@")) {
            edtEmailUser.setError(getString(R.string.error_email_not_correct));
            isValid = false;
        } else {
            verifInputUser = edtEmailUser.getText().toString();
            isValid = true;
        }
        return isValid;
    }
}
