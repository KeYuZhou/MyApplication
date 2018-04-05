package com.example.chirag.slidingtabsusingviewpager;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import com.ldoublem.thumbUplib.ThumbUpView;

import org.w3c.dom.Text;

import java.net.ConnectException;
import java.util.ArrayList;
import java.util.HashMap;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.content.Intent;

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
/**
 * Created by effy on 2018/4/4.
 */

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentHolder> {
    private int hotCommentLen = 1;
    private int commentLen = 3;
    private int myCommentLen = 1;
    private LayoutInflater mInflater;
    private final int TOP_COMMENT_VIEWTYPE = 0;
    private final int COMMENT_VIEWTYPE = 1;
    private final int MY_COMMENT_VIEWTYPE = 2;
    private final int MY_TOP_COMMENT_VIEWTPE = 3;

    String accountNo;

    // public String accountNo = LoginActivity.user.getAccountNo();


    private String[] usernames = {"AAA", "BBB", "CCC", "DDD", "EEE"};

    private ArrayList<String> uns = new ArrayList<>();

    // private String[] replyusernames= {};
    private String[] replyusernames = {"FFF", "GGG"};
    private String[] replycontent = {"commentF", "comentG"};
    private HashMap<String, String> replymap = new HashMap<>();

    public CommentAdapter(Context context, String accountNo) {
        this.mInflater = LayoutInflater.from(context);
        this.accountNo = accountNo;
        initData();

    }

    public void initData() {
        for (int i = 0; i < usernames.length; i++) {
            uns.add(usernames[i]);
        }
    }



    @Override
    public CommentHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //View item = mInflater.inflate(viewType==0?R.layout.hot_moment_card :R.layout.moment_card,parent,false);


        if (viewType == TOP_COMMENT_VIEWTYPE) {
            //hotComment
            View item = mInflater.inflate(R.layout.hot_moment_card, parent, false);
            return new CommentHolder(item);

        }
        if (viewType == MY_COMMENT_VIEWTYPE) {
            //myComment
            View item = mInflater.inflate(R.layout.my_moment_card, parent, false);

            TextView tv_delete = item.findViewById(R.id.tv_deleteBtn);
            tv_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(view.getContext(), "!!!", Toast.LENGTH_SHORT).show();


                }
            });

            return new CommentHolder(item);
        }
        if (viewType == MY_TOP_COMMENT_VIEWTPE) {
            View item = mInflater.inflate(R.layout.my_top_comment_card, parent, false);

            TextView tv_delete = item.findViewById(R.id.tv_deleteBtn);
            tv_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(view.getContext(), "!!!", Toast.LENGTH_SHORT).show();

                }
            });

            return new CommentHolder(item);

        }


        View item = mInflater.inflate(R.layout.others_moment_card, parent, false);



        return new CommentHolder(item);
    }


    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TOP_COMMENT_VIEWTYPE;
        }
        if (position == 1) {
            return MY_COMMENT_VIEWTYPE;
        }
        if (position == 2) {
            return MY_TOP_COMMENT_VIEWTPE;
        }

        return COMMENT_VIEWTYPE;
    }

    public void addItem(int position) {
        uns.add(position, accountNo);
        notifyItemInserted(position);
    }

    public void removeItem(int position) {
        uns.remove(position);
        notifyItemRemoved(position);
    }



    @Override
    public void onBindViewHolder(final CommentHolder holder, int position) {

        //  holder.username.setText(usernames[position]);
        holder.username.setText(uns.get(position));
        holder.likeNo.setText("1");
        // holder.time.setText();

        if (getItemViewType(position) == MY_TOP_COMMENT_VIEWTPE) {
            holder.thumbUpView.setLike();


        }
        holder.thumbUpView.setOnThumbUp(new ThumbUpView.OnThumbUp() {
            @Override
            public void like(boolean like) {
                if (like) {
                    holder.likeNo.setText(String.valueOf(Integer.valueOf(holder.likeNo.getText().toString()) + 1));
                } else {

                    holder.likeNo.setText(String.valueOf(Integer.valueOf(holder.likeNo.getText().toString()) - 1));

                }
            }
        });


        if (replyusernames.length != 0) {
            Log.e("replyusernames", Integer.toString(replyusernames.length));
            final ReplyAdapter replyAdapter = new ReplyAdapter(holder.context, accountNo, replyusernames, replycontent);


            holder.listView.setAdapter(replyAdapter);

            int totalHeight = refreshReplyViewSize(replyAdapter, holder.listView);

            ViewGroup.LayoutParams params = holder.listView.getLayoutParams();
            params.height = totalHeight + (holder.listView.getDividerHeight() * (replyAdapter.getCount() - 1));
            //listView.getDividerHeight()获取子项间分隔符占用的高度
            //params.height最后得到整个ListView完整显示需要的高度
            holder.listView.setLayoutParams(params);


            holder.imgButton_comment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Toast.makeText(view.getContext(), "let's comment", Toast.LENGTH_SHORT).show();
                    MaterialDialog.Builder dialog = new MaterialDialog.Builder(view.getContext());
                    dialog.title("Add Comment")
                            .positiveText("Submit")
                            .onPositive(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                                    Log.e("tag", dialog.getInputEditText().getText().toString());
                                    Log.e("replyAdapter before", Integer.toString(replyAdapter.getCount()));

                                    reply(accountNo, "2", dialog.getInputEditText().getText().toString());
                                    replyAdapter.addItem(replyAdapter.getCount(), accountNo, dialog.getInputEditText().getText().toString());


                                    int totalHeight = refreshReplyViewSize(replyAdapter, holder.listView);

                                    ViewGroup.LayoutParams params = holder.listView.getLayoutParams();
                                    params.height = totalHeight + (holder.listView.getDividerHeight() * (replyAdapter.getCount() - 1));
                                    //listView.getDividerHeight()获取子项间分隔符占用的高度
                                    //params.height最后得到整个ListView完整显示需要的高度
                                    holder.listView.setLayoutParams(params);
                                    Log.e("replyAdapter size", Integer.toString(replyAdapter.getCount()));

                                    Log.e("reply success", accountNo);
                                }
                            })
                            .negativeText("Cancel")
                            .onNegative(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                                }
                            })

                            .cancelListener(new DialogInterface.OnCancelListener() {
                                @Override
                                public void onCancel(DialogInterface dialog) {
                                    dialog.dismiss();
                                }
                            })
                            .inputType(InputType.TYPE_TEXT_VARIATION_LONG_MESSAGE)
                            .input("Comment here", "", new MaterialDialog.InputCallback() {
                                @Override
                                public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {

                                }
                            })
                            .show();


                }
            });


        }


    }

    public int refreshReplyViewSize(ReplyAdapter replyAdapter, ListView listView) {
        int totalHeight = 0;
        for (int i = 0, len = replyAdapter.getCount(); i < len; i++) {   //listAdapter.getCount()返回数据项的数目
            View listItem = replyAdapter.getView(i, null, listView);
            listItem.measure(0, 0);  //计算子项View 的宽高
            totalHeight += listItem.getMeasuredHeight();  //统计所有子项的总高度
        }
        return totalHeight;
    }

    public void reply(final String accountNumber, final String commentID, final String content) {
//请求地址
        String url = "http://39.107.109.19:8080/Groupweb/ReplyServlet";    //注①
        String tag = "reply";    //注②

//取得请求队列
        RequestQueue requestQueue = Volley.newRequestQueue(mInflater.getContext());

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
                params.put("CommentID", commentID);
                params.put("content", content);
                return params;
            }
        };

//设置Tag标签
        request.setTag(tag);

//将请求添加到队列中
        requestQueue.add(request);
    }


    @Override
    public int getItemCount() {
        return uns.size();
    }

    class CommentHolder extends RecyclerView.ViewHolder {
        private TextView username;
        private TextView comment;
        private TextView time;
        private TextView likeNo;
        private ThumbUpView thumbUpView;
        private ImageView imgButton_comment;
        private ListView listView;

        private Context context;


        private CommentHolder(View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.tv_username);
            comment = itemView.findViewById(R.id.tv_comment);
            time = itemView.findViewById(R.id.tv_time);
            likeNo = itemView.findViewById(R.id.tv_likeNo);
            thumbUpView = itemView.findViewById(R.id.tpv);
            imgButton_comment = itemView.findViewById(R.id.imgbtn_comment);
            listView = itemView.findViewById(R.id.lv_reply);

            context = itemView.getContext();



        }


    }
}
