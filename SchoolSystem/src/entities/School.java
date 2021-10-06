package entities;

import java.util.ArrayList;
import java.util.List;

public class School {
    private  static List<Student> students = new ArrayList<>();
    private static  List<Course>courses = new ArrayList<>();

    public static int getCurrentNumberOfStudents() {
        return students.size();
    }

    public static List<Student> getStudents() {
        return students;
    }

    public void setStudents(Student student) {

       School. students.add(student);
    }

}
