package Model;

import Controller.ControlOp;

import javax.swing.*;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MessagePanel extends JPanel implements KeyListener {
    private ControlOp controlOp;
    private JScrollPane consoleScroll, messageScroll;
    public JLabel idLabel,photoLabel,toolsLabel;
    private MyTextPane console;     //显示消息的控制台
    private JTextArea message;     //待发送的信息
    public Connect connect;   // 连接器
    private ImageIcon bgImg = new ImageIcon("src/images/Menu/blue.png");
    private ImageIcon txImg = new ImageIcon("src/images/login/touxiang.png");
    private ImageIcon toolsImg = new ImageIcon("src/images/Menu/QQ.png");

    /**
     * 初始化
     *
     * @param controlOp  本线程的主控
     * @param connect 本线程的连接器
     */
    public MessagePanel(ControlOp controlOp, Connect connect) {
        this.setLayout(null);
        this.controlOp = controlOp;
        this.connect = connect;

        txImg.setImage(txImg.getImage().getScaledInstance(120,120,Image.SCALE_DEFAULT));
        photoLabel = new JLabel(txImg);
        photoLabel.setBounds(5, 5, 105, 105);

        toolsImg.setImage(toolsImg.getImage().getScaledInstance(800,80,Image.SCALE_DEFAULT));
        toolsLabel = new JLabel(toolsImg);
        toolsLabel.setBounds(1, 370, 798, 80);

        console = new MyTextPane();
        consoleScroll = new JScrollPane(console);
        message = new JTextArea();
        messageScroll = new JScrollPane(message);

        idLabel = new JLabel();
        idLabel.setFont(new Font("楷体", Font.BOLD, 16));
        idLabel.setBounds(115,40,200,35);

        message.setFont(new Font("楷体", Font.BOLD, 14));
        message.setFocusable(true);
        message.setLineWrap(true);
        message.setWrapStyleWord(true);
        messageScroll.setBounds(0, 450, 800, 87);

        console.setEditable(false);
        console.setFont(new Font("楷体", Font.BOLD, 16));
        console.setFocusable(false);
        console.setOpaque(false);
        consoleScroll.setBounds(0, 120, 800, 250);
        consoleScroll.setOpaque(false);

        this.add(toolsLabel);
        this.add(photoLabel);
        this.add(idLabel);
        this.add(messageScroll);
        this.add(consoleScroll);

        message.addKeyListener(this);
    }

    /**
     * 重写键盘响应
     *
     * @param e 按键
     */
    @Override
    public void keyTyped(KeyEvent e) {
        if (e.getKeyChar() == KeyEvent.VK_ENTER) {
            //需要发送的消息
            String sendMsg = message.getText().trim();
            //消息不会空
            if (sendMsg.length() > 0) {
                System.out.println(connect.connectOver);
                if (connect != null && connect.isNetwork && !connect.connectOver) {
                    controlOp.sendMsg(sendMsg, 1);
                    Date date = new Date(); // this object contains the current date value
                    SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                    //向服务端发送消息
                    addMsg(controlOp.getUserName() +"  "+formatter.format(date)+ ": \n" + sendMsg + "\n", 2);
                    message.setText("");
                } else {
                    //不处于连接状态
                    String msg = message.getText();
                    message.setText(msg.substring(0, msg.length() - 1));
                    JOptionPane.showMessageDialog(null, "不是网络连接无法发送信息");
                }
            } else {
                //空消息
                message.setText("");
                JOptionPane.showMessageDialog(null, "不能发送空信息");
            }
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(bgImg.getImage(), 0, 0, getSize().width, getSize().height, this);// 图片会自动缩放
    }

    public void setidLabel(String str) {
        idLabel.setText(String.valueOf("用户（"+str+"）"));
    }

    /**
     * 在聊天窗口添加信息
     *
     * @param msg 信息
     */
    public void addMsg(String msg, int type) {
        if (type == 1) {
            console.setText(msg, new Font("黑体", Font.ITALIC, 16), Color.WHITE, Color.BLACK);
        }else if (type == 2) {
            console.setText(msg, new Font("楷体", Font.BOLD, 16), Color.WHITE, Color.BLUE);
        }else {
            console.setText(msg, new Font("楷体", Font.BOLD, 16), Color.WHITE, Color.GREEN);
        }
    }
}


class MyTextPane extends JTextPane {
    /**
     * 聊天室字体设置
     *
     * @param msg       带插入的文本
     * @param font      字体
     * @param backgroud 背景色
     * @param foregroud 前景色
     */
    public void setText(String msg, Font font, Color backgroud, Color foregroud) {
        try {
            SimpleAttributeSet set = new SimpleAttributeSet();
            StyleConstants.setBackground(set, backgroud);
            StyleConstants.setForeground(set, foregroud);
            StyleConstants.setFontSize(set, font.getSize());
            StyleConstants.setFontFamily(set, font.getFontName());
            if (font.isBold()) {
                StyleConstants.setBold(set, true);
            }
            if (font.isItalic()) {
                StyleConstants.setItalic(set, true);
            }
            this.getDocument().insertString(this.getDocument().getLength(), msg, set);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
