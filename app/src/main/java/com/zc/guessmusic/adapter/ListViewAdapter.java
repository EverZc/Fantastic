package com.zc.guessmusic.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.zc.guessmusic.R;
import com.zc.guessmusic.model.ChatItemListViewBean;
import com.zc.guessmusic.model.IChatItemClick;

import java.util.List;

/**
 * Created by Panda_Program on 2017/2/13 0013.
 */

public class ListViewAdapter extends BaseAdapter implements IChatItemClick {
    private List<ChatItemListViewBean> mData;
    private LayoutInflater mInflater;

    public ListViewAdapter(Context context,
                                   List<ChatItemListViewBean> data) {
        this.mData = data;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    //返回地position个Item是何种类型
    @Override
    public int getItemViewType(int position) {
        ChatItemListViewBean bean = mData.get(position);
        return bean.getType();
    }
    //返回不同布局的总数 2个
    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            if (getItemViewType(position) == 0) {
                holder = new ViewHolder();
                convertView = mInflater.inflate(
                        R.layout.chat_item_itemin, null);
                holder.icon = (ImageView) convertView.findViewById(
                        R.id.icon_in);
                holder.text = (TextView) convertView.findViewById(
                        R.id.text_in);
            } else {
                holder = new ViewHolder();
                convertView = mInflater.inflate(
                        R.layout.chat_item_itemout, null);
                holder.icon = (ImageView) convertView.findViewById(
                        R.id.icon_out);
                holder.text = (TextView) convertView.findViewById(
                        R.id.text_out);
                holder.text.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {

                       // Log.e("----",holder.text.getText().toString());
                        return false;
                    }
                });
            }
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.icon.setImageBitmap(mData.get(position).getIcon());
        holder.text.setText(mData.get(position).getText());
        return convertView;
    }

    @Override
    public void myOnClick() {

    }

    public final class ViewHolder {
        public ImageView icon;
        public TextView text;
    }
}