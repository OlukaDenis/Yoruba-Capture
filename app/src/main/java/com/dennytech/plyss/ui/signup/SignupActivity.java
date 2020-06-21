package com.dennytech.plyss.ui.signup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.dennytech.plyss.R;
import com.dennytech.plyss.data.model.User;
import com.dennytech.plyss.ui.login.LoginActivity;

import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SignupActivity extends AppCompatActivity {
    private static final String TAG = "SignupActivity";
    private SignupViewModel viewModel;

    EditText user_name;
    EditText user_mail;
    EditText user_phone;
    EditText user_password;

    @BindView(R.id.signup_loading)
    ProgressBar signup_loading;

    @BindView(R.id.signup)
    Button signup_btn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        ButterKnife.bind(this);

        user_name = findViewById(R.id.username);
        user_mail = findViewById(R.id.user_email);
        user_phone = findViewById(R.id.user_phone);
        user_password = findViewById(R.id.user_password);


        SignupViewModelFactory factory = new SignupViewModelFactory(this.getApplication());
        viewModel = new ViewModelProvider(this, factory).get(SignupViewModel.class);
    }

    @OnClick(R.id.signup)
    void saveUser() {

        String uuid = UUID.randomUUID().toString();
        String email =  user_mail.getText().toString().trim();
        String name =  user_name.getText().toString().trim();
        String phone = user_phone.getText().toString().trim();
        String password = user_password.getText().toString().trim();


        if (name.isEmpty()) {
            user_name.setError("Field must not be empty");
            user_name.requestFocus();
        } else if (phone.isEmpty()) {
            user_phone.setError("Field must not be empty");
            user_phone.requestFocus();
        } else if (!viewModel.isEmailValid(email)) {
            user_mail.setError("Invalid email");
            user_mail.requestFocus();
        } else if (!viewModel.isPasswordValid(password)) {
            user_password.setError("Password must be more than 5 characters");
            user_password.requestFocus();
        } else {
            signup_loading.setVisibility(View.VISIBLE);
            signup_btn.setEnabled(false);
            signup_btn.setBackgroundResource(R.drawable.inactive_button_bg);

            User user = new User(
                    uuid,
                    name,
                    email,
                    phone,
                    password
            );

            User existing = viewModel.existingUser(email);
            if (existing != null) {
                Toast.makeText(this," User exists", Toast.LENGTH_SHORT).show();
                signup_loading.setVisibility(View.GONE);
                signup_btn.setEnabled(true);
                signup_btn.setBackgroundResource(R.drawable.button_background);
            } else {
                viewModel.addUser(user);
                Log.d(TAG, "User saved: ");
                startActivity(new Intent(this, LoginActivity.class));
                finish();
            }

        }
    }




}
