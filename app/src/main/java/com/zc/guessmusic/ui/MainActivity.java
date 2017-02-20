package com.zc.guessmusic.ui;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.zc.guessmusic.R;
import com.zc.guessmusic.custom_ui.MyGridView;
import com.zc.guessmusic.data.Const;
import com.zc.guessmusic.model.IAlertDialogButtonListener;
import com.zc.guessmusic.model.IWordButtonClickListener;
import com.zc.guessmusic.model.Song;
import com.zc.guessmusic.model.WordButton;
import com.zc.guessmusic.util.GetRandom;
import com.zc.guessmusic.util.MyLog;
import com.zc.guessmusic.util.MyPlayer;
import com.zc.guessmusic.util.Util;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends BaseActivity implements IWordButtonClickListener {
    public final static int COUNTS_WORDS=24;
    //盘片动画
    private Animation mPanAnim;
    //对动画的加速减速  线性的播放
    private LinearInterpolator mPanLin;
    //播杆
    private Animation mBarInAnim;
    private LinearInterpolator mBarInLin;

    private ImageView mViewPan;
    private ImageView mViewPanBar;

    private Animation mBarOutAnim;
    private LinearInterpolator mBarOutLin;

    //文字框容器
    private ArrayList<WordButton> mAllWords;  // 待选框容器
    private ArrayList<WordButton> mWordSelect;//已选框的容器

    private MyGridView myGridView;
    //按键事件
    private ImageButton mPlayStart;
    private boolean mIsRunning=false;
    //已选择文字框UI  Layout 容器
    private LinearLayout mViewWordsContainer;

    //过关界面
    private View mPassView;
    //当前歌曲的对象
    private Song mCurrentSong;
    //当前关的索引  下标  标注
    private int mCurrentStageIndex=-1;  // 默认为 -1

    //当前金币的数量
    private int mCurrentCoins=Const.GOLD;
    //金币的View
    private TextView mViewGold;

    //闪烁的常量
    private static  final int SPASH_TIMES=5;

    //当前关的索引  过关界面
    private TextView mCurrentStagePassView;
    //当前关的索引  游戏中的界面
    private TextView mCurrentStageView;
        //当前歌曲名称
    private TextView mCurrentSongNamePassView;
    public final static int ID_DIALOG_DELETE_WORD=1;
    public final static int ID_DIALOG_TIP_ANSWER=2;
    public final static int ID_DIALOG_LACK_COINS=3;
    //分享
    private ImageButton imageButtonShare;
    //返回键
    private ImageButton imageButtonReturn;
    //侧拉
    private RadioGroup radioGroup;
    private RadioButton rbtuijian,rbliuxing,rbminyao,rbdianzi,rbyaogun,rbjinshu,rbshuochang;
    private List<RadioButton> buttonList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        buttonList=new ArrayList<>();
        //侧拉界面
        radioGroup= (RadioGroup) findViewById(R.id.full_left);
        rbtuijian= (RadioButton) findViewById(R.id.disc_tuijian);
        buttonList.add(rbtuijian);
        rbliuxing= (RadioButton) findViewById(R.id.disc_liuxing);
        buttonList.add(rbliuxing);
        rbminyao= (RadioButton) findViewById(R.id.disc_minyao);
        buttonList.add(rbminyao);
        rbdianzi= (RadioButton) findViewById(R.id.disc_dianzi);
        buttonList.add(rbdianzi);
        rbyaogun= (RadioButton) findViewById(R.id.disc_yaogun);
        buttonList.add(rbyaogun);
        rbjinshu= (RadioButton) findViewById(R.id.disc_jinshu);
        buttonList.add(rbjinshu);
        rbshuochang= (RadioButton) findViewById(R.id.disc_shuochang);
        buttonList.add(rbshuochang);
        //分享
        imageButtonShare= (ImageButton) findViewById(R.id.btn_share);
        imageButtonShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // ShareSDK.initSDK(this);
            }
        });
        //读取数据
        int[] datas=Util.loadData(this);
        mCurrentStageIndex=datas[Const.INDEX_LOAD_DATA_STAGE];
        mCurrentCoins=datas[Const.INDEX_LOAD_DATA_COINS];
        myGridView= (MyGridView) findViewById(R.id.gridview);
        imageButtonReturn= (ImageButton) findViewById(R.id.btn_bar_back);
        imageButtonReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,FirstZcActivity.class);
                startActivity(intent);
            }
        });
        //注册监听 (观察者)
        myGridView.regusterListener(this);
        mViewGold= (TextView) findViewById(R.id.txt_bar_coins);
        mViewGold.setText(mCurrentCoins+"");
        mViewPan= (ImageView) findViewById(R.id.imageView1);
        mViewPanBar= (ImageView) findViewById(R.id.imageView2);
        mViewWordsContainer= (LinearLayout) findViewById(R.id.word_select_container);
        //加载唱片部分的动画
        ActionLoad();
        //处理删除按钮
        DeleteWord();
        //处理提示事件
        TipWord();
        radioGrouplistener();


    }


    //加载动画
    private void ActionLoad() {
        //初始化动画
        mPanAnim= AnimationUtils.loadAnimation(this, R.anim.animaa);
        mPanLin=new LinearInterpolator();
        mPanAnim.setInterpolator(mPanLin);
        mPanAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
            mViewPanBar.startAnimation(mBarOutAnim);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        mBarInAnim= AnimationUtils.loadAnimation(this,R.anim.bar_in);
        mBarInLin=new LinearInterpolator();
        //停留在移动最后的位置
        mBarInAnim.setFillAfter(true);
        mBarInAnim.setInterpolator(mBarInLin);
        mBarInAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
            mViewPan.startAnimation(mPanAnim);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        mBarOutAnim= AnimationUtils.loadAnimation(this,R.anim.bar_out);
        mBarOutLin=new LinearInterpolator();
        mBarOutAnim.setInterpolator(mBarOutLin);
        mBarOutAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mIsRunning=false;
                mPlayStart.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        mPlayStart= (ImageButton) findViewById(R.id.btn_play_start);
        mPlayStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //开始播放音乐
               handlePlayButton();
            }
        });
        //初始化当前关的数据
        initCurrentStageData();
    }
    //播杆移回原位置的动画
    //开始播放音乐
    private void handlePlayButton(){
        Log.e("----","/声音播放");
        //播杆左移
        if (mViewPanBar!=null){
            if (!mIsRunning) {
                mIsRunning=true;
                //开始播杆动画
                mViewPanBar.startAnimation(mBarInAnim);
                mPlayStart.setVisibility(View.INVISIBLE);
                //播放音乐
                MyPlayer.playSong(MainActivity.this,
                        mCurrentSong.getSongFileName());
            }
        }
    }
    @Override
    protected void onPause() {
        //保存游戏数据
        Util.saveData(MainActivity.this,mCurrentStageIndex-1,
                mCurrentCoins);
       //停止动画
        mViewPan.clearAnimation();
        MyPlayer.stopTheSong(MainActivity.this);
        super.onPause();
    }

    //初始化当前关的数据
    private void initCurrentStageData(){
        //读取当前关的歌曲信息
        mCurrentSong=loadCurrentSong(++mCurrentStageIndex);
        //radioButton设置为True
        buttonList.get(mCurrentStageIndex).setChecked(true);
        //清空原来的答案
        mViewWordsContainer.removeAllViews();
        //初始化已选择框  增加新的答案框
        mWordSelect =initWordSelect();
        LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(140,140);
        for (int i = 0; i < mWordSelect.size(); i++) {
            //已选择文字框的容器
            mViewWordsContainer.addView(mWordSelect.get(i).mViewButton,
                    params);
        }
        //显示当前的索引
        mCurrentStageView= (TextView) findViewById(R.id.text_title);
        if (mCurrentStageView!=null){
            mCurrentStageView.setText((mCurrentStageIndex+1)+"");
        }
        //获得数据
        mAllWords=initAllWorid();
        //更新数据  MyGridView
        myGridView.updateData(mAllWords);
        //一开始就播放音乐
        handlePlayButton();
    }

    //读取当前关的歌曲信息
    private Song loadCurrentSong(int stageIndex){
        Song song=new Song();
        String[] stage= Const.SONG_INFO[stageIndex];
        song.setSongFileName(stage[Const.INDEX_FILE_NAME]);
        song.setSongName(stage[Const.INDEX_SONG_NAME]);
        return song;
    }
    //初始化待选文字框     待选
    private ArrayList<WordButton> initAllWorid(){
        ArrayList<WordButton> data=new ArrayList<>();
        //获得所有待选文字
        String[] words=generateWords();
        for (int i = 0; i < 24; i++) {
            WordButton button=new WordButton();
            button.mWordString=words[i];
            data.add(button);
        }
        return data;
    }
    //初始化已经选择的文字框 已选
    private ArrayList<WordButton> initWordSelect(){
        ArrayList<WordButton> data=new ArrayList<>();
        //获得所有待选文字 (点击过后的)
        for (int i = 0; i <mCurrentSong.getNameLength() ; i++) {
            View seleView= Util.getView(MainActivity.this, R.layout.custom_ui_item);
            final WordButton holder=new WordButton();
            holder.mViewButton= (Button) seleView.findViewById(R.id.item_btn);
            holder.mViewButton.setTextColor(Color.WHITE);
            holder.mViewButton.setText("");
            holder.mIsVisiable=false;
            holder.mViewButton.setBackgroundResource(R.drawable.game_wordblank);
            holder.mViewButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clearAnswer(holder);
                }
            });
            data.add(holder);
        }
        return data;
    }

    //待选框的点击事件
    @Override
    public void onWordButtonClick(WordButton wordButton) {
        //为已选择框设置文字  答案框的文字
        setSelectWord(wordButton);
        //答案状态的检测  获得答案状态
        int checkResult=checkAnswer();

        switch (checkResult){
            case 1:         //正确   过关并获得金币的奖励
                handlePassEvent();
                break;
            case 2:         //错误   并且进行错误提示: 闪烁文字
                sparkWrods();
                //最后设置成白颜色
                for (int i = 0; i < mWordSelect.size(); i++) {
                    mWordSelect.get(i).mViewButton.setTextColor(Color.WHITE);
                }
                break;
            case 3:         //答案的缺失
                break;
        }
    }
    private void clearAnswer(WordButton wordButton){
        wordButton.mIsVisiable=false;
        wordButton.mViewButton.setText("");
        wordButton.mWordString="";

        //设置待选框的可见性
        setButtonVisiable(mAllWords.get(wordButton.mIndex),View.VISIBLE);
    }
    //为已选择框设置文字  答案框的文字
    private void setSelectWord(WordButton wordButton){
        for (int i = 0; i < mWordSelect.size(); i++) {
            if (mWordSelect.get(i).mWordString.length()==0){
                //设置答案框的内容以及可见性
                mWordSelect.get(i).mViewButton.setText(wordButton.mWordString);
                mWordSelect.get(i).mIsVisiable=true;
                mWordSelect.get(i).mWordString=wordButton.mWordString;
                //记录索引
                mWordSelect.get(i).mIndex=wordButton.mIndex;
                //设置待选框的可见性
                setButtonVisiable(wordButton,View.INVISIBLE);
                break;
            }
        }
    }
    /*
    * 设置文字框是否可见
    * */
    private void setButtonVisiable(WordButton wordButton,int visiblity){
        wordButton.mViewButton.setVisibility(visiblity);
        wordButton.mIsVisiable=(visiblity==View.VISIBLE)?true:false;
        MyLog.d("----",wordButton.mIsVisiable+"");
    }
    //生成所有的待选文字
    private String[] generateWords(){
        String[]  words=new String[MainActivity.COUNTS_WORDS];
        //存入歌名
        for (int i = 0; i < mCurrentSong.getNameLength(); i++) {
            words[i]= mCurrentSong.getNameCharacters()[i]+"";
        }
        //获得随机文字
        GetRandom getRandom=new GetRandom();
        for (int i = mCurrentSong.getNameLength(); i <MainActivity.COUNTS_WORDS ; i++) {
            words[i]=getRandom.getRandomChar()+"";
        }
        /*打乱文字的顺序  用数组进行交换
        * 1.首先从所有元素中随机选择一个  与第一个元素进行交换
          2.在第二个数据之后选择一个让他跟第二个数据进行交换,过程是循环的, 知道循环到最后一个元素为止
          能够确保每个元素在每个位置的概率都是1/n.
          Random对象生成[0,24)
        * */
        Random randon=new Random();
        for (int i = MainActivity.COUNTS_WORDS-1; i >=0; i--) {
            int index=randon.nextInt(i+1);
            String buf=words[index];
            words[index]=words[i];
            words[i]=buf;
        }
        return words;
    }
    /*
    * 检查答案
    * */
    private int checkAnswer(){
        Const mConst = null;
        //首先检查长度才行
        for (int i = 0; i < mWordSelect.size(); i++) {
            //如果有空的 说明是不完整的..
            if(mWordSelect.get(i).mWordString.length()==0){
                return mConst.STATUS_ANSWER_LACK;
            }
        }
        //答案完整  继续向下检查
        StringBuffer sb=new StringBuffer();
        for (int i = 0; i < mWordSelect.size(); i++) {
            sb.append(mWordSelect.get(i).mWordString);
        }
        return (sb.toString().equals(mCurrentSong.getSongName())?
                Const.STATUS_ANSWER_RIGHT:Const.STATUS_ANSWER_WRONG);
    }

    //闪烁文字    变换文字的颜色实现
    private void sparkWrods(){
        //声明一个定时器相关的内容
        TimerTask task=new TimerTask() {
            boolean mChange=false; //要不要改变文字
            int mSpardTimes=0;      //计时器的值
            @Override
            public void run() {
                //注意
                //此处应该在UI线程中使用  用以下方法
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (++mSpardTimes>SPASH_TIMES){
                            return;
                        }
                        //执行闪烁的逻辑
                        //交替显示红色和白色文字
                        for (int i = 0; i < mWordSelect.size(); i++) {
                            mWordSelect.get(i).mViewButton.setTextColor(
                                    mChange?Color.RED:Color.WHITE
                            );
                        }
                        mChange=!mChange;
                    }
                });
            }
        };
        Timer timer=new Timer();
        timer.schedule(task,1,150);
    }
    //侧拉监听
    private void radioGrouplistener() {
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.disc_tuijian:
                        // 停止未完成的动画
                        mViewPan.clearAnimation();
                        //停止正在播放的音乐
                        MyPlayer.stopTheSong(MainActivity.this);
                        //加载关卡数据
                        initCurrentStageData_Sliding(0);
                        break;
                    case R.id.disc_liuxing:
                        // 停止未完成的动画
                        mViewPan.clearAnimation();
                        //停止正在播放的音乐
                        MyPlayer.stopTheSong(MainActivity.this);
                        //加载关卡数据
                        initCurrentStageData_Sliding(1);
                        break;
                    case R.id.disc_minyao:
                        // 停止未完成的动画
                        mViewPan.clearAnimation();
                        //停止正在播放的音乐
                        MyPlayer.stopTheSong(MainActivity.this);
                        //加载关卡数据
                        initCurrentStageData_Sliding(2);
                        break;
                    case R.id.disc_dianzi:
                        // 停止未完成的动画
                        mViewPan.clearAnimation();
                        //停止正在播放的音乐
                        MyPlayer.stopTheSong(MainActivity.this);
                        //加载关卡数据
                        initCurrentStageData_Sliding(3);
                        break;
                    case R.id.disc_yaogun:
                        // 停止未完成的动画
                        mViewPan.clearAnimation();
                        //停止正在播放的音乐
                        MyPlayer.stopTheSong(MainActivity.this);
                        //加载关卡数据
                        initCurrentStageData_Sliding(4);
                        break;
                    case R.id.disc_jinshu:
                        // 停止未完成的动画
                        mViewPan.clearAnimation();
                        //停止正在播放的音乐
                        MyPlayer.stopTheSong(MainActivity.this);
                        //加载关卡数据
                        initCurrentStageData_Sliding(5);
                        break;
                    case R.id.disc_shuochang:
                        // 停止未完成的动画
                        mViewPan.clearAnimation();
                        //停止正在播放的音乐
                        MyPlayer.stopTheSong(MainActivity.this);
                        //加载关卡数据
                        initCurrentStageData_Sliding(6);
                        break;
                    default:
                }
            }
        });
    }
    //处理过关界面及事件
    private void handlePassEvent(){
        //显示过关界面
        MyLog.e("----","right");
        mPassView=this.findViewById(R.id.pass_view);
        mPassView.setVisibility(View.VISIBLE);
        // 停止未完成的动画
        mViewPan.clearAnimation();
        //停止正在播放的音乐
        MyPlayer.stopTheSong(MainActivity.this);
        //播放得到金币的音效
        MyPlayer.playTongSong(MainActivity.this,MyPlayer.INDEX_STONE_COIN);
        //当前关的索引
        mCurrentStagePassView= (TextView) findViewById(R.id.text_current_stage_pass);
        if (mCurrentStagePassView!=null){
            mCurrentStagePassView.setText(mCurrentStageIndex+1+"");
        }
        //显示歌曲名称
        mCurrentSongNamePassView= (TextView) findViewById(R.id.text_current_song_name_pass);
        if (mCurrentSongNamePassView!=null){
            mCurrentSongNamePassView.setText(mCurrentSong.getSongName());
        }
        mCurrentCoins+=3;
        mViewGold.setText(mCurrentCoins+"");
        //下一关的按键处理
        ImageButton btnPass= (ImageButton) findViewById(R.id.btn_next);
        btnPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tongguanApp()){
                    //进入到通关的界面
                    Util.startActivity(MainActivity.this,AppPassView.class);
                }else {
                    //跳转到下一关
                    mPassView.setVisibility(View.GONE);

                    //加载关卡数据
                    initCurrentStageData();
                }
            }
        });
    }
    //判断是否通关
    private boolean tongguanApp(){
        return (mCurrentStageIndex==Const.SONG_INFO.length-1);
    }
    //相应处理 删除 待选文字事件
    private void DeleteWord(){
        ImageButton button= (ImageButton) findViewById(R.id.btn_delete_word);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showConfirmDialog(ID_DIALOG_DELETE_WORD);
            }
        });
    }
    //处理提示的事件
    private void TipWord(){
        ImageButton button= (ImageButton) findViewById(R.id.btn_tip_answer);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showConfirmDialog(ID_DIALOG_TIP_ANSWER);
            }
        });
    }
    /*
    * 增加或者减少指定数量的金币
    *  true 成功   false失败 不可以才做
    * */
    private boolean handleCoins(int data){
        //判断当前金币数量时候可以被减少
        if (mCurrentCoins+data>=0){
            mCurrentCoins+=data;
            mViewGold.setText(mCurrentCoins+"");
            return true;
        }
        return false;
    }
    //自动选择一个答案
    private void tipAnswer(){
        //不应该上来就扣钱
        //减少金币数量
        boolean tipWord=false;
        if(!handleCoins(-getTipWordGold())){
            //金币不足提示一个对话框
            showConfirmDialog(ID_DIALOG_LACK_COINS);
            return;
        }
        for (int i = 0; i < mWordSelect.size(); i++) {
            if (mWordSelect.get(i).mWordString.length()==0){
                //直接调用方法 可以进行是否显示的操作
                //根据当前的答案框条件选择对应的文字并且填入
                onWordButtonClick(findIsAnswerWord(i));
                tipWord=true;
                break;
            }
        }
        // 没有找到可以填充的答案                个人不同意这个代码  没有意义
        if (!tipWord){
            //闪烁文字提示用户
            sparkWrods();
        }
    }
    private void deleteOnWord(){
        //减少金币
        Log.e("----",getDeleteWordGold()+"");
        if (!handleCoins(-getDeleteWordGold())){
            //金币不够 显示提示的对话框
            showConfirmDialog(ID_DIALOG_LACK_COINS);
        }
        //将这个索引对应的WordButton设置为不可见
        setButtonVisiable(findNotAnswerWord(),View.INVISIBLE);
    }
    //获得XML文件的 int值   删除文字需要用的金币
    public int getDeleteWordGold(){
        return this.getResources().getInteger(R.integer.pay_delete_word);
    }
    //获得XML文件的 int值   提示文字需要用到的金币
    public int getTipWordGold(){
        return this.getResources().getInteger(R.integer.pay_tip_answer);
    }
    /*
    * 找到一个是答案的文字
    * */
    private WordButton findIsAnswerWord(int  index){
        WordButton buf=null;
        for (int i = 0; i < MainActivity.COUNTS_WORDS; i++) {
            buf=mAllWords.get(i);
            if (buf.mWordString.equals(""+mCurrentSong.getNameCharacters()[index])){
                return buf;
            }
        }
        return null;
    }
    /*
    * 找到的这个文字不是答案  而且当前还是可见的
    * */
    private WordButton findNotAnswerWord(){
        Random random=new Random();
        WordButton buf=null;
        while (true){
            int index=random.nextInt(MainActivity.COUNTS_WORDS);
            buf=mAllWords.get(index);
            //判断是不是标准答案  可不可见
            if (buf.mIsVisiable&&!isAnswerWord(buf)){
                return buf;
            }
        }
    }
    //判断某个文字是否是答案
    private boolean isAnswerWord(WordButton wordButton){
        boolean result=false;
        for (int i = 0; i < mCurrentSong.getNameLength(); i++) {
            // 字符串和字符的互转 +  ""
            if (wordButton.mWordString.equals(""+mCurrentSong.getNameCharacters()[i])){
                result=true;
                break;
            }

        }
        return result;
    }
    //自定义AlertDialog事件响应
    //删除错误答案
    private IAlertDialogButtonListener mBtnOkDeleteWordListener=new IAlertDialogButtonListener() {
        @Override
        public void myOnClick() {
            deleteOnWord();
        }
    };
    //答案提示
    private IAlertDialogButtonListener mBtnTipAnswerListener=new IAlertDialogButtonListener() {
        @Override
        public void myOnClick() {
            tipAnswer();
        }
    };
    //金币不足
    private IAlertDialogButtonListener mBtnOkLackCoinsListener=new IAlertDialogButtonListener() {
        @Override
        public void myOnClick() {

        }
    };
    //显示对话框
    private void showConfirmDialog(int id){
        switch (id){
            case ID_DIALOG_DELETE_WORD:
                Util.showDialog(MainActivity.this,"确认花掉"+getDeleteWordGold()+"个金币去掉一个错误答案",
                        mBtnOkDeleteWordListener);
                break;
            case  ID_DIALOG_TIP_ANSWER:
                Util.showDialog(MainActivity.this,"确认花掉"+getTipWordGold()+"个金币获得一个文字提示",
                        mBtnTipAnswerListener);
                break;
            case ID_DIALOG_LACK_COINS:
                Util.showDialog(MainActivity.this,"金币不足请去商店补充",
                        mBtnOkLackCoinsListener);
                break;
        }
    }

    //侧拉显示当前关卡数据
    //初始化当前关的数据
    private void initCurrentStageData_Sliding(int inti){
        mCurrentStageIndex=inti;
        //读取当前关的歌曲信息
        mCurrentSong=loadCurrentSong(inti);
        //清空原来的答案
        mViewWordsContainer.removeAllViews();
        //初始化已选择框  增加新的答案框
        mWordSelect =initWordSelect();
        LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(140,140);
        for (int i = 0; i < mWordSelect.size(); i++) {
            //已选择文字框的容器
            mViewWordsContainer.addView(mWordSelect.get(i).mViewButton,
                    params);
        }
        //显示当前的索引
        mCurrentStageView= (TextView) findViewById(R.id.text_title);
        if (mCurrentStageView!=null){
            mCurrentStageView.setText((mCurrentStageIndex+1)+"");
        }
        //获得数据
        mAllWords=initAllWorid();
        //更新数据  MyGridView
        myGridView.updateData(mAllWords);
        //一开始就播放音乐
        handlePlayButton();
    }
}