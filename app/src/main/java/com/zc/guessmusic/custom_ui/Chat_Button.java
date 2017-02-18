package com.zc.guessmusic.custom_ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.Button;

import com.zc.guessmusic.R;
import com.zc.guessmusic.ui.ChatListActivity;

/**
 * Created by Panda_Program on 2017/2/15 0015.
 */

public class Chat_Button extends Button {
    private Paint mPaint1;
    private Paint mPaint2;
    private ChatListActivity chatListActivity;
    public Chat_Button(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public Chat_Button(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        //在回调父类的方法前实现自己的逻辑,对TextView来说就是在绘制文本内容后
        mPaint1=new Paint();
        mPaint1.setColor(getResources().getColor(R.color.button_out));

        //STROKE 表示只画一条线
        //Fill表示填满整个View
        mPaint1.setStyle(Paint.Style.FILL_AND_STROKE);
        mPaint2=new Paint();
        mPaint2.setColor(getResources().getColor(R.color.button_in));
        mPaint2.setStyle(Paint.Style.FILL_AND_STROKE);
        //绘制外层矩形
        canvas.drawRoundRect(0,0,getMeasuredWidth(),getMeasuredHeight(),35,35,mPaint1);
        //绘制内层矩形
        canvas.drawRoundRect(5,5,getMeasuredWidth()-5,getMeasuredHeight()-5,35,35,mPaint2);
        canvas.save();//保存画布的状态
        //绘制文字前平移10个像素
        canvas.translate(10,0);
        //在回调父类的方法前实现自己的逻辑,对TextView来说就是在绘制文本内容前

            super.onDraw(canvas);

    }
}
