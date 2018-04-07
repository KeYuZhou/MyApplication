package com.example.chirag.slidingtabsusingviewpager;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by effy on 2018/4/7.
 */

public class ReplyRecyclerAdapter extends RecyclerView.Adapter<ReplyRecyclerAdapter.ReplyHolder> {
    private final int ME_TYPE = 0;
    private final int OTHER_TYPE = 1;
    ArrayList<Reply> replyList = new ArrayList<>();


    private LayoutInflater mInflater;
    String accountNo;


    public ReplyRecyclerAdapter(Context context, String accountNo, ArrayList<Reply> replyList) {
        this.replyList = replyList;
        this.mInflater = LayoutInflater.from(context);
        this.accountNo = accountNo;


    }

    @Override
    public long getItemId(int position) {
        Reply reply = replyList.get(position);
        if (reply.user) {
            return ME_TYPE;
        }
        return OTHER_TYPE;
    }


    @Override
    public ReplyHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == ME_TYPE) {
            //hotComment
            View item = mInflater.inflate(R.layout.reply, parent, false);


            return new ReplyRecyclerAdapter.ReplyHolder(item);

        }
        if (viewType == OTHER_TYPE) {
            //hotComment
            View item = mInflater.inflate(R.layout.reply, parent, false);


            return new ReplyRecyclerAdapter.ReplyHolder(item);

        }

        return null;
    }

    @Override
    public void onBindViewHolder(final ReplyHolder holder, final int position) {
        final Reply reply = replyList.get(position);

        holder.username.setText(reply.username);
        holder.replyContent.setText(reply.content);
        holder.time.setText(reply.getTime());


        if (reply.user) {

            holder.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //  Toast.makeText(holder.context, "delete reply", Toast.LENGTH_SHORT).show();

                    replyList.remove(position);
                    notifyDataSetChanged();

                }
            });

        } else {
            holder.delete.setVisibility(View.GONE);
        }


        holder.replyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //    Toast.makeText(holder.context, "reply to reply", Toast.LENGTH_SHORT).show();

                MaterialDialog.Builder dialog = new MaterialDialog.Builder(mInflater.getContext());
                dialog.title("Reply to " + reply.username)
                        .positiveText("Submit")
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {


                                Reply replyByaccount = new Reply(accountNo + " @ " + reply.username, dialog.getInputEditText().getText().toString(), "2", Calendar.getInstance().getTime(), true);
                                replyList.add(replyByaccount);
                                notifyDataSetChanged();

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
                        .input("Reply Here", "", new MaterialDialog.InputCallback() {
                            @Override
                            public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {

                            }
                        })
                        .show();


                notifyDataSetChanged();
            }
        });


    }

    @Override
    public int getItemCount() {
        return replyList.size();
    }
//    public void addItem(int position) {
//        replyList.add(position,);
//        notifyItemInserted(position);
//    }
//
//    public void removeItem(int position) {
//        replyList.remove(position);
//        notifyItemRemoved(position);
//    }

    public void addItem(int position, Reply reply) {
//        replyUserList.add(position, reply_username);
//
//        //replyMap.put(replyUserList.get(position),tempList);
//        contentList.add(position,reply_comment);

        replyList.add(position, reply);
        notifyDataSetChanged();

    }


    public class ReplyHolder extends RecyclerView.ViewHolder {

        private TextView username;
        private TextView replyContent;
        private TextView time;
        private TextView delete;
        private ImageView replyBtn;
        private Context context;


        public ReplyHolder(View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.tv_reply_username);


            replyContent = itemView.findViewById(R.id.tv_reply_content);

            time = itemView.findViewById(R.id.tv_time);
            delete = itemView.findViewById(R.id.tv_deleteBtn);
            replyBtn = itemView.findViewById(R.id.imgbtn_reply);

            context = itemView.getContext();

        }
    }
}
