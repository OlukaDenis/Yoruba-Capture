package com.dennytech.datacapture.ui.login;

import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.dennytech.datacapture.R;
import com.dennytech.datacapture.data.model.User;
import com.dennytech.datacapture.ui.HomeActivity;
import com.dennytech.datacapture.ui.signup.SignupActivity;
import com.dennytech.datacapture.utils.AppGlobals;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";

    private LoginViewModel loginViewModel;
    EditText email;
    EditText password;
    ProgressBar loading;

    @BindView(R.id.login)
    Button loginButton;

    @BindView(R.id.to_signup)
    TextView to_signup;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ButterKnife.bind(this);

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        loading = findViewById(R.id.loading);

        LoginViewModelFactory factory = new LoginViewModelFactory(this.getApplication());
        loginViewModel = new ViewModelProvider(this, factory).get(LoginViewModel.class);

        loginViewModel.getAllUsers().observe(this, users -> {
            Log.d(TAG, "Number of users: "+users.size());
        });

    }

    @OnClick(R.id.login)
    void logUserIn() {
        String mEmail = email.getText().toString().trim();
        String mPassword = password.getText().toString().trim();
        Log.d(TAG, "logUserIn: "+mEmail+", "+mPassword);

        if (!loginViewModel.isUserNameValid(mEmail)) {
            email.setError("Invalid email");
            email.requestFocus();
        } else if (!loginViewModel.isPasswordValid(mPassword)) {
            password.setError("Invalid password");
            password.requestFocus();
        } else {
            disableBtn();

            User user = loginViewModel.getUser(mEmail);
            if (user != null) {
                if (!user.getPassword().equals(mPassword)) {
                    Toast.makeText(this, " Wrong password", Toast.LENGTH_SHORT).show();
                    enableBtn();
                } else {
                    Toast.makeText(this, user.getName()+" Welcome", Toast.LENGTH_SHORT).show();
                    AppGlobals.logged_in_user_email = user.getEmail();
                    startActivity(new Intent(this, HomeActivity.class));
                    finish();
                }

            } else {
                Toast.makeText(this, "Sorry!, User does not exist!", Toast.LENGTH_SHORT).show();
                enableBtn();
                reset();
            }
        }
    }

    private void enableBtn() {
        loading.setVisibility(View.GONE);
        loginButton.setEnabled(true);
        loginButton.setBackgroundResource(R.drawable.button_background);
    }

    private void disableBtn() {
        loginButton.setEnabled(false);
        loading.setVisibility(View.VISIBLE);
        loginButton.setBackgroundResource(R.drawable.inactive_button_bg);
    }
    private void reset(){
        email.setText("");
        password.setText("");
    }

    @OnClick(R.id.to_signup)
    void createAccount() {
        startActivity(new Intent(this, SignupActivity.class));
    }

}
