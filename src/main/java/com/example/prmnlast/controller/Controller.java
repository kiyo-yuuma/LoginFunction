package com.example.prmnlast.controller;

import com.example.prmnlast.dao.LoginDAO;
import com.example.prmnlast.model.UserBean;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@org.springframework.stereotype.Controller
@SessionAttributes(value = {"user", "tmpUser"})
public class Controller {

    private LoginDAO loginDao = new LoginDAO();
    private final String adminPass = "zansin";

    // オブジェクトをセッションに追加する
    @ModelAttribute(value = "user")
    public UserBean setUpUser() {
        return new UserBean();
    }

    // オブジェクトをセッションに追加する
    @ModelAttribute(value = "tmpUser")
    public UserBean setUpTmpUser() {
        return new UserBean();
    }

    @RequestMapping("/")
    public String top() {
        return "top";
    }

    @RequestMapping("/main")
    public String main() {
        return "home/main";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute("loginUser") UserBean userBean,
                        @ModelAttribute("user") UserBean user,
                        Model model) {
        try {
            UserBean checkUser = loginDao.findUser(user);
            if (!isEmpty(checkUser)) {
                userSetting(user, checkUser);
                return "home/main";
            }
        } catch (NullPointerException e) {
            model.addAttribute("error", "ログイン失敗");
            return "error";
        }
        model.addAttribute("error", "ログイン失敗");
        return "error";
    }

    private boolean isEmpty(UserBean user) {
        return user == UserBean.empty;
    }

    @PostMapping({"/register", "backToRegister"})
    public String register() {
        return "register/register";
    }

    @PostMapping("/registerCheck")
    public String registerCheck(@ModelAttribute("registerUser") UserBean userBean,
                                @ModelAttribute("user") UserBean user,
                                Model model) {
        try {
            UserBean returnUser = loginDao.findUser(userBean);
            if (!isEmpty(returnUser)) {
                model.addAttribute("error", "すでに存在するユーザです");
                return "error";
            }
        } catch (NullPointerException e) {
            userSetting(user, userBean);
            return "register/check";
        }
        userSetting(user, userBean);
        return "register/check";
    }

    @PostMapping("/registerConfirm")
    public String registerConfirm(@ModelAttribute("user") UserBean user) {
        loginDao.registerUser(user);
        reset(user);
        return "register/completed";
    }

    @PostMapping("/logout")
    public String logout(@ModelAttribute("user") UserBean user) {
        reset(user);
        return "logout";
    }

    @PostMapping("/hidden")
    public String hidden() {
        return "home/hidden/certification";
    }

    @PostMapping("/certify")
    public String certify(@ModelAttribute("pass") String password, Model model) {
        // 管理者パスワード以外が入力された場合はmainに戻す
        if (!isAdmin(password)) return "home/main";

        List<UserBean> userList = loginDao.findAllUser();
        model.addAttribute("userList", userList);
        return "home/hidden/admin";
    }

    private boolean isAdmin(String pass) {
        return Objects.equals(pass, adminPass);
    }

    @PostMapping("/setting")
    public String setting(@ModelAttribute("user") UserBean user) {
        return "home/setting/setting";
    }

    @PostMapping("/backToSetting")
    public String backToSetting() {
        return "home/setting/setting";
    }

    @PostMapping("/settingCheck")
    public String settingCheck(@ModelAttribute("settingUser") UserBean settingUser,
                               @ModelAttribute("tmpUser") UserBean tmpUser) {
        userSetting(tmpUser, settingUser);
        return "home/setting/check";
    }

    @PostMapping("/settingConfirm")
    public String settingConfirm(@ModelAttribute("user") UserBean user,
                                 @ModelAttribute("tmpUser") UserBean tmpUser) {
        loginDao.updateUser(user, tmpUser);
        userSetting(user, tmpUser);
        return "home/setting/completed";
    }

    @PostMapping("/deleteCheck")
    public String deleteCheck() {
        return "home/setting/delete/check";
    }

    @PostMapping("/deleteConfirm")
    public String deleteConfirm(@ModelAttribute("user") UserBean user) {
        loginDao.userDelete(user);
        reset(user);
        return "home/setting/delete/completed";
    }

    // userの情報をリセットする
    private void reset(@ModelAttribute("user") UserBean user) {
        user.setId("");
        user.setPassword("");
        user.setUserName("");
    }

    // userに情報をセットする
    private void userSetting(UserBean user, UserBean tmp) {
        user.setId(tmp.getId());
        user.setPassword(tmp.getPassword());
        user.setUserName(tmp.getUserName());
    }

}
