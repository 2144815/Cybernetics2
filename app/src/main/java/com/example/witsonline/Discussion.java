package com.example.witsonline;

import android.util.Log;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.Date;


public class Discussion implements Serializable {

    //for view profile
    private String studentNumber;

    private String student;
    private String text;
    private String topic;
    private int status;
    private int number_of_replies;
    private Date date;
    private String DiscId;

    //setters
    public void setDiscussionID(String id){
        this.DiscId = id;
    }
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
    public void setDiscussionStudentNumber(String studnum){
        this.studentNumber = studnum;
    }
    public  void setDiscussionDate(Date date){
        this.date = date;
    }

    //Getters
    public String getDiscussionID() {
        return DiscId;
    }
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
    public String getDiscussionStudentNumber(){
        return this.studentNumber;
    }
    public String getDiscussionStatus(){
        if(this.status==0){
            return "Closed";
        }
        else{
            return "Open";
        }
    }

}
