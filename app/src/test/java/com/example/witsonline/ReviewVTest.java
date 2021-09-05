package com.example.witsonline;

import org.junit.Test;

import static org.junit.Assert.*;

public class ReviewVTest {

    @Test
    public void setStudentFName() {
        ReviewV temp= new ReviewV();
        String expected="testFname";
        temp.setStudentFName("testFname");
        String output=temp.getStudentFName();
        assertEquals(expected,output);
    }

    @Test
    public void setStudentLName() {
        ReviewV temp= new ReviewV();
        String expected="testLname";
        temp.setStudentLName("testLname");
        String output=temp.getStudentLName();
        assertEquals(expected,output);
    }

    @Test
    public void setReviewDescription() {
        ReviewV temp= new ReviewV();
        String expected="good";
        temp.setReviewDescription("good");
        String output=temp.getReviewDescription();
        assertEquals(expected,output);
    }

    @Test
    public void setReviewRating() {
        ReviewV temp= new ReviewV();
        String expected="5";
        temp.setReviewRating("5");
        String output=temp.getReviewRating();
        assertEquals(expected,output);

    }
    @Test
    public void setStudentNumber() {
        ReviewV temp= new ReviewV();
        temp.setStudentNumber("test");
        String output=temp.getStudentNumber();
        assertEquals("test",output);
    }
    @Test
    public void setReviewID() {
        ReviewV temp= new ReviewV();
        temp.setReviewID("test");
        String output=temp.getReviewID();
        assertEquals("test",output);
    }

    @Test
    public void getStudentFNameWithNullValue() {
        ReviewV temp= new ReviewV();
        String expected =null;
        String output=temp.getStudentFName();
        assertEquals(expected,output);
    }
    @Test
    public void getStudentFNameWithNonNullValue() {
        ReviewV temp= new ReviewV();
        String expected ="testFname";
        temp.setStudentFName("testFname");
        String output=temp.getStudentFName();
        assertEquals(expected,output);
    }

    @Test
    public void getStudentLNameWithNullValue() {
        ReviewV temp= new ReviewV();
        String expected =null;
        String output=temp.getStudentLName();
        assertEquals(expected,output);
    }
    @Test
    public void getStudentLNameWithNonNullValue() {
        ReviewV temp= new ReviewV();
        String expected ="testLname";
        temp.setStudentLName("testLname");
        String output=temp.getStudentLName();
        assertEquals(expected,output);
    }

    @Test
    public void getReviewDescriptionWithNullValue() {
        ReviewV temp= new ReviewV();
        String expected =null;
        String output=temp.getReviewDescription();
        assertEquals(expected,output);
    }
    @Test
    public void getReviewDescriptionWithNonNullValue() {
        ReviewV temp= new ReviewV();
        String expected ="good";
        temp.setReviewDescription("good");
        String output=temp.getReviewDescription();
        assertEquals(expected,output);
    }

    @Test
    public void getReviewRatingWithNullValue() {
        ReviewV temp= new ReviewV();
        String expected ="0.0";
        String output=temp.getReviewRating();
        assertEquals(expected,output);

    }
    @Test
    public void getReviewRatingWithNonNullValue() {
        ReviewV temp= new ReviewV();
        temp.setReviewRating("5");
        String expected="5";
        String output=temp.getReviewRating();
        assertEquals(expected,output);
    }
    @Test
    public void getReviewID() {
        ReviewV temp= new ReviewV();
        String output=temp.getReviewID();
        assertEquals(null,output);
    }
    @Test
    public void getStudentNumber() {
        ReviewV temp= new ReviewV();
        String output=temp.getStudentNumber();
        assertEquals(null,output);
    }

}