package com.example.witsonline;

import java.util.Date;

public class Discussion {
    private String student;
    private String text;
    private String topic;
    private int status;
    private int number_of_replies;
    private Date date;

    //setters
    public void setDiscussionText(String text){
        this.text = text;
    }
    public void setDiscussionTopic(String topic){
        this.topic = topic;
    }
    public void setDiscussionStudent(String student){
        this.student = student;
    }
    public void setDiscussionStatus(int status){
        this.status = status;
    }
    public  void  setDiscussionReplies(int number_of_replies){
        this.number_of_replies = number_of_replies;
    }
    public  void SetDiscussionDate(Date date){
        this.date = date;
    }

    //Getters

    public String getDiscussionText(){
        return this.text;
    }
    public String getDiscussionTopic(){
        return this.topic;
    }
    public String getDiscussionStudent(){
        return this.student;
    }
    public int getDiscussionReplies(){
        return this.number_of_replies;
    }
    public Date getDiscussionDate(){
        return this.date;
    }
    public String getDiscussionStatus(){
        if(this.status==1){
            return "Closed";
        }
        else{
            return "Open";
        }
    }

}
