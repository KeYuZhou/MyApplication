package com.example.chirag.slidingtabsusingviewpager;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * Created by effy on 2018/4/4.
 */

public class RecommendAdapter extends BaseAdapter{
    private LayoutInflater mInflater;
    private TextView rcm_date;
    private String[] date={"2018/4/1","2018/4/2","2018/4/3"};

    @Override
    public int getCount() {
        return date.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        view = view.inflate(view.getContext(),i%2==0?R.layout.daily_recommend_odd :R.layout.daily_recommend_even,viewGroup);
        rcm_date = view.findViewById(R.id.rcm_date);
        rcm_date.setText(date[i]);

        return null;
    }
}
