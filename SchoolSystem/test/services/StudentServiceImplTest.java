package services;

import datastore.StudentRepo;
import entities.School;
import entities.Student;
import exceptions.InvalidIdException;
import exceptions.InvalidRegistrationException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.jupiter.api.Assertions.*;

public class StudentServiceImplTest {
    StudentService schoolService;
    School school;
    Student paul;


    @Before
    public void setUp() {
        paul = new Student();
        school = new School();
        schoolService = new StudentServiceImpl();
        paul.setFirstName("paul");
        paul.setLastName("ajegunle");
        paul.setGender("male");
        paul.setDateOfBirth("10/05/2000");
    }

    @After
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
        try {
        assertNotNull(paul);
        assertTrue(StudentRepo.getStudents().isEmpty());
        schoolService.registerStudent(paul);
        assertEquals(1, School.getStudents().size());
        assertTrue(paul.attemptToLogIn("ajegunle", "pass"));
        } catch (InvalidIdException | InvalidRegistrationException e) {
            e.printStackTrace();
        }

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
            String[] courses = {"Java", "python", "javascript", "web"};
            paul.applyForCourses( courses);
        } catch (InvalidIdException | InvalidRegistrationException e) {
            e.printStackTrace();
        }

    }


}
