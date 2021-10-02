package com.example.witsonline;

public class RequestV {
    String studentFName;
    String studentLName;
    String courseName;
    String courseCode;
    String studentNumber;
    private String studentImageUrl;

    void setCourseName(String strCourse){ courseName = strCourse;}
    void setStudentFName(String strFName){ studentFName = strFName;}
    void setStudentLName(String strLName){ studentLName = strLName;}
    void setStudentNumber(String strNumber){ studentNumber = strNumber;}
    void setStudentImageUrl(String url){ studentImageUrl = url; }
    void setCourseCode(String strCourseCode){ courseCode = strCourseCode;}

    String getCourseName(){return courseName;}
    String getStudentFName(){return studentFName;}
    String getStudentLName(){return studentLName;}
    String getStudentNumber(){return studentNumber;}
    String getStudentImageUrl(){ return studentImageUrl;}
    String getCourseCode(){return courseCode;}
}
