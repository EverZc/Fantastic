package com.zc.guessmusic.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.zc.guessmusic.util.MyUtils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;

public class TCPServerService extends Service {
    //判断Socket是否停止了
    private boolean mIsServiceDestoryed = false;
    private String[] mDefinedMessages = new String[] {
            "四川行步行科技有限公司是一家以旅游信息技术服务为主导、立足于互联网旅游信息技术领域的前沿，专业致力于旅游服务机软件开发及应用的高科技民营企业。",
            "行步行科技有限公司宗旨:“精诚致业，追求创新”？",
            "行步行科技有限公司崇尚“以人为本”的管理思想",
            "追求服务、诚信、专业的经营理念，博众之长、补己之短，以专业和创新的理念",
            "行步行科技有限公司在旅游、金融、互联网、等多个行业内拥有丰富的技术研发经验。"

    };

    @Override
    public void onCreate() {
        new Thread(new TcpServer()).start();
        super.onCreate();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        mIsServiceDestoryed = true;
        Log.e("-----","TCPServer _____mIsServiceDestoryed = true" );
        super.onDestroy();
    }

    private class TcpServer implements Runnable {

        @SuppressWarnings("resource")
        @Override
        public void run() {
            ServerSocket serverSocket = null;
            try {
                serverSocket = new ServerSocket(8008);
            } catch (IOException e) {
                System.err.println("establish tcp server failed, port:8688");
                e.printStackTrace();
                return;
            }

            while (!mIsServiceDestoryed) {
                try {
                    // 接受客户端请求
                    final Socket client = serverSocket.accept();
                    Log.e("-----","_Tcp Server ______接收开始了_____" );
                    new Thread() {
                        @Override
                        public void run() {
                            try {
                                responseClient(client);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        };
                    }.start();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void responseClient(Socket client) throws IOException {
        // 用于接收客户端消息
        BufferedReader in = new BufferedReader(new InputStreamReader(
                client.getInputStream()));
        // 用于向客户端发送消息
        PrintWriter out = new PrintWriter(new BufferedWriter(
                new OutputStreamWriter(client.getOutputStream())), true);
        //out.println("欢迎来到聊天室！");
        while (!mIsServiceDestoryed) {
            String str = in.readLine();
            Log.e("-----","TCPServerService----客户端的内容为:" + str );

            if (str == null) {
                break;
            }
            int i = new Random().nextInt(mDefinedMessages.length);
            String msg = mDefinedMessages[i];
            out.println(msg);
            Log.e("-----","TCPServerService----send :" + msg );
        }
        Log.e("-----","TCPServerService----SERVER退出了. 准备关闭流 " );
        // 关闭流
        MyUtils.close(out);
        MyUtils.close(in);
        client.close();
    }

}
