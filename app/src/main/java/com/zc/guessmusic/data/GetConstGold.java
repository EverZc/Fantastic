package com.zc.guessmusic.data;

import android.content.Context;

import com.zc.guessmusic.R;

/**
 * Created by Administrator on 2016/12/23 0023.
 * 从配置文件中读取 删除和提示操作所要用的金币
 */

public class GetConstGold {

    Context context;
    public int getDeleteWordGold(){
        return context.getResources().getInteger(R.integer.pay_delete_word);
    }
    public int getTipWordGold(){
        return context.getResources().getInteger(R.integer.pay_tip_answer);
    }

}
