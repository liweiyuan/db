package com.mysql.mybatis.service.impl;

import com.mysql.mybatis.dao.User;
import com.mysql.mybatis.dao.UserDao;
import com.mysql.mybatis.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by tingyun on 2017/4/6.
 */
@Service("userService")
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    public User getUserById(int userId) {
        return this.userDao.selectByPrimaryKey(userId);
    }
}
