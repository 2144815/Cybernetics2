package com.example.witsonline;

import java.util.Comparator;
import java.util.Date;

public class Comment {
    private String UserFullName;
    private int discussionId;
    private String Comment;
    private Date time;


    public static Comparator<Comment> CommentDatesComparator = new Comparator<com.example.witsonline.Comment>() {
        @Override
        public int compare(com.example.witsonline.Comment C1, com.example.witsonline.Comment C2) {
            //If it returns a positive number, C1 has more votes
            //If it returns a negative number, C2 has more votes
            //If if returns zero, then both comments have the same nummber of votes
            return C1.getTime().compareTo( C2.getTime() );
        }
    };
    public static Comparator<Comment> CommentRolesComparator = new Comparator<com.example.witsonline.Comment>() {
        @Override
        public int compare(com.example.witsonline.Comment C1, com.example.witsonline.Comment C2) {
            //If it returns a positive number, C1 has more votes
            //If it returns a negative number, C2 has more votes
            //If if returns zero, then both comments have the same nummber of votes
            int i= 0;

            if(C1.getUserRole().equals( "Instructor" )){
                switch (C2.getUserRole()){
                    case "Student":
                        i = -1;
                    case "Instructor":
                        i = 0;
                    case "Tutor":
                        i = -1;
                }
            }
            else if(C1.getUserRole().equals( "Tutor" )){
                switch (C2.getUserRole()){
                    case "Instructor":
                        i = 1;
                    case "Student":
                        i = -1;
                    case "Tutor":
                        i = 0;
                }
            }
            else if(C1.getUserRole().equals( "Student" )){
                switch (C2.getUserRole()){
                    case "Instructor":
                        i = 1;
                    case "Student":
                        i = 0;
                    case "Tutor":
                        i = 1;
                }
            }




            return i;
        }
    };

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

    public Date getTime() {
        return this.time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public Integer getNoVotes() {
        return NoVotes;
    }

    public void setNoVotes(int noVotes) {
        NoVotes = NoVotes + noVotes;
    }


}
