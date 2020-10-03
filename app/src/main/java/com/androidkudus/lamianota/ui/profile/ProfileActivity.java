package com.androidkudus.lamianota.ui.profile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.androidkudus.lamianota.R;
import com.androidkudus.lamianota.ui.auth.signin.SigninActivity;
import com.androidkudus.lamianota.ui.main.MainActivity;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import java.util.Objects;

public class ProfileActivity extends AppCompatActivity {
    private Button btnSignOut, btnSaveProfile;
    private static final String TAG = ProfileActivity.class.getSimpleName();
    private FirebaseAuth mAuth;
    private TextInputEditText tvAccountInfoName, tvAccountInfoEmail, tvAccountInfoNumber, tvAccountInfoPassword, tvAccountInfoConfirmPassword;
    private TextView tvNameProfile;
    private String userNameUpdated, emailUpdated, passwordUpdated;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        btnSignOut = findViewById(R.id.btn_sign_out);
        btnSaveProfile = findViewById(R.id.btn_save_profile);
        tvNameProfile = findViewById(R.id.name_profile);
        tvAccountInfoName = findViewById(R.id.account_info_name);
        tvAccountInfoEmail = findViewById(R.id.account_info_email);
        tvAccountInfoNumber = findViewById(R.id.account_info_number);
        tvAccountInfoPassword = findViewById(R.id.account_info_password);
        tvAccountInfoConfirmPassword = findViewById(R.id.account_info_confirm_password);
        toolbar = findViewById(R.id.toolbar);
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    protected void onStart() {
        super.onStart();
        toolbar.setTitle(getString(R.string.profile));
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_arrow_back));
        toolbar.setNavigationOnClickListener(v -> {startActivity(new Intent(getApplicationContext(), MainActivity.class)); finish();});
        FirebaseUser currentUser = mAuth.getCurrentUser();
        tvNameProfile.setText(Objects.requireNonNull(currentUser).getDisplayName());
        tvAccountInfoName.setText(Objects.requireNonNull(currentUser).getDisplayName());
        tvAccountInfoEmail.setText(Objects.requireNonNull(currentUser).getEmail());
        tvAccountInfoNumber.setText(Objects.requireNonNull(currentUser).getPhoneNumber());
        btnSaveProfile.setOnClickListener(v -> {
            if(validateInput()) {
                userNameUpdated = Objects.requireNonNull(tvAccountInfoName.getText()).toString();
                emailUpdated = Objects.requireNonNull(tvAccountInfoEmail.getText()).toString();
                passwordUpdated = Objects.requireNonNull(tvAccountInfoPassword.getText()).toString();
                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                        .setDisplayName(userNameUpdated)
                        .build();
                currentUser.updateProfile(profileUpdates);
                currentUser.updateEmail(emailUpdated);
                currentUser.updatePassword(passwordUpdated);
                Toast.makeText(this, "Update Successful", Toast.LENGTH_SHORT).show();
            }
        });
        btnSignOut.setOnClickListener( v -> {
            mAuth.signOut();
            Log.w(TAG, getString(R.string.auth_error));
            Toast.makeText(getApplicationContext(), getString(R.string.sign_out_success), Toast.LENGTH_SHORT).show();
            startActivity(new Intent(getApplicationContext(), SigninActivity.class));
            finish();
        });
    }

    //validation input user
    private boolean validateInput() {
        boolean isValid;
        String updateUserName = Objects.requireNonNull(tvAccountInfoName.getText()).toString().trim();
        String updateUserEmail = Objects.requireNonNull(tvAccountInfoEmail.getText()).toString().trim();
        String updateUserPassword = Objects.requireNonNull(tvAccountInfoPassword.getText()).toString().trim();
        String confirmUserPassword = Objects.requireNonNull(tvAccountInfoConfirmPassword.getText()).toString().trim();
        if (updateUserName.isEmpty()) {
            tvAccountInfoName.setError(getString(R.string.error_empty_field));
            isValid = false;
        } else if (!updateUserEmail.contains("@")) {
            tvAccountInfoEmail.setError(getString(R.string.error_email_not_correct));
            isValid = false;
        } else if (updateUserPassword.isEmpty()) {
            Toast.makeText(this, getString(R.string.error_empty_password), Toast.LENGTH_SHORT).show();
            isValid = false;
        } else if (confirmUserPassword.isEmpty()) {
            Toast.makeText(this, getString(R.string.error_confirm_empty_password), Toast.LENGTH_SHORT).show();
            isValid = false;
        } else if (updateUserPassword.length() < 8) {
            Toast.makeText(this, getString(R.string.error_password_min_8_char), Toast.LENGTH_LONG).show();
            isValid = false;
        } else if (!updateUserPassword.equals(confirmUserPassword)) {
            Toast.makeText(this, getString(R.string.error_password_not_match), Toast.LENGTH_SHORT).show();
            isValid = false;
        } else {
            userNameUpdated = tvAccountInfoName.getText().toString();
            emailUpdated = Objects.requireNonNull(tvAccountInfoEmail.getText()).toString();
            passwordUpdated = tvAccountInfoPassword.getText().toString();
            isValid = true;
        }
        return isValid;
    }
}
