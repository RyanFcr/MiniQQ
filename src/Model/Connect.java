package Model;

import javax.swing.*;

public class Connect extends JPanel {
    public boolean connectOver = true;         // 默认结束连接
    public boolean isNetwork = false;     //网络正在连接

    /**
     * 对决开始
     */
    public void begin() {
        isNetwork = true;
        connectOver = false;
    }
    /**
     * 处理对方终止链接的情况
     */
    public void getDisconnect() {
        JOptionPane.showMessageDialog(null, "对方终止链接");
        connectOver = true;
        isNetwork = false;
    }
}
