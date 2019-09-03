package com.creasypita.learning.service;

import com.creasypita.learning.model.Student;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by creasypita on 9/3/2019.
 *
 * @ProjectName: MybatisTutorial
 */

public interface StudentService {

    List<Student> GetAll();
}
