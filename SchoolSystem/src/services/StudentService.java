package services;

import entities.Student;
import exceptions.InvalidIdException;
import exceptions.InvalidRegistrationException;

public interface StudentService {
    Student registerStudent(Student theStudent);
    boolean validateLogIn(String username, String id);
    boolean findStudent(String id) ;

}
