package com.example.chirag.slidingtabsusingviewpager;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


import com.shuyu.textutillib.EmojiLayout;
import com.shuyu.textutillib.RichEditBuilder;
import com.shuyu.textutillib.RichEditText;
import com.shuyu.textutillib.listener.OnEditTextUtilJumpListener;

public class WriteCommentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_comment);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            // Enable the Up button
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        }

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        RichEditText richEditText = (RichEditText) findViewById(R.id.emoji_edit_text2);
//        EmojiLayout emojiLayout = new EmojiLayout();
//        emojiLayout.setEditTextSmile(richEditText);
        RichEditBuilder richEditBuilder = new RichEditBuilder();
        richEditBuilder.setEditText(richEditText)
//                .setTopicModels(topicModels)
//                .setUserModels(nameList)
                .setColorAtUser("#FF00C0")
                .setColorTopic("#F0F0C0")
                .setEditTextAtUtilJumpListener(new OnEditTextUtilJumpListener() {
                    @Override
                    public void notifyAt() {
                        //  JumpUtil.goToUserList(MainActivity.this, MainActivity.REQUEST_USER_CODE_INPUT);
                    }

                    @Override
                    public void notifyTopic() {
                        // JumpUtil.goToTopicList(MainActivity.this, MainActivity.REQUEST_TOPIC_CODE_INPUT);
                    }
                })
                .builder();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.write_comment_toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
//            case R.id.action_cancel:
//                // User chose the "Favorite" action, mark the current item
//                // as a favorite...


            //  return true;
            case R.id.action_submit:
                // User chose the "Favorite" action, mark the current item
                // as a favorite...
                //intent = new Intent(WriteCommentActivity.this,MainActivity.class);

                //TODO submit comment

                WriteCommentActivity.this.finish();

                return true;
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);
        }
    }


}
