package com.example.witsonline;

public class Comment {
    private String UserFullName;
    private int discussionId;
    private String Comment;
    private String time;
    private int NoVotes = 0;

    public Comment(String userFullName, Integer discussionId, String comment, String time) {
        UserFullName = userFullName;
        this.discussionId = discussionId;
        Comment = comment;
        this.time = time;

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
