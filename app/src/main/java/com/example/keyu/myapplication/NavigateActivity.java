package com.example.keyu.myapplication;

import android.app.Fragment;
import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

import android.app.FragmentTransaction;

public class NavigateActivity extends AppCompatActivity {

    private TextView mTextMessage;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_search:
                    mTextMessage.setText(R.string.title_search);
                    showNav(R.id.navigation_search);
                    return true;
                case R.id.navigation_map:
                    mTextMessage.setText(R.string.title_map);
                    showNav(R.id.navigation_map);
                    return true;
                case R.id.navigation_book:
                    mTextMessage.setText(R.string.title_book);
                    showNav(R.id.navigation_book);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigate);


        init();

        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        //handleIntent(getIntent());
//
//        Intent intent = getIntent();
//        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
//            String query = intent.getStringExtra(SearchManager.QUERY);
//            doMySearch(query);
//        }

//        SearchView simpleSearchView = (SearchView) findViewById(R.id.searchIC); // inititate a search view
//        CharSequence query = simpleSearchView.getQuery();



    }

//    @Override
//    protected void onNewIntent(Intent intent) {
//        setIntent(intent);
//        handleIntent(intent);
//    }
//
//    private void handleIntent(Intent intent) {
//        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
//            String query = intent.getStringExtra(SearchManager.QUERY);
//            doMySearch(query);
//        }
//    }
//
//    private void doMySearch(String query){
//
//        Intent intent = new Intent(NavigateActivity.this, MainActivity.class);
//        startActivity(intent);
//
//    }

    private void init(){
        FragmentOne fragmentOne=new FragmentOne();
        Fragment fragmentTwo=new FragmentTwo();
        Fragment fragmentThree=new FragmentThree();
        FragmentTransaction beginTransaction=getFragmentManager().beginTransaction();
        beginTransaction.add(R.id.content,fragmentOne).add(R.id.content,fragmentTwo).add(R.id.content,fragmentThree);//开启一个事务将fragment动态加载到组件
        beginTransaction.hide(fragmentOne).hide(fragmentTwo).hide(fragmentThree);//隐藏fragment
        beginTransaction.addToBackStack(null);//返回到上一个显示的fragment
        beginTransaction.commit();//每一个事务最后操作必须是commit（），否则看不见效果
        showNav(R.id.navigation_search);
    }

    private void showNav(int navid){

        FragmentOne fragmentOne=new FragmentOne();
        Fragment fragmentTwo=new FragmentTwo();
        Fragment fragmentThree=new FragmentThree();

        FragmentTransaction beginTransaction=getFragmentManager().beginTransaction();
        beginTransaction.add(R.id.content,fragmentOne).add(R.id.content,fragmentTwo).add(R.id.content,fragmentThree);//开启一个事务将fragment动态加载到组件

        switch (navid){
            case R.id.navigation_search:
                beginTransaction.hide(fragmentTwo).hide(fragmentThree);
                beginTransaction.show(fragmentOne);
                beginTransaction.addToBackStack(null);
                beginTransaction.commit();
                break;
            case R.id.navigation_map:
                beginTransaction.hide(fragmentOne).hide(fragmentThree);
                beginTransaction.show(fragmentTwo);
                beginTransaction.addToBackStack(null);
                beginTransaction.commit();
                break;
            case R.id.navigation_book:
                beginTransaction.hide(fragmentTwo).hide(fragmentOne);
                beginTransaction.show(fragmentThree);
                beginTransaction.addToBackStack(null);
                beginTransaction.commit();
                break;
        }
    }



}
