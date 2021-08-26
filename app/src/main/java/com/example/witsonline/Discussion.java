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
    private String student;
    private String text;
    private String topic;
    private int status;
    private int number_of_replies;
    private Date date;

    public Discussion getDiscId() {
        String webURL = "https://lamp.ms.wits.ac.za/home/s2105624/getDiscId.php?StName=";
        String JsonResponse;
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest( webURL + student, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        return DiscId;
    }

    private Discussion DiscId;





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
