package com.creasypita.learning.mappers;

import com.creasypita.learning.model.Student;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by creasypita on 9/3/2019.
 *
 * @ProjectName: MybatisTutorial
 */
@Component
public interface StudentMapper {
//    @Select(value = "SELECT * FROM STUDENT;")
    List<Student> GetAll();

//    @Select(value = "select * from student where id=#{id}")
    Student getById(int id);

//    @Insert("INSERT INTO STUDENT (name, branch, percentage, phone, email ) values (#{name}, #{branch}, #{percentage}, #{phone}, #{email})")
    void insertStudent(Student student);

//    @Update("UPDATE STUDENT SET name=#{name}, branch=#{branch}, percentage=#{percentage}, phone=#{phone}, email=#{email} where id=#{id}")
    void updateStudent(Student student);

//    @Delete("delete from student where id=#{id}")
    void deleteStudent(int id);
}
