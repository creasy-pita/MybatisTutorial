package com.creasypita.learning.Dao;

import com.creasypita.learning.model.Student;
import com.creasypita.learning.model.User;

import java.util.List;

/**
 * Created by lujq on 10/17/2021.
 */
public interface StudentMapper {
    //获取所有学生，及其关联的老师
    public List<Student> findStudents();

    public List<Student> findStudents2();

}
