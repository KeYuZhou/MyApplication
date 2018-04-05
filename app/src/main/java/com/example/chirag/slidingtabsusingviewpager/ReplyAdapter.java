package com.example.chirag.slidingtabsusingviewpager;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by effy on 2018/4/5.
 */

public class ReplyAdapter extends BaseAdapter {

    String[] replyUsers;

    HashMap<String, String> replyMap = new HashMap<>();
    ArrayList<String> rus = new ArrayList<>();
    String[] replyConent;
    private LayoutInflater mInflater;
    String accountNo;
    String dataSize;

    public ReplyAdapter(Context context, String accountNo, String[] replyUsers, String[] replyContent) {
        this.replyConent = replyContent;
        this.mInflater = LayoutInflater.from(context);
        this.replyUsers = replyUsers;
        this.accountNo = accountNo;
        initData();


    }

    public void initData() {
        for (int i = 0; i < replyUsers.length; i++) {

            replyMap.put(replyUsers[i], replyConent[i]);
            rus.add(replyUsers[i]);
        }
    }

    @Override
    public int getCount() {
        return rus.size();
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
    public View getView(int position, View convertView, ViewGroup parent) {
        View item = mInflater.inflate(R.layout.reply, null);
        TextView reply_user = item.findViewById(R.id.tv_reply_username);
        TextView reply_content = item.findViewById(R.id.tv_reply_content);

        reply_user.setText(rus.get(position));
        reply_content.setText(replyMap.get(rus.get(position)));


        return item;
    }

    public void addItem(int position, String reply_username, String reply_comment) {
        rus.add(position, reply_username);
        replyMap.put(rus.get(position), reply_comment);
        notifyDataSetChanged();

    }

    public void removeItem(int position) {
        replyMap.remove(rus.get(position));
        rus.remove(position);

        notifyDataSetChanged();
    }
}
