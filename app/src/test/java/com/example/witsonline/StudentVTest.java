package com.example.witsonline;

import org.junit.Test;

import static org.junit.Assert.*;

public class StudentVTest {

    @Test
    public void setStudentNumber() {
        StudentV temp = new StudentV();
        temp.setStudentNumber("test");
        String output =temp.getStudentNumber();
        assertEquals("test",output);
    }

    @Test
    public void setStudentFName() {
        StudentV temp = new StudentV();
        temp.setStudentFName("test");
        String output =temp.getStudentFName();
        assertEquals("test",output);
    }

    @Test
    public void setStudentLName() {
        StudentV temp = new StudentV();
        temp.setStudentLName("test");
        String output =temp.getStudentLName();
        assertEquals("test",output);
    }

    @Test
    public void setTutorState() {
        StudentV temp = new StudentV();
        temp.setTutorState("test");
        String output =temp.getTutorState();
        assertEquals("test",output);
    }

    @Test
    public void getStudentNumber() {
        StudentV temp = new StudentV();
        String output =temp.getStudentNumber();
        assertEquals(null,output);
    }

    @Test
    public void getStudentFName() {
        StudentV temp = new StudentV();
        String output =temp.getStudentFName();
        assertEquals(null,output);
    }

    @Test
    public void getStudentLName() {
        StudentV temp = new StudentV();
        String output =temp.getStudentLName();
        assertEquals(null,output);
    }

    @Test
    public void getTutorState() {
        StudentV temp = new StudentV();
        String output =temp.getTutorState();
        assertEquals(null,output);
    }
}