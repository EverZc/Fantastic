package com.zc.guessmusic.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import com.zc.guessmusic.R;

public class AppPassView extends AppCompatActivity {
private ImageButton imageButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tongguan_layout);
        //隐藏金币
        FrameLayout view = (FrameLayout) findViewById(R.id.layout_bar_coin);
        view.setVisibility(View.INVISIBLE);
        imageButton= (ImageButton) findViewById(R.id.btn_bar_back);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(AppPassView.this,FirstZcActivity.class);
                startActivity(intent);
            }
        });

    }
}
