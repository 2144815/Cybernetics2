package com.example.witsonline;

import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.*;

public class DiscussionTest {

    @Test
    public void setDiscussionID() {
        Discussion temp = new Discussion();
        temp.setDiscussionID("test");
        String output =temp.getDiscussionID();
        assertEquals("test",output);
    }

    @Test
    public void setDiscussionText() {
        Discussion temp = new Discussion();
        temp.setDiscussionText("test");
        String output =temp.getDiscussionText();
        assertEquals("test",output);
    }

    @Test
    public void setDiscussionTopic() {
        Discussion temp = new Discussion();
        temp.setDiscussionTopic("test");
        String output =temp.getDiscussionTopic();
        assertEquals("test",output);
    }

    @Test
    public void setDiscussionStudent() {
        Discussion temp = new Discussion();
        temp.setDiscussionStudent("test");
        String output =temp.getDiscussionStudent();
        assertEquals("test",output);
    }

    @Test
    public void setDiscussionStatus() {
        Discussion temp = new Discussion();
        temp.setDiscussionStatus(0);
        String output =temp.getDiscussionStatus();
        assertEquals("Closed",output);
    }

    @Test
    public void setDiscussionReplies() {
        Discussion temp = new Discussion();
        temp.setDiscussionReplies(0);
        int output =temp.getDiscussionReplies();
        assertEquals(0,output);
    }

    @Test
    public void setDiscussionStudentNumber() {
        Discussion temp = new Discussion();
        temp.setDiscussionStudentNumber("test");
        String output =temp.getDiscussionStudentNumber();
        assertEquals("test",output);
    }

    @Test
    public void setDiscussionDate() {
        Discussion temp = new Discussion();
        temp.setDiscussionDate(null);
        Date output =temp.getDiscussionDate();
        assertEquals(null,output);
    }

    @Test
    public void getDiscussionID() {
        Discussion temp = new Discussion();
        String output =temp.getDiscussionID();
        assertEquals(null,output);
    }

    @Test
    public void getDiscussionText() {
        Discussion temp = new Discussion();
        String output =temp.getDiscussionText();
        assertEquals(null,output);
    }

    @Test
    public void getDiscussionTopic() {
        Discussion temp = new Discussion();
        String output =temp.getDiscussionTopic();
        assertEquals(null,output);
    }

    @Test
    public void getDiscussionStudent() {
        Discussion temp = new Discussion();
        String output =temp.getDiscussionStudent();
        assertEquals(null,output);
    }

    @Test
    public void getDiscussionReplies() {
        Discussion temp = new Discussion();
        int output =temp.getDiscussionReplies();
        assertEquals(0,output);
    }

    @Test
    public void getDiscussionDate() {
        Discussion temp = new Discussion();
        Date output =temp.getDiscussionDate();
        assertEquals(null,output);
    }

    @Test
    public void getDiscussionStudentNumber() {
        Discussion temp = new Discussion();
        String output =temp.getDiscussionStudentNumber();
        assertEquals(null,output);
    }

    @Test
    public void getDiscussionStatus_closed() {
        Discussion temp = new Discussion();
        String output =temp.getDiscussionStatus();
        assertEquals("Closed",output);
    }
    @Test
    public void getDiscussionStatus_open() {
        Discussion temp = new Discussion();
        temp.setDiscussionStatus(1);
        String output =temp.getDiscussionStatus();
        assertEquals("Open",output);
    }
}