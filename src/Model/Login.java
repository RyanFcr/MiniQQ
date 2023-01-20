package Model;

import javax.swing.*;
import java.awt.*;

public class Login {
    public JFrame loginFrame;   //登陆框架
    public JLabel loginLabel;   //登陆页面
    public JButton loginButton; //登陆按钮
    public JButton registButton;    //注册按钮
    public JTextField adminText;    //用户
    public JPasswordField pwdText;  //密码
    public JLabel photoLabel; //头像

    /**
     * 初始化登陆界面
     */
    public Login() {
        Font font = new Font("黑体", Font.PLAIN, 20);

        registButton = new JButton("注册");
        registButton.setBounds(250, 250, 100, 50);
        registButton.setFont(font);

        loginButton = new JButton("登陆");
        loginButton.setBounds(90, 250, 100, 50);
        loginButton.setFont(font);

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

        loginLabel = new JLabel();

        loginLabel.add(photoLabel);


        loginLabel.add(adminText);
        loginLabel.add(pwdText);

        loginLabel.add(loginButton);
        loginLabel.add(registButton);

        loginFrame = new JFrame("登陆");
        loginFrame.setSize(450, 400);

        loginFrame.add(loginLabel);
//        loginFrame.add(photoLabel);
//        loginFrame.add(loginLabel);

        loginFrame.setVisible(true);
        loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //窗体关闭时退出程序
        loginFrame.setLocationRelativeTo(null);
    }
}
