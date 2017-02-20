package com.zc.guessmusic.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zc.guessmusic.R;

public class SecondActivity extends AppCompatActivity {
    public static final String PHOTO_NAME="photo_name";
    public static final String PHOTO_IMAGE_ID="photo_image_id";
    public static final String PHOTO_CONTEXT="photo_context";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        Intent intent=getIntent();
        String photoName=intent.getStringExtra(PHOTO_NAME);
        int imageId=intent.getIntExtra(PHOTO_IMAGE_ID,0);
        String photoContext0001=intent.getStringExtra(PHOTO_CONTEXT);
        Toolbar toolbar= (Toolbar) findViewById(R.id.second_toolbar);
        CollapsingToolbarLayout collapsingToolbar=
                (CollapsingToolbarLayout) findViewById(R.id.second_collasping_toolbar);
        ImageView imageViewTitle= (ImageView) findViewById(R.id.second_image_view);
        TextView textViewContext= (TextView) findViewById(R.id.second_context_text);
        setSupportActionBar(toolbar);
        //实例actionBar
        ActionBar actionBar=getSupportActionBar();
        if (actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        //设置标题
        collapsingToolbar.setTitle(photoName);
        Glide.with(this).load(imageId).into(imageViewTitle);
        String photoContext=photoContext0001;
        textViewContext.setText(photoContext);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
