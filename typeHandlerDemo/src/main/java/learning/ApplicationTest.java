package learning;

import learning.mappers.StudentMapper;
import learning.model.Student;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by lujq on 11/9/2021.
 */
public class ApplicationTest {
    public static void main(String[] args) {

//        insertStudent();
//        updateStudent();
        select(3);
    }

    static void insertStudent(){
        ApplicationContext context = new ClassPathXmlApplicationContext("springConfig.xml");
        StudentMapper studentMapper = ((StudentMapper) context.getBean("studentMapper"));
        Student student = new Student();
        student.setName("f");
        student.setIcon("f");
        studentMapper.insertStudent(student);
    }

    static void select(int id){
        ApplicationContext context = new ClassPathXmlApplicationContext("springConfig.xml");
        StudentMapper studentMapper = ((StudentMapper) context.getBean("studentMapper"));
        System.out.println(studentMapper.getById(id).getIcon());
    }

    static void updateStudent(){
        ApplicationContext context = new ClassPathXmlApplicationContext("springConfig.xml");
        StudentMapper studentMapper = ((StudentMapper) context.getBean("studentMapper"));

        Student student = studentMapper.getById(3);
        student.setIcon("嘻嘻");
        studentMapper.updateStudent(student);
    }


}
