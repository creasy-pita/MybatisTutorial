package com.creasypita.learning.main;

import com.creasypita.learning.mappers.StudentMapper;
import com.creasypita.learning.model.Student;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.Reader;
import java.util.List;

/**
 * Created by creasypita on 9/17/2019.
 *
 * @ProjectName: MybatisTutorial
 */
public class mybatisselect {
    public static void main(String[] args) throws IOException {
        Reader reader = Resources.getResourceAsReader("SqlMapConfig.xml");
        SqlSessionFactory sessionFactory = new SqlSessionFactoryBuilder().build(reader);
        SqlSession session = sessionFactory.openSession();
        StudentMapper studentMapper = session.getMapper(StudentMapper.class);

        List<Student> list = studentMapper.GetAll();
        for (Student s : list) {
            System.out.println("--------------------------------");
            System.out.println("id" + s.getId());
            System.out.println("name" + s.getName());
            System.out.println("getBranch" + s.getBranch());
            System.out.println("getEmail" + s.getEmail());
            System.out.println("getPercentage" + s.getPercentage());
            System.out.println("getPhone" + s.getPhone());
            System.out.println("-------------------------------");
        }
    }
}
