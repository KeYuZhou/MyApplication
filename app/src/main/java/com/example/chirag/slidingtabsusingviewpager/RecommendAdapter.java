package com.example.chirag.slidingtabsusingviewpager;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by effy on 2018/4/8.
 */

public class RecommendAdapter extends RecyclerView.Adapter<RecommendAdapter.RecommendHolder> {
    private final int LAST_SEARCH = 0;
    private final int RECOMMEND = 1;
    private LayoutInflater mInflater;
    private int dateSize = 8;
    String[] date = new String[dateSize];
    ArrayList<String> dateList = new ArrayList<>();
    private OnItemClickListener mOnItemClickListener;

    String accountNo;

    public RecommendAdapter(Context context, String accountNo) {
        this.mInflater = LayoutInflater.from(context);
        this.accountNo = accountNo;
        setRcm_date();

    }

    @Override
    public RecommendAdapter.RecommendHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View item = mInflater.inflate(R.layout.daily_recommend, parent, false);

        item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(v, (int) v.getTag());
                }

            }
        });
        return new RecommendHolder(item);
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return LAST_SEARCH;
        }
        return RECOMMEND;
        //return position;
    }

    @Override
    public void onBindViewHolder(RecommendAdapter.RecommendHolder holder, int position) {

        if (getItemViewType(position) == LAST_SEARCH) {
            holder.tv_time.setText("THE BOOK SEARCHED BEFORE ");
        }
        holder.tv_time.setText(dateList.get(position));
        holder.itemView.setTag(position);
//

//holder.imageView.setOnClickListener(new View.OnClickListener() {
//    @Override
//    public void onClick(View v) {
//
//    }
//});
    }


    @Override
    public int getItemCount() {
        return dateList.size();
    }

    private void setRcm_date() {
        date[0] = "Last time";
        dateList.add(0, "THE BOOK SEARCHED BEFORE");


        dateList.add(1, "Today's Recommendation");

        dateList.add(2, "Yesterday's Recommendation");
        Calendar calendar = Calendar.getInstance();
        String temp = new String();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        calendar.add(Calendar.DATE, -2);
        for (int i = 3; i < dateSize; i++) {
            calendar.add(Calendar.DATE, -1);
            temp = dateFormat.format(calendar.getTime());
            dateList.add(i, temp);
        }
    }

    public void addItem(int position) {
        // uns.add(position, accountNo);
        dateList.add(position, accountNo);
        notifyItemInserted(position);
    }

    public void removeItem(int position) {
        dateList.remove(position);
        notifyItemRemoved(position);
    }


    public static interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mOnItemClickListener = listener;
    }

    public class RecommendHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        private TextView tv_time;
        private TextView tv_bookname;
        private TextView tv_authorname;

        public RecommendHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image);
            tv_time = itemView.findViewById(R.id.tv_time);
            tv_bookname = itemView.findViewById(R.id.tv_bookname);
            tv_authorname = itemView.findViewById(R.id.tv_authorname);

        }
    }
}
