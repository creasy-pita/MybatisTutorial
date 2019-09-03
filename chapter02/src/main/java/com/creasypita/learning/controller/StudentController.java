package com.creasypita.learning.controller;

import com.creasypita.learning.model.Student;
import com.creasypita.learning.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * Created by creasypita on 9/3/2019.
 *
 * @ProjectName: MybatisTutorial
 */
@Controller
public class StudentController {

    @Autowired
    private StudentService studentService;

    @RequestMapping(value = "studentlist",method = {RequestMethod.GET})
    public String StudentList(Model model) {
        List<Student> list = studentService.GetAll();
        model.addAttribute("students", list);
        return "";
    }

}
