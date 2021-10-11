package com.example.witsonline;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Generated
public class USER {
	public static boolean STUDENT;
	public static String USER_NUM;
	public static String USERNAME;
	public static String PASSWORD;
	public static String EMAIL;
	public static ArrayList<String> COURSES;
	public static String temp;

	public static String FNAME;
	public static String LNAME;

	public static ArrayList<CourseV> FEATURED_COURSES;
	public static Set<String> SUBSCRIBED_TO_FEAT_COURSE;
	public static Map<String, Integer> VOTES  = new HashMap<>();
	public static Map<String, Integer> INSTRUCTOR_VOTES = new HashMap<>();

	//for registration
	public static String regUser = "";
	public static String regPass = "";
	public static boolean regStudent = true;
}
