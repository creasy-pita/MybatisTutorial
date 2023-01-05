package com.creasypita.learning.Dao;

import com.creasypita.learning.model.User;

/**
 * Created by lujq on 10/17/2021.
 */
public interface UserMapper {
    User findUserById(int id);

    User selectByIdWithLocalCache(int id);

    User selectByIdWithSecondLevelCache(int id);

    User selectByIdWithoutCache(int id);

    int addUser(User user);

}
