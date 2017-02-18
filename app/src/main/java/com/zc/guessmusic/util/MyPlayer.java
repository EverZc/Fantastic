package com.zc.guessmusic.util;

/**
 * Created by Administrator on 2016/12/27 0027.
 */

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.MediaPlayer;
import android.util.Log;

import java.io.IOException;

/**
 * 音乐播放类
 */
public class MyPlayer {
    //索引
    public final static int INDEX_STONE_ENTER  =0;
    public final static int INDEX_STONE_CANCEL =1;
    public final static int INDEX_STONE_COIN   =2;
    //定义 对应的音效文件名字
    private final static String[] SONG_NAMES_TONE=
            {"enter.mp3","cancel.mp3","coin.mp3"};
    //音效
    private static MediaPlayer[] mToneMediaPlayer=
            new MediaPlayer[SONG_NAMES_TONE.length];

    //单例模式
    private static MediaPlayer mMusicMediaPlayer;
    //播放歌曲

    public static void playSong(Context context,String fileName){

        if (mMusicMediaPlayer==null){
            mMusicMediaPlayer=new MediaPlayer();
        }
        //状态的强制重置  (针对非第一次播放的时候)
        mMusicMediaPlayer.reset();
        Log.e("----","MyPlayer");
        //加载声音文件 assets文件夹中
        AssetManager assetManager=context.getAssets();
        try {
            AssetFileDescriptor fileDescriptor=assetManager.openFd(fileName);
            //设置数据源
            mMusicMediaPlayer.setDataSource(
                    //指定文件的路径
                    fileDescriptor.getFileDescriptor(),
                    fileDescriptor.getStartOffset(),
                    fileDescriptor.getLength());
            //进入到准备状态
            mMusicMediaPlayer.prepare();
            //声音播放
            //Log.e("----","/声音播放");
            mMusicMediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //停止播放
    public static void stopTheSong(Context context){
        if (mMusicMediaPlayer!=null){
            mMusicMediaPlayer.stop();
        }
    }
    //音效的播放方法
    public static void playTongSong(Context context,int index){
        //加载声音
        AssetManager assetManager=context.getAssets();
        if(mToneMediaPlayer[index]==null){
            mToneMediaPlayer[index]=new MediaPlayer();

            try {
                AssetFileDescriptor fileDescriptor=
                        assetManager.openFd(SONG_NAMES_TONE[index]);
                mToneMediaPlayer[index].setDataSource(
                        fileDescriptor.getFileDescriptor(),
                        fileDescriptor.getStartOffset(),
                        fileDescriptor.getLength());
                mToneMediaPlayer[index].prepare();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        mToneMediaPlayer[index].start();
    }
}
