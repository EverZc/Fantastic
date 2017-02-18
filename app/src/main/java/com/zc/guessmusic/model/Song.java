package com.zc.guessmusic.model;

/**
 * Created by Administrator on 2016/12/21 0021.
 */

public class Song {
    //歌曲名称
    private String mSongName;
    //歌曲文件名称
    private String mSongFileName;
    //歌曲名字的长度
    private int mNameLength;

    //字符串装换成一个一个的文字
    public char[] getNameCharacters(){
        return mSongName.toCharArray();  //返回成Char数组
    }

    public String getSongName() {
        return mSongName;
    }

    public void setSongName(String songName) {
        this.mSongName = songName;
        this.mNameLength=songName.length();
    }

    public String getSongFileName() {
        return mSongFileName;
    }

    public void setSongFileName(String mSongFileName) {
        this.mSongFileName = mSongFileName;
    }

    public int getNameLength() {
        return mNameLength;
    }

    public void setNameLength(int mNameLength) {
        this.mNameLength = mNameLength;
    }
}
