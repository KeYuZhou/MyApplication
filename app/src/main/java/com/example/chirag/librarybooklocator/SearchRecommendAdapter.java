package com.example.chirag.librarybooklocator;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.chirag.librarybooklocator.Crawler.Book;

import java.util.ArrayList;

/**
 * Created by effy on 2018/4/19.
 */

public class SearchRecommendAdapter extends RecyclerView.Adapter<SearchRecommendAdapter.SearchRecommendHolder> {
    private LayoutInflater mInflater;
    ArrayList<Book> bookList;
    private SearchRecommendAdapter.OnItemClickListener mOnItemClickListener;

    public SearchRecommendAdapter(Context context, ArrayList<Book> bookList) {
        this.mInflater = LayoutInflater.from(context);
        this.bookList = bookList;
    }

    public static interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(SearchRecommendAdapter.OnItemClickListener listener) {
        mOnItemClickListener = listener;
    }


    public void addItem(int position, Book book) {
//        replyUserList.add(position, reply_username);
//
//        //replyMap.put(replyUserList.get(position),tempList);
//        contentList.add(position,reply_comment);

        bookList.add(position, book);
        notifyDataSetChanged();

    }

    public void removeItem(Book book) {
        bookList.remove(book);
        notifyDataSetChanged();


    }

    @Override
    public SearchRecommendHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View item = mInflater.inflate(R.layout.search_recommend, parent, false);
        item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(v, (int) v.getTag());
                }

            }
        });
        return new SearchRecommendHolder(item);
    }


    @Override
    public void onBindViewHolder(SearchRecommendHolder holder, int position) {
        holder.tv_title.setText(bookList.get(position).getTitle());
        holder.tv_author.setText(bookList.get(position).getAuthorName());
        holder.itemView.setTag(position);

    }

    @Override
    public int getItemCount() {
        return bookList.size();
    }

    public class SearchRecommendHolder extends RecyclerView.ViewHolder {
        private TextView tv_author;
        private TextView tv_title;

        public SearchRecommendHolder(View itemView) {
            super(itemView);
            tv_author = itemView.findViewById(R.id.tv_authorname);
            tv_title = itemView.findViewById(R.id.tv_bookname);


        }
    }
}
