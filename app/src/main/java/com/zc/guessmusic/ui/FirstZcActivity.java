package com.zc.guessmusic.ui;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.BounceInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.Toast;

import com.zc.guessmusic.R;
import com.zc.guessmusic.adapter.PhoneAdapter;
import com.zc.guessmusic.model.Photo;
import com.zc.guessmusic.ui.JiaoXue.BasketballActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class FirstZcActivity extends AppCompatActivity {
    private DrawerLayout mDrawerLayout;
    private int[] mRes = {R.id.imageView_a, R.id.imageView_b, R.id.imageView_c,
            R.id.imageView_d, R.id.imageView_e};
    private List<ImageView> mImageViews = new ArrayList<ImageView>();

    private boolean mFlag = true;
    //
    private String name[]={"呼圈儿","时尚志","创业那点事"};

    private int imageId[]={R.drawable.huquan,R.drawable.shishangzhi,R.drawable.chuanyenadianshi};
    private List<Photo> photoList=new ArrayList<>();
    private PhoneAdapter adapter;
    //下拉刷新
    private SwipeRefreshLayout swipeRefresh;
    private ImageView vector;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firstzc);
        NavigationView navigationView= (NavigationView) findViewById(R.id.first_nav);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.nav_csdn:
                        Intent webView=new Intent(FirstZcActivity.this,WebViewActivity.class);
                        startActivity(webView);
                        break;
                    case R.id.nav_blog:
                        Intent webView1=new Intent(FirstZcActivity.this,WebViewActivity.class);
                        startActivity(webView1);
                        break;

                }
                return false;
            }
        });
        //图片随机排列
        //initPhoto();
       /* vector= (ImageView) findViewById(R.id.vector);
        Drawable drawable = vector.getDrawable();
        if (drawable instanceof Animatable) {
            ((Animatable) drawable).start();
        }*/
        String context1[]={
                "\n项目简介：\n\n\n" +
                        "    呼圈儿是一款立足于呼和浩特的生活信息互动平台，呼圈儿涵盖八个版块，" +
                        "为居民提供二手物品交易、拼车、法律资讯、宠物信息、生活饮食、服装鞋包等服务，" +
                        "绝大部分生活必需品都可以在这里找到，其中以二手物品交易最为火热，" +
                        "能以更加经济的价格购买到满意的物品，方便人们的生活。\n",

                        "项目简介：\n\n\n" +
                        "    OL时尚志是为时尚女性打造的资讯APP，提供免费的时尚资讯。" +
                        "覆盖明星八卦、穿衣搭配，" +
                        "囊括欧美风、日系、韩流、嘻哈、波西米亚、朋克、森女、复古、白领OL、学院风、洛丽塔等" +
                        "各类穿衣风格。这里可以看网红直播、时尚专家为你专业测评时尚新品、爆款。" +
                        "同时，还可以和明星面对面聊时尚，带你直击伦敦、纽约、东京、巴黎、米兰等时装周。" +
                        "春、夏、秋、冬潮流新款，让你显白、显瘦、显高的服饰搭配，够fashion。" +
                        "时尚志，一个你值得拥有的时尚百科APP。 " +
                        "时尚：更直观、更简易、更专业的时尚理念帮助你升级时髦指数。" +
                        "美容：技巧派，都能满足你对高颜值的所有好奇和幻想。" +
                        "生活：当你想要提升生活品质时，这里有现成且实践过所有新奇探索。 " +
                        "OnlyLady是中国优质的女性时尚生活平台，于2002年5月25日创立，是一个女性垂直网站。" +
                        "历经十年沉淀，OnlyLady为爱自己、爱生活、爱分享的千万 " +
                        "中国时尚女性提供专业的内容和优质的互动服务，深耕于美容、时尚、生活三大领域，"
                ,"项目描述：\n\n\n" +
                "    创业那点事关注中小企业创业者，打造信息平台和服务平台，帮助中国创业者实现创业梦想。为创业者和风险投资人提供各种创业类最新资讯和实用知识手册，打造创业者和投资人的社交平台\n"

        };
        swipeRefresh= (SwipeRefreshLayout) findViewById(R.id.first_swipe_refresh);
        swipeRefresh.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                swipeRefresh.setRefreshing(false);
                                Toast.makeText(FirstZcActivity.this,
                                        "刷新成功,并没有其他数据(⊙︿⊙)",Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }).start();

            }
        });
        for (int i = 0; i < 3; i++) {
            StringBuilder fruitContent = new StringBuilder();
            fruitContent.append(context1[i]);
            Photo photo=new Photo();
            photo.setContext(fruitContent.toString());
            photo.setName(name[i]);
            photo.setImageId(imageId[i]);
            photoList.add(photo);
        }
        RecyclerView recyclerView= (RecyclerView) findViewById(R.id.first_recycle_view);
        GridLayoutManager layoutManager=new GridLayoutManager(this,2);
        recyclerView.setLayoutManager(layoutManager);
        adapter=new PhoneAdapter(photoList);
        recyclerView.setAdapter(adapter);

        Toolbar toolbar= (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);

        mDrawerLayout= (DrawerLayout) findViewById(R.id.drawerlayout);
        ActionBar actionBar=getSupportActionBar();
        if (actionBar!=null){
            //让导航键显示出来
            actionBar.setDisplayHomeAsUpEnabled(true);
            //设置导航的图标
            actionBar.setHomeAsUpIndicator(R.drawable.choosen5);
        }
        //初始化属性动画
        init();

    }



    private void initPhoto() {
        photoList.clear();
        for (int i=0;i<2;i++){
            Random random=new Random();
            /*int index=random.nextInt(photos.length);
            photoList.add(photos[index]);*/
        }
    }

    private void init() {
        for (int i = 0; i < mRes.length; i++) {
            ImageView imageView = (ImageView) findViewById(mRes[i]);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (v.getId()) {
                        case R.id.imageView_a:
                            if (mFlag) {
                                startAnim();
                            } else {
                                closeAnim();
                            }
                            break;
                        case R.id.imageView_b:
                            Intent intentBasketball=new Intent(FirstZcActivity.this,BasketballActivity.class);
                            startActivity(intentBasketball);
                            break;
                        case R.id.imageView_c:
                            Intent intentMusic=new Intent(FirstZcActivity.this,MainActivity.class);
                            startActivity(intentMusic);
                            break;
                        case R.id.imageView_d:
                            Intent intBaiduMap=new Intent(FirstZcActivity.this,MapMainActivityTEXT.class);
                            startActivity(intBaiduMap);
                            break;
                        case R.id.imageView_e:
                            Intent intent=new Intent(FirstZcActivity.this,ChatListActivity.class);
                            startActivity(intent);
                            break;
                        default:
                            Toast.makeText(FirstZcActivity.this, "" + v.getId(),
                                    Toast.LENGTH_SHORT).show();
                            break;
                    }
                }
            });
            mImageViews.add(imageView);
        }
    }

    private void closeAnim() {
        ObjectAnimator animator0 = ObjectAnimator.ofFloat(mImageViews.get(0),
                "alpha", 0.5F, 1F);
        ObjectAnimator animator1 = ObjectAnimator.ofFloat(mImageViews.get(1),
                "translationY", 200F, 0);
        ObjectAnimator animator2 = ObjectAnimator.ofFloat(mImageViews.get(2),
                "translationX", 200F, 0);
        ObjectAnimator animator3 = ObjectAnimator.ofFloat(mImageViews.get(3),
                "translationY", -200F, 0);
        ObjectAnimator animator4 = ObjectAnimator.ofFloat(mImageViews.get(4),
                "translationX", -200F, 0);
        AnimatorSet set = new AnimatorSet();
        set.setDuration(500);
        set.setInterpolator(new BounceInterpolator());
        set.playTogether(animator0, animator1, animator2, animator3, animator4);
        set.start();
        mFlag = true;
       /* Drawable drawable = vector.getDrawable();
        vector.setVisibility(View.VISIBLE);
        if (drawable instanceof Animatable) {
            ((Animatable) drawable).start();
        }*/
    }

    private void startAnim() {
        ObjectAnimator animator0 = ObjectAnimator.ofFloat(
                mImageViews.get(0),
                "alpha",
                1F,
                0.5F);
        ObjectAnimator animator1 = ObjectAnimator.ofFloat(
                mImageViews.get(1),
                "translationY",
                200F);
        ObjectAnimator animator2 = ObjectAnimator.ofFloat(
                mImageViews.get(2),
                "translationX",
                200F);
        ObjectAnimator animator3 = ObjectAnimator.ofFloat(
                mImageViews.get(3),
                "translationY",
                -200F);
        ObjectAnimator animator4 = ObjectAnimator.ofFloat(
                mImageViews.get(4),
                "translationX",
                -200F);
        AnimatorSet set = new AnimatorSet();
        set.setDuration(500);
        set.setInterpolator(new OvershootInterpolator());
        set.playTogether(
                animator0,
                animator1,
                animator2,
                animator3,
                animator4);
        set.start();
        mFlag = false;
       /* vector.setVisibility(View.GONE);*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu__chat:
                Intent intentChat=new Intent(FirstZcActivity.this,ChatListActivity.class);
                startActivity(intentChat);
                break;
            case R.id.menu_guessmusic:
                Intent intentMusic=new Intent(FirstZcActivity.this,MainActivity.class);
                startActivity(intentMusic);
                break;
            case R.id.menu_basketball:
                Intent intentBasketball=new Intent(FirstZcActivity.this,BasketballActivity.class);
                startActivity(intentBasketball);
                break;
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
            default:
        }
        return true;

    }
}
