package com.example.chirag.librarybooklocator;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import com.example.chirag.librarybooklocator.Crawler.Httprequest;
import com.ldoublem.thumbUplib.ThumbUpView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;

import android.util.Log;
import android.view.View.OnClickListener;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

/**
 * Created by effy on 2018/4/4.
 */

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentHolder> {
    private final int TOP_COMMENT_VIEWTYPE = 2;
    private final int COMMENT_VIEWTYPE = 0;
    private final int MY_COMMENT_VIEWTYPE = 1;
    private final int MY_TOP_COMMENT_VIEWTPE = 3;
    ArrayList<BookComment> bookComments = new ArrayList<>();
    String accountNo;
    boolean isRemoved = false;
    private int hotCommentLen = 1;
    private int commentLen = 3;
    private int myCommentLen = 1;


    private LayoutInflater mInflater;
    private String[] usernames = {"AAA", "BBB", "CCC", "DDD", "EEE"};
    private ArrayList<String> uns = new ArrayList<>();

    private String[] replyusernames = {"FFF", "GGG"};
    private String[] replycontent = {"commentF", "comentG"};
    private HashMap<String, String> replymap = new HashMap<>();

    public CommentAdapter(Context context, String accountNo, ArrayList<BookComment> bookComments) {
        this.bookComments = bookComments;
        this.mInflater = LayoutInflater.from(context);
        this.accountNo = accountNo;


    }

    public void initData() {
        for (int i = 0; i < usernames.length; i++) {
            uns.add(usernames[i]);
        }
    }


    @Override
    public CommentHolder onCreateViewHolder(ViewGroup parent, final int viewType) {
        // BookComment bookComment = bookComments.get(viewType);
        //int vt = bookComment.kind;
        Log.e("viewtype", Integer.toString(viewType));
        Log.e("kind", Integer.toString(viewType));

        if (viewType == TOP_COMMENT_VIEWTYPE) {
            //hotComment
            View item = mInflater.inflate(R.layout.my_top_comment_card, parent, false);


            return new CommentHolder(item);

        }
        if (viewType == MY_COMMENT_VIEWTYPE) {
            //myComment
            View item = mInflater.inflate(R.layout.my_top_comment_card, parent, false);

            return new CommentHolder(item);
        }
        if (viewType == MY_TOP_COMMENT_VIEWTPE) {
            View item = mInflater.inflate(R.layout.my_top_comment_card, parent, false);

            return new CommentHolder(item);

        }


        View item = mInflater.inflate(R.layout.my_top_comment_card, parent, false);

        return new CommentHolder(item);

    }


    @Override
    public int getItemViewType(int position) {

        return bookComments.get(position).kind;
        //return position;
    }

    public void addItem(int position, BookComment bookComment) {
        // uns.add(position, accountNo);
        bookComments.add(position, bookComment);
        notifyItemInserted(position);
    }

    public void removeItem(int position) {
        uns.remove(position);
        notifyItemRemoved(position);
    }


    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(final CommentHolder holder, final int position) {


        final BookComment bookComment = bookComments.get(position);


        holder.username.setText(bookComment.usernmae);

        holder.likeNo.setText(Integer.toString(bookComment.like));
        holder.time.setText(bookComment.getTime());
        holder.comment.setText(bookComment.content);

        loadupvote(bookComment.usernmae, Integer.toString(bookComment.id));//用户名和commentid

        SharedPreferences m2 = PreferenceManager.getDefaultSharedPreferences(mInflater.getContext());

        String s = m2.getString("upvoteResponse", "");


        Log.e("response", s);

        if (getItemViewType(position) == COMMENT_VIEWTYPE) {
            holder.delete.setVisibility(View.GONE);
            holder.top.setVisibility(View.GONE);
        }
        if (getItemViewType(position) == TOP_COMMENT_VIEWTYPE) {
            holder.delete.setVisibility(View.GONE);

        }
        if (getItemViewType(position) == MY_COMMENT_VIEWTYPE) {
            holder.top.setVisibility(View.GONE);


            holder.delete.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {

                    Log.e("bookcomment id", Integer.toString(bookComment.id));

                    DeleteComment(Integer.toString(bookComment.id));

                    bookComments.remove(position);
                    notifyDataSetChanged();


                }
            });
        }


        if (bookComment.kind == MY_TOP_COMMENT_VIEWTPE) {

            holder.delete.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {

                    Log.e("bookcomment id", Integer.toString(bookComment.id));
                    DeleteComment(Integer.toString(bookComment.id));
                    bookComments.remove(position);
                    notifyDataSetChanged();


                }
            });
//            holder.thumbUpView.setLike();

        }


        if (Convertloadupvote(s)) {
            holder.thumbUpView.setLike();

        } else {
            holder.thumbUpView.setUnlike();
        }

        if (bookComment.like == 0) {
            holder.thumbUpView.setUnlike();
        }
        if (bookComment.like == 1) {
            holder.thumbUpView.setLike();
        }


        holder.thumbUpView.setOnThumbUp(new ThumbUpView.OnThumbUp() {
            @Override
            public void like(boolean like) {
                if (like) {
                    holder.likeNo.setText(String.valueOf(Integer.valueOf(holder.likeNo.getText().toString()) + 1));
                    //  loadupvote(bookComment.usernmae,Integer.toString(bookComment.id));//用户名和commentid

                    upvote(accountNo, Integer.toString(bookComment.id));

                } else {

                    holder.likeNo.setText(String.valueOf(Integer.valueOf(holder.likeNo.getText().toString()) - 1));
                    Cancelupvote(Integer.toString(bookComment.id), accountNo);


                }
            }
        });


        ArrayList<Reply> rep = new ArrayList<>();



//        if (bookComment.id == 2) {

//        loadreply(Integer.toString(bookComment.id));//2是书评的id号码，此参数如如书评id
//
//        SharedPreferences m = PreferenceManager.getDefaultSharedPreferences(mInflater.getContext());//复制粘贴
//
//        //   private ArrayList<Reply> Convertreply(String response, String CommentID, String usernanme) {
//        rep = Convertreply(m.getString("Response", ""), Integer.toString(bookComment.id), accountNo);
//
//        Log.e("reppppp", Integer.toString(rep.size()));
//

        final ReplyRecyclerAdapter replyRecyclerAdapter = new ReplyRecyclerAdapter(holder.context, accountNo, rep, bookComment.id);


        holder.recyclerView.setAdapter(replyRecyclerAdapter);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(holder.context);
        holder.recyclerView.setLayoutManager(linearLayoutManager);

        LoadReplyTask loadReplyTask = new LoadReplyTask(replyRecyclerAdapter, holder.context, bookComment);
        loadReplyTask.execute("");
        // final ReplyRecyclerAdapter finalReplyRecyclerAdapter = replyRecyclerAdapter;

        holder.imgButton_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Toast.makeText(view.getContext(), "let's comment", Toast.LENGTH_SHORT).show();
                MaterialDialog.Builder dialog = new MaterialDialog.Builder(view.getContext());
                dialog.title("Add Comment to " + bookComment.usernmae)
                        .positiveText("Submit")
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {


                                reply(accountNo, Integer.toString(bookComment.id), dialog.getInputEditText().getText().toString());

                                Reply reply = new Reply(accountNo, dialog.getInputEditText().getText().toString(), Integer.toString(bookComment.id), Calendar.getInstance().getTime(), true);

                                replyRecyclerAdapter.addItem(replyRecyclerAdapter.getItemCount(), reply);
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

    public void upvote(final String accountNumber, final String commentID) {
        //请求地址
        String url = "http://39.107.109.19:8080/Groupweb/UpvoteServlet";    //注①
        String tag = "upvote";    //注②

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
                            if (result.equals("upvoted")) {  //注⑤

                                Log.d("CommentAdapter", "upvote");
                            } else if (result.equals("success")) {

                                Log.d("CommentAdapter", "upvote_success");
                            } else {
                                Log.d("CommentAdapter", "upvote_fail");
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
                params.put("username", accountNumber);  //注⑥
                params.put("CommentID", commentID);

                return params;
            }
        };

        //设置Tag标签
        request.setTag(tag);

        //将请求添加到队列中
        requestQueue.add(request);
    }

    public void Cancelupvote(final String commentID, final String username) {
        //请求地址

        String url = "http://39.107.109.19:8080/Groupweb/CancelupvoteServlet";    //注①
        String tag = "cancelupvote";    //注②

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

                                Log.d("CommentAdapter", "Cancelupvote_success");
                            } else {

                                Log.d("CommentAdapter", "Cancelupvote_success");
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

                params.put("CommentID", commentID);
                params.put("username", username);
                return params;
            }
        };

        //设置Tag标签
        request.setTag(tag);

        //将请求添加到队列中
        requestQueue.add(request);
    }


    public void DeleteComment(final String commentID) {
//请求地址

        String url = "http://39.107.109.19:8080/Groupweb/DeleteCommentServlet";    //注①
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
//成功
                                Log.d("CommentAdater", "deletecomment_success");
                            } else {

                                Log.d("CommentAdapter", "deltecomment_fail");
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

                params.put("CommentID", commentID);

                return params;
            }
        };

//设置Tag标签
        request.setTag(tag);

//将请求添加到队列中
        requestQueue.add(request);
    }

    public void loadupvote(final String username, final String CommentID) {
//请求地址
        String url = "http://39.107.109.19:8080/Groupweb/LoadUpvoteServlet";    //注①
        String tag = "loadreply";    //注②

//取得请求队列
        RequestQueue requestQueue = Volley.newRequestQueue(mInflater.getContext());

//防止重复请求，所以先取消tag标识的请求队列
        requestQueue.cancelAll(tag);

//创建StringRequest，定义字符串请求的请求方式为POST(省略第一个参数会默认为GET方式)
        final StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
// Convertreply(response, CommentID);
                        Log.e("CommentAdater", "loadupvote_success");
                        sharedupvoteResponse(response);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//做自己的响应错误操作，如Toast提示（“请稍后重试”等）
                Log.e("loadreply", error.getMessage(), error);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.put("CommentID", CommentID);
                params.put("username", username);


                return params;
            }
        };

//设置Tag标签
        request.setTag(tag);

//将请求添加到队列中
        requestQueue.add(request);
    }


    private void sharedupvoteResponse(String response) {

        //  Log.e("share", response);
        SharedPreferences m = PreferenceManager.getDefaultSharedPreferences(mInflater.getContext());

        SharedPreferences.Editor editor = m.edit();


        editor.putString("upvoteResponse", response);


        editor.commit();


    }

    private boolean Convertloadupvote(String response) {
        ArrayList<Reply> reply = new ArrayList<Reply>();
        try {
            JSONObject jsonObject = (JSONObject) new JSONObject(response).get("params");  //注③
            String result = jsonObject.getString("result");
            if (result.equals("upvoted")) {
                Log.d("CommentAdapter", "converloadupvote_success");
                return true;
            } else {
                Log.d("CommentAdapter", "converloadupvote_success");
                return false;
            }
        } catch (JSONException e) {
//做自己的请求异常操作，如Toast提示（“无网络连接”等）
            Log.e("TAG", e.getMessage(), e);
        }
        return true;
    }


    private void sharedResponse(String response) {
        SharedPreferences m = PreferenceManager.getDefaultSharedPreferences(mInflater.getContext());
        SharedPreferences.Editor editor = m.edit();
        editor.putString("Response", response);
        editor.commit();
    }


    public void loadreply(final String CommentID) {//传入书评的id


        String url = "http://39.107.109.19:8080/Groupweb/LoadReplyServlet";    //注①
        String tag = "loadreply";    //注②

//取得请求队列
        RequestQueue requestQueue = Volley.newRequestQueue(mInflater.getContext());

//防止重复请求，所以先取消tag标识的请求队列
        requestQueue.cancelAll(tag);

//创建StringRequest，定义字符串请求的请求方式为POST(省略第一个参数会默认为GET方式)
        final StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
// Convertreply(response, CommentID);
                        Log.e("loadreply", CommentID);
                        sharedResponse(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//做自己的响应错误操作，如Toast提示（“请稍后重试”等）
                Log.e("loadReply", error.getMessage(), error);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.put("CommentID", CommentID);


                return params;
            }
        };

//设置Tag标签
        request.setTag(tag);

//将请求添加到队列中
        requestQueue.add(request);
    }


    private ArrayList<Reply> Convertreply(String response, String CommentID, String usernanme) {
        ArrayList<Reply> reply = new ArrayList<Reply>();
        try {
            JSONObject jsonObject = (JSONObject) new JSONObject(response).get("params");  //注③
            int number = Integer.parseInt(jsonObject.getString("number"));  //注④
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");


            if (number == 0) {
                Log.e("convert", "fail");
            } else {
                Log.e("convert", CommentID);
                Log.e("convert", number + " f");
                for (int i = 0; i < number; i++) {
                    boolean user = false;
                    if (usernanme.equals(jsonObject.getString(i + "username"))) {
                        user = true;
                    }
                    try {
                        reply.add(new Reply(jsonObject.getString(i + "username"),
                                jsonObject.getString(i + "content"),
                                CommentID, df.parse(jsonObject.getString(i + "date")), user));
                    } catch (Exception e) {
                        Log.e("TAG", "wrong");
                    }

                }
                Collections.sort(reply, new ComReply());

            }


        } catch (JSONException e) {
//做自己的请求异常操作，如Toast提示（“无网络连接”等）
            Log.e("TAG", e.getMessage(), e);
        }
        return reply;
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

                                Log.e("reply", "success");
                            } else {

                                Log.e("reply", "fail");
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
                Log.e("reply", error.getMessage(), error);
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
        return bookComments.size();
    }

    class CommentHolder extends RecyclerView.ViewHolder {
        private TextView username;
        private TextView comment;
        private TextView time;
        private TextView likeNo;
        private ThumbUpView thumbUpView;
        private ImageView imgButton_comment;
        //private ListView listView;
        private TextView delete;
        private TextView top;

        private RecyclerView recyclerView;


        private Context context;


        private CommentHolder(View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.tv_username);
            comment = itemView.findViewById(R.id.tv_comment);
            time = itemView.findViewById(R.id.tv_time);
            likeNo = itemView.findViewById(R.id.tv_likeNo);
            thumbUpView = itemView.findViewById(R.id.tpv);
            imgButton_comment = itemView.findViewById(R.id.imgbtn_comment);
            //  listView = itemView.findViewById(R.id.lv_reply);
            delete = itemView.findViewById(R.id.tv_deleteBtn);
            top = itemView.findViewById(R.id.tv_top);

            recyclerView = itemView.findViewById(R.id.reply_recyclerView);

            context = itemView.getContext();


        }


    }

    class LoadReplyTask extends AsyncTask<String, Void, ArrayList<Reply>> {

        private ReplyRecyclerAdapter replyRecyclerAdapter;
        private BookComment bookComment;
        private RecyclerView recyclerView;
        private Context context;

        public LoadReplyTask(ReplyRecyclerAdapter replyRecyclerAdapter, Context context, BookComment bookComment) {
            this.replyRecyclerAdapter = replyRecyclerAdapter;
            this.bookComment = bookComment;

            this.context = context;
        }

        @Override
        protected ArrayList<Reply> doInBackground(String... params) {
            String string = params[0];


            ArrayList<Reply> replies = downloadReply(string);
            return replies;
        }

        @Override
        protected void onPostExecute(ArrayList<Reply> replies) {

            int i = 0;
            for (Reply reply : replies) {
                replyRecyclerAdapter.addItem(i, reply);
                i++;
            }

            Log.d("CommentAdapter", "loadReplySuccess");

        }

        private ArrayList<Reply> downloadReply(String imageUrl) {
            ArrayList<Reply> replies = new ArrayList<>();
            String response = Httprequest.loadreply(Integer.toString(bookComment.id));


            return Reply.Convertreply(response, Integer.toString(bookComment.id), bookComment.usernmae);

        }

    }


}
