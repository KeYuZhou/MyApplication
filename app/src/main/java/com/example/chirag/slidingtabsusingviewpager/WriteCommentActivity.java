package com.example.chirag.slidingtabsusingviewpager;

import android.content.Intent;
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
import com.shuyu.textutillib.EmojiLayout;
import com.shuyu.textutillib.RichEditBuilder;
import com.shuyu.textutillib.RichEditText;
import com.shuyu.textutillib.listener.OnEditTextUtilJumpListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class WriteCommentActivity extends AppCompatActivity {
    RichEditText richEditText;
    public String accountNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_comment);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        accountNo = getIntent().getStringExtra("accountNo");

        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            // Enable the Up button
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        }

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        richEditText = (RichEditText) findViewById(R.id.emoji_edit_text2);
//        EmojiLayout emojiLayout = new EmojiLayout();
//        emojiLayout.setEditTextSmile(richEditText);
        RichEditBuilder richEditBuilder = new RichEditBuilder();
        richEditBuilder.setEditText(richEditText)
//                .setTopicModels(topicModels)
//                .setUserModels(nameList)
                .setColorAtUser("#FF00C0")
                .setColorTopic("#F0F0C0")
                .setEditTextAtUtilJumpListener(new OnEditTextUtilJumpListener() {
                    @Override
                    public void notifyAt() {
                        //  JumpUtil.goToUserList(MainActivity.this, MainActivity.REQUEST_USER_CODE_INPUT);
                    }

                    @Override
                    public void notifyTopic() {
                        // JumpUtil.goToTopicList(MainActivity.this, MainActivity.REQUEST_TOPIC_CODE_INPUT);
                    }
                })
                .builder();
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
//            case R.id.action_cancel:
//                // User chose the "Favorite" action, mark the current item
//                // as a favorite...
//
//
//              return true;
            case R.id.action_submit:
                // User chose the "Favorite" action, mark the current item
                // as a favorite...
                //intent = new Intent(WriteCommentActivity.this,MainActivity.class);

                comment(accountNo, "pipilu", richEditText.getRealText().toString());




                Log.e("write comment", accountNo);
                Intent intent1 = new Intent(WriteCommentActivity.this, MainActivity.class);
                intent1.putExtra("refresh", "true");

                Log.e("write comment 2", "true");
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
//请求地址
        String url = "http://39.107.109.19:8080/Groupweb/CommentServlet";    //注①
        String tag = "comment";    //注②

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
                            JSONObject jsonObject = (JSONObject) new JSONObject(response).get("params");  //注③
                            String result = jsonObject.getString("Result");  //注④
                            if (result.equals("success")) {  //注⑤

                                Log.e("TAG", "success");
                            } else {

                                Log.e("TAG", "fail");
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

//设置Tag标签
        request.setTag(tag);

//将请求添加到队列中
        requestQueue.add(request);
    }



}
