package com.zc.guessmusic.ui.Fragment;


import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
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
import com.zc.guessmusic.ui.JiaoXue.MusicUtils;

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
public class BlankFragment extends Fragment {

    
    private View view;
    private String jsonData;
    private MyAdapter adapter;
    private RecyclerView recyclerView;
    private List<MusicUtils> list=new ArrayList<>();
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            adapter.notifyDataSetChanged();
        }
    };




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_disc, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        recyclerView= (RecyclerView) view.findViewById(R.id.dis_recycleView);
       // recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),3));
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.HORIZONTAL));
        adapter=new MyAdapter();
        recyclerView.setAdapter(adapter);
        getData();
    }

    private void getData() {
        Log.e("--------", "执行了getData");
        String url="http://new.9sky.com/api/disc/list";
        OkHttpUtils.get(url).execute(new AbsCallback<String>() {
            @Override
            public String parseNetworkResponse(Response response) throws Exception {
                String result=response.body().string().trim();

                jsonData=result;
                Json();
                return jsonData;
            }

            @Override
            public void onSuccess(String s, Call call, Response response) {

            }
        });
    }

    private void Json() throws JSONException {
        Log.e("--------", "执行了Json");
        JSONObject json=new JSONObject(jsonData);
        JSONObject json1=json.optJSONObject("data");
        JSONArray json2=json1.optJSONArray("list");
        for (int i = 0; i < json2.length(); i++) {
            MusicUtils musicUtils=new MusicUtils();
            JSONObject object=json2.optJSONObject(i);
            musicUtils.setDisc_id(object.optString("disc_id"));
            musicUtils.setDisc_name(object.optString("disc_name"));
            musicUtils.setDisc_img_url(object.optString("disc_img_url"));
           // Log.e("--------", ""+musicUtils.getDisc_name());
            list.add(musicUtils);
        }
        Log.e("--------", "Json: "+json2);
        Log.e("---mMMMMMMMM---", String.valueOf(json2.length()));

        handler.sendEmptyMessage(100);

    }


    private class MyAdapter extends  RecyclerView.Adapter{
        class MyViewHolder extends RecyclerView.ViewHolder {
            private ImageView imageView;
            private TextView textView;
            public MyViewHolder(View itemView) {
                super(itemView);
                imageView= (ImageView) itemView.findViewById(R.id.item_changpian_imageview);
                textView= (TextView) itemView.findViewById(R.id.item_changpian_text);
            }
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            final View view=LayoutInflater.from(getActivity()).inflate(R.layout.item_changpian,parent,false);
            MyViewHolder myViewHolder=new MyViewHolder(view);
            return myViewHolder;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            MyViewHolder myViewHolder= (MyViewHolder) holder;
            MusicUtils musicUtil=list.get(position);
            myViewHolder.itemView.setTag(position);
            myViewHolder.textView.setText(musicUtil.getDisc_name());
            Picasso.with(getActivity()).load(musicUtil.getDisc_img_url()).placeholder(R.drawable.basic_iv_disc_default).into(myViewHolder.imageView);

        }

        @Override
        public int getItemCount() {
            return list.size();
        }
    }

}
