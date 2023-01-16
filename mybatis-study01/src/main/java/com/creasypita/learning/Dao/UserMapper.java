package com.creasypita.learning.Dao;

import com.creasypita.learning.model.User;

import java.util.List;

/**
 * Created by lujq on 10/17/2021.
 */
public interface UserMapper {
    User findUserById(int id);

    List<User> findUsers();

    int addUser(User user);

}
