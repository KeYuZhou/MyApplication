package com.example.chirag.slidingtabsusingviewpager;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ListFragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
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

import java.util.ArrayList;
import java.util.List;
import java.util.SimpleTimeZone;

import me.yuqirong.cardswipelayout.CardItemTouchHelperCallback;
import me.yuqirong.cardswipelayout.CardLayoutManager;
import me.yuqirong.cardswipelayout.OnSwipeListener;

public class SearchFragment extends Fragment implements MaterialSearchView.OnQueryTextListener, MenuItem.OnActionExpandListener, SwipeGestureHelper.OnSwipeListener {

    List<String> mAllValues;
    String[] values = {"china", "usa", "uk"};
    //private ArrayAdapter<String> mAdapter;
    private Context mContext;
    //ListView listView;
    RecyclerView recyclerView;
    RecyclerView horizon_recyclerView;
    RecommendAdapter recommendAdapter;
    String accountNo;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
        setHasOptionsMenu(true);


        Bundle bundle = this.getArguments();

        // 步骤2:获取某一值
        // accountNo = bundle.getString("message");
        accountNo = getActivity().getIntent().getStringExtra("accountNo");
        Log.e("searchFragment", accountNo);


    }


    @Override
    public void onDetach() {
        super.onDetach();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View layout = inflater.inflate(R.layout.search_fragment, container, false);


        RelativeLayout rl = (RelativeLayout) layout.findViewById(R.id.container);


//
        //  horizon_recyclerView = layout.findViewById(R.id.horizontal_recyclerView);
        SnappyLinearLayoutManager snappyLinearLayoutManager = new SnappyLinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);

//        LinearLayoutManager horizon_LayoutManager = new LinearLayoutManager(getActivity());
        //   horizon_LayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);

//        ViewOnTouchDelegate touchDelegate = new ViewOnTouchDelegate();
//        snappyLinearLayoutManager.setSnapMethod(SnappyLinearLayoutManager.SnappyLinearSmoothScroller.SNAP_CENTER);
//
//
//
//        SwipeGestureHelper swipeGestureHelper  = new SwipeGestureHelper(getActivity());
//        swipeGestureHelper.attachToRecyclerView(horizon_recyclerView,touchDelegate);


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

//
//        horizon_recyclerView.setItemAnimator(new SlideInAnimator());
//
//
//        horizon_recyclerView.setOnTouchListener(touchDelegate);
//
//       // horizon_recyclerView.setLayoutManager(horizon_LayoutManager);
//        horizon_recyclerView.setLayoutManager(snappyLinearLayoutManager);
//
//        horizon_recyclerView.setAdapter(recommendAdapter);
////


        recommendAdapter.setOnItemClickListener(new RecommendAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(getActivity(), MainActivity.class);
                intent.putExtra("accountNo", accountNo);

                intent.putExtra("query", recommendAdapter.dateList.get(position));
                startActivity(intent);

            }
        });


//        recyclerView = layout.findViewById(R.id.recyclerView);
//
//
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
//
//        recyclerView.setLayoutManager(linearLayoutManager);
//        SummonerAdapter summonerAdapter = new SummonerAdapter(getActivity());
//        recyclerView.setAdapter(summonerAdapter);
//

        //   horizon_recyclerView.setAdapter(summonerAdapter);

//        final List<String> list = new ArrayList<>();
//        for (int i = 0; i < 10; i++) {
//            list.add(String.valueOf(i));
//        }
//       MyAdapter myAdapter =new MyAdapter(layout.getContext(),list);
//
//
//       recyclerView.setAdapter(myAdapter);
//        recyclerView.setRemovedListener(new ItemRemovedListener() {
//            @Override
//            public void onRightRemoved() {
////                Toast.makeText(MainActivity.this, list.get(list.size() - 1) + " was right removed", Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onLeftRemoved() {
////                Toast.makeText(MainActivity.this, list.get(list.size() - 1) + " was left removed", Toast.LENGTH_SHORT).show();
//            }
//        });

//
//        CardItemTouchHelperCallback cardCallback = new CardItemTouchHelperCallback(recyclerView.getAdapter(), summonerAdapter.dateList);
//        ItemTouchHelper touchHelper = new ItemTouchHelper(cardCallback); CardLayoutManager cardLayoutManager = new CardLayoutManager(recyclerView, touchHelper);
//        recyclerView.setLayoutManager(cardLayoutManager);
//        touchHelper.attachToRecyclerView(recyclerView);
//
//        cardCallback.setOnSwipedListener(new OnSwipeListener<String>() {
//                                             @Override
//                                             public void onSwiping(RecyclerView.ViewHolder viewHolder, float ratio, int direction) {
//
//
//                                             }
//
//                                             @Override
//                                             public void onSwiped(RecyclerView.ViewHolder viewHolder, String s, int direction) {
//
//                                             }
//
//                                             @Override
//                                             public void onSwipedClear() {
//
//                                             }
//                                         }
//        );

        return layout;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.search_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);

        MaterialSearchView searchView = getActivity().findViewById(R.id.search_view);
        searchView.setMenuItem(searchItem);

        searchView.setOnQueryTextListener(this);

        searchView.setSuggestions(values);
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


//        FragmentManager fragmentManager = getFragmentManager();
//        Fragment fragment = fragmentManager.findFragmentByTag("search");
//
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//
        //收起键盘
        InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);


        //   fragmentTransaction.remove(fragment);


        Intent intent = new Intent(getActivity(), MainActivity.class);
        intent.putExtra("query", query);
        intent.putExtra("accountNo", accountNo);
        //   Log.e("searchFragemnt2", accountNo);
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


