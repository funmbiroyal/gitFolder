package services;

import datastore.StudentRepo;
import entitities.School;
import entitities.Student;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.jupiter.api.Assertions.*;

public class StudentServiceImplTest {
    StudentService schoolService;
    School school;
    Student paul;


    @Before
    public void setUp()  {
        paul = new Student();
        school = new School();
        schoolService = new StudentServiceImpl();
        paul.setFirstName("paul");
        paul.setLastName("ajegunle");
        paul.setGender("male");
        paul.setDateOfBirth("10/05/2000");
    }

    @After
    public void tearDown(){
    }

    @Test
    public void registerNewStudent(){
        assertNotNull(paul);
        assertTrue(StudentRepo.getStudents().isEmpty());
        Student studentPaul = schoolService.registerStudent(paul);
        school.addStudent(studentPaul);
        assertEquals(1,school.getCurrentNumberOfStudents());
       // System.out.println(StudentRepo.getStudents().size());

    }
}
