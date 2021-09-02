package com.example.witsonline;

public class Comment {
    private String UserFullName;
    private int discussionId;
    private String Comment;
    private String time;
    private String role;
    private String username;
    private int NoVotes = 0;
    private String id;

    public String getId(){
        return id;
    }
    public void setId(String id){
        this.id = id;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username =username;
    }
    public String getUserRole() {
        return role;
    }
    public void setUserRole(String role) {
        this.role =role;
    }
    public String getUserFullName() {
        return UserFullName;
    }

    public void setUserFullName(String userFullName) {
        UserFullName = userFullName;
    }

    public Integer getDiscussionId() {
        return discussionId;
    }

    public void setDiscussionId(int discussionId) {
        this.discussionId = discussionId;
    }

    public String getComment() {
        return Comment;
    }

    public void setComment(String comment) {
        Comment = comment;
    }

    public String getTime() {
        return "At: "+time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Integer getNoVotes() {
        return NoVotes;
    }

    public void setNoVotes(int noVotes) {
        NoVotes = NoVotes + noVotes;
    }


}
