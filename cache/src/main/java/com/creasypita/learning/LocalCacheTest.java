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
public class LocalCacheTest
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
        SqlSession firstSqlSession = sqlSessionFactory.openSession();
        UserMapper firstMapper = firstSqlSession.getMapper(UserMapper.class);
        User firstSelectWithCache = firstMapper.selectByIdWithLocalCache(1);
        System.out.println("first session first query with cache:" + firstSelectWithCache.getName());
        User secondSelectWithCache = firstMapper.selectByIdWithLocalCache(1);
        System.out.println("first session second query with cache:" + secondSelectWithCache.getName());
        firstSqlSession.commit();

        //二级缓存在一个会话中有效，新会话不能使用前面会话的缓存
        SqlSession secondSqlSession = sqlSessionFactory.openSession();
        UserMapper secondMapper = secondSqlSession.getMapper(UserMapper.class);
        firstSelectWithCache = secondMapper.selectByIdWithLocalCache(1);
        System.out.println("second Session first query with cache:" + firstSelectWithCache.getName());
        secondSelectWithCache = secondMapper.selectByIdWithLocalCache(1);
        System.out.println("second Session second query with cache:" + secondSelectWithCache.getName());

//        User thirdSelectWithCache = firstMapper.selectByIdWithCache(1);
//        System.out.println("first session third query with cache:" + thirdSelectWithCache.getName());



        secondSqlSession.commit();
        System.out.println("success");
    }
}
