package com.zc.guessmusic.util;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by Administrator on 2016/12/25 0025.
 */

public class MyTextView extends TextView {
    private Paint paint;
    public MyTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint=new Paint();
        paint.setColor(Color.WHITE);
        paint.setAntiAlias(true);
    }

}
