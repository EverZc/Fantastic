package com.zc.guessmusic.ui;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.zc.guessmusic.R;
import com.zc.guessmusic.adapter.ListViewAdapter;
import com.zc.guessmusic.custom_ui.Chat_Button;
import com.zc.guessmusic.model.Chat;
import com.zc.guessmusic.model.ChatItemListViewBean;
import com.zc.guessmusic.service.TCPServerService;
import com.zc.guessmusic.util.MyUtils;

import org.litepal.crud.DataSupport;
import org.litepal.tablemanager.Connector;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import static com.zc.guessmusic.R.drawable.android;

public class ChatListActivity extends BaseActivity {
    private static final int MESSAGE_RECEIVE_NEW_MSG = 1;
    private static final int MESSAGE_SOCKET_CONNECTED = 2;
    private ListView mListView;
    private EditText receiverEdit;
    private Chat_Button receiverButton;
    private int ANDROID=0;
    private int APPLE=1;
    private List<ChatItemListViewBean> data ;
    private  ListViewAdapter listViewAdapter;

    private Socket mClientSocket;
    private PrintWriter mPrintWriter;

    private String[] mCustomItems = new String[]{"复制", "转发","收藏","删除"};
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_RECEIVE_NEW_MSG: {
                    /*mMessageTextView.setText(mMessageTextView.getText()
                            + (String) msg.obj );//*/
                    SystemClock.sleep(800);
                    Chat chat=new Chat();
                    chat.setText((String) msg.obj);
                    chat.setType(1);
                    chat.setName("apple");
                    chat.save();
                    ChatItemListViewBean bean3 = new ChatItemListViewBean();
                    bean3.setType(0);
                    bean3.setIcon(BitmapFactory.decodeResource(getResources(),
                            R.drawable.apple));
                    bean3.setText((String) msg.obj);
                    data.add(bean3);
                    listViewAdapter.notifyDataSetChanged();

                    mListView.setSelection(data.size()-1);
                    Log.e("-----","Activity 这里写入的是服务器返回的聊天内容"+(String) msg.obj );

                    break;
                }
                case MESSAGE_SOCKET_CONNECTED: {
                    receiverButton.setEnabled(true);
                    break;
                }
                default:
                    break;
            }
        }
    };
    //返回键
    private ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
     //   getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.activity_chat_list);
        data = new ArrayList<ChatItemListViewBean>();
        init();
        receiverButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("-----","activity onclick");
                final String msg = receiverEdit.getText().toString();
                if(TextUtils.isEmpty(msg)){
                    Toast.makeText(ChatListActivity.this,"输入消息不得为空",Toast.LENGTH_SHORT).show();
                }else if (mPrintWriter!=null){

                    Chat chat=new Chat();
                    chat.setText(receiverEdit.getText().toString());
                    chat.setType(1);
                    chat.setName("android");
                    chat.save();
                    ChatItemListViewBean bean2 = new ChatItemListViewBean();
                    bean2.setType(1);
                    bean2.setIcon(BitmapFactory.decodeResource(getResources(),
                            android));
                    bean2.setText(receiverEdit.getText().toString());
                    data.add(bean2);
                    listViewAdapter.notifyDataSetChanged();
                    mPrintWriter.println(msg);
                }
                mListView.setSelection(data.size()-1);
                Intent intent=new Intent(ChatListActivity.this,ChatListActivity_Apple.class);
                PendingIntent pi=PendingIntent.getActivity(ChatListActivity.this,0,intent,0);
                if (!receiverEdit.getText().toString().equals("")){
                    NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                    Notification notification = new NotificationCompat.Builder(ChatListActivity.this)
                            .setContentTitle("收到一条新信息")
                            .setContentText( receiverEdit.getText().toString())
                            .setWhen(System.currentTimeMillis())        //指定被创建的时间
                            .setSmallIcon(R.drawable.android)
                            .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.android))//设置大图标
                            .setDefaults(NotificationCompat.DEFAULT_ALL)            //设置震动
                            .setPriority(NotificationCompat.PRIORITY_MAX)
                            .setContentIntent(pi)  //设置跳转
                            .build();
                    //调用后可以显示通知
                    manager.notify(1, notification);
                }
                receiverEdit.getText().clear();
            }
        });
        Intent service = new Intent(this, TCPServerService.class);
        startService(service);
        new Thread() {
            @Override
            public void run() {
                connectTCPServer();
            }
        }.start();

    }

    private void init() {
        imageView= (ImageView) findViewById(R.id.chat_title_return_imp);
        mListView = (ListView) findViewById(R.id.listView_chat);
        receiverEdit= (EditText) findViewById(R.id.chat_receiver);
        receiverButton= (Chat_Button) findViewById(R.id.chat_receiver_button);
        //返回主界面
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ChatListActivity.this,FirstZcActivity.class);
                startActivity(intent);
            }
        });
        //listView跟随聊天上移
        mListView.setTranscriptMode(ListView.TRANSCRIPT_MODE_NORMAL);
        //设置适配器
        listViewAdapter=new ListViewAdapter(this, data);
        mListView.setAdapter(listViewAdapter);
        //加载数据库中的内容
        addSQLContent();

        //创建数据库的表
        Connector.getDatabase();
        NotificationManager manager1= (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        manager1.cancel(2);
        //listItem的监听
        onItemLongClickListener();
        mListView.setSelection(data.size()-1);
    }

    private void connectTCPServer() {
        Socket socket = null;
        while (socket == null) {
            try {
                socket = new Socket("localhost", 8008);
                mClientSocket = socket;
                //客户端输出数据
                mPrintWriter = new PrintWriter(new BufferedWriter(
                        //客户端输出数据
                        new OutputStreamWriter(socket.getOutputStream())), true);
                mHandler.sendEmptyMessage(MESSAGE_SOCKET_CONNECTED);
                Log.e("---------------","Activity connect server success");
            } catch (IOException e) {
                SystemClock.sleep(1000);
                Log.e("---------------","Activity connect tcp server failed, retry...");
            }
        }
        try {
            // 接收服务器端的消息
            BufferedReader br = new BufferedReader(new InputStreamReader(
                    socket.getInputStream()));
            while (!ChatListActivity.this.isFinishing()) {
                String msg = br.readLine();

                if (msg != null) {
                    Log.e("---------------","Activity 接收到的消息是: " +msg);
                    //String time = formatDateTime(System.currentTimeMillis());
                    final String showedMsg =  msg;
                    mHandler.obtainMessage(MESSAGE_RECEIVE_NEW_MSG, showedMsg)
                            .sendToTarget();
                }
            }
            Log.e("---------------","Activity quit...");
            MyUtils.close(mPrintWriter);
            MyUtils.close(br);
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void onItemLongClickListener() {
        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Log.e("---", String.valueOf(view.getId()));
                switch (view.getId()){

                    //接收的
                    case R.id.chat_linear_in:
                        View v=mListView.getChildAt(position);
                        TextView text1=(TextView) v.findViewById(R.id.text_in);
                        String textin=text1.getText().toString();
                        myOnItemClick(position,textin);

                        break;
                    case 2131427443:
                        View v1=mListView.getChildAt(position);
                        TextView text=(TextView) v1.findViewById(R.id.text_out);
                        String textout=text.getText().toString();
                        myOnItemClick(position,textout);
                        break;
                }
                return false;
            }
        });
    }

    private void myOnItemClick(int position,String text) {
        showDialogCustom(position, text);
    }
    //显示自定义对话框
    private void showDialogCustom(final int position, final String text) {
        AlertDialog.Builder builder = new AlertDialog.Builder(
                ChatListActivity.this);
        final String textUP=text.toString();
        builder.setTitle("选择：");
        Log.e("-textUP--", textUP);
        builder.setItems(mCustomItems,
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (0 == which) {
                            //复制
                            ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                            //将文本内容放到系统剪贴板里。
                                    cm.setText(text);
                          Toast.makeText(ChatListActivity.this,"复制成功",Toast.LENGTH_SHORT).show();
                        } else if (1 == which) {
                            Toast.makeText(ChatListActivity.this,"转发成功(测试)",Toast.LENGTH_SHORT).show();
                        }else if (2==which){
                            Toast.makeText(ChatListActivity.this,"收藏成功(测试)",Toast.LENGTH_SHORT).show();
                        }else if (3==which){
                            int id=-1;
                            List<Chat> chats=DataSupport.findAll(Chat.class);
                            for (Chat chat:chats){
                                if (chat.getText().equals(textUP)){
                                    id= chat.getId();
                                    Log.e("- id up--", String.valueOf(id));
                                }
                            }
                            Log.e("- id--", String.valueOf(id));
                            DataSupport.delete(Chat.class, id);
                            data.clear();
                            addSQLContent();
                           // listViewAdapter.notifyDataSetChanged();
                        }
                    }
                });
        builder.create().show();
    }
    //加载数据库中的内容
    private void addSQLContent() {
        List<Chat> chats= DataSupport.findAll(Chat.class);
        for (Chat chat: chats) {
            if (chat.getName().equals("android")){
                ChatItemListViewBean  cache= new ChatItemListViewBean();
                cache.setType(1);
                cache.setIcon(BitmapFactory.decodeResource(getResources(),
                        android));
                cache.setText(chat.getText());

                data.add(cache);
                listViewAdapter.notifyDataSetChanged();
            }else {
                ChatItemListViewBean  cache= new ChatItemListViewBean();
                cache.setType(0);
                cache.setIcon(BitmapFactory.decodeResource(getResources(),
                        R.drawable.apple));
                cache.setText(chat.getText());

                data.add(cache);
                listViewAdapter.notifyDataSetChanged();
            }
            mListView.setSelection(data.size()-1);

        }
    }

    //获取ListView的高度
    private void setListViewHeightBasedOnChildern(ListView mListView) {
        ListAdapter listAdapter=mListView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        int totalHeight = 0;
        for (int i = 0, len = 5; i < len; i++) {
            // listAdapter.getCount()返回数据项的数目
            View listItem = listAdapter.getView(i, null, mListView);
            // 计算子项View 的宽高
            listItem.measure(0, 0);
            // 统计所有子项的总高度
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = mListView.getLayoutParams();
        params.height = totalHeight+ (mListView.getDividerHeight() * (listAdapter.getCount() - 1));
        // listView.getDividerHeight()获取子项间分隔符占用的高度
        // params.height最后得到整个ListView完整显示需要的高度
        mListView.setLayoutParams(params);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            //获取焦点 上的View
            View v = getCurrentFocus();
            //判断是否需要隐藏键盘
                if (isShouldHideKeyboard(v, ev)) {
                    //隐藏键盘~
                    hideKeyboard(v.getWindowToken());
                }
        }
        return super.dispatchTouchEvent(ev);
    }

    private boolean isShouldHideKeyboard(View v, MotionEvent event) {
        if (v != null && (v instanceof Button)) {
            return false;
        }
        //如果点击的是EditText做以下的处理
        if (v != null && (v instanceof EditText)) {
            int[] l = {0, 0};
            v.getLocationInWindow(l);
                int left = l[0],
                    top = l[1],
                    bottom = top + v.getHeight(),

                    right = left + v.getWidth()+1200;

            //判断点击的位置是否在键盘上
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击EditText的事件，忽略它。
                return false;
            } else {
                return true;
            }
        }
        // 如果焦点不是EditText则忽略，这个发生在视图刚绘制完，第一个焦点不在EditText上，和用户用轨迹球选择其他的焦点
        return false;
    }

    /**
     * 获取InputMethodManager，隐藏软键盘
     * @param token
     */
    private void hideKeyboard(IBinder token) {
        if (token != null) {
            InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            im.hideSoftInputFromWindow(token, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    @Override
    protected void onDestroy() {
        if (mClientSocket != null) {
            try {
                mClientSocket.shutdownInput();
                mClientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        super.onDestroy();
    }
}
