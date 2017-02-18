package com.zc.guessmusic.data;

/**
 * Created by Administrator on 2016/12/21 0021.
 * 常量   歌曲的信息
 */

public class Const {
    public static  final  int INDEX_FILE_NAME=0;
    public static final  int GOLD=1000;
    public static  final int INDEX_SONG_NAME=1;

    public final static int STATUS_ANSWER_RIGHT=1;//答案正确
    public final static int STATUS_ANSWER_WRONG=2;//答案错误
    public final static int STATUS_ANSWER_LACK=3;//答案不完整
    public  static  final String SONG_INFO[][]={
            {"z1.m4a","成都"},
            {"z6.mp3","海阔天空"},
            {"z5.mp3","喜欢你"},
            {"z4.mp3","夜空中最亮的星"},
            {"z2.mp3","小幸运"},
            {"z3.mp3","南山南"},
            {"z7.mp3","鸿雁"},
            {"z8.mp3","我的歌声里"},
    };
    //保存文件的名字  有无后缀都可以
    public static final String FILE_NAME_SAVE_DATA="data.dat";
    public static final int INDEX_LOAD_DATA_STAGE=0;
    public static final int INDEX_LOAD_DATA_COINS=1;
}
