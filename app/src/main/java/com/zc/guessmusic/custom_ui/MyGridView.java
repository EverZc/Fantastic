package com.zc.guessmusic.custom_ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;

import com.zc.guessmusic.R;
import com.zc.guessmusic.model.IWordButtonClickListener;
import com.zc.guessmusic.model.IWordButtonSubject;
import com.zc.guessmusic.model.WordButton;
import com.zc.guessmusic.util.Util;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/12/19 0019.
 */

public class MyGridView extends GridView implements IWordButtonSubject{
    //文字按键数组
    private ArrayList<WordButton> mArrayList=new ArrayList<>();
    private MyGridAdapter myAdapter;

    private Context mcontext;
    //定义动画的对象
    private Animation mScaleAnimation;

    //接口的声明
    private IWordButtonClickListener mWordButtonListener;

    public MyGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mcontext=context;
        myAdapter= new MyGridAdapter();
        this.setAdapter(myAdapter);
    }
    public void updateData(ArrayList<WordButton> list){
         mArrayList=list;

        //重新设置数据源
        setAdapter(myAdapter);
    }
    //注册监听
    @Override
    public void regusterListener(IWordButtonClickListener iWordButtonClickListener) {
        mWordButtonListener=iWordButtonClickListener;
    }

    class MyGridAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return mArrayList.size();
        }

        @Override
        public Object getItem(int position) {
            return mArrayList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final WordButton holder;

            if (convertView==null){
                convertView= Util.getView(mcontext, R.layout.custom_ui_item);
                holder=mArrayList.get(position);
                //加载动画
                mScaleAnimation = AnimationUtils.loadAnimation(mcontext,R.anim.scale);
                //设置动画的延迟时间
                mScaleAnimation.setStartOffset(position*100);
                holder.mIndex=position;
                holder.mViewButton= (Button) convertView.findViewById(R.id.item_btn);
                holder.mViewButton.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mWordButtonListener.onWordButtonClick(holder);
                    }
                });
                convertView.setTag(holder);
            }else {
                holder= (WordButton) convertView.getTag();
            }
            holder.mViewButton.setText(holder.mWordString);
            //动画的播放
            convertView.startAnimation(mScaleAnimation);

            return convertView;
        }
    }
}
