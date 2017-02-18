package com.zc.guessmusic.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.zc.guessmusic.R;
import com.zc.guessmusic.data.Const;
import com.zc.guessmusic.model.IAlertDialogButtonListener;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Administrator on 2016/12/19 0019.
 */

public class Util {
    private static AlertDialog mAlertDialog;

    //封装一个getView方法 以后用View可以直接用
    public static View getView(Context context, int layoutId) {
        // 加载布局的第一种写法
        LayoutInflater inflater = (LayoutInflater) context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //加载布局的第二种写法
        LayoutInflater inflater1 = LayoutInflater.from(context);
        View layout = inflater.inflate(layoutId, null);
        return layout;
    }

    //做Activity的跳转
    public static void startActivity(Context context, Class desti) {
        Intent intent = new Intent();
        intent.setClass(context, desti);
        context.startActivity(intent);

        //关闭当前的Activity;
        ((Activity) context).finish();
    }

    /*
    * 显示自定义对话框  猜歌游戏
    * */
    public static void showDialog(final Context context, String message, final IAlertDialogButtonListener listener) {
        View dialogView = null;

        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.Theme_Transparent);
        dialogView = getView(context, R.layout.hint_layout);

        ImageButton btnOkView = (ImageButton) dialogView.findViewById(R.id.btn_dialog_ok);
        ImageButton btnCancelView = (ImageButton) dialogView.findViewById(R.id.btn_dialog_cancel);
        TextView txtMessageView = (TextView) dialogView.findViewById(R.id.text_message);
        txtMessageView.setText(message);

        //为dialog设置VIew
        builder.setView(dialogView);
        mAlertDialog = builder.create();
        //显示对话框
        mAlertDialog.show();

        btnOkView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //关闭对话框
                if (mAlertDialog != null) {
                    mAlertDialog.cancel();
                }
                //事件回调
                if (listener != null) {
                    listener.myOnClick();
                }
                //播放音效
                MyPlayer.playTongSong(context, MyPlayer.INDEX_STONE_ENTER);
            }
        });
        btnCancelView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //关闭对话框
                if (mAlertDialog != null) {
                    mAlertDialog.cancel();
                }
                //播放音效
                MyPlayer.playTongSong(context, MyPlayer.INDEX_STONE_CANCEL);
            }
        });
    }

    //数据的保存
    public static void saveData(Context context, int stageIndex, int coins) {

        FileOutputStream fileOutputStream = null;
        try {
            //文件存储
            fileOutputStream = context.openFileOutput(
                    Const.FILE_NAME_SAVE_DATA,//文件名
                    Context.MODE_PRIVATE);
            DataOutputStream dataOutputStream = new DataOutputStream(fileOutputStream);
            dataOutputStream.writeInt(stageIndex);
            dataOutputStream.writeInt(coins);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (fileOutputStream!=null){
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }
    //数据文件的读取
    public static int[] loadData(Context context) {
        FileInputStream fileInputStream = null;
        int[] datas = {-1, Const.GOLD};
        try {
            fileInputStream = context.openFileInput(Const.FILE_NAME_SAVE_DATA);
            DataInputStream dataInputStream = new DataInputStream(fileInputStream);
            try {
                datas[Const.INDEX_LOAD_DATA_STAGE] = dataInputStream.readInt();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                datas[Const.INDEX_LOAD_DATA_COINS] = dataInputStream.readInt();
            } catch (IOException e) {
                e.printStackTrace();
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (fileInputStream!=null){
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return datas;
    }
}