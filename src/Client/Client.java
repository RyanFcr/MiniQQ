package Client;


import Controller.ConnPool;
import Controller.ControlOp;

import javax.swing.*;
import java.io.*;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Client {
    private ControlOp controlOp;
    private final String IP = "127.0.0.1";
    private final int PORT = 8080;  //监听8080端口
    private Socket socket;

    private BufferedReader br;
    private PrintWriter pw;

    private String userID;      //用户ID
    private String oppentID;    //对方ID

    /**
     * 初始化客户端
     *
     * @param uID 用户ID
     * @param c   主控
     */
    public Client(String uID, ControlOp c) {
        userID = uID;
        controlOp = c;
        try {
            socket = new Socket(IP, PORT);
            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            pw = new PrintWriter(socket.getOutputStream(), true);
            pw.println("0:" + userID);    //告知服务器本线程的用户ID
            pw.flush();
            receiveMsg();   //创建接受消息线程
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    /**
     * 客户端接受信息
     */
    public void receiveMsg() {
        Runnable runnable = new Runnable() {
            String msg;

            @Override
            public void run() {
                while (true) {
                    try {
                        if (!((msg = br.readLine()) != null))
                            break;
                        //打印已收到的信息
                        System.out.println(userID + "收到" + msg);
                        String[] message = msg.split(":");
                        if (message[0].equals("1")) {
                            //收到对方的聊天内容
                            Date date = new Date(); // this object contains the current date value
                            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                            controlOp.menu.messagePanel.addMsg(controlOp.getOpponentName()+"  "+formatter.format(date) + ": \n" + message[3] + "\n", 3);
                        }
                        else if (message[0].equals("3")) {
                            //收到有关网络连线的信息
                            switch (message[3]) {
                                case "#":
                                    int n = JOptionPane.showConfirmDialog(null, "接受" + message[1] + "连线?", "连线请求", JOptionPane.YES_NO_OPTION);
                                    oppentID = message[1];
                                    controlOp.setInfo(1, oppentID, ConnPool.getName(oppentID));
                                    if (n == JOptionPane.YES_OPTION) {
                                        //在数据库中更改现在的状态是连线状态
                                        ConnPool.setBusy(userID, 1);
                                        controlOp.menu.messagePanel.addMsg("开始和id为 "+oppentID+" 的用户连线！\n", 1);
                                        //开启连线标志，接受连线
                                        controlOp.menu.connect.begin();
                                        //更改数据库，更改当前状态为连线状态
                                        ConnPool.setBusy(userID, 1);
                                        //发送接受连线的信息
                                        sendMsg("0", 3);
                                    } else if (n == JOptionPane.NO_OPTION) {
                                        //发送不接受连线邀请的信息
                                        sendMsg("1", 3);
                                    }
                                    break;
                                case "0":
                                    //收到对方同意连线的指令
                                    controlOp.menu.messagePanel.addMsg("开始和id为 "+oppentID+" 的用户连线！\n", 1);
                                    //对方接受连线，设置自己的颜色是黑色
                                    controlOp.menu.connect.begin();
                                    //设置当前连线状态
                                    ConnPool.setBusy(userID, 1);
                                    break;
                                case "1":
                                    //收到对方不同意连线的指令
                                    JOptionPane.showMessageDialog(null, "对方不同意");
                                    break;
                                default:
                                    System.out.println(userID + "连线内收到无归属信息");
                            }
                        } else if (message[0].equals("4")) {
                            //对方结束连线
                            controlOp.menu.connect.getDisconnect();
                            ConnPool.setBusy(userID, 0);
                            controlOp.menu.messagePanel.addMsg("连线结束！\n", 1);
                        }
                        else {
                            System.out.println(userID + "收到无归属信息");
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }
        };
        /**
         * 开一个线程接受消息
         */
        new Thread(runnable).start();
    }

    /**
     * 发送一条信息
     *
     * @param msg  待发送信息
     * @param type 消息类型
     */
    public void sendMsg(String msg, int type) {
        String message = type + ":" + userID + ":" + oppentID + ":" + msg;
        pw.println(message);    //发送给另一个客户端，需要服务端转发
        pw.flush();
    }

    /**
     * 设置对方ID
     *
     * @param oppentID 对方ID
     */
    public void setOppentID(String oppentID) {
        this.oppentID = oppentID;
    }
}