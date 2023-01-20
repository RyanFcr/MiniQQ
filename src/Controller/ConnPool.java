package Controller;

import java.sql.*;

/**
 * 数据库操作
 */
public class ConnPool {

    static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    //改为你自己的jdbc链接
    static final String DB_URL = "jdbc:mysql://localhost:3306/chat?useSSL=false&serverTimezone=UTC";
    static final String USER = "root";
    //改为你自己的密码
    static final String PASSWORD = "fcr20140333";

    /**
     * 连接本地mysql数据库
     */
    public ConnPool() {
    }

    /**
     * @param userName 用户名
     * @param password 用户密码
     * @return 登陆成功返回用户ID，登陆失败返回null
     * @throws SQLException
     */
    public static String login(String userName, String password) throws SQLException {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            Class.forName(JDBC_DRIVER);
            conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);
            stmt = conn.createStatement();
            String sql = "SELECT * from user where userName = '" + userName + "' and userPwd = '" + password + "'";
            System.out.println("SQL语句成功：" + sql);
            rs = stmt.executeQuery(sql);
            if (rs.next()) {
                String userID = rs.getString("userID");
                sql = "UPDATE user set state = 1 where userID = '" + userID + "'";
                stmt.execute(sql);
                return userID;
            } else {
                return null;
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null)
                    rs.close();
                if (stmt != null)
                    stmt.close();
                if (conn != null)
                    conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 确认用户名是否存在
     *
     * @param userName 用户名
     * @return 用户名存在返回true，否则返回false
     */
    public static boolean userExist(String userName) {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            Class.forName(JDBC_DRIVER);
            conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);
            stmt = conn.createStatement();
            String sql = "SELECT * from user where userName = '" + userName + "'";
            rs = stmt.executeQuery(sql);
            if (rs.next())
                return true;
            else
                return false;
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null)
                    rs.close();
                if (stmt != null)
                    stmt.close();
                if (conn != null)
                    conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * 用户注册
     *
     * @param userName 用户名
     * @param password 密码
     * @return 注册成功，返回true；注册失败，返回false
     */
    public static boolean regist(String userName, String password) {
        Connection conn = null;
        Statement stmt = null;
        try {
            Class.forName(JDBC_DRIVER);
            conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);
            stmt = conn.createStatement();
            String sql = "insert into user(userName, userPwd) values ('" + userName + "', '" + password + "')";
            System.out.println("SQL语句成功：" + sql);
            stmt.execute(sql);
            return true;
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                if (stmt != null)
                    stmt.close();
                if (conn != null)
                    conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * 用户注销
     *
     * @param userID 用户ID
     */
    public static void loginOut(String userID) {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            Class.forName(JDBC_DRIVER);
            conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);
            stmt = conn.createStatement();
            String sql = "UPDATE user set state = 0 where userID = '" + userID + "'";
            System.out.println("SQL语句成功：" + sql);
            stmt.execute(sql);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null)
                    rs.close();
                if (stmt != null)
                    stmt.close();
                if (conn != null)
                    conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 更改用户聊天状态
     *
     * @param userID 用户ID
     * @param busy  {1:连线中；0:空闲}
     */
    public static void setBusy(String userID, int busy) {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            Class.forName(JDBC_DRIVER);
            conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);
            stmt = conn.createStatement();
            String sql = "UPDATE user set busy = " + busy + " where userID = '" + userID + "'";
            System.out.println("SQL语句成功：" + sql);
            stmt.execute(sql);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null)
                    rs.close();
                if (stmt != null)
                    stmt.close();
                if (conn != null)
                    conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 查找对手
     *
     * @param userID 用户ID
     * @return 返回对手ID和姓名
     */
    public static String findOpponent(String userID) {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            Class.forName(JDBC_DRIVER);
            conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);
            stmt = conn.createStatement();
            String sql = "SELECT userID, userName from user where state = 1 and busy = 0 and userID != '" + userID + "'";
            System.out.println("SQL语句成功：" + sql);
            rs = stmt.executeQuery(sql);
            if (rs.next()) {
                return rs.getString("userID") + ":" + rs.getString("userName");
            }
            return null;
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null)
                    rs.close();
                if (stmt != null)
                    stmt.close();
                if (conn != null)
                    conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 获得用户名
     *
     * @param userID 用户ID
     * @return 用户昵称
     */
    public static String getName(String userID) {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            Class.forName(JDBC_DRIVER);
            conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);
            stmt = conn.createStatement();
            String sql = "SELECT userName from user where userID = '" + userID + "'";
            System.out.println("SQL语句成功：" + sql);
            rs = stmt.executeQuery(sql);
            if (rs.next()) {
                return rs.getString("userName");
            }
            return null;
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null)
                    rs.close();
                if (stmt != null)
                    stmt.close();
                if (conn != null)
                    conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
