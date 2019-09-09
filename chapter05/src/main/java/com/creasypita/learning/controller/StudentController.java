package com.creasypita.learning.controller;

import com.creasypita.learning.model.Student;
import com.creasypita.learning.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
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

    @RequestMapping(value = "/student",method = {RequestMethod.GET})
    public String StudentList(Model model) {
        List<Student> list = studentService.GetAll();
        model.addAttribute("students", list);
        return "student";
    }

    @RequestMapping(value="/student_view", method = RequestMethod.GET)
    public String studentView(int id, Model model) {
        Student student = studentService.getById(id);
        model.addAttribute("student", student);
        return "student_view";
    }


    @RequestMapping(value="/student_insert", method = RequestMethod.GET)
    public String studentviewinsert(Model model) {
        model.addAttribute("student", new Student());
        return "student_insert";
    }

    @RequestMapping(value="/student_insert", method = RequestMethod.POST)
    public String studentsaveinsert(@ModelAttribute Student student) {
        //Student student = (Student)model.asMap().get("student");
        studentService.insertStudent(student);
        return "redirect:student";
    }

    @RequestMapping(value="/student_update", method = RequestMethod.GET)
    public String studentviewupdate(int id,Model model) {
        Student student = studentService.getById(id);
        model.addAttribute("student", student);
        return "student_update";
    }

    @RequestMapping(value="/student_update", method = RequestMethod.POST)
    public String studentsaveupdate(@ModelAttribute Student student) {
        studentService.updateStudent(student);
        return "redirect:student";
    }
    @RequestMapping(value="/student_delete",method = RequestMethod.GET)
    public String studentdelete(int id) {
        studentService.deleteStudent(id);
        return "redirect:student";
    }

    @RequestMapping(value="/insertStudents", method = RequestMethod.GET)
    public String insertStudents() {
        Student s1 = new Student("name1", "1", 11, 11, "email1");
        Student s2 = new Student("name2", "1", 11, 11, "email2");
        List<Student> students = new ArrayList<Student>(2);
        students.add(s1);
        students.add(s2);
        studentService.insertStudents(students);
        return "redirect:student";
    }

    @RequestMapping(value="/insertStudentRequired", method = RequestMethod.GET)
    public String insertStudentRequired() {
        Student s1 = new Student("name1", "1", 11, 11, "email1");
        studentService.insertStudentRequired(s1);
        return "redirect:student";
    }
    @RequestMapping(value="/insertStudentRequiredNew", method = RequestMethod.GET)
    public String insertStudentRequiredNew() {
        Student s1 = new Student("name1", "2", 11, 11, "email1");
        studentService.insertStudentRequiredNew(s1);
        return "redirect:student";
    }
    @RequestMapping(value="/modifyTranactional",method = RequestMethod.GET)
    public String modifyTranactional() {
        Student s1 = studentService.getById(1);
        Student s2 = studentService.getById(2);
        s1.setPercentage(s1.getPercentage()+1);
        s2.setPercentage(s2.getPercentage()+1);
        studentService.modifyTranactional(s1,s2);
        return "redirect:student";
    }

    @RequestMapping(value="/modifyNoTranactional",method = RequestMethod.GET)
    public String modifyNoTranactional() {
        Student s1 = studentService.getById(1);
        Student s2 = studentService.getById(2);
        s1.setPercentage(s1.getPercentage()+1);
        s2.setPercentage(s2.getPercentage()+1);
        studentService.modifyNoTranactional(s1,s2);
        return "redirect:student";
    }




}
