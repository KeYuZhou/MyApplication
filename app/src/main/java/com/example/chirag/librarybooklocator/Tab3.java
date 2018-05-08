package com.example.chirag.librarybooklocator;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.chirag.librarybooklocator.Crawler.Httprequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


public class Tab3 extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private String mParam1;
    private String mParam2;

    String accountNo;
    private OnFragmentInteractionListener mListener;

    RecyclerView commentRecycler;
    ArrayList<BookComment> bookComments = new ArrayList<>();

    CommentAdapter commentAdapter;
    String bookTitle;
    TextView tv_noComments;
    public boolean shouldRefresh = false;

    public Tab3() {
        // Required empty public constructor
    }


    public static Tab3 newInstance(String param1, String param2) {
        Tab3 fragment = new Tab3();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onResume() {


        super.onResume();


    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        accountNo = getActivity().getIntent().getStringExtra("accountNo");

        bookTitle = getActivity().getIntent().getStringExtra("query");


        Log.e("bookTab3", bookTitle);


    }


    @SuppressLint("HandlerLeak")
    Handler handle = new Handler() {
        @Override
        public void handleMessage(final Message msg) {
            switch (msg.what) {
                case 0:
                    Log.e("handle", "bookcommets");
                    Bundle bundle = msg.getData();
                    String result = bundle.getString("bookComments");

                    tv_noComments.setVisibility(View.GONE);
                    bookComments = convert(result, accountNo);
                    if (bookComments.isEmpty()) {
                        tv_noComments.setVisibility(View.VISIBLE);
                    }
                    int i = 0;
                    for (BookComment book : bookComments) {
                        commentAdapter.addItem(i, book);
                        i++;
                    }

                    Log.e("Tab3", "load BookComment Success");


                    break;

                case 1:
                    Log.e("handle", "addcomment");
                    tv_noComments.setVisibility(View.GONE);
                    BookComment bookComment = (BookComment) msg.obj;
                    commentAdapter.addItem(0, bookComment);
                    break;
                case 2:


                    int j = 0;
                    for (BookComment book : bookComments) {
                        commentAdapter.addItem(j, book);
                        j++;
                    }

            }
        }

        ;
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tab3, container, false);


        new Thread(new Runnable() {
            @Override
            public void run() {
                String bookComments = Httprequest.loadBookComment(bookTitle);

                Message msg = new Message();
                Bundle bundle = new Bundle();
                bundle.putString("bookComments", bookComments);
                msg.setData(bundle);

                msg.what = 0;


                handle.sendMessage(msg);
                Log.e("thd-loadbook", "start");


            }
        }).start();

        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {//go to barcode scan


                Intent intent = new Intent(getActivity(), WriteCommentActivity.class);
                intent.putExtra("bookTitle", bookTitle);
                intent.putExtra("accountNo", accountNo);


                startActivityForResult(intent, 1);

            }
        });

        commentRecycler = view.findViewById(R.id.comment_recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        commentRecycler.setLayoutManager(linearLayoutManager);


        commentAdapter = new CommentAdapter(getActivity(), accountNo, bookComments);


        commentRecycler.setAdapter(commentAdapter);

        tv_noComments = view.findViewById(R.id.tv_noComment);
        if (bookComments.isEmpty()) {
            tv_noComments.setVisibility(View.VISIBLE);
        }
        return view;

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);


        if (resultCode == 1) {


            String[] temp = data.getStringArrayExtra("result");
            final BookComment bookComment = new BookComment(accountNo, temp[2]);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Message msg = new Message();

                    msg.what = 1;
                    msg.obj = bookComment;

                    handle.sendMessage(msg);
                    Log.e("thd-addcomment", "start");
                }
            }).start();

        }


    }

    private void sharedcommentResponse(String response) {
        SharedPreferences m = PreferenceManager.getDefaultSharedPreferences(getContext());
        SharedPreferences.Editor editor = m.edit();

        editor.putString("commentResponse", response);
        editor.commit();
    }//  此方法放在粘贴即可
    private void sharedResponse(String response) {
        SharedPreferences m = PreferenceManager.getDefaultSharedPreferences(getContext());
        SharedPreferences.Editor editor = m.edit();

        editor.putString("Response", response);
        editor.commit();
    }//  此方法放在粘贴即可

    public void loadbook(final String book) {//book填写pipilu,加载关于pipilu的书评
        String url = "http://39.107.109.19:8080/Groupweb/LoadBookServlet";
        String tag = "load";
//取得请求队列
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
//防止重复请求，所以先取消tag标识的请求队列
        requestQueue.cancelAll(tag);

//创建StringRequest，定义字符串请求的请求方式为POST(省略第一个参数会默认为GET方式)
        final StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        sharedcommentResponse(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//做自己的响应错误操作，如Toast提示（“请稍后重试”等）
                Log.e("loadbook", error.getMessage(), error);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.put("Book", book);

                return params;
            }
        };

//设置Tag标签
        request.setTag(tag);

//将请求添加到队列中
        requestQueue.add(request);

    }

    public static ArrayList<BookComment> convert(String result, String username) {
        ArrayList<BookComment> com = new ArrayList<BookComment>();
        try {


            JSONObject jsonObject = (JSONObject) new JSONObject(result).get("params");
            int number = Integer.parseInt(jsonObject.getString("number"));
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
            if (number == 0) {
                System.out.println("fail");
            } else {
                System.out.println("good");
                for (int i = 0; i < number; i++) {

                    try {
                        int kind = 0;
                        if (jsonObject.getString(i + "username").equals(username)) {
                            kind = 1;
                        }
                        if (Integer.parseInt(jsonObject.getString(i + "like")) > 10) {
                            kind += 2;
                        }

                        BookComment a = new BookComment(Integer.parseInt(jsonObject.getString(i + "id")),
                                jsonObject.getString(i + "username"),
                                df.parse(jsonObject.getString(i + "date")),
                                jsonObject.getString(i + "content"), Integer.parseInt(jsonObject.getString(i + "like")), kind);
                        com.add(a);

                        // Log.e("TAG",result);
                    } catch (Exception e) {
                        System.out.println("error");
                    }

                }
                Collections.sort(com, new com());



            }


        } catch (JSONException e) {
            //做自己的请求异常操作，如Toast提示（“无网络连接”等）

        }
        return com;
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
