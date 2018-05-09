package com.example.chirag.librarybooklocator;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Tab1.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Tab1#newInstance} factory method to
 * create an instance of this fragment.
 */




public class Tab1 extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters

    String accountNo;
    private String mParam1;
    private String mParam2;

    private ImageView ivInternet;
    private TextView tvMsgType;
    String ISBN;
    String imgUrl;

    private Context mContext;
    Handler handle;
    final String url = "https://img3.doubanio.com/lpic/s28509222.jpg";

    private OnFragmentInteractionListener mListener;
    static LruCache<String, Bitmap> mMemoryCache;
    Handler handlerBook;
    private TextView tv_bookname;

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

    @SuppressLint("HandlerLeak")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }


        mContext = getActivity();
        accountNo = getActivity().getIntent().getStringExtra("accountNo");
        handle = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 0:
                        System.out.println("111");
                        Bitmap bmp = (Bitmap) msg.obj;
                        ivInternet.setImageBitmap(bmp);
                        break;

                    case 2:
                        Log.e("handle", "imgURL");
                        Bundle bundle = msg.getData();
                        imgUrl = bundle.getString("imgUrl");


                }
            }

            ;
        };

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


       View layout = inflater.inflate(R.layout.fragment_tab1, container, false);
        ListView listView = (ListView) layout.findViewById(android.R.id.list);

        ivInternet = (ImageView) layout.findViewById(R.id.ivInternet);

        Bundle bundle = getArguments();


        final String data = getActivity().getIntent().getStringExtra("query");

        String author = getActivity().getIntent().getStringExtra("author");

        imgUrl = getActivity().getIntent().getStringExtra("imgUrl");
        String douban = getActivity().getIntent().getStringExtra("douban");
        String callno = getActivity().getIntent().getStringExtra("callno");
        String publication = getActivity().getIntent().getStringExtra("publicion");
        String avilable = getActivity().getIntent().getStringExtra("avil");
        //ISBN = getActivity().getIntent().getStringExtra("ISBN");


        tv_bookname = (TextView) layout.findViewById(R.id.tv_bookname);
        tv_bookname.setText(data);

        TextView tv_authorname = (TextView) layout.findViewById(R.id.tv_authorname);
        tv_authorname.setText(author);

        TextView tv_publicationname = (TextView) layout.findViewById(R.id.tv_publicationName);
        tv_publicationname.setText(publication);

        TextView tv_callNo = (TextView) layout.findViewById(R.id.tv_callNo);
        tv_callNo.setText(callno);

        TextView tv_content = (TextView) layout.findViewById(R.id.tv_content);

        tv_content.setText(douban);

        TextView tv_avil = layout.findViewById(R.id.avaliable);
        tv_avil.setText(avilable);

        TextView tv_location = layout.findViewById(R.id.tv_location);


        CallNoToBookshelf callNoToBookshelf = new CallNoToBookshelf(getActivity().getAssets());
        int[] location = callNoToBookshelf.findBookShelf(callno);
        String place = "Floor " + location[0] + ", Bookshelf " + location[1];
        tv_location.setText(place);


        if (!imgUrl.equals("not found")) {

            new Thread(new Runnable() {

                @Override
                public void run() {

                    Bitmap bmp = getURLimage(imgUrl);
                    Message msg = new Message();
                    msg.what = 0;
                    msg.obj = bmp;
                    System.out.println("000");
                    handle.sendMessage(msg);
                }
            }).start();

        }


        return layout;

    }

    public Bitmap getURLimage(String url) {
        Bitmap bmp = null;

        try {
            URL myurl = new URL(url);
            // 获得连接
            HttpURLConnection conn = (HttpURLConnection) myurl.openConnection();
            conn.setConnectTimeout(6000);//设置超时
            conn.setDoInput(true);
            conn.setUseCaches(false);//不缓存
            conn.connect();
            InputStream is = conn.getInputStream();//获得图片的数据流
            bmp = BitmapFactory.decodeStream(is);
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bmp;
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

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
