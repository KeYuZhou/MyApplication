package com.example.chirag.slidingtabsusingviewpager;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.io.InputStream;
import java.util.Random;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Tab1.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Tab1#newInstance} factory method to
 * create an instance of this fragment.
 */


//TODO: 作为book info界面的接口， 未解决问题：从网上获得图书图片无法显示（ivIntenet)，该线程在之前的版本能够获得图片，但是现在的版本不行，我不知道是怎么回事

public class Tab1 extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private ImageView ivInternet;
    private TextView tvMsgType;
    private Handler handler;
    private Context mContext;

    private OnFragmentInteractionListener mListener;

    public Tab1() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Tab1.
     */
    // TODO: Rename and change types and number of parameters
    public static Tab1 newInstance(String param1, String param2) {
        Tab1 fragment = new Tab1();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        mContext = getActivity();

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                Bitmap bmp = null;
                // 通过消息码确定使用什么方式传递图片信息
                if (msg.what == 0) {
                    bmp = (Bitmap) msg.obj;
                    // tvMsgType.setText("使用obj传递数据");
                } else {
                    Bundle ble = msg.getData();
                    bmp = (Bitmap) ble.get("bmp");
                    // tvMsgType.setText("使用Bundle传递数据");
                }
                // 设置图片到ImageView中
                ivInternet.setImageBitmap(bmp);
            }
        };


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       // return inflater.inflate(R.layout.fragment_tab1, container, false);


       View layout = inflater.inflate(R.layout.fragment_tab1, container, false);
        ListView listView = (ListView) layout.findViewById(android.R.id.list);
//        TextView emptyTextView = (TextView) layout.findViewById(android.R.id.empty);
//        listView.setEmptyView(emptyTextView);
        ivInternet = (ImageView) layout.findViewById(R.id.ivInternet);

        //  btnInternet = (Button) layout.findViewById(R.id.btnInternet);

//            }
//        });
//CustomAdapter customAdapter = new CustomAdapter();
//setListAdapter(customAdapter);
//listView.setAdapter(customAdapter);

        Bundle bundle = getArguments();
        String data = getActivity().getIntent().getStringExtra("query");
//data 为用户的搜索词条 bookname authorname等信息通过data来检索
        TextView tv_bookname = (TextView) layout.findViewById(R.id.tv_bookname);
        TextView tv_authorname = (TextView) layout.findViewById(R.id.tv_authorname);
        TextView tv_publicationname = (TextView) layout.findViewById(R.id.tv_publicationName);
        TextView tv_callNo = (TextView) layout.findViewById(R.id.tv_callNo);
        TextView tv_content = (TextView) layout.findViewById(R.id.tv_content);

        //TODO:setText  such as  book name, author, callNo...
        tv_bookname.setText(data);
        //tv_authorname.setText("")
        //....


//                //清空之前获取的数据
//            tvMsgType.setText("");
        ivInternet.setImageBitmap(null);

        //定义一个线程类
        new Thread() {
            @Override
            public void run() {
                try {
                    //获取网络图片
                    InputStream inputStream = HttpUtils
                            .getImageViewInputStream();
                    Bitmap bitmap = BitmapFactory
                            .decodeStream(inputStream);


                    Message msg = new Message();
                    Random rd = new Random();
                    int ird = rd.nextInt(10);
                    //通过一个随机数，随机选择通过什么方式传递图片信息到消息中
                    if (ird / 2 == 0) {
                        msg.what = 0;
                        msg.obj = bitmap;
                    } else {
                        Bundle bun = new Bundle();
                        bun.putParcelable("bmp", bitmap);
                        msg.what = 1;
                        msg.setData(bun);
                    }
                    //发送消息
                    handler.sendMessage(msg);
                } catch (Exception e) {

                }
            }
        }.start();

        return layout;

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
