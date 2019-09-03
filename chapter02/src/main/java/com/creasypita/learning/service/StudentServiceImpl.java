package com.creasypita.learning.service;

import com.creasypita.learning.mappers.StudentMapper;
import com.creasypita.learning.model.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by creasypita on 9/3/2019.
 *
 * @ProjectName: MybatisTutorial
 */
@Service("studentService")
public class StudentServiceImpl implements StudentService {

    @Autowired
    private StudentMapper studentMapper;

    public List<Student> GetAll() {
        return studentMapper.GetAll();
    }
}
