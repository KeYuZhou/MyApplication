package com.example.chirag.slidingtabsusingviewpager;

import android.annotation.SuppressLint;
import android.app.ListFragment;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.io.InputStream;
import java.util.Random;

/**
 * Created by effy on 2018/3/31.
 */

public class BookInfoFragment extends ListFragment {

    private ImageView ivInternet;
    private TextView tvMsgType;
    private Handler handler;

    private Context mContext;


    String[] bookNames = {"AI", "OS", "AI", "OS", "AI", "OS", "AI", "OS"};


    @SuppressLint("HandlerLeak")
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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


//        //清空之前获取的数据
//        tvMsgType.setText("");
//        ivInternet.setImageBitmap(null);
//        //定义一个线程类
//        new Thread() {
//            @Override
//            public void run() {
//                try {
//                    //获取网络图片
//                    InputStream inputStream = HttpUtils
//                            .getImageViewInputStream();
//                    Bitmap bitmap = BitmapFactory
//                            .decodeStream(inputStream);
//
//
//                    Message msg = new Message();
//                    Random rd = new Random();
//                    int ird = rd.nextInt(10);
//                    //通过一个随机数，随机选择通过什么方式传递图片信息到消息中
//                    if (ird / 2 == 0) {
//                        msg.what = 0;
//                        msg.obj = bitmap;
//                    } else {
//                        Bundle bun = new Bundle();
//                        bun.putParcelable("bmp", bitmap);
//                        msg.what = 1;
//                        msg.setData(bun);
//                    }
//                    //发送消息
//                    handler.sendMessage(msg);
//                } catch (Exception e) {
//
//                }
//            }
//        }.start();


    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.book_info_fragment, container, false);
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
        String data = bundle.getString("query"); //搜索词条


        if (data == null) {
            data = "111";
        }
        TextView tv_bookname = (TextView) layout.findViewById(R.id.tv_bookname);
        TextView tv_authorname = (TextView) layout.findViewById(R.id.tv_authorname);
        TextView tv_publicationname = (TextView) layout.findViewById(R.id.tv_publicationName);
        TextView tv_callNo = (TextView) layout.findViewById(R.id.tv_callNo);
        TextView tv_content = (TextView) layout.findViewById(R.id.tv_content);

        //TODO:setText
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


//    class CustomAdapter extends BaseAdapter {
//
//        @Override
//        public int getCount() {
//            return bookNames.length;
//        }
//
//        @Override
//        public Object getItem(int i) {
//            return null;
//        }
//
//        @Override
//        public long getItemId(int i) {
//            return 0;
//        }
//
//        @Override
//        public View getView(int i, View view, ViewGroup viewGroup) {
//
//            view = getActivity().getLayoutInflater().inflate(R.layout.book_info_1, null);
//            ivInternet = (ImageView) view.findViewById(R.id.ivInternet);
//            //tvMsgType = (TextView) view.findViewById(R.id.tbMsgType);
//
//            TextView tv_bookname = (TextView) view.findViewById(R.id.tv_bookname);
//            TextView tv_authorname = (TextView) view.findViewById(R.id.tv_authorname);
//            TextView tv_publicationname = (TextView) view.findViewById(R.id.tv_publicationName);
//
//            TextView tv_callNo = (TextView) view.findViewById(R.id.tv_callNo);
//
//            tv_bookname.setText(bookNames[i]);
//
////
////        btnInternet.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View v) {
//
//
//            // 定义一个handler，用于接收消息
//            handler = new Handler() {
//                @Override
//                public void handleMessage(Message msg) {
//                    Bitmap bmp = null;
//                    // 通过消息码确定使用什么方式传递图片信息
//                    if (msg.what == 0) {
//                        bmp = (Bitmap) msg.obj;
//                        // tvMsgType.setText("使用obj传递数据");
//                    } else {
//                        Bundle ble = msg.getData();
//                        bmp = (Bitmap) ble.get("bmp");
//                        // tvMsgType.setText("使用Bundle传递数据");
//                    }
//                    // 设置图片到ImageView中
//                    ivInternet.setImageBitmap(bmp);
//                }
//            };
////                //清空之前获取的数据
////            tvMsgType.setText("");
//            ivInternet.setImageBitmap(null);
//            //定义一个线程类
//            new Thread() {
//                @Override
//                public void run() {
//                    try {
//                        //获取网络图片
//                        InputStream inputStream = HttpUtils
//                                .getImageViewInputStream();
//                        Bitmap bitmap = BitmapFactory
//                                .decodeStream(inputStream);
//
//
//                        Message msg = new Message();
//                        Random rd = new Random();
//                        int ird = rd.nextInt(10);
//                        //通过一个随机数，随机选择通过什么方式传递图片信息到消息中
//                        if (ird / 2 == 0) {
//                            msg.what = 0;
//                            msg.obj = bitmap;
//                        } else {
//                            Bundle bun = new Bundle();
//                            bun.putParcelable("bmp", bitmap);
//                            msg.what = 1;
//                            msg.setData(bun);
//                        }
//                        //发送消息
//                        handler.sendMessage(msg);
//                    } catch (Exception e) {
//
//                    }
//                }
//            }.start();
//
//            return view;
//        }
//    }
//

}
