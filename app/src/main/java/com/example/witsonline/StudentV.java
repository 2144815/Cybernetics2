package com.example.witsonline;

public class StudentV {
    private String strStudentNumber;
    private String strStudentFName;
    private String strStudentLName;
    private String strTutorState;

    public void setStudentNumber(String studentNumber){ this.strStudentNumber = studentNumber;}
    public void setStudentFName(String studentFName){ this.strStudentFName = studentFName;}
    public void setStudentLName(String studentLName){ this.strStudentLName = studentLName;}
    public void setTutorState(String tutorState){ this.strTutorState = tutorState;}

    public String getStudentNumber(){ return strStudentNumber;}
    public String getStudentFName(){ return strStudentFName;}
    public String getStudentLName(){ return strStudentLName;}
    public String getTutorState(){ return strTutorState;}
}
