package services;

import entitities.Student;
import exceptions.InvalidIdException;
import exceptions.InvalidRegistrationException;

public interface StudentService {
    Student registerStudent(Student theStudent);
    boolean validateLogIn(String username, String id) throws InvalidRegistrationException, InvalidIdException;
    boolean findStudent(String id) ;

}
