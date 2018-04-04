package com.example.chirag.slidingtabsusingviewpager;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

/**
 * Created by effy on 2018/4/4.
 */

public class CommentAdpater extends RecyclerView.Adapter<CommentAdpater.CommentHolder> {
    @Override
    public CommentHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(CommentHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class CommentHolder extends RecyclerView.ViewHolder {
        public CommentHolder(View itemView) {
            super(itemView);
        }
    }
}
