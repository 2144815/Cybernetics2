package com.example.witsonline;

import java.util.Date;

public class DISCUSSIONS {
    public static String DISCUSSION_STUDENT;
    public static String DISCUSSION_TEXT;
    public static String DISCUSSION_TOPIC;
    public static int DISCUSSION_STATUS;
    public static int DISCUSSION_NUM_REPLIES;
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


}
