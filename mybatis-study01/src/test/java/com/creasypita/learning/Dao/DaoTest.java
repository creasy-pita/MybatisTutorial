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
        System.out.println(user.getId());
        System.out.println(user.getName());
        System.out.println(user.getPwd());
        System.out.println(user.getAge());
    }

    @Test
    public void addUser(){
        SqlSession sqlSession = MybatisUtil.openSession();
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        User user = new User();
        user.setId(3);
        user.setName("canada");
        user.setPwd("123");
        user.setAge(12);
        mapper.addUser(user);
        sqlSession.commit();
        sqlSession.close();
    }

}
