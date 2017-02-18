package com.zc.guessmusic.util;

import java.io.UnsupportedEncodingException;
import java.util.Random;

/**
 * Created by Administrator on 2016/12/22 0022.
 * GB2312对汉字进行了分区处理
 * 高位:
 * 01-09特殊符号
 * 16-55一级汉字  BO
 * 56-87二级汉字
 */
//生成随机汉字
public class GetRandom {
    public char getRandomChar(){
        String str="";
        int hightPos;
        int lowPos;

        Random random=new Random();
        //汉字的高位为176 十六进制为B0
        hightPos=(176+Math.abs(random.nextInt(39)));
        //161---A1
        lowPos=(161+Math.abs(random.nextInt(93)));

        //字节数组组合高位和低位
        byte[] b=new byte[2];
        //组合字节
        b[0]=Integer.valueOf(hightPos).byteValue();
        b[1]=Integer.valueOf(lowPos).byteValue();

        //Byte数组转换成字符串
        try {
            str=new String(b,"GBK");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        //返回CHAR 的 0号位就可以
        return str.charAt(0);
    }
}
