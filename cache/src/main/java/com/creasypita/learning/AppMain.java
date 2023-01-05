package com.creasypita.learning;

import com.creasypita.learning.Dao.UserMapper;
import com.creasypita.learning.model.User;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by lujq on 10/17/2021.
 */
public class AppMain
{
    public static void main(String[] args) {
        String resource = "com/creasypita/learning/mybatis-config.xml";
        InputStream inputStream = null;
        try {
            inputStream = Resources.getResourceAsStream(resource);
        } catch (IOException e) {
            e.printStackTrace();
        }
        SqlSessionFactory sqlSessionFactory =
                new SqlSessionFactoryBuilder().build(inputStream);
        SqlSession sqlSession = sqlSessionFactory.openSession();
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        User firstSelectWithCache = mapper.selectByIdWithLocalCache(1);
        System.out.println("first query with cache:" + firstSelectWithCache.getName());
        User secondSelectWithCache = mapper.selectByIdWithLocalCache(1);
        System.out.println("second query with cache:" + secondSelectWithCache.getName());
        User firstSelectWithoutCache = mapper.selectByIdWithoutCache(1);
        System.out.println("first query without cache:" + firstSelectWithoutCache.getName());
        User secondSelectWithoutCache = mapper.selectByIdWithoutCache(1);
        System.out.println("second query without cache:" + secondSelectWithoutCache.getName());
        sqlSession.commit();
        System.out.println("success");
    }
}
