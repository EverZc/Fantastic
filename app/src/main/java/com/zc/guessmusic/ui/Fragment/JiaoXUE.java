package com.zc.guessmusic.ui.Fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zc.guessmusic.R;
import com.zc.guessmusic.ui.JiaoXue.XiangXiAcivity;


/**
 * A simple {@link Fragment} subclass.
 */
public class JiaoXUE extends Fragment {

    private View view;
    private String []page ={"100","101","102","103","104","105","106","107"};
    private String []text={"运球","上篮","投篮","进攻", "防守", "篮板" ,"传球" ,"弹跳"};
    private int [] src={R.drawable.yunqiu, R.drawable.shanglan,R.drawable.toulan,R.drawable.jinggong,R.drawable.fangshou,R.drawable.lanban,R.drawable.chuanqiu,R.drawable.tantiao};
    private RecyclerView recyclerView;
    private MyAdapter adapter;

    public JiaoXUE() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

       view=inflater.inflate(R.layout.fragment_3, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);

        recyclerView= (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter=new MyAdapter();
        recyclerView.setAdapter(adapter);
        Log.e("-----","篮球教学");
    }

    private class MyAdapter extends RecyclerView.Adapter{
        private  class  MyHolder extends RecyclerView.ViewHolder{
            private ImageView imageView;
            private TextView textView;
            public MyHolder(final View itemView) {
                super(itemView);
                this.imageView= (ImageView) itemView.findViewById(R.id.item_imageView);
                this.textView= (TextView) itemView.findViewById(R.id.item_textView);
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int posit= (int) itemView.getTag();

                        Intent intent =new Intent(getActivity(), XiangXiAcivity.class);
                            intent.putExtra("Text",text[posit]);
                        intent.putExtra("Page",page[posit]);
                        startActivity(intent);
                    }
                });
            }

        }
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view=LayoutInflater.from(getActivity()).inflate(R.layout.listlayout,parent,false);
            MyHolder myHolder=new MyHolder(view);
            return myHolder;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            MyHolder myHolder= (MyHolder) holder;
            myHolder.itemView.setTag(position);
                myHolder.textView.setText(text[position]);
                myHolder.imageView.setImageResource(src[position]);

        }

        @Override
        public int getItemCount() {
            return text.length;
        }
    }
}
