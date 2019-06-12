package cn.shrmus.spring.service;

import cn.shrmus.spring.dao.UserDao;

public class UserService {
    private UserDao userDao;

    public UserService() {

    }

    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    public void getUserInfo(){
        System.out.println("UserService：getUserInfo()");
        this.userDao.getUserInfo();
    }

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }
}
