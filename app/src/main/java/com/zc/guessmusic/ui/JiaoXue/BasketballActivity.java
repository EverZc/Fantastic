package com.zc.guessmusic.ui.JiaoXue;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.zc.guessmusic.R;
import com.zc.guessmusic.ui.Fragment.CaiPan;
import com.zc.guessmusic.ui.Fragment.JiaoXUE;
import com.zc.guessmusic.ui.Fragment.JingDianZhuanTi;
import com.zc.guessmusic.ui.Fragment.WomenFragment;

import java.util.ArrayList;
import java.util.List;

public class BasketballActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private RelativeLayout rl;
    private ViewPager vp;
    private RadioGroup radioGroup;
    private RadioButton radioJingdian,radioJiaoxue,radioCaipan,radioWomen;
    private List<Fragment> listFragment=new ArrayList<>();
    private TextView textTitle;
    public static final String[] DISC_RINK=new String[]{
            //唱片推荐——纯音乐地址 按顺序排列的 其中有的返回的数据为空
            "http://new.9sky.com/api/top/disc/list?platform=Android",
            "http://new.9sky.com/api/disc/list?genre_id=1&page_size=18",
            "http://new.9sky.com/api/disc/list?genre_id=3&page_size=18",
            "http://new.9sky.com/api/disc/list?genre_id=9&page_size=18",
            "http://new.9sky.com/api/disc/list?genre_id=2&page_size=18",
            "http://new.9sky.com/api/disc/list?genre_id=7&page_size=18",
            "http://new.9sky.com/api/disc/list?genre_id=12&page_size=18",
            "http://new.9sky.com/api/disc/list?genre_id=10&page_size=18",
            "http://new.9sky.com/api/disc/list?genre_id=4&page_size=18",
            "http://new.9sky.com/api/disc/list?genre_id=6&page_size=18",
            "http://new.9sky.com/api/disc/list?genre_id=5&page_size=18"
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      //透明状态栏
        // getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //透明导航栏
      //  getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        setContentView(R.layout.activity_basketball_main);
        findId();
        initSystemBar(this);
        addFragment();
        vp.setAdapter(new MyAdapter(getSupportFragmentManager()));
        setListener();

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




    private void setListener() {
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.btn_jiaoxue:
                       vp.setCurrentItem(0);
                        textTitle.setText("篮球教学");

                        break;
                    case R.id.btn_jingdian:
                        vp.setCurrentItem(1);
                        textTitle.setText("经典专题");
                        break;
                    case R.id.btn_caipan:
                        vp.setCurrentItem(2);
                        textTitle.setText("篮球裁判");
                        break;
                    case R.id.btn_address:
                        vp.setCurrentItem(3);
                        textTitle.setText("关于我们");
                        break;
                }
            }
        });
        vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position){
                    case 0:
                        radioJiaoxue.setChecked(true);
                        break;
                    case 1:
                        radioJingdian.setChecked(true);
                        break;
                    case 2:
                        radioCaipan.setChecked(true);
                        break;
                    case 3:
                        radioWomen.setChecked(true);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }



    private  class MyAdapter extends FragmentStatePagerAdapter {
        public MyAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return listFragment.get(position);
        }

        @Override
        public int getCount() {
            return listFragment.size();
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {

        }
    }

    private void addFragment() {
        listFragment.add(new JiaoXUE());
        listFragment.add(new JingDianZhuanTi());
        listFragment.add(new CaiPan());
        listFragment.add(new WomenFragment());
    }

    private void findId() {
        radioGroup= (RadioGroup) findViewById(R.id.radioGroup);
        radioCaipan= (RadioButton) findViewById(R.id.btn_caipan);
        radioWomen= (RadioButton) findViewById(R.id.btn_address);
        radioJiaoxue= (RadioButton) findViewById(R.id.btn_jiaoxue);
        radioJingdian= (RadioButton) findViewById(R.id.btn_jingdian);
        textTitle= (TextView) findViewById(R.id.title_text);
        vp= (ViewPager) findViewById(R.id.viewPager);
        rl= (RelativeLayout) findViewById(R.id.title_linear);
        toolbar= (Toolbar) findViewById(R.id.toolbar);
    }
}
