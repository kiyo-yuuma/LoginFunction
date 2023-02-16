package com.example.prmnlast.dao;

import com.example.prmnlast.model.UserBean;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LoginDAO {

    private final String DATABASE = ""; // データベースの名前を入れる
    private final String URL = "jdbc:mysql://localhost:3306/" + DATABASE;
    private final String USERNAME = ""; // MySQLのユーザネームを入れる
    private final String PASSWORD = ""; // MySQLのパスワードを入れる
    private final String TABLE = ""; // テーブルの名前を入れる

    /*
    Table
        id       : varchar(30), not null, primary key
        password : varchar(64), not null
        userName : varchar(30), not null
     */


    // すべてのユーザをリストで取得
    public List<UserBean> findAllUser() {
        List<UserBean> returnUserList = new ArrayList<>();
        try {
            Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            String sql = select();
            PreparedStatement ps = conn.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                UserBean user = new UserBean();
                user.setId(rs.getString("id"));
                user.setPassword(rs.getString("password"));
                user.setUserName(rs.getString("userName"));
                returnUserList.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return returnUserList;
    }

    // 特定のユーザを検索
    public UserBean findUser(UserBean userBean) {
        UserBean returnUserBean = new UserBean();
        try {
            Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            String sql = select()  + " WHERE id = ? AND password = ?";
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setString(1, userBean.getId());
            ps.setString(2, userBean.getPassword());

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                returnUserBean.setId(rs.getString("id"));
                returnUserBean.setPassword(rs.getString("password"));
                returnUserBean.setUserName(rs.getString("userName"));
            } else {
                return UserBean.empty;
            }
        } catch (SQLException e) {
            return UserBean.empty;
        }
        return returnUserBean;
    }

    // ユーザの新規登録
    public void registerUser(UserBean userBean) {
        try {
            Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            String sql = insert();
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setString(1, userBean.getId());
            ps.setString(2, userBean.getPassword());
            ps.setString(3, userBean.getUserName());

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // ユーザの情報更新
    public void updateUser(UserBean user, UserBean tmpUser) {
        try {
            Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            String sql = update();
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setString(1, tmpUser.getId());
            ps.setString(2, tmpUser.getPassword());
            ps.setString(3, tmpUser.getUserName());
            ps.setString(4, user.getId());
            ps.setString(5, user.getPassword());

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // ユーザの情報削除
    public void userDelete(UserBean user) {
        try {
            Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            String sql = delete();
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setString(1, user.getId());
            ps.setString(2, user.getPassword());

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private String select() {
        return "SELECT * FROM " + TABLE;
    }

    private String insert() {
        return "INSERT INTO " + TABLE + " VALUES (?, ?, ?)";
    }

    private String update() {
        return "UPDATE " + TABLE + " SET id = ?, password = ?, userName = ? WHERE id = ? AND password = ?";
    }

    private String delete() {
        return "DELETE FROM " + TABLE + " WHERE id = ? AND password = ?";
    }

}
