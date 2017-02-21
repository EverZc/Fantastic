package com.zc.guessmusic.ui.JiaoXue;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.lzy.okhttputils.OkHttpUtils;
import com.lzy.okhttputils.callback.AbsCallback;
import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.squareup.picasso.Picasso;
import com.zc.guessmusic.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

public class XiangXiAcivity extends AppCompatActivity {
    private String textTitle;
    private TextView textView,Jiazai;
    private RecyclerView recyclerView;
    private String json;
    private String page;
    private MyAdapter myAdapter;


    private List<Util1> list=new ArrayList<>();
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 100:
                    myAdapter.notifyDataSetChanged();
                    break;

            }

        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_xiang_xi_acivity);
        initSystemBar(this);
        Intent intent=getIntent();
        textTitle=intent.getStringExtra("Text");
        page=intent.getStringExtra("Page");
        Log.e("-----page",page+"");
        textView= (TextView) findViewById(R.id.xinagxi_title_text);
        textView.setText(textTitle);
        recyclerView= (RecyclerView) findViewById(R.id.xiangxi_recycler);

        recyclerView.setLayoutManager(new LinearLayoutManager(XiangXiAcivity.this));
        myAdapter=new MyAdapter();
        recyclerView.setAdapter(myAdapter);

        getData();

    }
    public void initSystemBar(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);

        }
        SystemBarTintManager tintManager = new SystemBarTintManager(activity);
        tintManager.setStatusBarTintEnabled(true);
// 使用颜色资源
        tintManager.setStatusBarTintResource(R.color.color_true);
    }
    @TargetApi(19)
    private void setTranslucentStatus(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    private class MyAdapter extends RecyclerView.Adapter{
        private class MyHolder extends RecyclerView.ViewHolder {
            private ImageView imageView;
            private TextView text,time;
                public MyHolder(final View itemView) {
                super(itemView);
                    Jiazai= (TextView) findViewById(R.id.jiazai);
                    imageView= (ImageView) itemView.findViewById(R.id.item_imageView);
                    text= (TextView) itemView.findViewById(R.id.item_textView);
                    time= (TextView) itemView.findViewById(R.id.item_time);
                    itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            int pois= (int) itemView.getTag();
                            Intent intent=new Intent(XiangXiAcivity.this, VideoActivity.class);
                            Util1 util1=list.get(pois);
                            String url=util1.getUrl();
                            Log.e("------","MMM" +url);
                            intent.putExtra("url",url);
                            startActivity(intent);
                            //Toast.makeText(XiangXiAcivity.this,"item time0",Toast.LENGTH_SHORT).show();
                        }
                    });
            }
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view= LayoutInflater.from(XiangXiAcivity.this).inflate(R.layout.listlayout_time,parent,false);
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
            Picasso.with(XiangXiAcivity.this).load(utils.getCover()).into(myHolder.imageView);
            Jiazai.setText(" ");
        }

        @Override
        public int getItemCount() {
            return list.size();
        }
    }
//获取数据
    private void getData(){
       String url="http://video.tibaing.com/scenics/video/"+page;
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
    //解析数据
    private void Json() throws JSONException {
        Log.e("------","执行到这里了");
        JSONArray json01=new JSONArray(json);

        for (int i = 0; i < json01.length(); i++) {
            JSONObject object=json01.optJSONObject(i);
            Util1 util1 = new Util1();
            util1.setId(object.optString("_id"));
            util1.setUrl(object.optString("url"));
            util1.setCategory(object.optString("category"));
            util1.setTitle(object.optString("title"));
            util1.setCover(object.optString("cover"));
            util1.setLength(object.optString("length"));
            list.add(util1);

        }
        handler.sendEmptyMessage(100);
        Util1 util1 = new Util1();
        Log.e("------", String.valueOf(list.size()));
    }
}
