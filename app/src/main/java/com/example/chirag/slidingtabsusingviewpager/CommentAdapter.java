package com.example.chirag.slidingtabsusingviewpager;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by effy on 2018/4/4.
 */

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentHolder> {
    private int hotCommentLen = 1;
    private int commentLen = 10;
    private int myCommentLen = 1;
    private LayoutInflater mInflater;

    public CommentAdapter(Context context) {
        this.mInflater = LayoutInflater.from(context);
    }



    @Override
    public CommentHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //View item = mInflater.inflate(viewType==0?R.layout.hot_moment_card :R.layout.moment_card,parent,false);


        if (viewType == 0) {
            //hotComment
            View item = mInflater.inflate(R.layout.hot_moment_card, parent, false);
            return new CommentHolder(item);

        }
        if (viewType == 1) {
            //myComment
            View item = mInflater.inflate(R.layout.my_moment_card, parent, false);
            return new CommentHolder(item);
        }
        View item = mInflater.inflate(R.layout.moment_card, parent, false);


        return new CommentHolder(item);
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public void onBindViewHolder(CommentHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return hotCommentLen + commentLen + myCommentLen;
    }

    public class CommentHolder extends RecyclerView.ViewHolder {
        public CommentHolder(View itemView) {
            super(itemView);
        }


    }
}
