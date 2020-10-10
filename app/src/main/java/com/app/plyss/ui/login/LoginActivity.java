package com.app.plyss.ui.login;

import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.app.plyss.R;
import com.app.plyss.data.model.User;
import com.app.plyss.ui.HomeActivity;
import com.app.plyss.ui.signup.SignupActivity;
import com.app.plyss.utils.AppGlobals;
import com.app.plyss.utils.AppUtils;
import com.app.plyss.utils.Vars;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";

    private LoginViewModel loginViewModel;
    private EditText email;
    private EditText password;
    private ProgressBar loading;

    @BindView(R.id.login)
    Button loginButton;
    
    private User user;
    private Vars vars;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ButterKnife.bind(this);
        vars = new Vars(this);
        user = new User();

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        loading = findViewById(R.id.loading);

        LoginViewModelFactory factory = new LoginViewModelFactory(this.getApplication());
        loginViewModel = new ViewModelProvider(this, factory).get(LoginViewModel.class);

        loginViewModel.getAllUsers().observe(this, users -> {
            Log.d(TAG, "Number of users: "+users.size());
        });

        checkLoggedInUser();

    }

    public void checkLoggedInUser() {
        String userEmail = vars.getSignedUserEmail();
        User mUser = loginViewModel.getUser(userEmail);
        if (mUser != null) {
            AppGlobals.logged_in_user_email = userEmail;
            startActivity(new Intent(this, HomeActivity.class));
            finish();
        }
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
            signIn(mEmail, mPassword);
        }
    }

    @OnClick(R.id.tvCreateAccount)
    void goToSignup() {
        startActivity(new Intent(getApplicationContext(), SignupActivity.class));
    }

    public void signIn(String email, String password){
        vars.yorubaApp.mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, task -> {
            if (task.isSuccessful()) {
                Log.d(TAG, "signInWithEmail:success");
                FirebaseUser currentUser = vars.yorubaApp.currentUser;

                if (currentUser != null) {
                    vars.saveUserEmail(email);
                    user.setEmail(currentUser.getEmail());
                    user.setUuid(currentUser.getUid());
                    user.setName(currentUser.getDisplayName());
                    user.setPhone(currentUser.getPhoneNumber());
                    user.setPassword(password);

                    loginViewModel.addUser(user);
                    startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                } else {
                    Toasty.error(this, "No user found", Toasty.LENGTH_SHORT).show();
                    enableBtn();
                }
            } else {
                // If sign in fails, display a message to the user.
                Log.w(TAG, "signInWithEmail:failure", task.getException());
                Toasty.error(this, "Wrong email or password", Toasty.LENGTH_SHORT).show();
                reset();
                enableBtn();
            }
        });
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

}
