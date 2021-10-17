package com.creasypita.learning.main;

import com.creasypita.learning.mappers.StudentMapper;
import com.creasypita.learning.model.Student;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.Reader;

/**
 * Created by creasypita on 9/17/2019.
 *
 * @ProjectName: MybatisTutorial
 */
public class Main {
    public static void main(String[] args) throws IOException {
        Reader reader = Resources.getResourceAsReader("SqlMapConfig.xml") ;
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);
        SqlSession session = sqlSessionFactory.openSession();
        //Create a new student object
        Student student = new Student("111222Mohammad","It", 10, 984803322, "Mohammad@gmail.com" );

        StudentMapper studentMapper = session.getMapper(StudentMapper.class);
        studentMapper.insertStudent(student);

        System.out.println("record inserted successfully");

        session.commit();
        session.close();
    }
}
