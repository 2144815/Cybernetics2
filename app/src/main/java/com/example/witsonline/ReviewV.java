package com.example.witsonline;

public class ReviewV {

    //for view profile
    private String strReviewID;
    private String strStudentNumber;

    //for reviews
    private String strStudentFName;
    private String strStudentLName;
    private String strReviewDescription;
    private String strReviewRating;
    private String strReviewImageUrl;

    public void setReviewID(String reviewID){ this.strReviewID = reviewID; }
    public void setStudentNumber(String studentNumber){ this.strStudentNumber = studentNumber; }
    public void setStudentFName(String studentFName){ this.strStudentFName = studentFName; }
    public void setStudentLName(String studentLName){
        this.strStudentLName = studentLName;
    }
    public void setReviewDescription(String reviewDescription){ this.strReviewDescription = reviewDescription;}
    public void setReviewRating(String reviewRating){ this.strReviewRating = reviewRating;}
    public void setReviewImageUrl(String url){ this.strReviewImageUrl = url;}

    public String getReviewID(){ return strReviewID; }
    public String getStudentNumber(){ return strStudentNumber; }
    public String getStudentFName(){
        return strStudentFName;
    }
    public String getStudentLName(){
        return strStudentLName;
    }
    public String getReviewDescription(){
        return strReviewDescription;
    }
    public String getReviewImageUrl(){ return  strReviewImageUrl; }
    public String getReviewRating(){
        if(strReviewRating!=null) {
            return strReviewRating;
        }
        else{
            return "0.0";
        }
    }

}
