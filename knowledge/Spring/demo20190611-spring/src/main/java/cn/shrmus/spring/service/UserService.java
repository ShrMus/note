package cn.shrmus.spring.service;

import cn.shrmus.spring.dao.UserDao;

public class UserService {
    private UserDao userDao;

    public UserService() {

    }

    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    public void execute(){
        System.out.println("UserService：execute()");
        this.userDao.execute();
    }

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }
}
