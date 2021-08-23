package entities;

import java.util.ArrayList;
import java.util.List;

public class School {
    private  static List<Student> students = new ArrayList<>();
    private static int currentNumberOfStudents = 0;
    private static  List<Course>courses = new ArrayList<>();

    public static int getCurrentNumberOfStudents() {
        currentNumberOfStudents++;
        return currentNumberOfStudents;
    }

    public static void setCurrentNumberOfStudents(int currentNumberOfStudents) {
        
        School.currentNumberOfStudents = currentNumberOfStudents;
    }

//    public static String generatePassword() {
//        return students.get(1).getDateOfBirth();
//    }

    public static List<Student> getStudents() {
        return students;
    }

    public void setStudents(Student student) {

       School. students.add(student);
    }

}
