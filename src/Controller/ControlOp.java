package Controller;

import Client.Client;
import Model.Menu;
import Model.Login;
import Model.Regist;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class ControlOp {
    private ControlOp controlOp;    //本类
    public Login loginView;         //注册界面
    public Regist registView;       //登陆界面
    public Menu menu;     //聊天界面
    private Client client;          //客户端
    private String userID;          //用户ID
    private String userName;        //用户名
    private String opponentID;      //对方ID
    private String opponentName;    //对方名字

    /**
     * 初始化ID和名字
     *
     * @param i    用户
     * @param ID   ID
     * @param name 昵称
     */
    public void setInfo(int i, String ID, String name) {
        if (i == 0) {
            userID = ID;
            userName = name;
        } else {
            opponentID = ID;
            opponentName = name;
        }
    }

    /**
     * 初始化
     */
    public ControlOp() {
        loginView = new Login();
        registView = new Regist();
        menu = new Menu(this);
        controlOp = this;
        login();
    }

    /**
     * 登陆、注册
     */
    public void login() {
        /**
         * 点击登陆事件
         */
        ActionListener loginButtonLs = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String admin = loginView.adminText.getText();
                String password = loginView.pwdText.getText();
                try {
                    System.out.println("登陆中...\n" +
                            "用户名: " + admin + " 密码: " + password);
                    String userID = ConnPool.login(admin, password);

                    if (userID == null) {   //登陆失败
                        System.out.println("登陆失败");
                        JOptionPane.showMessageDialog(null, "密码错误或者用户不存在");
                    } else {    //登陆成功
                        loginView.loginFrame.setVisible(false);
                        System.out.println("ID为" + userID + "的用户登陆成功");
                        setInfo(0, userID, admin);
                        menu.Menu.setVisible(true);
                        controlOp.menu.messagePanel.addMsg("欢迎登陆！\n"+userName , 1);
                        controlOp.menu.messagePanel.setidLabel(userName);
                        client = new Client(userID, controlOp);
                        System.out.println(userID + " 客户端开启");
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        };

        /**
         * 点击注册事件
         */
        ActionListener registButtion = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loginView.loginFrame.setVisible(false);
                registView.registFrame.setVisible(true);
            }
        };
        loginView.loginButton.addActionListener(loginButtonLs);
        loginView.registButton.addActionListener(registButtion);

        /**
         * 点击注册事件
         */
        ActionListener registButtonLs = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String admin = registView.getAdmin();
                String password = registView.getPassword();
                System.out.println("注册中...\n ");
                if (ConnPool.userExist(admin)) {
                    System.out.println(
                            "用户名已存在");
                    JOptionPane.showMessageDialog(null, "用户名已存在");
                } else {
                    if (ConnPool.regist(admin, password)) {
                        System.out.println("注册成功");
                        JOptionPane.showMessageDialog(null, "注册成功");
                        registView.registFrame.setVisible(false);
                        loginView.loginFrame.setVisible(true);
                    } else {
                        System.out.println("注册失败");
                        JOptionPane.showMessageDialog(null, "注册失败");
                    }
                }
            }
        };
        /**
         * 点击返回事件
         */
        ActionListener returnButtonLs = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                registView.registFrame.setVisible(false);
                loginView.loginFrame.setVisible(true);
            }
        };
        registView.registButton.addActionListener(registButtonLs);
        registView.returnButton.addActionListener(returnButtonLs);
    }

    /**
     * 退出登陆
     */
    public void loginOut() {
        //未结束连接
        if (menu.connect.isNetwork && !menu.connect.connectOver) {
            JOptionPane.showMessageDialog(null, "请先结束连接！");
            return;
        }
        //用户注销
        ConnPool.loginOut(userID);
        System.out.println("注销...ID为" + userID + "的用户注销");
        //聊天室不可见
        menu.Menu.setVisible(false);
        //登陆画面可见
        loginView.loginFrame.setVisible(true);
    }

    /**
     * 请求双人连线
     *
     * @throws SQLException 数据库异常
     */
    public void beginPlay() throws SQLException {
        String i = ConnPool.findOpponent(userID);
        //查找不到对象
        if (i == null) {
            JOptionPane.showMessageDialog(null, "无匹配对象");
            return;
        }
        //根据查找到对手，根据反馈信息解析对手ID和昵称
        String[] rs = i.split(":");
        opponentID = rs[0];
        opponentName = rs[1];
        System.out.println("请求连线...\n" +
                "找到的可以连线的ID为" + opponentID);
        client.setOppentID(opponentID);
        //发送类别为3内容为#的信息
        client.sendMsg("#", 3);
    }

    public void sendMsg(String msg, int type) {
        client.sendMsg(msg, type);
    }


    public String getUserName() {
        return userName;
    }
    public String getOpponentName() {
        return opponentName;
    }
    public String getUserID() {
        return userID;
    }
}
