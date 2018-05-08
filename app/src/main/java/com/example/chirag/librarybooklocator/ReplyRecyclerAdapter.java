package com.example.chirag.librarybooklocator;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by effy on 2018/4/7.
 */

public class ReplyRecyclerAdapter extends RecyclerView.Adapter<ReplyRecyclerAdapter.ReplyHolder> {
    private final int ME_TYPE = 0;
    private final int OTHER_TYPE = 1;
    ArrayList<Reply> replyList = new ArrayList<>();
    String commentID;


    private LayoutInflater mInflater;
    String accountNo;


    public ReplyRecyclerAdapter(Context context, String accountNo, ArrayList<Reply> replyList, int commentID) {
        this.replyList = replyList;
        this.mInflater = LayoutInflater.from(context);
        this.accountNo = accountNo;

        this.commentID = Integer.toString(commentID);
    }
    @Override
    public int getItemViewType(int position) {
        Reply reply = replyList.get(position);
        if (reply.user) {
            return ME_TYPE;
        }
        return OTHER_TYPE;
    }



    @Override
    public ReplyHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == ME_TYPE) {
            //hotComment
            View item = mInflater.inflate(R.layout.reply, parent, false);


            return new ReplyRecyclerAdapter.ReplyHolder(item);

        }
        if (viewType == OTHER_TYPE) {
            //hotComment
            View item = mInflater.inflate(R.layout.reply, parent, false);


            return new ReplyRecyclerAdapter.ReplyHolder(item);

        }

        return null;
    }

    @Override
    public void onBindViewHolder(final ReplyHolder holder, final int position) {
        final Reply reply = replyList.get(position);

        holder.username.setText(reply.username);
        holder.replyContent.setText(reply.content);
        holder.time.setText(reply.getTime());


        // if (reply.user) {
        if (reply.username.equals(accountNo) || reply.username.contains(accountNo + " @")) {

            holder.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //  Toast.makeText(holder.context, "delete reply", Toast.LENGTH_SHORT).show();

                    Deletereply(reply.username, reply.commentID, reply.date);

//                    replyList.remove(position);
//                    notifyDataSetChanged();
                    removeItem(position);


                }
            });

        } else {
            holder.delete.setVisibility(View.GONE);
        }


        holder.replyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //    Toast.makeText(holder.context, "reply to reply", Toast.LENGTH_SHORT).show();

                MaterialDialog.Builder dialog = new MaterialDialog.Builder(mInflater.getContext());
                dialog.title("Reply to " + reply.username)
                        .positiveText("Submit")
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                                reply(accountNo, commentID, dialog.getInputEditText().getText().toString());

                                Reply replyByaccount = new Reply(accountNo + " @ " + reply.username, dialog.getInputEditText().getText().toString(), commentID, Calendar.getInstance().getTime(), true);

                                replyList.add(replyByaccount);
                                notifyDataSetChanged();

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
                        .input("Reply Here", "", new MaterialDialog.InputCallback() {
                            @Override
                            public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {

                            }
                        })
                        .show();


                notifyDataSetChanged();
            }
        });


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

                                Log.d("ReplyRecyclerAdapter", "reply2Reply_success");
                            } else {

                                Log.e("ReplyRecyclerAdapter", "reply2Reply_success");
                            }
                        } catch (JSONException e) {
//做自己的请求异常操作，如Toast提示（“无网络连接”等）
                            Log.e("replyreply", e.getMessage(), e);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//做自己的响应错误操作，如Toast提示（“请稍后重试”等）
                Log.e("replyreply", error.getMessage(), error);
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


    public void Deletereply(final String username, final String commentID, final Date date) {
//请求地址
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        final String DATE = df.format(date);
        String url = "http://39.107.109.19:8080/Groupweb/DeleteReplyServlet";    //注①
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
                            String result = jsonObject.getString("result");  //注④
                            if (result.equals("success")) {  //注⑤

                                Log.d("ReplyRecyclerAdapter", "deleteReply_success");
                            } else {

                                Log.e("ReplyRecyclerAdapter", "deleteReply_fail");
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
                params.put("username", username);  //注⑥
                params.put("CommentID", commentID);
                params.put("date", DATE);
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
        return replyList.size();
    }
//    public void addItem(int position) {
//        replyList.add(position,);
//        notifyItemInserted(position);
//    }
//
public void removeItem(int position) {
    replyList.remove(position);
    notifyItemRemoved(position);
}

    public void addItem(int position, Reply reply) {
//        replyUserList.add(position, reply_username);
//
//        //replyMap.put(replyUserList.get(position),tempList);
//        contentList.add(position,reply_comment);

        replyList.add(position, reply);
        notifyDataSetChanged();

    }


    public class ReplyHolder extends RecyclerView.ViewHolder {

        private TextView username;
        private TextView replyContent;
        private TextView time;
        private TextView delete;
        private ImageView replyBtn;
        private Context context;


        public ReplyHolder(View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.tv_reply_username);


            replyContent = itemView.findViewById(R.id.tv_reply_content);

            time = itemView.findViewById(R.id.tv_time);
            delete = itemView.findViewById(R.id.tv_deleteBtn);
            replyBtn = itemView.findViewById(R.id.imgbtn_reply);

            context = itemView.getContext();

        }
    }
}
