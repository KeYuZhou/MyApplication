package com.example.chirag.slidingtabsusingviewpager;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ListFragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Created by effy on 2018/3/25.
 */

public class InitialFragment extends ListFragment {
    private Context mContext;
    private Button button;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
        setHasOptionsMenu(true);



    }


    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.initial_fragment, container, false);

        button = (Button) layout.findViewById(R.id.button_initial);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager=getFragmentManager();


                FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();

                fragmentTransaction.add(R.id.content_frame, new SearchFragment(),"search");
                fragmentTransaction.commit();




            }
        });


        return layout;
    }

}
