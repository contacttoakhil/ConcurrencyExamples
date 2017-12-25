package main.vo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Akhil on 10/16/2015.
 */
public class Student {
    private Integer studentID;
    private String studentName;

    public Student(Integer studentID, String studentName) {
        this.studentID = studentID;
        this.studentName = studentName;
    }

    public Integer getStudentID() {
        return studentID;
    }

    public void setStudentID(Integer studentID) {
        this.studentID = studentID;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    @Override
    public String toString() {
        return "Student{" +
                "studentID=" + studentID +
                ", studentName='" + studentName + '\'' +
                '}';
    }

    public static List<Student> getStudents() {
        List<Student> inputList = new ArrayList<>(5);
        inputList.add(new Student(1,"John"));
        inputList.add(new Student(2,"Ram"));
        inputList.add(new Student(3,"Mohan"));
        inputList.add(new Student(4,"Aman"));
        inputList.add(new Student(5,"Ramesh"));
        inputList.add(new Student(6,"Mohan"));
        return inputList;
    }
}
