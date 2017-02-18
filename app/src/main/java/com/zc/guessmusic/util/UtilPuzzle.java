package com.zc.guessmusic.util;

import android.app.AlertDialog;
import android.content.Context;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.zc.guessmusic.R;
import com.zc.guessmusic.model.IAlertDialogButtonListener;

import static com.zc.guessmusic.util.Util.getView;

/**
 * Created by Panda_Program on 2017/2/7 0007.
 */

public class UtilPuzzle {
    private static AlertDialog mAlertDialog;
    public static void showDialog(final Context context, String message, final IAlertDialogButtonListener listener) {
        View dialogView = null;

        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.Theme_Transparent);
        dialogView = getView(context, R.layout.hint_layout);

        ImageButton btnOkView = (ImageButton) dialogView.findViewById(R.id.btn_dialog_ok);
        ImageButton btnCancelView = (ImageButton) dialogView.findViewById(R.id.btn_dialog_cancel);
        TextView txtMessageView = (TextView) dialogView.findViewById(R.id.text_message);
        txtMessageView.setText(message);

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
        //为dialog设置VIew
        builder.setView(dialogView);
        mAlertDialog = builder.create();
        //显示对话框
        mAlertDialog.show();
    }
}
