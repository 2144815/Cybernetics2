package com.example.witsonline;

import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.*;

public class CommentTest {

    @Test
    public void getId() {
        Comment temp = new Comment();
        String output =temp.getId();
        assertEquals(null,output);
    }

    @Test
    public void setId() {
        Comment temp = new Comment();
        temp.setId("test");
        String output =temp.getId();
        assertEquals("test",output);
    }

    @Test
    public void getUsername() {
        Comment temp = new Comment();
        String output =temp.getUsername();
        assertEquals(null,output);
    }

    @Test
    public void setUsername() {
        Comment temp = new Comment();
        temp.setUsername("test");
        String output =temp.getUsername();
        assertEquals("test",output);
    }

    @Test
    public void getUserRole() {
        Comment temp = new Comment();
        String output =temp.getUserRole();
        assertEquals(null,output);
    }

    @Test
    public void setUserRole() {
        Comment temp = new Comment();
        temp.setUserRole("test");
        String output =temp.getUserRole();
        assertEquals("test",output);
    }

    @Test
    public void getUserFullName() {
        Comment temp = new Comment();
        String output =temp.getUserFullName();
        assertEquals(null,output);
    }

    @Test
    public void setUserFullName() {
        Comment temp = new Comment();
        temp.setUserFullName("test");
        String output =temp.getUserFullName();
        assertEquals("test",output);
    }

    @Test
    public void getDiscussionId() {
        Comment temp = new Comment();
        int output =temp.getDiscussionId();
        assertEquals(0,output);
    }

    @Test
    public void setDiscussionId() {
        Comment temp = new Comment();
        temp.setDiscussionId(1);
        int output =temp.getDiscussionId();
        assertEquals(1,output);
    }

    @Test
    public void getComment() {
        Comment temp = new Comment();
        String output =temp.getComment();
        assertEquals(null,output);
    }

    @Test
    public void setComment() {
        Comment temp = new Comment();
        temp.setComment("test");
        String output =temp.getComment();
        assertEquals("test",output);
    }

    @Test
    public void getTime() {
        Comment temp = new Comment();
        Date output =temp.getTime();
        assertEquals(null,output);
    }

    @Test
    public void setTime() {
        Comment temp = new Comment();
        temp.setTime(null);
        Date output =temp.getTime();
        assertEquals(null,output);
    }

    @Test
    public void getNoVotes() {
        Comment temp = new Comment();
        int output =temp.getNoVotes();
        assertEquals(0,output);
    }

    @Test
    public void setNoVotes() {
        Comment temp = new Comment();
        temp.setNoVotes(0);
        int output =temp.getNoVotes();
        assertEquals(0,output);
    }
}