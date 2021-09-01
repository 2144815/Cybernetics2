package com.example.witsonline;

import java.util.Date;

public class DISCUSSIONS {
    public static String DISCUSSION_STUDENT;
    public static String DISCUSSION_TEXT;
    public static String DISCUSSION_TOPIC;
    public static int DISCUSSION_STATUS;
    public static String DISCUSSION_NUM_REPLIES;
    public static Date DISCUSSION_DATE;
    public static String DISCUSSION_ID;

    public static String getDiscussionStatus() {
        if(DISCUSSION_STATUS==1){
            return "Open";
        }
        else{
            return"Closed";
        }
    }
    public static void updateNoReplies(){
        int x = (Integer.valueOf( DISCUSSION_NUM_REPLIES )+1);
        DISCUSSION_NUM_REPLIES = String.valueOf( x );
    }


}
