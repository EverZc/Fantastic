package com.zc.guessmusic.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.zc.guessmusic.util.ScreenUtils;

import java.util.List;

/**
 * Created by Panda_Program on 2017/2/5 0005.
 */

public class GridPicListAdapter extends BaseAdapter {
    //映射到List
    private List<Bitmap> piclist;
    private Context context;
    public GridPicListAdapter(Context context,List<Bitmap> piclist){
        this.context=context;
        this.piclist=piclist;
    }
    @Override
    public int getCount() {
        return piclist.size();
    }

    @Override
    public Object getItem(int position) {
        return piclist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView iv_pic_item=null;
        int density= (int) ScreenUtils.getDeviceDensity(context);
        if (convertView==null){
            iv_pic_item=new ImageView(context);
            //设置布局图片
            iv_pic_item.setLayoutParams(new GridView.LayoutParams(
                    80*density,100*density));
            //设置显示比例类型
            iv_pic_item.setScaleType(ImageView.ScaleType.FIT_XY);
        }else {
            iv_pic_item= (ImageView) convertView;
        }
       // iv_pic_item.setBackground(color.black);
        iv_pic_item.setImageBitmap(piclist.get(position));
        return iv_pic_item;
    }
}
