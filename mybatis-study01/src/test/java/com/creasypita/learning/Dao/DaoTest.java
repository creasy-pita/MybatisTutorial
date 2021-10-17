package com.creasypita.learning.Dao;

import com.creasypita.learning.Util.MybatisUtil;
import com.creasypita.learning.model.User;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by lujq on 10/17/2021.
 */
public class DaoTest {
    @Test
    public void findUserById(){
        SqlSession sqlSession = MybatisUtil.openSession();
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        User user = mapper.findUserById(1);
        sqlSession.close();
        System.out.println(user);
    }

    @Test
    public void addUser(){
        SqlSession sqlSession = MybatisUtil.openSession();
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        User user = new User();
        user.setId(2);
        user.setName("tokio");
        user.setPwd("123");
        mapper.addUser(user);
        sqlSession.commit();
        sqlSession.close();
    }

}
