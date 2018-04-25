package com.example.chirag.slidingtabsusingviewpager;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Tab2.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Tab2#newInstance} factory method to
 * create an instance of this fragment.
 */
//TODO:地图界面接口
public class Tab2 extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Tab2Surface mGLSurfaceView;
    private Render2 mRenderer;
    private Render2_2 mRenderer2;

    private Button next_map;
    private OnButtonClick onButtonClick;
//    private Tab2_2.OnButtonClick onButtonClick;

    //楼层（8or9）；书架层（1-6）；书架地图编号（1-34）；书架本体编号（1-8）
    private float floornumber = 9;
    private float bookcasefloor = 6;
    private float casenumber1 = 25;
    private int casenumber2 = 5;
//    private float bookX=5;
//    private float bookY=5;
//    private float bookX_2=12.5f;
//    private float bookY_2=4.5f;


    private OnFragmentInteractionListener mListener;

    public Tab2() {
        // Required empty public constructor
    }


    public static Tab2 newInstance(String param1, String param2) {
        Tab2 fragment = new Tab2();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_tab2, container, false);


        View view = inflater.inflate(R.layout.fragment_tab2, container, false);
        Fragment tab2_firstmap = new Tab2_firstmap();
        FragmentManager fragmentManager = getFragmentManager();

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.animator.left_enter, R.animator.left_exit, R.animator.right_enter, R.animator.right_exit);
        fragmentTransaction.replace(R.id.content_frame, tab2_firstmap, "first").commit();

        return view;

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    public OnButtonClick getOnButtonClick() {
        return onButtonClick;
    }

    public void setOnButtonClick(OnButtonClick onButtonClick) {
        this.onButtonClick = onButtonClick;
    }

    public interface OnButtonClick {
        void onClick(View view);
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
