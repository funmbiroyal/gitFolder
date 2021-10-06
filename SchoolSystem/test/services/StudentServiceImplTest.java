package services;

import datastore.StudentRepo;
import entities.Course;
import entities.School;
import entities.Student;
import exceptions.InvalidIdException;
import exceptions.InvalidRegistrationException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;

public class StudentServiceImplTest {
    StudentService schoolService;
    School school;
    Student paul;


    @BeforeEach
    public void setUp() {
        paul = new Student();
        school = new School();
        schoolService = new StudentServiceImpl();
        paul.setFirstName("paul");
        paul.setLastName("ajegunle");
        paul.setPassword("pass");
        paul.setGender("male");
        paul.setDateOfBirth("10/05/2000");
    }

    @AfterEach
    public void tearDown() {
    }

    @Test
    public void registerNewStudent() {
        assertNotNull(paul);
        assertTrue(StudentRepo.getStudents().isEmpty());
        schoolService.registerStudent(paul);
        assertEquals(1, School.getStudents().size());
    }

    @Test
    public void loginRegisteredStudent() {

        assertNotNull(paul);
        assertTrue(StudentRepo.getStudents().isEmpty());
        schoolService.registerStudent(paul);
        Student grace = new Student();
        grace.setLastName("Gracie");
        grace.setPassword("gee");
        schoolService.registerStudent(grace);
        assertEquals(2, School.getStudents().size());
        assertTrue(grace.attemptToLogIn("Gracie", "gee"));
        assertTrue(paul.attemptToLogIn("Ajegunle", "pass"));

    }

    @Test
    public void loginWithWithWrongId() {
        assertNotNull(paul);
        assertTrue(StudentRepo.getStudents().isEmpty());
        schoolService.registerStudent(paul);
        assertThrows(InvalidIdException.class,()->paul.attemptToLogIn("ajegunle", "pas"));
    }

    @Test
    public void studentCanApplyForCourses() {
        try {
            assertNotNull(paul);
            assertTrue(StudentRepo.getStudents().isEmpty());
            schoolService.registerStudent(paul);
            assertEquals(1, School.getStudents().size());
            assertTrue(paul.attemptToLogIn("ajegunle", "pass"));

            paul.applyForCourses(new Course("Java"), new Course("Python"), new Course("JavaScript") );
        } catch (InvalidIdException | InvalidRegistrationException e) {
            e.printStackTrace();
            System.out.println("I did not pass");
        }
    }

}
