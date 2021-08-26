package com.example.witsonline;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class CourseHomePageInstructor extends AppCompatActivity implements  View.OnScrollChangeListener{
    LinearLayout outlineLayout;
    boolean browse;
    //Creating a list of Courses
    private ArrayList<ReviewV> listReviewVs;

    //Creating a list of tags
    private ArrayList<String> tags;
    private ArrayList<TagV> listTagVs;

    private ImageView image;
    private TextView courseName;
    private TextView courseDescription;
    private TextView courseInstructor;
    private RatingBar courseRating;

    //This is for the unsubscribe pop up menu
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private Button btnUnsubscribe, btnCancel;

    //This is for the review pop up menu
    private EditText edtReviewDescription;
    private RatingBar rtbReviewRating;
    private Button btnReviewPostReview;

    //This is for the add tag pop up menu
    private TextView DialogueInstruction;

    //CreatingViews
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;
    private RecyclerView recyclerViewTags;
    private RecyclerView.LayoutManager layoutManagerTags;
    private RecyclerView.Adapter adapterTags;
    String webURL = "https://lamp.ms.wits.ac.za/home/s2105624/loadReviews.php?page=";
    String tagURL = "https://lamp.ms.wits.ac.za/home/s2105624/tagretrieve.php?ccode=";
    String courseURL = "https://lamp.ms.wits.ac.za/home/s2105624/getCourseInfo.php?ccode=";

    //Volley Request Queue
    private RequestQueue requestQueue;

    //button for a lesson
    private Button btnAddLesson;
    private Button btnViewLesson;
    private ImageButton imgForum;
    //button for editing a course
    private ImageView imgEditCourse;

    //button for assigning tutors;
    private Button btnAssignTutors;

    //This is for the delay while loading correct course info
    private ProgressBar progressBar;
    private RelativeLayout relativeLayout;

    //The request counter to send ?page=1, ?page=2 requests
    private int reviewCount = 1;
    @SuppressLint("WrongConstant")
    @Override
    @Generated
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_course_home_page_instructor);
        super.onCreate(savedInstanceState);
        courseName =(TextView)findViewById(R.id.courseName);
        courseDescription =(TextView)findViewById(R.id.courseDescription);
        courseInstructor =(TextView)findViewById(R.id.courseInstructor);
        courseRating = (RatingBar)findViewById(R.id.courseRating);
        outlineLayout = findViewById(R.id.courseOutline);
        image = (ImageView)findViewById(R.id.courseImage);

        //Initializing Views
        recyclerView = (RecyclerView)findViewById(R.id.reviewRecyclerView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        recyclerViewTags = (RecyclerView)findViewById(R.id.tagsRecyclerViewInstructor);
        recyclerViewTags.setHasFixedSize(true);
        layoutManagerTags = new LinearLayoutManager(this);
        recyclerViewTags.setLayoutManager(layoutManagerTags);

        //Initializing our Course list
        listReviewVs = new ArrayList<>();
        listTagVs = new ArrayList<>();
        tags = new ArrayList<>();
        requestQueue = Volley.newRequestQueue(this);

        //Adding an scroll change listener to recyclerView
        recyclerView.setOnScrollChangeListener(this);
        recyclerViewTags.setOnScrollChangeListener(this);

        //initializing our adapters
        adapter = new ReviewCardAdapter(listReviewVs, this);
        adapterTags = new TagAdapter(listTagVs, this);

        //Adding adapter to recyclerviews's
        recyclerView.setAdapter(adapter);
        recyclerViewTags.setAdapter(adapterTags);

        //Calling methods getData to fetch data
        getData();
        getTagData();
        getCourseData();

        //The setting of views happens within the parseCourseData function now
        /*//set views
        if(!COURSE.IMAGE.equals("null")){
            Glide.with(this).load(COURSE.IMAGE).into(image);
        }
        courseName.setText(COURSE.NAME);
        courseDescription.setText(COURSE.DESCRIPTION);
        courseInstructor.setText("By: "+COURSE.INSTRUCTOR);
        courseRating.setRating(Float.parseFloat(COURSE.RATING));
        addOutlineTopics(COURSE.OUTLINE); */

        //Adding on-click for adding a lesson
        btnAddLesson = findViewById(R.id.addLesson);
        btnAddLesson.setOnClickListener(new View.OnClickListener() {
            @Override
            @Generated
            public void onClick(View v) {
                Intent intent = new Intent(CourseHomePageInstructor.this, CreateLesson.class);
              //  intent.putExtra("activity","instructor");
                startActivity(intent);
              //  finish();
            }
        });

        //Adding on-click for assigning a tutor
        btnAssignTutors = findViewById(R.id.assignTutorsButton);
        btnAssignTutors.setOnClickListener(new View.OnClickListener() {
            @Override
            @Generated
            public void onClick(View v) {
                Intent intent = new Intent(CourseHomePageInstructor.this, BrowseStudents.class);
                intent.putExtra("activity","instructor");
                startActivity(intent);
                //  finish();
            }
        });

        btnViewLesson = findViewById(R.id.viewLessons);
        btnViewLesson.setOnClickListener(new View.OnClickListener() {
            @Override
            @Generated
            public void onClick(View v) {
                Intent intent = new Intent(CourseHomePageInstructor.this, BrowseLessons.class);
                intent.putExtra("activity","instructor");
                startActivity(intent);
               // finish();
            }
        });

        //edit course
        imgEditCourse = findViewById(R.id.editCourse);
        imgEditCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            @Generated
            public void onClick(View v) {
                Intent intent = new Intent(CourseHomePageInstructor.this, EditCourse.class);
                startActivity(intent);
                finish();
            }
        });
        imgForum = findViewById(R.id.forumButton);
        imgForum.setOnClickListener(new View.OnClickListener() {
            @Override
            @Generated
            public void onClick(View v) {
                Intent intent = new Intent(CourseHomePageInstructor.this, ForumActivity.class);
                startActivity(intent);
                finish();
            }
        });


        //Progress bar for the whole page and the page's relative layout
        progressBar = findViewById(R.id.progressBar);
        relativeLayout = findViewById(R.id.CourseHomeInstructorLayout);
        progressBar.setVisibility(View.VISIBLE);
    }
    @Generated
    private JsonArrayRequest getDataFromServer(int requestCount){
        //Initializing progressbar
        final ProgressBar progressBar = (ProgressBar) findViewById(R.id.reviewProgressBar);

        //Displaying ProgressBar
        progressBar.setVisibility(View.VISIBLE);
        setProgressBarIndeterminateVisibility(true);

        //JsonArrayRequest of volley
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(webURL + String.valueOf(requestCount) + "&courseCode=" + COURSE.CODE,
                (response) -> {
                    //Calling method parseData to parse the json responce
                    parseData(response);
                    //Hiding the progressBar
                    progressBar.setVisibility(View.GONE);
                },
                (error) -> {
                    progressBar.setVisibility(View.GONE);
                    //If an error occurs that means end of the list has been reached
                    //Toast.makeText(CourseHomePage.this, "No More Items Available", Toast.LENGTH_SHORT).show();
                });
        //Returning the request
        return jsonArrayRequest;
    }
    @Generated
    private JsonArrayRequest getTagDataFromServer(){
        Log.i("method", "getTagDataFromServer() called");
        //Initializing progressbar
        final ProgressBar progressBar = (ProgressBar) findViewById(R.id.tagsProgressBarInstructor);

        //Displaying ProgressBar
        progressBar.setVisibility(View.VISIBLE);
        setProgressBarIndeterminateVisibility(true);

        //JsonArrayRequest of volley
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(tagURL + COURSE.CODE,
                (response) -> {
                    //Creating an array of tags making use of taglist method
                    String[] taglist=taglist(response);
//                    Log.i("Length", "Length: "+Integer.toString(taglist.length));
                    for (int i=0; i<taglist.length; i++){
                        tags.add(taglist[i]);
                        Log.i(Integer.toString(i), taglist[i]);
                    }
                    //Hiding the progressBar
                    progressBar.setVisibility(View.GONE);
                },
                (error) -> {
                    progressBar.setVisibility(View.GONE);
                    //If an error occurs that means end of the list has been reached
                    //Toast.makeText(CourseHomePage.this, "No More Items Available", Toast.LENGTH_SHORT).show();
                });
        //Returning the request
        return jsonArrayRequest;
    }
    @Generated
    private JsonArrayRequest getCourseDataFromServer(){
        //Initializing progressbar
        final ProgressBar progressBar = (ProgressBar) findViewById(R.id.tagsProgressBarInstructor);

        //Displaying ProgressBar
        progressBar.setVisibility(View.VISIBLE);
        setProgressBarIndeterminateVisibility(true);

        //JsonArrayRequest of volley
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(courseURL + COURSE.CODE,
                (response) -> {
                    //parse JSON response in parseCourseData method
                    parseCourseData(response);
                    //Hiding the progressBar
                    progressBar.setVisibility(View.GONE);
                },
                (error) -> {
                    progressBar.setVisibility(View.GONE);
                    //If an error occurs that means end of the list has been reached
                    //Toast.makeText(CourseHomePage.this, "No More Items Available", Toast.LENGTH_SHORT).show();
                });
        //Returning the request
        return jsonArrayRequest;
    }

    //These methods will get Data from the web api
    @Generated
    private void getData(){
        //Adding the method to the queue by calling the method getDatafromServer
        requestQueue.add(getDataFromServer(reviewCount));
        //Incrementing the course counter
        reviewCount++;
    }
    @Generated
    private void getTagData(){
        //Adding the method to the queue by calling the method getTagData
        requestQueue.add(getTagDataFromServer());
    }
    @Generated
    private void getCourseData(){
        //Adding the method to the queue by calling the method getTagData
        requestQueue.add(getCourseDataFromServer());
    }

    //This method will parse json Data
    @Generated
    private String[] taglist(JSONArray all){
        String[] tagTemps = null;
        for(int i = 0; i< all.length(); i++){
            // Tag object
            JSONObject json = null;
            try {
                //getting json
                JSONObject item = all.getJSONObject(i);

                //Adding data to tag object
                COURSE.TAGS = item.getString("Course_Tags");
                String[] tagtemp = COURSE.TAGS.split(";");
                tagTemps = tagtemp;
                for(String j : tagtemp){
                    TagV tagV = new TagV();
                    tagV.setTag(j);
                    if(!j.equals(null) && j.length()>0){
                        listTagVs.add(tagV);
                    }
                }

            }catch (JSONException e) {
                e.printStackTrace();
            }
        }
        adapterTags.notifyDataSetChanged();
        return tagTemps;
    }

    //This method will parse json Data for course
    @Generated
    private void parseCourseData(JSONArray array){
        for (int i = 0; i< array.length(); i++){
            // Creating the Course object
            JSONObject json = null;
            try {
                //Getting json
                json = array.getJSONObject(i);

                //Adding data to the course object
                COURSE.NAME = json.getString("Course_Name");
                COURSE.DESCRIPTION = json.getString("Course_Description");
                COURSE.OUTLINE = json.getString("Course_Outline");
                COURSE.IMAGE = json.getString("Course_Image");
                COURSE.TAGS = json.getString("Course_Tags");
                COURSE.FACULTY = json.getInt("Course_Faculty");
                COURSE.VISIBILITY = json.getString("Course_Visibility");
                //set views
                if(!COURSE.IMAGE.equals("null")){
                    Glide.with(this).load(COURSE.IMAGE).into(image);
                }
                courseName.setText(COURSE.NAME);
                courseDescription.setText(COURSE.DESCRIPTION);
                //courseInstructor.setText("By: "+COURSE.INSTRUCTOR);
                courseInstructor.setText("By: " + USER.FNAME + " " + USER.LNAME);
                courseRating.setRating(Float.parseFloat(COURSE.RATING));
                addOutlineTopics(COURSE.OUTLINE);

                //Make the page visible after displaying the correct course info
                relativeLayout.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
            } catch (JSONException e){
                e.printStackTrace();
            }
        }
    }

    //This method will parse json Data
    @Generated
    private void parseData(JSONArray array){
        for (int i = 0; i< array.length(); i++){
            // Creating the Course object
            ReviewV reviewV = new ReviewV();
            JSONObject json = null;
            try {
                //Getting json
                json = array.getJSONObject(i);

                //Adding data to the course object
                reviewV.setReviewID(json.getString("reviewID"));
                reviewV.setStudentNumber(json.getString("reviewStudentNumber"));
                reviewV.setStudentFName(json.getString("reviewStudentFName"));
                reviewV.setStudentLName(json.getString("reviewStudentLName"));
                reviewV.setReviewRating(json.getString("reviewRating"));
                reviewV.setReviewDescription(json.getString("reviewDescription"));
            } catch (JSONException e){
                e.printStackTrace();
            }
            //Adding the request object to the list
            listReviewVs.add(reviewV);
        }
        //Notifying the adapter that data has been added or changed
        adapter.notifyDataSetChanged();
    }
    //This method will check if the recyclerview has reached the bottom or not
    private boolean isLastItemDisplaying(RecyclerView recyclerView){
        if(recyclerView.getAdapter().getItemCount() != 0){
            int lastVisibleItemPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastCompletelyVisibleItemPosition();
            if (lastVisibleItemPosition != RecyclerView.NO_POSITION && lastVisibleItemPosition == recyclerView.getAdapter().getItemCount() -1){
                return true;
            }
        }
        return false;
    }
    @Generated
    void addOutlineTopics(String outline) {
        //this assume every modules is separated by a space
        String[] outlineTopics = outline.split(";");

        outlineLayout.removeAllViews();

        //for each topic create a card to come up:
        for(int i=0; i < outlineTopics.length; i++)
        {
            if(outlineTopics[i].length()>0) {
                View topicView = View.inflate(this, R.layout.outline_topic_entry, null);
                TextView topicName = topicView.findViewById(R.id.outlineTopic);

                topicName.setText("➤ " + outlineTopics[i]);

                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                params.setMargins(10, 0, 10, 0);

                outlineLayout.addView(topicView, params);
            }
        }
    }
    @Override
    @Generated
    public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
        //if Scrolled at last then
        if(isLastItemDisplaying(recyclerView)){
            //Calling the method getData again
            getData();
        }
    }

    @Override
    @Generated
    public void onBackPressed(){
        Intent i = new Intent(CourseHomePageInstructor.this,MyCourses.class);
        startActivity(i);
        finish();
    }


}