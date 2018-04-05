package com.example.chirag.slidingtabsusingviewpager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.HashMap;

/**
 * Created by effy on 2018/4/5.
 */

public class ReplyAdapter extends BaseAdapter {

    String[] replyUsers;

    // HashMap<String,String> replyMap;
    String[] replyConent;
    private LayoutInflater mInflater;


    public ReplyAdapter(Context context, String[] replyUsers, String[] replyConent) {
        this.replyConent = replyConent;
        this.mInflater = LayoutInflater.from(context);
        this.replyUsers = replyUsers;
    }


    @Override
    public int getCount() {
        return replyUsers.length;
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

        reply_user.setText(replyUsers[position]);
        reply_content.setText(replyConent[position]);


        return item;
    }
}
