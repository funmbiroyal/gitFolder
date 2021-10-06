package datastore;

import entities.School;
import entities.Student;

import java.util.HashMap;
import java.util.Map;

public class StudentRepo {


    private static Map<String, Student> students = new HashMap();

    public static Map<String, Student> getStudents() {
        return students;
    }

    public static void setStudents(Map<String, Student> students) {
        StudentRepo.students = students;
    }
  static {
        setUp();
       }

    public static void setUp(){
        Student studentPaul = new Student();
        School school = new School();
        studentPaul.setFirstName("paul");
        studentPaul.setLastName("ajegunle");
        studentPaul.setGender("male");
        studentPaul.setDateOfBirth("10/12/2020");

    }


}
