package com.example.chirag.slidingtabsusingviewpager;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class RegisterActivity extends AppCompatActivity {
    private EditText username;

    private EditText password;
    private EditText password_again;
    private View progressView;
    private View register_form;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


      //  username = findViewById(R.id.et_username_sign_up);
        username = findViewById(R.id.et_username_sign_up);
        password = findViewById(R.id.et_password_signup);
        username.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    attemptRegister();
                    return true;
                }
                return false;
            }
        });
        password_again = findViewById(R.id.et_password_again);


        Button btn_register = findViewById(R.id.register_button);
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterActivity.this,HomeActivity.class);
                startActivity(intent);
            }
        });

        register_form = findViewById(R.id.register_form);
        //mProgressView = findViewById(R.id.login_progress);

    }

    private void attemptRegister() {
        // Reset errors.

        username.setError(null);
        password.setError(null);
        password_again.setError(null);

        // Store values at the time of the login attempt.

        String u = username.getText().toString();
        String p = password.getText().toString();
        String p2=password_again.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(p) && !isPasswordValid(p)) {
            password.setError(getString(R.string.error_invalid_password));
            focusView = password;
            cancel = true;
        }
        if (!TextUtils.isEmpty(p2) && !isPasswordValid(p2)&& p2.equals(p)) {
            password_again.setError(getString(R.string.error_invalid_password));
            focusView = password_again;
            cancel = true;
        }
        if (TextUtils.isEmpty(u)){
//            username.setError();
        }
        //TODO:定义username规则

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
//            showProgress(true);
//            mAuthTask = new LoginActivity.UserLoginTask(email, password);
//            mAuthTask.execute((Void) null);
        }
    }

    private boolean isEmailValid(EditText email) {

        return false;
    }

    private boolean isPasswordValid(String p) {
        return true;
    }


}