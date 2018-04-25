package com.example.chirag.slidingtabsusingviewpager;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.wang.avi.AVLoadingIndicatorView;

public class RefreshActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refresh);

        AVLoadingIndicatorView indicatorView = findViewById(R.id.avi);
        indicatorView.show();

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // indicatorView.hide();
        Intent intent1 = new Intent(RefreshActivity.this, MainActivity.class);
        intent1.putExtra("accountNo", getIntent().getStringExtra("accountNo"));
        intent1.putExtra("query", getIntent().getStringExtra("query"));
        intent1.putExtra("author", getIntent().getStringExtra("author"));
        intent1.putExtra("imgUrl", getIntent().getStringExtra("imgUrl"));
        intent1.putExtra("douban", getIntent().getStringExtra("douban"));
        intent1.putExtra("callno", getIntent().getStringExtra("callno"));
        intent1.putExtra("publicion", getIntent().getStringExtra("publicion"));
        intent1.putExtra("avil", getIntent().getStringExtra("avil"));
        startActivity(intent1);

    }

}
