package com.example.witsonline;

public class QuizV {

    private String quizID;
    private String quizName;
    private int quizMarkAlloc;
    private int quizNumQuestions;
    private int quizVisibility;

    public void setQuizID(String id){ this.quizID = id;}
    public void setQuizName(String name){ this.quizName = name;}
    public void setQuizMarkAlloc(int mark){ this.quizMarkAlloc = mark;}
    public void setQuizNumQuestions(int num){ this.quizNumQuestions = num;}
    public void setQuizVisibility(int visibility){ this.quizVisibility = visibility;}

    public String getQuizID(){return quizID;}
    public String getQuizName(){return quizName;}
    public int getQuizMarkAlloc(){return quizMarkAlloc;}
    public int getQuizNumQuestions(){return quizNumQuestions;}
    public int getQuizVisibility(){return quizVisibility;}
}
