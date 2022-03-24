package learning;

import com.sun.org.slf4j.internal.Logger;
import com.sun.org.slf4j.internal.LoggerFactory;
import learning.mappers.StudentMapper;
import learning.model.Student;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by lujq on 11/9/2021.
 */
public class ApplicationTest {
    
    private static final Logger logger = LoggerFactory.getLogger(ApplicationTest.class);

    public static void main(String[] args) {
        logger.debug("ddddd");
//        insertStudent();
        updateStudent();
//        select(1);
//        select(2);
//        select(3);
//        select(4);
//        select(5);
    }

    static void insertStudent(){
        ApplicationContext context = new ClassPathXmlApplicationContext("springConfig.xml");
        StudentMapper studentMapper = ((StudentMapper) context.getBean("studentMapper"));
        Student student = new Student();
        student.setName("f");
        student.setIcon("啦啦");
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
        student.setIcon("哈哈");
        studentMapper.updateStudent(student);
    }


}
