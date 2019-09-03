package com.creasypita.learning.mappers;

import com.creasypita.learning.model.Student;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Created by creasypita on 9/3/2019.
 *
 * @ProjectName: MybatisTutorial
 */
public interface StudentMapper {
    @Select(value = "SELECT * FROM STUDENT;")
    List<Student> GetAll();
}
