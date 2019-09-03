package com.creasypita.learning.mybatis;

import com.creasypita.learning.mybatis.domain.Student;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.Reader;

/**
 * Created by creasypita on 9/3/2019.
 *
 * @ProjectName: MybatisTutorial
 */
public class mybatisUpdate {
    public static void main(String[] args) throws IOException {
        Reader reader = Resources.getResourceAsReader("SqlMapConfig.xml");
        SqlSessionFactory sessionFactory = new SqlSessionFactoryBuilder().build(reader);
        SqlSession session = sessionFactory.openSession();
        Student s = session.selectOne("Student.selectById",1);
        s.setName("new" + s.getName());
        int resultRow = session.update("Student.update", s);
        if (resultRow > 0) {
            System.out.println("record update successfully");
        }
        session.commit();
        session.close();
    }
}
