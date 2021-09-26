package com.example.witsonline;

import org.junit.Test;

import static org.junit.Assert.*;

public class RequestVTest {

    @Test
    public void setCourseName() {
        RequestV temp = new RequestV();
        temp.setCourseName("test");
        assertEquals("test",temp.getCourseName());
    }

    @Test
    public void setStudentFName() {
        RequestV temp = new RequestV();
        temp.setStudentFName("test");
        assertEquals("test",temp.getStudentFName());
    }

    @Test
    public void setStudentLName() {
        RequestV temp = new RequestV();
        temp.setStudentLName("test");
        assertEquals("test",temp.getStudentLName());
    }

    @Test
    public void setStudentNumber() {
        RequestV temp = new RequestV();
        temp.setStudentNumber("test");
        assertEquals("test",temp.getStudentNumber());
    }

    @Test
    public void setCourseCode() {
        RequestV temp = new RequestV();
        temp.setCourseCode("test");
        assertEquals("test",temp.getCourseCode());
    }

    @Test
    public void getCourseName() {
        RequestV temp = new RequestV();
        assertEquals(null,temp.getCourseName());
    }

    @Test
    public void getStudentFName() {
        RequestV temp = new RequestV();
        assertEquals(null,temp.getStudentFName());
    }

    @Test
    public void getStudentLName() {
        RequestV temp = new RequestV();
        assertEquals(null,temp.getStudentLName());
    }

    @Test
    public void getStudentNumber() {
        RequestV temp = new RequestV();
        assertEquals(null,temp.getStudentNumber());
    }

    @Test
    public void getCourseCode() {
        RequestV temp = new RequestV();
        assertEquals(null,temp.getCourseCode());
    }
}