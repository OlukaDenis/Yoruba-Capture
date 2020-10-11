package com.app.plyss.ui.forgot_password;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.app.plyss.R;
import com.app.plyss.ui.login.LoginActivity;
import com.app.plyss.utils.AppUtils;
import com.app.plyss.utils.Vars;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;

public class ForgotPasswordActivity extends AppCompatActivity {
    @BindView(R.id.btnResetPassword)
    Button resetPasswordBtn;

    @BindView(R.id.email_reset)
    EditText email;

    @BindView(R.id.resetLoading)
    ProgressBar loading;

    private Vars vars;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        ButterKnife.bind(this);
        vars = new Vars(this);
    }

    @OnClick(R.id.tvToLogin)
    void goToForgot() {
        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
    }

    @OnClick(R.id.btnResetPassword)
    void sendResetLink() {
        String mEmail = email.getText().toString().trim();

        if (!AppUtils.isUserNameValid(mEmail)) {
            email.setError("Invalid email");
            email.requestFocus();
        } else {
            disableBtn();
            vars.yorubaApp.mAuth.sendPasswordResetEmail(mEmail)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toasty.success(getApplicationContext(), "An email has been sent to you.", Toasty.LENGTH_LONG).show();
                            finish();
                        } else {
                            enableBtn();
                            Toasty.error(getApplicationContext(), Objects.requireNonNull(Objects.requireNonNull(task.getException()).getMessage()), Toasty.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    private void enableBtn() {
        loading.setVisibility(View.GONE);
        resetPasswordBtn.setEnabled(true);
        resetPasswordBtn.setBackgroundResource(R.drawable.button_background);
    }

    private void disableBtn() {
        resetPasswordBtn.setEnabled(false);
        loading.setVisibility(View.VISIBLE);
        resetPasswordBtn.setBackgroundResource(R.drawable.inactive_button_bg);
    }
}