package com.app.plyss.ui.signup;

import androidx.annotation.NonNull;
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

import com.app.plyss.R;
import com.app.plyss.data.model.User;
import com.app.plyss.ui.HomeActivity;
import com.app.plyss.ui.login.LoginActivity;
import com.app.plyss.utils.AppGlobals;
import com.app.plyss.utils.AppUtils;
import com.app.plyss.utils.Vars;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import java.util.Objects;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;

public class SignupActivity extends AppCompatActivity {
    private static final String TAG = "SignupActivity";
    private SignupViewModel viewModel;

    @BindView(R.id.username)
    EditText userName;

    @BindView(R.id.user_email)
    EditText userMail;

    @BindView(R.id.user_phone)
    EditText userPhone;

    @BindView(R.id.user_password)
    EditText userPassword;

    @BindView(R.id.signup_loading)
    ProgressBar signupLoading;

    @BindView(R.id.signup)
    Button signupBtn;

    private User user;
    private Vars vars;
    private UserProfileChangeRequest profileChangeRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        ButterKnife.bind(this);
        vars = new Vars(this);
        user = new User();

        SignupViewModelFactory factory = new SignupViewModelFactory(this.getApplication());
        viewModel = new ViewModelProvider(this, factory).get(SignupViewModel.class);
    }

    @OnClick(R.id.tvLogin)
    void goToLogin() {
        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
    }

    @OnClick(R.id.signup)
    void saveUser() {

        String uuid = UUID.randomUUID().toString();
        String email =  userMail.getText().toString().trim();
        String name =  userName.getText().toString().trim();
        String phone = userPhone.getText().toString().trim();
        String password = userPassword.getText().toString().trim();

        if (name.isEmpty()) {
            userName.setError("Field must not be empty");
            userName.requestFocus();
        } else if (phone.isEmpty()) {
            userPhone.setError("Field must not be empty");
            userPhone.requestFocus();
        } else if (!viewModel.isEmailValid(email)) {
            userMail.setError("Invalid email");
            userMail.requestFocus();
        } else if (!viewModel.isPasswordValid(password)) {
            userPassword.setError("Password must be more than 5 characters");
            userPassword.requestFocus();
        } else {

            user.setEmail(email);
            user.setName(name);
            user.setPhone(phone);
            user.setPassword(password);

            profileChangeRequest = new UserProfileChangeRequest.Builder()
                    .setDisplayName(name)
                    .build();


            createNewUser(email, password);
        }
    }

    private void createNewUser(String email, String password) {
        disableBtn();
        vars.yorubaApp.mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "createUserWithEmail:success");
                        Toasty.success(getApplicationContext(), "Your account has been created successfully.", Toasty.LENGTH_LONG ).show();

                        FirebaseUser firebaseUser = vars.yorubaApp.currentUser;
                        if (firebaseUser != null) {
                            user.setUuid(firebaseUser.getUid());
                            firebaseUser.updateProfile(profileChangeRequest).addOnCompleteListener(task1 -> {
                                if (task1.isSuccessful()) {
                                    Log.d(TAG, "User profile updated ");
                                    saveUserDetails(firebaseUser.getUid());
                                }
                            });
                        }
                    } else {
                        Log.w(TAG, "createUserWithEmail:failure", task.getException());
                        String err = Objects.requireNonNull(task.getException()).getMessage();
                        Toasty.error(getApplicationContext(), err, Toasty.LENGTH_LONG ).show();
                        enableBtn();
                    }
                });

    }

    private void saveUserDetails(String uid) {
        Task<Void> newUser = vars.yorubaApp.db.collection(AppGlobals.USERS).document(uid).set(user);
        newUser.addOnSuccessListener(aVoid -> {
            Toast.makeText(this, "User created", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "addNewRegisteredUser: SUCCESS!!");
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }).addOnFailureListener(e -> {
            Log.e(TAG, "addNewRegisteredUser: ERROR -> ", e );
            Toasty.error(getApplicationContext(), Objects.requireNonNull(e.getMessage()), Toasty.LENGTH_LONG).show();
            enableBtn();
        });

    }

    private void enableBtn() {
        signupLoading.setVisibility(View.GONE);
        signupBtn.setEnabled(true);
        signupBtn.setBackgroundResource(R.drawable.button_background);
    }

    private void disableBtn() {
        signupBtn.setEnabled(false);
        signupLoading.setVisibility(View.VISIBLE);
        signupBtn.setBackgroundResource(R.drawable.inactive_button_bg);
    }

}
