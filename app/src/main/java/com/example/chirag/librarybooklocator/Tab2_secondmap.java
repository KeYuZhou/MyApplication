package com.example.chirag.librarybooklocator;

/**
 * Created by effy on 2018/4/23.
 */


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


public class Tab2_secondmap extends Fragment {

    private Tab2_secSurface mGLSurfaceView;
    private Render2_2 mRenderer;

    private float bookX_2 = 12.5f;
    private float bookY_2 = 4.5f;
    private ImageView next_map;
    String callNo;

    public Tab2_secondmap() {

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


//        AssetManager assetManager = getActivity().getAssets();
//        CallNoToBookshelf callNoToBookshelf = new CallNoToBookshelf(assetManager);
//        int[] location=callNoToBookshelf.findBookShelf(callNo);



        final DisplayMetrics displayMetrics = new DisplayMetrics();
        View view2 = inflater.inflate(R.layout.fragment_tab2_secondmap, container, false);
        mGLSurfaceView = view2.findViewById(R.id.gl_Second_surface_view);
        mGLSurfaceView.setEGLContextClientVersion(2);
        mRenderer = new Render2_2();
        mGLSurfaceView.setRenderer(mRenderer, displayMetrics.density);
        next_map = view2.findViewById(R.id.first_map);
        next_map.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {


                FragmentManager fm = getActivity().getSupportFragmentManager();
                Fragment fragment = new Tab2_firstmap();


                fm.beginTransaction().replace(R.id.content_frame, fragment).commit();
//
//                mGLSurfaceView.setRenderer2(mRenderer2,displayMetrics.density);

            }
        });


        mRenderer.bookX = bookX_2;
        mRenderer.bookY = bookY_2;
        return view2;
    }


}
