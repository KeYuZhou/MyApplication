package com.example.chirag.slidingtabsusingviewpager;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;

import android.support.v7.widget.LinearLayoutManager;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
;
import com.example.chirag.slidingtabsusingviewpager.Crawler.Book;
import com.example.chirag.slidingtabsusingviewpager.Crawler.SearchCrawler;
import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.wang.avi.AVLoadingIndicatorView;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SearchFragment extends Fragment implements MaterialSearchView.OnQueryTextListener, MenuItem.OnActionExpandListener, SwipeGestureHelper.OnSwipeListener {


    String[] values = new String[100];

    private Context mContext;
    HashMap<String, String> map = new HashMap<>();
    RecommendAdapter recommendAdapter;
    String accountNo;
    ArrayAdapter mAdapter;
    ListAdapter adapter;
    SearchRecommendAdapter searchRecommendAdapter;
    Book book;
    RecyclerView rv_search;
    ArrayList<Book> bookList = new ArrayList<>();
    RecyclerView recyclerView;
    List<String> mAllValues = new ArrayList<>();
    private ListView listView;
    public AVLoadingIndicatorView indicatorView;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
        setHasOptionsMenu(true);

        accountNo = getActivity().getIntent().getStringExtra("accountNo");
        Log.e("searchFragment", accountNo);
        read();
//populateList();




    }


    private void save() {


        try {

            FileOutputStream outputStream = getActivity().openFileOutput("query.txt",
                    Activity.MODE_PRIVATE);
            String s = new String();
            for (int i = 0; i < mAllValues.size(); i++) {
                s = s + " " + mAllValues.get(i);
            }
            outputStream.write(s.getBytes());
            outputStream.flush();
            outputStream.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onResume() {


        super.onResume();
        //save();
        // read ();
    }

    private void read() {
        try {
            FileInputStream inputStream = getActivity().openFileInput("query.txt");
            byte[] bytes = new byte[1024];
            ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
            while (inputStream.read(bytes) != -1) {
                arrayOutputStream.write(bytes, 0, bytes.length);
            }
            inputStream.close();
            arrayOutputStream.close();
            String content = new String(arrayOutputStream.toByteArray());
            String temp[] = content.split(" ");


            for (int i = 0; i < temp.length; i++) {
                mAllValues.add(temp[i]);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static boolean isExternalStorageReadOnly() {
        String extStorageState = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(extStorageState)) {
            return true;
        }
        return false;
    }

    private static boolean isExternalStorageAvailable() {
        String extStorageState = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(extStorageState)) {
            return true;
        }
        return false;
    }


    @Override
    public void onDetach() {
        super.onDetach();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View layout = inflater.inflate(R.layout.search_fragment, container, false);

        //    mAdapter = new ArrayAdapter(layout.getContext(), android.R.layout.simple_list_item_1, mAllValues);
        //   listView = (ListView) layout.findViewById(R.id.list);

//        listView.setAdapter(mAdapter);
        //   listView.setTextFilterEnabled(true);

        RelativeLayout rl = (RelativeLayout) layout.findViewById(R.id.container);
        indicatorView = layout.findViewById(R.id.avi);
        indicatorView.hide();

        recommendAdapter = new RecommendAdapter(getActivity(), accountNo);

        recyclerView = new SnappingSwipingViewBuilder(getActivity())
                .setAdapter(recommendAdapter)
                .setHeadTailExtraMarginDp(17F)
                .setItemMarginDp(8F, 20F, 8F, 20F)
                .setOnSwipeListener(this)
                .setSnapMethod(SnappyLinearLayoutManager.SnappyLinearSmoothScroller.SNAP_CENTER)
                .build();

        if (rl != null) {
            recyclerView.setLayoutParams(new ViewGroup.MarginLayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            rl.addView(recyclerView);
        }


        recommendAdapter.setOnItemClickListener(new RecommendAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(getActivity(), MainActivity.class);
                intent.putExtra("accountNo", accountNo);
                intent.putExtra("query", recommendAdapter.dateList.get(position));
                startActivity(intent);

            }
        });

        rv_search = layout.findViewById(R.id.bookList);
        searchRecommendAdapter = new SearchRecommendAdapter(getActivity(), bookList);
        LinearLayoutManager linearLayout = new LinearLayoutManager(getActivity());
        rv_search.setLayoutManager(linearLayout);
        rv_search.setAdapter(searchRecommendAdapter);

        searchRecommendAdapter.setOnItemClickListener(new SearchRecommendAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {


                Intent intent = new Intent(getActivity(), MainActivity.class);
                String title = searchRecommendAdapter.bookList.get(position).getTitle();

                mAllValues.set(0, title);
                recommendAdapter.notifyDataSetChanged();
                intent.putExtra("query", searchRecommendAdapter.bookList.get(position).getTitle());
                intent.putExtra("ISBN", "9787121325212");
                intent.putExtra("accountNo", accountNo);
                //intent.putExtra("ISBN",bookList.get(0).getMarcNo());
                //intent.putExtra("bookTitle",book.getTitle());
                startActivity(intent);

            }
        });


        rv_search.setVisibility(View.GONE);

//        listView.setVisibility(View.GONE);

        return layout;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.search_menu, menu);
        final MenuItem searchItem = menu.findItem(R.id.action_search);

        final MaterialSearchView searchView = getActivity().findViewById(R.id.search_view);
        searchView.setMenuItem(searchItem);

        searchView.setOnQueryTextListener(this);
        String[] temp = new String[mAllValues.size()];
        for (int i = 0; i < mAllValues.size(); i++) {
            temp[i] = mAllValues.get(i);
        }
        //searchView.setSuggestions(temp);
        searchView.setHint("Search Here");

        //  searchView.setAdapter(mAdapter);

        searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {
                rv_search.setVisibility(View.VISIBLE);


                recyclerView.setVisibility(View.GONE);


                searchRecommendAdapter.notifyDataSetChanged();
//                populateList();
//
////
////                  listView = (ListView) getView().findViewById(android.R.id.list);
////                  listView.setAdapter(mAdapter);
//listView.setVisibility(View.VISIBLE);


            }

            @Override
            public void onSearchViewClosed() {
                rv_search.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
                bookList.clear();
                indicatorView.hide();
                searchRecommendAdapter.notifyDataSetChanged();

            }
        });


        super.onCreateOptionsMenu(menu, inflater);

        super.onCreateOptionsMenu(menu, inflater);
    }

    final Handler handle = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    System.out.println("111");
                    indicatorView.hide();
                    // Bitmap bmp=(Bitmap)msg.obj;
                    bookList = (ArrayList<Book>) msg.obj;
                    Log.e("booklistSize", Integer.toString(bookList.size()));
                    //searchRecommendAdapter = new SearchRecommendAdapter(getActivity(), bookList);
                    rv_search.setVisibility(View.VISIBLE);
                    for (int i = 0; i < bookList.size(); i++) {
                        searchRecommendAdapter.addItem(i, bookList.get(i));
                    }
                    //searchRecommendAdapter.notifyDataSetChanged();


                    //   ivInternet.setImageBitmap(bmp);
                    break;
            }
        }

        ;
    };


    @Override
    public boolean onQueryTextSubmit(final String query) {



        Bundle bundle = new Bundle();
        bundle.putString("query", query);//这里的values就是我们要传的值

        indicatorView.show();

        new Thread(new Runnable() {

            @Override
            public void run() {
                System.out.println("booook");

                ArrayList<Book> bookList = SearchCrawler.bookCrawler(query);
                //Book book = bookList.get(0);


                Message msg = new Message();
                msg.what = 0;
                msg.obj = bookList;

                Log.e("thread", "start");
                handle.sendMessage(msg);
            }
        }).start();



        InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);

        searchRecommendAdapter.notifyDataSetChanged();


        //searchRecommendAdapter.notifyDataSetChanged();

        rv_search.setVisibility(View.VISIBLE);
//        values[mAllValues.size()] = query;


        save();

        // read();




        return true;
    }


    @Override
    public boolean onQueryTextChange(String s) {
        indicatorView.hide();
        bookList.clear();
        searchRecommendAdapter.notifyDataSetChanged();


        return false;

    }

    @Override
    public boolean onMenuItemActionExpand(MenuItem item) {
        return true;
    }

    @Override
    public boolean onMenuItemActionCollapse(MenuItem item) {


        return true;
    }

    @Override
    public void onSwipe(RecyclerView rv, int adapterPosition, float dy) {
        recommendAdapter.removeItem(adapterPosition);
        rv.invalidateItemDecorations();
    }

    public interface OnItem1SelectedListener {
        void OnItem1SelectedListener(String item);
    }


}


