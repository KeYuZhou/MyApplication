package com.example.chirag.slidingtabsusingviewpager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.view.menu.MenuBuilder;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.daasuu.bl.ArrowDirection;
import com.daasuu.bl.BubbleLayout;
import com.daasuu.bl.BubblePopupHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import me.kareluo.ui.OptionMenu;
import me.kareluo.ui.OptionMenuView;
import me.kareluo.ui.PopupMenuView;


/**
 * Created by effy on 2018/4/5.
 */

public class ReplyAdapter extends BaseAdapter {

    String[] replyUsers;

    // HashMap<String, ArrayList> replyMap = new HashMap<>();

    ArrayList<Reply> replyList = new ArrayList<>();


    private LayoutInflater mInflater;
    String accountNo;


    public ReplyAdapter(Context context, String accountNo, ArrayList<Reply> replyList) {
        //   this.replyConent = replyContent;
        this.mInflater = LayoutInflater.from(context);
        //    this.replyUsers = replyUsers;
        this.accountNo = accountNo;
        this.replyList = replyList;
        //   initData();
        Log.e("reply!!!!!!!!!!1", Integer.toString(replyList.size()));


    }



    @Override
    public int getCount() {
        return replyList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {


//        replyList = rep;

        final View item = mInflater.inflate(R.layout.reply, null);

        final Reply reply = replyList.get(position);

        TextView reply_user = item.findViewById(R.id.tv_reply_username);
        reply_user.setText(reply.username);

        TextView reply_content = item.findViewById(R.id.tv_reply_content);
        reply_content.setText(reply.content);

        TextView delete = item.findViewById(R.id.tv_deleteBtn);

        ImageView reply_reply = item.findViewById(R.id.imgbtn_reply);


        if (reply.user) {

            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Toast.makeText(item.getContext(), "delete reply", Toast.LENGTH_SHORT).show();

                    replyList.remove(position);
                    notifyDataSetChanged();

                }
            });

        } else {
            delete.setVisibility(View.GONE);
        }

        reply_reply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(item.getContext(), "reply to reply", Toast.LENGTH_SHORT).show();

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




        return item;
    }

    public void addItem(int position, Reply reply) {
//        replyUserList.add(position, reply_username);
//
//        //replyMap.put(replyUserList.get(position),tempList);
//        contentList.add(position,reply_comment);

        replyList.add(position, reply);
        notifyDataSetChanged();

    }


    public void removeItem(int position) {
        //replyMap.remove(replyUserList.get(position));
        replyList.remove(position);

        notifyDataSetChanged();
    }


}
