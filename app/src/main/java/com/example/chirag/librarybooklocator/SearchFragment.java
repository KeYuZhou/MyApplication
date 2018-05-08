package com.example.chirag.librarybooklocator;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import android.support.v7.widget.LinearLayoutManager;
import android.widget.ListAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.chirag.librarybooklocator.Crawler.Book;
import com.example.chirag.librarybooklocator.Crawler.SearchCrawler;
import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import java.util.Map;

public class SearchFragment extends Fragment implements MaterialSearchView.OnQueryTextListener, MenuItem.OnActionExpandListener, SwipeGestureHelper.OnSwipeListener {


    private Context mContext;

    RecommendAdapter recommendAdapter;
    String accountNo;

    ListAdapter adapter;
    SearchRecommendAdapter searchRecommendAdapter;
    Book book;
    RecyclerView rv_search;
    ArrayList<Book> bookList = new ArrayList<>();
    RecyclerView recyclerView;
    ArrayList<String> mAllValues = new ArrayList<>();

    ArrayList<Book> recommendBooks = new ArrayList<>();
    public AVLoadingIndicatorView indicatorView;
    TextView tv_notFound;
    boolean nocontent = true;
    TextView tv_nocontent;

    boolean closed = false;
    Book searchedBook = new Book();


    Runnable getSearchResults;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = getActivity();
        setHasOptionsMenu(true);


        accountNo = getActivity().getIntent().getStringExtra("accountNo");
        Log.e("searchFragment", accountNo);


        read();

        loadMeiRiTuiJian();
        SharedPreferences m = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String s = m.getString("MeiRiTuiJian", "");
        Book booklist[] = ConvertMeiRi(s);

        Book searched = new Book();
        Log.e("mAllValues", Integer.toString(mAllValues.size()));


        if (!mAllValues.isEmpty()) {
            searched.setBook(mAllValues);
            if (searched.getAvailable().contains("Total")) {
                recommendBooks.add(searched);
                nocontent = false;
            }
        }

        if (booklist[1] != null) {

            for (Book book : booklist) {
                recommendBooks.add(book);
            }
//
            nocontent = false;

        }

    }

    private void save() {


        try {
            String filename = accountNo + ".txt";

//            FileOutputStream outputStream = getActivity().openFileOutput("query.txt",
//                    Activity.MODE_PRIVATE);
            FileOutputStream outputStream = getActivity().openFileOutput(filename,
                    Activity.MODE_PRIVATE);
            String s = new String();
            for (int i = 0; i < mAllValues.size(); i++) {
                s = s + mAllValues.get(i) + "@";
            }
            Log.e("save", mAllValues.get(0));
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

        recommendAdapter.notifyDataSetChanged();
    }

    private void read() {
        try {
            String filename = accountNo + ".txt";
//            FileInputStream inputStream = getActivity().openFileInput("query.txt");
            FileInputStream inputStream = getActivity().openFileInput(filename);

            byte[] bytes = new byte[1024];
            ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
            while (inputStream.read(bytes) != -1) {
                arrayOutputStream.write(bytes, 0, bytes.length);
            }
            inputStream.close();
            arrayOutputStream.close();
            String content = new String(arrayOutputStream.toByteArray());

            String temp[] = content.split("@");


            for (int i = 0; i < temp.length; i++) {
                mAllValues.add(temp[i]);
                Log.e("mAllValues", mAllValues.get(i));
            }


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    @Override
    public void onDetach() {
        super.onDetach();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View layout = inflater.inflate(R.layout.search_fragment, container, false);


        tv_notFound = layout.findViewById(R.id.tv_notFound);
        tv_nocontent = layout.findViewById(R.id.tv_nocontent);
        tv_notFound.setText("Oops... Loading error");
        if (nocontent) {

            tv_nocontent.setVisibility(View.VISIBLE);
        } else {
            tv_nocontent.setVisibility(View.GONE);
        }

        tv_notFound.setVisibility(View.GONE);


        RelativeLayout rl = (RelativeLayout) layout.findViewById(R.id.container);
        indicatorView = layout.findViewById(R.id.avi);
        indicatorView.hide();

        recommendAdapter = new RecommendAdapter(getActivity(), accountNo, recommendBooks);

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
                Book book = recommendAdapter.bookList.get(position);
                Intent intent = new Intent(getActivity(), MainActivity.class);
                intent.putExtra("accountNo", accountNo);
                intent.putExtra("query", book.getTitle());
                intent.putExtra("author", book.getAuthorName());
                intent.putExtra("imgUrl", book.getimageLink());
                intent.putExtra("douban", book.getContent());
                intent.putExtra("callno", book.getCallNo());
                intent.putExtra("publicion", book.getPublisherInformation());
                intent.putExtra("avil", book.getAvailable());
                intent.putExtra("accountNo", accountNo);
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

                searchedBook = searchRecommendAdapter.bookList.get(position);
// public Book(String title, String link, String marcNo, String description, String authorName, String callNo, String publisherInformation, String ISBN, String available, String imgLink, String content) {


                mAllValues.clear();

                Log.e("packinfo", Integer.toString(searchedBook.packInfo().size()));


                mAllValues = searchedBook.packInfo();



                save();
                recommendBooks.remove(0);
                recommendBooks.add(0, searchedBook);
                recommendAdapter.notifyDataSetChanged();

                intent.putExtra("query", searchRecommendAdapter.bookList.get(position).getTitle());

                intent.putExtra("author", searchRecommendAdapter.bookList.get(position).getAuthorName());
                intent.putExtra("imgUrl", searchRecommendAdapter.bookList.get(position).getimageLink());
                intent.putExtra("douban", searchRecommendAdapter.bookList.get(position).getContent());
                intent.putExtra("callno", searchRecommendAdapter.bookList.get(position).getCallNo());
                intent.putExtra("publicion", searchRecommendAdapter.bookList.get(position).getPublisherInformation());
                intent.putExtra("avil", searchRecommendAdapter.bookList.get(position).getAvailable());

                intent.putExtra("accountNo", accountNo);

                startActivity(intent);

            }
        });


        rv_search.setVisibility(View.GONE);


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

        searchView.setHint("Search Here");


        searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {
                closed = false;

                Log.e("shown", "false_closed");
                tv_nocontent.setVisibility(View.GONE);
                rv_search.setVisibility(View.VISIBLE);


                recyclerView.setVisibility(View.GONE);
                tv_notFound.setVisibility(View.GONE);


                searchRecommendAdapter.notifyDataSetChanged();

            }

            @Override
            public void onSearchViewClosed() {

                closed = true;
                Log.e("closed", "true_closed");
                if (nocontent) {
                    tv_nocontent.setVisibility(View.VISIBLE);
                }

                rv_search.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
//
//                handle.removeCallbacks(getSearchResults);
//
//                handle.removeMessages(0);

                tv_notFound.setVisibility(View.GONE);

                bookList.clear();
                searchRecommendAdapter.notifyDataSetChanged();

                indicatorView.hide();


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

                    if (closed) {
                        Log.e("closed", "closed");
                        if (nocontent) {
                            tv_nocontent.setVisibility(View.VISIBLE);
                        }
                    }
                    if (!closed) {

                        // Bitmap bmp=(Bitmap)msg.obj;
                        bookList = (ArrayList<Book>) msg.obj;
                        Log.e("booklistSize", Integer.toString(bookList.size()));

                        if (bookList.isEmpty()) {
                            tv_notFound.setText("Not Found! Please try another keyword!");
                            tv_notFound.setVisibility(View.VISIBLE);

                        } else {
                            tv_notFound.setVisibility(View.GONE);

                            rv_search.setVisibility(View.VISIBLE);
                            for (int i = 0; i < bookList.size(); i++) {
                                searchRecommendAdapter.addItem(i, bookList.get(i));
                            }
                        }
                    }


                    Log.e("search end", Long.toString(System.currentTimeMillis()));
                    //searchRecommendAdapter.notifyDataSetChanged();

                    Log.d("SearchFragment", "loadresults_success");

                    break;
            }
        }


    };


    @Override
    public boolean onQueryTextSubmit(final String query) {


        Bundle bundle = new Bundle();
        bundle.putString("query", query);//这里的values就是我们要传的值


        indicatorView.show();
        tv_notFound.setVisibility(View.GONE);


//        getSearchResults=new Runnable() {
//            @Override
//            public void run() {
//
//
//                System.out.println("booook");
//                Log.e("search start",Long.toString(System.currentTimeMillis()));
//                ArrayList<Book> bookList = SearchCrawler.bookCrawler(query);
//
//
//                Message msg = new Message();
//                msg.what = 0;
//                msg.obj = bookList;
//
//                Log.e("thdSearch", "start");
//                handle.sendMessage(msg);
//            }
//        };
//        new Thread(getSearchResults).start();
//
        new Thread(new Runnable() {

            @Override
            public void run() {


                Log.e("search start", Long.toString(System.currentTimeMillis()));
                ArrayList<Book> bookList = SearchCrawler.bookCrawler(query);


                Message msg = new Message();
                msg.what = 0;
                msg.obj = bookList;

                Log.e("thdSearch", "start");
                handle.sendMessage(msg);
            }
        }).start();


        InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);



        rv_search.setVisibility(View.VISIBLE);


        return true;
    }

    public void loadMeiRiTuiJian() {//book填写pipilu,加载关于pipilu的书评


        String url = "http://39.107.109.19:8080/Groupweb/MeiRiTuiJianServlet";    //注①
        String tag = "loadMeiRi";    //注②

        //取得请求队列
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());

        //防止重复请求，所以先取消tag标识的请求队列
        requestQueue.cancelAll(tag);

        //创建StringRequest，定义字符串请求的请求方式为POST(省略第一个参数会默认为GET方式)
        final StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        sharedMeiRiTuiJianResponse(response);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //做自己的响应错误操作，如Toast提示（“请稍后重试”等）
                Log.e("TAG", error.getMessage(), error);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                return params;
            }
        };

        //设置Tag标签
        request.setTag(tag);

        //将请求添加到队列中
        requestQueue.add(request);

    }

    private void sharedMeiRiTuiJianResponse(String response) {

        SharedPreferences m = PreferenceManager.getDefaultSharedPreferences(getActivity());
        SharedPreferences.Editor editor = m.edit();
        editor.putString("MeiRiTuiJian", response);
        editor.commit();
    }

    private Book[] ConvertMeiRi(String response) {
        if (response != null && !"".equals(response)) {
            //TODO
        }

        Book booklist[] = new Book[7];
        try {
            JSONObject jsonObject = (JSONObject) new JSONObject(response).get("params");  //注③

            for (int i = 0; i < 7; i++) {
                booklist[i] = new Book(jsonObject.getString("Title" + i), jsonObject.getString("Library Link" + i),
                        jsonObject.getString("MarcNo" + i), null,
                        jsonObject.getString("Author" + i),
                        jsonObject.getString("CallNo" + i),
                        jsonObject.getString("Publish Information" + i), null, null,
                        jsonObject.getString("Image Link" + i)
                        , jsonObject.getString("Content" + i)
                );

            }
            //Title
            //Author
            //CallNo
            //Publish Information
            //Library Link
            //MarcNo
            //Image Link


        } catch (JSONException e) {
            //做自己的请求异常操作，如Toast提示（“无网络连接”等）

            Log.e("TAG", e.getMessage(), e);
        }

        return booklist;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        indicatorView.hide();
        bookList.clear();
        tv_notFound.setVisibility(View.GONE);

        searchRecommendAdapter.notifyDataSetChanged();


        return false;

    }

    @Override
    public boolean onMenuItemActionExpand(MenuItem item) {


        return true;
    }

    @Override
    public boolean onMenuItemActionCollapse(MenuItem item) {

        tv_notFound.setVisibility(View.GONE);
        rv_search.setVisibility(View.GONE);
        closed = true;

//        if (nocontent){
//            tv_nocontent.setVisibility(View.VISIBLE);
//        }
        //handle.removeCallbacks(getSearchResults);
        Log.e("clapsed", "closed");

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


