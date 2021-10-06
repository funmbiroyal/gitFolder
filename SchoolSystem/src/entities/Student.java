package entities;

import exceptions.InvalidIdException;
import exceptions.InvalidRegistrationException;
import services.StudentService;
import services.StudentServiceImpl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Student {
    private String firstName;
    private String lastName;
    private String gender;
    private String dateOfBirth;
    private String password;
    private String department;
    private String Id;
    private List<Course> courseList = new ArrayList<>();


    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

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


    public boolean attemptToLogIn(String userName, String password) {
        if(!this.lastName.equalsIgnoreCase(userName)) throw new InvalidIdException("Invalid user");
        StudentService studentService = new StudentServiceImpl();
       return studentService.validateLogIn(userName,password);

    }

    public boolean applyForCourses(Course... coursesToApplyFor) throws InvalidIdException, InvalidRegistrationException {
//        String username = " ",id = " ";
//        attemptToLogIn(username,id);
        boolean hasApplied = false;

        courseList.addAll(Arrays.asList(coursesToApplyFor));
        hasApplied = true;

        return hasApplied;

    }

}

