package entitities;

import exceptions.InvalidIdException;
import exceptions.InvalidRegistrationException;
import services.StudentService;
import services.StudentServiceImpl;

import java.util.ArrayList;
import java.util.List;

public class Student {
    private String firstName;
    private String lastName;
    private String gender;
    private String dateOfBirth;
    private String password = "pass";
    private String department;
    private String Id;
    ;

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }


    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }


    public boolean attemptToLogIn(String userName, String id) throws InvalidIdException, InvalidRegistrationException {
        StudentService studentService = new StudentServiceImpl();
        studentService.validateLogIn(userName,id);
        return false;
    }

    public boolean applyForCourses(String[] coursesToApplyFor) throws InvalidIdException, InvalidRegistrationException {
        String username = " ",id = " ";
        attemptToLogIn(username,id);
        boolean hasApplied = false;
        List<String> courseList = new ArrayList<>();
        for (String course : coursesToApplyFor) {
            courseList.add(course);
        }
        hasApplied = true;

        return hasApplied;

    }

}

