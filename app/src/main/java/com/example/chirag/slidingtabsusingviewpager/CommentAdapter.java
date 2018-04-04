package com.example.chirag.slidingtabsusingviewpager;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.ldoublem.thumbUplib.ThumbUpView;

import org.w3c.dom.Text;

/**
 * Created by effy on 2018/4/4.
 */

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentHolder> {
    private int hotCommentLen = 1;
    private int commentLen = 3;
    private int myCommentLen = 1;
    private LayoutInflater mInflater;
    private final int TOP_COMMENT_VIEWTYPE = 0;
    private final int COMMENT_VIEWTYPE = 1;
    private final int MY_COMMENT_VIEWTYPE = 2;


    private String[] usernames = {"AAA", "BBB", "CCC", "DDD", "EEE"};

    public CommentAdapter(Context context) {
        this.mInflater = LayoutInflater.from(context);
    }



    @Override
    public CommentHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //View item = mInflater.inflate(viewType==0?R.layout.hot_moment_card :R.layout.moment_card,parent,false);


        if (viewType == TOP_COMMENT_VIEWTYPE) {
            //hotComment
            View item = mInflater.inflate(R.layout.hot_moment_card, parent, false);
            return new CommentHolder(item);

        }
        if (viewType == MY_COMMENT_VIEWTYPE) {
            //myComment
            View item = mInflater.inflate(R.layout.my_moment_card, parent, false);
//TODO:只有第一个delete button成功
            TextView tv_delete = item.findViewById(R.id.tv_deleteBtn);
            tv_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(view.getContext(), "!!!", Toast.LENGTH_SHORT).show();

                }
            });
            return new CommentHolder(item);
        }
        View item = mInflater.inflate(R.layout.my_moment_card, parent, false);

        // View item = mInflater.inflate(R.layout.hot_moment_card, parent, false);


        return new CommentHolder(item);
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TOP_COMMENT_VIEWTYPE;
        }
        if (position == 1) {
            return MY_COMMENT_VIEWTYPE;
        }

        return COMMENT_VIEWTYPE;
    }

    @Override
    public void onBindViewHolder(final CommentHolder holder, int position) {

        holder.username.setText(usernames[position]);
        holder.likeNo.setText("1");
        // holder.time.setText();
        holder.thumbUpView.setOnThumbUp(new ThumbUpView.OnThumbUp() {
            @Override
            public void like(boolean like) {
                if (like) {
                    holder.likeNo.setText(String.valueOf(Integer.valueOf(holder.likeNo.getText().toString()) + 1));
                } else {

                    holder.likeNo.setText(String.valueOf(Integer.valueOf(holder.likeNo.getText().toString()) - 1));

                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return usernames.length;
    }

    class CommentHolder extends RecyclerView.ViewHolder {
        private TextView username;
        private TextView comment;
        private TextView time;
        private TextView likeNo;
        private ThumbUpView thumbUpView;


        private CommentHolder(View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.tv_username);
            comment = itemView.findViewById(R.id.tv_comment);
            time = itemView.findViewById(R.id.tv_time);
            likeNo = itemView.findViewById(R.id.tv_likeNo);
            thumbUpView = itemView.findViewById(R.id.tpv);


        }


    }
}
