package com.example.chirag.slidingtabsusingviewpager;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import com.ldoublem.thumbUplib.ThumbUpView;

import org.w3c.dom.Text;

import java.net.ConnectException;
import java.util.HashMap;

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
    private final int MY_TOP_COMMENT_VIEWTPE = 3;


    private String[] usernames = {"AAA", "BBB", "CCC", "DDD", "EEE"};

    // private String[] replyusernames= {};
    private String[] replyusernames = {"FFF", "GGG", "DDD"};
    private String[] replycontent = {"commentF", "commentG", "commentD"};
    private HashMap<String, String> replymap = new HashMap<>();

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

            TextView tv_delete = item.findViewById(R.id.tv_deleteBtn);
            tv_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(view.getContext(), "!!!", Toast.LENGTH_SHORT).show();

                }
            });

            return new CommentHolder(item);
        }
        if (viewType == MY_TOP_COMMENT_VIEWTPE) {
            View item = mInflater.inflate(R.layout.my_top_comment_card, parent, false);

            TextView tv_delete = item.findViewById(R.id.tv_deleteBtn);
            tv_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(view.getContext(), "!!!", Toast.LENGTH_SHORT).show();

                }
            });

            return new CommentHolder(item);

        }


        View item = mInflater.inflate(R.layout.others_moment_card, parent, false);



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
        if (position == 2) {
            return MY_TOP_COMMENT_VIEWTPE;
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

        holder.imgButton_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Toast.makeText(view.getContext(), "let's comment", Toast.LENGTH_SHORT).show();
                MaterialDialog.Builder dialog = new MaterialDialog.Builder(view.getContext());
                dialog.title("Add Comment")
                        .positiveText("Submit")
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                            }
                        })
                        .negativeText("Cancel")
                        .onNegative(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                            }
                        })

                        .cancelListener(new DialogInterface.OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialog) {
                                dialog.dismiss();
                            }
                        })
                        .inputType(InputType.TYPE_TEXT_VARIATION_LONG_MESSAGE)
                        .input("Comment here", "", new MaterialDialog.InputCallback() {
                            @Override
                            public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {

                            }
                        })
                        .show();


            }
        });

        if (replyusernames.length != 0) {
            ReplyAdapter replyAdapter = new ReplyAdapter(holder.context, replyusernames, replycontent);
            holder.listView.setAdapter(replyAdapter);

            int totalHeight = 0;
            for (int i = 0, len = replyAdapter.getCount(); i < len; i++) {   //listAdapter.getCount()返回数据项的数目
                View listItem = replyAdapter.getView(i, null, holder.listView);
                listItem.measure(0, 0);  //计算子项View 的宽高
                totalHeight += listItem.getMeasuredHeight();  //统计所有子项的总高度
            }

            ViewGroup.LayoutParams params = holder.listView.getLayoutParams();
            params.height = totalHeight + (holder.listView.getDividerHeight() * (replyAdapter.getCount() - 1));
            //listView.getDividerHeight()获取子项间分隔符占用的高度
            //params.height最后得到整个ListView完整显示需要的高度
            holder.listView.setLayoutParams(params);


        }









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
        private ImageView imgButton_comment;
        private ListView listView;

        private Context context;


        private CommentHolder(View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.tv_username);
            comment = itemView.findViewById(R.id.tv_comment);
            time = itemView.findViewById(R.id.tv_time);
            likeNo = itemView.findViewById(R.id.tv_likeNo);
            thumbUpView = itemView.findViewById(R.id.tpv);
            imgButton_comment = itemView.findViewById(R.id.imgbtn_comment);
            listView = itemView.findViewById(R.id.lv_reply);

            context = itemView.getContext();



        }


    }
}
