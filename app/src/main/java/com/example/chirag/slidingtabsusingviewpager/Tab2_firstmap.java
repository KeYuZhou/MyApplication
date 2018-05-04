package com.example.chirag.slidingtabsusingviewpager;

import android.content.res.AssetManager;
import android.support.v4.app.Fragment;
import android.os.Bundle;

import android.support.v4.app.FragmentManager;
import android.telecom.Call;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

/**
 * Created by effy on 2018/4/23.
 */

public class Tab2_firstmap extends Fragment {
    private Tab2Surface mGLSurfaceView;
    private Render2 mRenderer;
    private Render2_2 mRenderer2;

    private ImageView next_map;
    private Tab2.OnButtonClick onButtonClick;
//    private Tab2_2.OnButtonClick onButtonClick;

    //楼层（8or9）；书架层（1-6）；书架地图编号（1-34）；书架本体编号（1-8）
//    private float floornumber = 9;
//    private float bookcasefloor = 6;
//    private float casenumber1 = 25;
//    private int casenumber2 = 5;
    private float floornumber;
    private float bookcasefloor;
    private float casenumber1;
    private int casenumber2;


    String callNo;
    //    private float bookX=5;
//    private float bookY=5;
//    private float bookX_2=12.5f;
//    private float bookY_2=4.5f;
    @Override

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        callNo = getActivity().getIntent().getStringExtra("callno");
        AssetManager assetManager = getActivity().getAssets();
        CallNoToBookshelf callNoToBookshelf = new CallNoToBookshelf(assetManager);
        int[] location = callNoToBookshelf.findBookShelf(callNo);
        this.floornumber = location[0];
        this.bookcasefloor = location[1];
        this.casenumber1 = location[2];
        this.casenumber2 = location[3];
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final DisplayMetrics displayMetrics = new DisplayMetrics();
        View view = inflater.inflate(R.layout.fragment_tab2_firstmap, container, false);
        //float a=view.getWidth;


        mGLSurfaceView = view.findViewById(R.id.gl_surface_view);
        mGLSurfaceView.setEGLContextClientVersion(2);
        mRenderer = new Render2();
        mRenderer2 = new Render2_2();


        mGLSurfaceView.setRenderer(mRenderer, displayMetrics.density);
        //如果能直接在这个fragment读取到按钮被按下然后执行下面这行代码应该即可实现画面转换
        //mGLSurfaceView.setRenderer2(mRenderer2,displayMetrics.density);
        next_map = view.findViewById(R.id.first_map);
        next_map.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {


                FragmentManager fm = getActivity().getSupportFragmentManager();
                Fragment fragment = new Tab2_secondmap();


                fm.beginTransaction().replace(R.id.content_frame, fragment).commit();
//
//                mGLSurfaceView.setRenderer2(mRenderer2,displayMetrics.density);

            }
        });


        mRenderer.casenumber1 = casenumber1;
        mRenderer.casenumber2 = casenumber2;
        mRenderer.floornumber = floornumber;
        mRenderer2.bookcasefloor = bookcasefloor;
        return view;

    }
}
