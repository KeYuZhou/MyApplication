package com.example.chirag.librarybooklocator;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


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

import fj.edittextcount.lib.FJEditTextCount;

public class WriteCommentActivity extends AppCompatActivity {

    public String accountNo;
    FJEditTextCount fjEdit;
    String bookTitle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_comment);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        accountNo = getIntent().getStringExtra("accountNo");
        bookTitle = getIntent().getStringExtra("bookTitle");
        setSupportActionBar(toolbar);
        toolbar.setTitle("");


        if (getSupportActionBar() != null) {
            // Enable the Up button


            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        }


        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WriteCommentActivity.this, MainActivity.class);
                intent.putExtra("cancel", "cancel");
                WriteCommentActivity.this.setResult(2, intent);
                finish();
            }
        });


        fjEdit = (FJEditTextCount) findViewById(R.id.fjEdit);




    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.write_comment_toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {

            case R.id.action_submit:


                if (fjEdit.getText().toString().isEmpty()) {

                    Snackbar.make(fjEdit, "Please enter your mind.", Snackbar.LENGTH_LONG)
                            .setAction("Action", null)
                            .show();


                    return true;
                }


                comment(accountNo, bookTitle, fjEdit.getText().toString());

                String[] result = new String[3];
                result[0] = accountNo;
                result[1] = bookTitle;

                result[2] = fjEdit.getText().toString();


                Intent intent1 = new Intent(WriteCommentActivity.this, MainActivity.class);

                intent1.putExtra("result", result);


                WriteCommentActivity.this.setResult(1, intent1);


                //TODO submit comment

                // startActivity(intent1);

                WriteCommentActivity.this.finish();

                return true;
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);
        }
    }

    public void comment(final String accountNumber, final String book, final String comment) {
        String url = "http://39.107.109.19:8080/Groupweb/CommentServlet";
        String tag = "comment";
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.cancelAll(tag);
        final StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = (JSONObject) new JSONObject(response).get("params");
                            String result = jsonObject.getString("Result");
                            if (result.equals("success")) {

                                Log.e("WriteCommentActivity", "writecomment_success");
                            } else {

                                Log.e("WriteCommentActivity", "writecomment_fail");
                            }
                        } catch (JSONException e) {
                            Log.e("WriteCommentActivity", e.getMessage(), e);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("TAG", error.getMessage(), error);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("AccountNumber", accountNumber);  //注⑥
                params.put("Book", book);
                params.put("comment", comment);
                return params;
            }
        };
        request.setTag(tag);
        requestQueue.add(request);
    }



}
