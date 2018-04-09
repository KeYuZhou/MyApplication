package com.example.chirag.slidingtabsusingviewpager;

import android.Manifest;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ListFragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.TouchDelegate;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;
;
import com.github.czy1121.view.TurnCardListView;
import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.silencedut.expandablelayout.ExpandableLayout;
import com.wangxiandeng.swipecardrecyclerview.ItemRemovedListener;
import com.wangxiandeng.swipecardrecyclerview.SwipeCardLayoutManager;
import com.wangxiandeng.swipecardrecyclerview.SwipeCardRecyclerView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.SimpleTimeZone;
import java.util.prefs.Preferences;

import me.yuqirong.cardswipelayout.CardItemTouchHelperCallback;
import me.yuqirong.cardswipelayout.CardLayoutManager;
import me.yuqirong.cardswipelayout.OnSwipeListener;

public class SearchFragment extends Fragment implements MaterialSearchView.OnQueryTextListener, MenuItem.OnActionExpandListener, SwipeGestureHelper.OnSwipeListener {


    String[] values = new String[100];

    private Context mContext;
    HashMap<String, String> map = new HashMap<>();
    RecommendAdapter recommendAdapter;
    String accountNo;

    ArrayList<String> stringArrayList = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
        setHasOptionsMenu(true);

        accountNo = getActivity().getIntent().getStringExtra("accountNo");
        Log.e("searchFragment", accountNo);
        read();



    }

    private void save() {


        try {

            FileOutputStream outputStream = getActivity().openFileOutput("query.txt",
                    Activity.MODE_PRIVATE);
            String s = new String();
            for (int i = 0; i < stringArrayList.size(); i++) {
                s = s + " " + stringArrayList.get(i);
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
        read();
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
                stringArrayList.add(temp[i]);
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


        RelativeLayout rl = (RelativeLayout) layout.findViewById(R.id.container);


        recommendAdapter = new RecommendAdapter(getActivity(), accountNo);

        RecyclerView recyclerView = new SnappingSwipingViewBuilder(getActivity())
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



        return layout;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.search_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);

        MaterialSearchView searchView = getActivity().findViewById(R.id.search_view);
        searchView.setMenuItem(searchItem);

        searchView.setOnQueryTextListener(this);
        String[] temp = new String[stringArrayList.size()];
        for (int i = 0; i < stringArrayList.size(); i++) {
            temp[i] = stringArrayList.get(i);
        }
        searchView.setSuggestions(temp);
        searchView.setHint("Search Here");


        searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {


                //  listView = (ListView) getView().findViewById(android.R.id.list);


            }

            @Override
            public void onSearchViewClosed() {
//                FragmentManager fragmentManager = getFragmentManager();
//
//
//                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//
//                Fragment fragment = fragmentManager.findFragmentByTag("search");
//                fragmentTransaction.remove(fragment);
//                fragmentTransaction.commit();
            }
        });


        super.onCreateOptionsMenu(menu, inflater);

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {


        Bundle bundle = new Bundle();
        bundle.putString("query", query);//这里的values就是我们要传的值



        InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);


        stringArrayList.add(query);

        save();
        read();



        Intent intent = new Intent(getActivity(), MainActivity.class);
        intent.putExtra("query", query);
        intent.putExtra("accountNo", accountNo);

        startActivity(intent);


        return true;
    }


    @Override
    public boolean onQueryTextChange(String newText) {
//        if (newText == null || newText.trim().isEmpty()) {
//
//            return false;
//        }
//
//        List<String> filteredValues = new ArrayList<String>(mAllValues);
//        for (String value : mAllValues) {
//            if (!value.toLowerCase().contains(newText.toLowerCase())) {
//                filteredValues.remove(value);
//            }
//        }

//        mAdapter = new ArrayAdapter<>(mContext, android.R.layout.simple_list_item_1, filteredValues);
//        listView.setAdapter(mAdapter);

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


