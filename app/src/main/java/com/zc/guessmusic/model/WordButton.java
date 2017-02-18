package com.zc.guessmusic.model;

import android.widget.Button;

/**
 * Created by Administrator on 2016/12/19 0019.
 *
 * 猜歌游戏
 *
 * 文字按钮的模型和数据
 */

public class WordButton {
    public int mIndex;
    public boolean mIsVisiable; //按钮是否被显示
    public String mWordString;  //按钮的文字是什么

    public Button mViewButton;  //Button控件

    public WordButton(){
        mIsVisiable=true;
        mWordString="";
    }


}
