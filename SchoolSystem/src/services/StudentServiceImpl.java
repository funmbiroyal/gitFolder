package services;

import entities.School;
import entities.Student;
import exceptions.InvalidIdException;

public class StudentServiceImpl implements StudentService {

    @Override
    public Student registerStudent(Student theStudent) {
        School.getStudents().add(theStudent);

        return theStudent;
    }

@Override
    public boolean validateLogIn(String username, String password) throws InvalidIdException {
        for (Student student: School.getStudents()){
            if (student.getLastName().equalsIgnoreCase(username)){
                if(student.getPassword().equals(password)){
                    return true;
                }
                else{ throw new InvalidIdException("Invalid password");}
            }
        }
        throw new InvalidIdException("No student found with that Id");
    }

@Override
    public boolean findStudent(String id)  {
        boolean hasFound = false;
        for (Student student:School.getStudents()){
            if (id.equals(School.getStudents().get(0).getId())) {
                hasFound = true;
                break;
            }
        }
        return hasFound;
    }

}


