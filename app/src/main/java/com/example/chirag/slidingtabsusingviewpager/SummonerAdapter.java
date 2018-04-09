//package com.example.chirag.slidingtabsusingviewpager;
//
//import android.content.Context;
//import android.support.v7.widget.RecyclerView;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import com.silencedut.expandablelayout.ExpandableLayout;
//
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Calendar;
//import java.util.Date;
//import java.util.HashSet;
//import java.util.SimpleTimeZone;
//
///**
// * Created by effy on 2018/4/4.
// */
//
//public class SummonerAdapter extends RecyclerView.Adapter<SummonerAdapter.SummonerHolder>{
//    private LayoutInflater mInflater;
//    private HashSet<Integer> mExpandedPositionSet = new HashSet<>();
//    private ImageView rcm_pic;
//    private TextView rcm_date;
//    private TextView rcm_book;
//    private TextView rcm_background;
//    private int dateSize = 10;
//    String[] date = new String[dateSize];
//    ArrayList<String> dateList = new ArrayList<>();
//    private String[] book={};
//    private String[] background = {};
//    private String[] pic_Urls = {};
//
//
//    public SummonerAdapter(Context context) {
//        this.mInflater = LayoutInflater.from(context);
//    }
//
//    @Override
//    public SummonerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        View item = mInflater.inflate(viewType%2==0?R.layout.daily_recommend_odd :R.layout.daily_recommend_even,parent,false);
//
//        rcm_date = item.findViewById(R.id.rcm_date);
//        rcm_pic = item.findViewById(R.id.rcm_pic);
//        rcm_book = item.findViewById(R.id.rcm_book);
//        rcm_background = item.findViewById(R.id.rcm_backgroud);
//        setRcm_date();
//        //TODO: 填信息！！！
//        rcm_date.setText(date[viewType]);
//
//
//
//
//
//        return new SummonerHolder(item);
//    }
//
//    private void setRcm_date(){
//
//        date[0] = "Today";
//        dateList.add(date[0]);
//        date[1] = "Yesterday";
//        dateList.add(date[1]);
//        Calendar calendar = Calendar.getInstance();
//
//        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
//        calendar.add(Calendar.DATE,-2);
//        for (int i = 2; i<dateSize; i++){
//            calendar.add(Calendar.DATE, -1);
//            date[i] = dateFormat.format(calendar.getTime());
//            dateList.add(date[i]);
//        }
//    }
//
//    @Override
//    public void onBindViewHolder(SummonerHolder holder, int position) {
//        holder.updateItem(position);
//    }
//
//    @Override
//    public int getItemViewType(int position) {
//        return position;
//    }
//
//    @Override
//    public int getItemCount() {
//        return date.length;
//    }
//
//    class SummonerHolder extends RecyclerView.ViewHolder {
//        private ExpandableLayout expandableLayout ;
//        private SummonerHolder(final View itemView) {
//            super(itemView);
//            expandableLayout = (ExpandableLayout) itemView.findViewById(R.id.expandable_layout);
//        }
//
//        private void updateItem(final int position) {
//            expandableLayout.setOnExpandListener(new ExpandableLayout.OnExpandListener() {
//                @Override
//                public void onExpand(boolean expanded) {
//                    registerExpand(position);
//                }
//            });
//            expandableLayout.setExpand(mExpandedPositionSet.contains(position));
//
//        }
//    }
//
//    private void registerExpand(int position) {
//        if (mExpandedPositionSet.contains(position)) {
//            removeExpand(position);
//        }else {
//            addExpand(position);
//        }
//    }
//
//    private void removeExpand(int position) {
//        mExpandedPositionSet.remove(position);
//    }
//
//    private void addExpand(int position) {
//        mExpandedPositionSet.add(position);
//    }
//
//}