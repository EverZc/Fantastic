package com.zc.guessmusic.ui.Fragment;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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

import com.lzy.okhttputils.OkHttpUtils;
import com.lzy.okhttputils.callback.AbsCallback;
import com.squareup.picasso.Picasso;
import com.zc.guessmusic.R;
import com.zc.guessmusic.ui.JiaoXue.Util1;
import com.zc.guessmusic.ui.JiaoXue.VideoActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class CaiPan extends Fragment {

    private RecyclerView recyclerView;
    private String json;

    private View view;
    private TextView Jiazai;
    private MyAdapter myAdapter;
    private List<Util1> list=new ArrayList<>();
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            myAdapter.notifyDataSetChanged();
        }
    };

    public CaiPan() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       view=inflater.inflate(R.layout.fragment_1, container, false);
        Log.e("-----","篮球裁判");
        return view;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        recyclerView= (RecyclerView)view.findViewById(R.id.recy);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        myAdapter=new MyAdapter();

        getData();
        recyclerView.setAdapter(myAdapter);

    }

    private class MyAdapter extends RecyclerView.Adapter{
        private class MyHolder extends RecyclerView.ViewHolder {
            private ImageView imageView;
            private TextView text,time;
            public MyHolder(final View itemView) {
                super(itemView);
                imageView= (ImageView) itemView.findViewById(R.id.item_imageView);
                text= (TextView) itemView.findViewById(R.id.item_textView);
                time= (TextView) itemView.findViewById(R.id.item_time);
                Jiazai= (TextView) view.findViewById(R.id.jiazai);
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int pois= (int) itemView.getTag();
                        Intent intent=new Intent(getActivity(), VideoActivity.class);
                        Util1 util1=list.get(pois);
                        String url=util1.getUrl();
                        Log.e("------","MMM" +url);
                        intent.putExtra("url",url);

                        startActivity(intent);


                    }
                });
            }
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view= LayoutInflater.from(getActivity()).inflate(R.layout.listlayout_time,parent,false);
            MyHolder myHolder=new MyHolder(view);
            return myHolder;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            MyHolder myHolder= (MyHolder) holder;
            myHolder.itemView.setTag(position);
            Util1 utils=list.get(position);
            myHolder.text.setText(utils.getTitle());
            myHolder.time.setText(utils.getLength());
            Picasso.with(getActivity()).load(utils.getCover()).into(myHolder.imageView);
            Jiazai.setText(" ");
        }

        @Override
        public int getItemCount() {
            return list.size();
        }
    }
    private void getData(){
        Log.e("---","执行到获取数据 裁判！");
        String url="http://video.tibaing.com/scenics/video/500";
        OkHttpUtils.get(url).execute(new AbsCallback<String>() {
            @Override
            public String parseNetworkResponse(Response response) throws Exception {
                String str = response.body().string().trim();
                Log.e("-------",str);
                json=str;
                Json();
                return json;
            }
            @Override
            public void onSuccess(String s, Call call, Response response) {
            }
        });

    }
    private void Json() throws JSONException {
        Log.e("------","执行到解析数据caipan");
        JSONArray json01=new JSONArray(json);

        for (int i = 0; i < json01.length(); i++) {
            JSONObject object=json01.optJSONObject(i);
            Util1 util1 = new Util1();
            util1.setId(object.optString("_id"));
            util1.setCategory(object.optString("category"));
            util1.setTitle(object.optString("title"));
            util1.setCover(object.optString("cover"));
            util1.setLength(object.optString("length"));
            util1.setUrl(object.optString("url"));
            list.add(util1);


        }
        handler.sendEmptyMessage(100);
        Log.e("---- -", String.valueOf(list.size()));
    }

    @Override
    public void onDestroy() {
        list.clear();
        super.onDestroy();
    }
}
