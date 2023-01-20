package Model;

import Controller.ConnPool;
import Controller.ControlOp;
import Client.Main;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class Menu {
    private static final int width = 800;
    private static final int height = 580;
    private ControlOp controlOp;
    public JFrame Menu;
    public Connect connect;
    public MessagePanel messagePanel;
    private JMenuBar bar;
    private JMenu Double, Other;
    private JMenuItem  begin, fail, exit, login;
    private JFrame frame = null;

    public Menu(ControlOp c) {
        controlOp = c;
        Menu = new JFrame("QQ聊天室");
        Menu.setLayout(null);

        connect = new Connect();
        connect.setBounds(0, 0, 0, 0);

        messagePanel = new MessagePanel(c, connect);
        messagePanel.setBounds(0, 0, width, height);
        bar = new JMenuBar();

        Double = new JMenu("双人连线");
        begin = new JMenuItem("开始连线");
        fail = new JMenuItem("结束");
        Double.add(begin);
        Double.add(fail);

        bar.add(Double);

        Other = new JMenu("其他");
        login = new JMenuItem("登陆");
        exit = new JMenuItem("退出");
        Other.add(login);
        Other.add(exit);

        bar.add(Other);

        Menu.add(messagePanel);
        Menu.add(connect);
        Menu.setJMenuBar(bar);
        Menu.setSize(width, height);
        Menu.setResizable(false);
        Menu.setVisible(false);
        Menu.setLocationRelativeTo(null);
        addEvent();
    }

    /**
     * 给按钮添加事件
     */
    private void addEvent() {
        /**
         * 开始连线
         */
        ActionListener beginEvent = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    controlOp.beginPlay();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        };
        /**
         * 结束连线
         */
        ActionListener failEvent = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (connect.isNetwork && !connect.connectOver) {
                    if (JOptionPane.showConfirmDialog(frame, "确认结束连线？", "系统提示", JOptionPane.YES_NO_OPTION) == 0) {
                        controlOp.sendMsg("#", 4);
                        controlOp.menu.connect.isNetwork = false;
                        controlOp.menu.connect.connectOver = true;
                        controlOp.menu.messagePanel.addMsg("连线结束！\n", 1);
                        ConnPool.setBusy(controlOp.getUserID(), 0);
                    }
                }
            }
        };
        /**
         * 再次登陆一个用户
         */
        ActionListener loginEvent = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Main.myThread.createThread();
            }
        };
        /**
         * 退出
         */
        ActionListener exitEvent = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controlOp.loginOut();
            }
        };

        login.addActionListener(loginEvent);
        exit.addActionListener(exitEvent);
        begin.addActionListener(beginEvent);
        fail.addActionListener(failEvent);
    }
}
