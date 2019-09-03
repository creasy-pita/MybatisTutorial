package com.creasypita.learning.mappers;

import com.creasypita.learning.model.Student;
import org.apache.ibatis.annotations.Select;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by creasypita on 9/3/2019.
 *
 * @ProjectName: MybatisTutorial
 */
@Component
public interface StudentMapper {
    @Select(value = "SELECT * FROM STUDENT;")
    List<Student> GetAll();
}
