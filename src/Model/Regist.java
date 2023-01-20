package Model;

import javax.swing.*;
import java.awt.*;

public class Regist {

    public JFrame registFrame;      //注册框架
    public JLabel registLabel;      //注册页面
    public JButton registButton;    //注册按钮
    public JButton returnButton;    //返回按钮
    public JTextField adminText;    //用户
    public JPasswordField pwdText;  //密码
    public JLabel photoLabel; //头像
    /**
     * 初始化注册界面
     */
    public Regist() {
        Font font = new Font("黑体", Font.PLAIN, 20);


        registButton = new JButton("注册");
        registButton.setBounds(250, 250, 100, 50);
        registButton.setFont(font);

        returnButton = new JButton("返回");
        returnButton.setBounds(90, 250, 100, 50);
        returnButton.setFont(font);

        adminText = new JTextField();
        adminText.setBounds(150, 50, 250, 50);
        adminText.setFont(font);

        pwdText = new JPasswordField();
        pwdText.setBounds(150, 120, 250, 50);
        pwdText.setFont(font);

        ImageIcon img = new ImageIcon("src/images/login/touxiang.png");
        img.setImage(img.getImage().getScaledInstance(120,120,Image.SCALE_DEFAULT));
        photoLabel = new JLabel(img);
//        photoLabel.setIcon(img);
        photoLabel.setBounds(20, 50, 120, 120);

        registLabel = new JLabel();

        registLabel.add(adminText);
        registLabel.add(pwdText);

        registLabel.add(photoLabel);

        registLabel.add(registButton);

        registFrame = new JFrame("注册");
        registFrame.setSize(450, 400);

        registFrame.add(returnButton);
        registFrame.add(registLabel);

        registFrame.setVisible(false);
        registFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //窗体关闭时退出程序
        registFrame.setLocationRelativeTo(null);

    }

    /**
     * 获得用户名
     *
     * @return 用户名
     */
    public String getAdmin() {
        return adminText.getText();
    }

    /**
     * 获得密码文本
     *
     * @return 密码
     */
    public String getPassword() {
        return pwdText.getText();
    }
}
