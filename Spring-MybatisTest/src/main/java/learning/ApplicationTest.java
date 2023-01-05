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
        ApplicationContext context = new ClassPathXmlApplicationContext("springConfig.xml");
        StudentMapper studentMapper = ((StudentMapper) context.getBean("studentMapper"));
//        StudentMapper studentMapper = context.getBean(StudentMapper.class);
//        Student student = new Student();
//        student.setPercentage(10);
//        student.setBranch("1");
//        student.setName("jon3");
//        student.setPhone(1);
////        student.setId(1);
//        student.setEmail("aaa");
//        studentMapper.insertStudent(student);
//        System.out.println(studentMapper.GetAll().get(3).getId());


    }
}
