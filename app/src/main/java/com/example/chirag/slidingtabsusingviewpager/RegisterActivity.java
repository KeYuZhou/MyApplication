package com.example.chirag.slidingtabsusingviewpager;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    private EditText username;

    private EditText password;
    private EditText password_again;
    private View progressView;
    private View register_form;

    ImageView back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
                finish();
                // startActivity(intent);
            }
        });

        username = findViewById(R.id.et_username_sign_up);

        username.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (username.getText().toString().length() >= 25) {
                    username.setError("Username is tooo long");
                    username.requestFocus();
                }

                return false;
            }
        });

        password = findViewById(R.id.et_password_signup);

        password_again = findViewById(R.id.et_password_again);
        password_again.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (!password_again.getText().toString().equals(password.getText().toString())) {
                    password_again.setError("Passwords don't match.");
                    password_again.requestFocus();
                }

                return false;
            }
        });


        Button btn_register = findViewById(R.id.register_button);
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("register", "register");
                register(username.getText().toString(),password.getText().toString(),"77@qq.com");




            }
        });

        register_form = findViewById(R.id.register_form);
        progressView = findViewById(R.id.register_progress);
        // d(R.id.login_progress);

    }

    private void attemptRegister() {
        // Reset errors.

        username.setError(null);
        password.setError(null);
        password_again.setError(null);

        // Store values at the time of the login attempt.

        String u = username.getText().toString();
        String p = password.getText().toString();
        String p2 = password_again.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(p) && !isPasswordValid(p)) {
            password.setError(getString(R.string.error_invalid_password));
            focusView = password;
            cancel = true;
        }
        if (!TextUtils.isEmpty(p2) && !isPasswordValid(p2)) {
            password_again.setError(getString(R.string.error_invalid_password));
            focusView = password_again;
            cancel = true;
        }

        if (TextUtils.isEmpty(u)) {
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

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            register_form.setVisibility(show ? View.GONE : View.VISIBLE);
            register_form.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    register_form.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            progressView.setVisibility(show ? View.VISIBLE : View.GONE);
            progressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    progressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            progressView.setVisibility(show ? View.VISIBLE : View.GONE);
            register_form.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    public void register(final String accountNumber, final String passwords, final String email) {
//请求地址
        String url = "http://39.107.109.19:8080/Groupweb/RegisterServlet";
        String tag = "register";


//取得请求队列
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

//防止重复请求，所以先取消tag标识的请求队列
        requestQueue.cancelAll(tag);

//创建StringRequest，定义字符串请求的请求方式为POST(省略第一个参数会默认为GET方式)
        final StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = (JSONObject) new JSONObject(response).get("params");
                            String result = jsonObject.getString("Result");
                            if (result.equals("success")) {
//注册成功   ***
//todo
//做自己的登录成功操作，如页面跳转


                                Intent intent = new Intent(RegisterActivity.this, HomeActivity.class);
                                intent.putExtra("accountNo", accountNumber);
                                startActivity(intent);
                            } else if (result.equals("uduplicated")) {
                                username.setError("The username has been registered.");
                                username.requestFocus();


//此用户名字已经被注册了****
                            } else if (result.equals("eduplicated")) {

//此邮箱已经被注册了***
                            } else {

                                if (username.getText().toString().isEmpty()) {
                                    username.setError("Please enter your username!");
                                    username.requestFocus();
                                }
                                if (password.getText().toString().isEmpty()) {
                                    password.setError("Please enter your passwords!");
                                    password.requestFocus();
                                }
                                if (password_again.getText().toString().isEmpty()) {
                                    password_again.setError("Please enter your passwords!");
                                    password_again.requestFocus();
                                }
//注册失败
                            }
                        } catch (JSONException e) {
//做自己的请求异常操作，如Toast提示（“无网络连接”等）
                            Log.e("TAG", e.getMessage(), e);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//做自己的响应错误操作，如Toast提示（“请稍后重试”等）
                Snackbar.make(register_form, "Network Error", Snackbar.LENGTH_LONG)
                        .setAction("Action", null)
                        .show();
                Log.e("TAG", error.getMessage(), error);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("AccountNumber", accountNumber);  //注⑥
                params.put("Password", passwords);
                params.put("Email", email);
                return params;
            }
        };

//设置Tag标签
        request.setTag(tag);

//将请求添加到队列中
        requestQueue.add(request);
    }


    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
//        return new CursorLoader(this,
//                // Retrieve data rows for the device user's 'profile' contact.
//                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
//                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), LoginActivity.ProfileQuery.PROJECTION,
//
//                // Select only email addresses.
//                ContactsContract.Contacts.Data.MIMETYPE +
//                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
//                .CONTENT_ITEM_TYPE},
//
//                // Show primary email addresses first. Note that there won't be
//                // a primary email address if the user hasn't specified one.
//                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}