package com.example.chirag.slidingtabsusingviewpager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Comment;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Tab3.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Tab3#newInstance} factory method to
 * create an instance of this fragment.
 */

//TODO:朋友圈界面接口
public class Tab3 extends Fragment {
    /*
    TODO: Rename parameter arguments, choose names that match
    the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    */
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    String accountNo;
    private OnFragmentInteractionListener mListener;

    RecyclerView commentRecycler;
    ArrayList<BookComment> bookComments = new ArrayList<>();

    CommentAdapter commentAdapter;

    public boolean shouldRefresh = false;

    public Tab3() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Tab3.
     */
    // TODO: Rename and change types and number of parameters
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
//        onDestroy();
//        onCreate(null);

//        Log.e("书评大小！！!on Resume1",Integer.toString(bookComments.size()));
//
        loadbook("pipilu");
        SharedPreferences m = PreferenceManager.getDefaultSharedPreferences(getContext());
//        try {
//            Thread.sleep(3000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

        bookComments = convert(m.getString("commentResponse", ""), accountNo);

//        Log.e("response",m.getString("commentResponse",""));
//        Log.e("书评大小！！!on Resume",Integer.toString(bookComments.size()));
//
//
//        Calendar calendar = Calendar.getInstance();
//        Date date1 = calendar.getTime();
//        BookComment bc1 = new BookComment(0, "david", date1, "i am david", 1, 0);

        //  bookComments.add(bc1);
        //        commentAdapter.addItem(commentAdapter.getItemCount(),bc1);
        commentAdapter.notifyDataSetChanged();


    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        accountNo = getActivity().getIntent().getStringExtra("accountNo");
//        Log.e("Tab3", accountNo);

        String temp = getActivity().getIntent().getStringExtra("refresh");

//        if (temp == null) {
//            Log.e("should refresh?", "Yes");
//        }

        if (temp == "true") {
            shouldRefresh = true;
            //Log.e("should refresh?","Yes");
        }

        if (shouldRefresh) {
            Log.e("should refresh?", "Yes");
        }

        Log.e("书评大小！！!on Create 1", Integer.toString(bookComments.size()));
        loadbook("pipilu");
        SharedPreferences m = PreferenceManager.getDefaultSharedPreferences(getContext());

        bookComments = convert(m.getString("commentResponse", ""), accountNo);


        Log.e("response", m.getString("commentResponse", ""));
        Log.e("书评大小！！!on create2", Integer.toString(bookComments.size()));




    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tab3, container, false);


        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {//go to barcode scan


                Intent intent = new Intent(getActivity(), WriteCommentActivity.class);
                intent.putExtra("accountNo", accountNo);


                startActivity(intent);

//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
            }
        });

        commentRecycler = view.findViewById(R.id.comment_recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        commentRecycler.setLayoutManager(linearLayoutManager);


        Date date = new Date();
        System.out.println(date);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String dateNowStr = sdf.format(date);

        System.out.println("格式化后的日期：" + dateNowStr);


        Calendar calendar = Calendar.getInstance();

        Date date1 = calendar.getTime();
        calendar.add(Calendar.DAY_OF_MONTH, -3);//正数可以得到当前时间+n天，负数可以得到当前时间-n天

        Date date2 = calendar.getTime();


        System.out.println("获取当前时间未来的第三天：" + date);
        calendar.setTime(date);

        String time = sdf.format(date);
        System.out.println("格式化获取当前时间未来的第三天：" + time);

        calendar.add(Calendar.DAY_OF_MONTH, -4);//正数可以得到当前时间+n天，负数可以得到当前时间-n天

        Date date3 = calendar.getTime();
        calendar.add(Calendar.DAY_OF_MONTH, -5);//正数可以得到当前时间+n天，负数可以得到当前时间-n天

        Date date4 = calendar.getTime();

        ////0: 他人的评论，1：我的评论，2：他人的热评，3：我的热评

        BookComment bc1 = new BookComment(0, "david", date1, "i am david", 1, 0);

        BookComment bc2 = new BookComment(1, "effy", date2, "i am effy", 30, 1);

        BookComment bc3 = new BookComment(2, "lucy", date1, "i am david", 1, 2);

        BookComment bc4 = new BookComment(3, "effy", date2, "i am effy", 100, 3);


//        bookComments.add(bc1);
//        bookComments.add(bc2);
//        bookComments.add(bc3);
//        bookComments.add(bc4);


        commentAdapter = new CommentAdapter(getActivity(), accountNo, bookComments);

        Log.e("书评大小！！!!!1", Integer.toString(bookComments.size()));
        commentRecycler.setAdapter(commentAdapter);


        //    commentAdapter.notifyDataSetChanged();

        return view;

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
                Log.e("TAG", error.getMessage(), error);
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


    private ArrayList<BookComment> convert(String result, String username) {
        ArrayList<BookComment> com = new ArrayList<BookComment>();
        Log.e("result1", result);
        try {

            JSONObject jsonObject = (JSONObject) new JSONObject(result).get("params");
            int number = Integer.parseInt(jsonObject.getString("number"));
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
            if (number == 0) {
                Log.e("TAG", "fail");
            } else {
                Log.e("TAG", number + "");
                for (int i = 0; i < number; i++) {

                    try {
                        int kind = 0;
                        if (jsonObject.getString(i + "username").equals(username)) {
                            kind = 1;
                        }
                        if (Integer.parseInt(jsonObject.getString(i + "like")) > 100) {
                            kind += 2;
                        }

                        BookComment a = new BookComment(Integer.parseInt(jsonObject.getString(i + "id")),
                                jsonObject.getString(i + "username"),
                                df.parse(jsonObject.getString(i + "date")),
                                jsonObject.getString(i + "content"), Integer.parseInt(jsonObject.getString(i + "like")), kind);
                        com.add(a);
                        Log.e(i + "comment", jsonObject.getString(i + "id") + "");

// Log.e("TAG",result);
                    } catch (Exception e) {
                        Log.e("TAG", "time error");
                    }

                }

                ArrayList<BookComment> temp = new ArrayList<>();


                Iterator<BookComment> iter = com.iterator();
                while (iter.hasNext()) {
                    BookComment bookComment = iter.next();
                    if (bookComment.kind == 2 || bookComment.kind == 3) {
                        temp.add(bookComment);
                        iter.remove();
                    }
                }


                Collections.sort(temp, new com());

                Collections.sort(com, new com());

                for (BookComment bookComment : com) {
                    temp.add(bookComment);
                }
                com = temp;


            }


        } catch (JSONException e) {
//做自己的请求异常操作，如Toast提示（“无网络连接”等）
            Log.e("TAG", e.getMessage(), e);
        }
        return com;
    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
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


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
