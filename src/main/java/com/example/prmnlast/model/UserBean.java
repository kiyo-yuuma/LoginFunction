package com.example.prmnlast.model;

import com.example.prmnlast.model.user.Id;
import com.example.prmnlast.model.user.Password;
import com.example.prmnlast.model.user.UserName;

import java.io.Serializable;

public class UserBean implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final UserBean empty = new UserBean(1); // nullを避けるため

    private Id id;
    private Password password;
    private UserName userName;

    public UserBean() {
        super();
    }

    // nullの代用（EmptyUser）
    public UserBean(int i) {
        this.id = new Id("");
        this.password = new Password("");
        this.userName = new UserName("");
    }

    public String getId() {
        return id.getValue();
    }

    public void setId(String id) {
        this.id = new Id(id);
    }

    public String getPassword() {
        return password.getValue();
    }

    public void setPassword(String password) {
        this.password = new Password(password);
    }

    public String getUserName() {
        return userName.getValue();
    }

    public void setUserName(String userName) {
        this.userName = new UserName(userName);
    }
}
