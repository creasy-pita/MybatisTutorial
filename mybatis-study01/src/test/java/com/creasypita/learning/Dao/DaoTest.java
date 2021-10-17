package com.creasypita.learning.Dao;

import com.creasypita.learning.Util.MybatisUtil;
import com.creasypita.learning.model.Student;
import com.creasypita.learning.model.User;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Created by lujq on 10/17/2021.
 */
public class DaoTest {

    @Test
    public void findStudents(){
        SqlSession sqlSession = MybatisUtil.openSession();
        StudentMapper mapper = sqlSession.getMapper(StudentMapper.class);
        List<Student> students = mapper.findStudents();
        for (Student student : students) {
            System.out.println(student);
        }
        sqlSession.close();
    }

    @Test
    public void findStudents2(){
        SqlSession sqlSession = MybatisUtil.openSession();
        StudentMapper mapper = sqlSession.getMapper(StudentMapper.class);
        List<Student> students = mapper.findStudents2();
        for (Student student : students) {
            System.out.println(student);
        }
        sqlSession.close();
    }

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
