package com.example.witsonline;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
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

public class CourseHomePage extends AppCompatActivity implements  View.OnScrollChangeListener{

    LinearLayout outlineLayout;
    private Button subscribe;
    private Button review;
    private boolean browse = false;
    private boolean mycourses = false;
    private boolean dashboard = false;
    private Button viewLesson;
    private TextView courseInstructor;
    //Creating a list of Courses
    private ArrayList<ReviewV> listReviewVs;
    private ImageView imgEditCourse;

    //Creating a list of tags
    private ArrayList<String> tags;
    private ArrayList<TagV> listTagVs;

    //progress bar for entire page
    private ProgressBar progressBar;
    private RelativeLayout relativeLayout;

    private ImageView image;
    //This is for the unsubscribe pop up menu
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private Button btnUnsubscribe, btnCancel;

    //This is for the review pop up menu
    private EditText edtReviewDescription;
    private RatingBar rtbReviewRating;
    private Button btnReviewPostReview;

    // for viewing instructor's profile
    private Button btnView, btnProfileCancel;
    private ImageButton imgForum;
    //CreatingViews
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;
    private RecyclerView recyclerViewTags;
    private RecyclerView.LayoutManager layoutManagerTags;
    private RecyclerView.Adapter adapterTags;
    String webURL = "https://lamp.ms.wits.ac.za/home/s2105624/loadReviews.php?page=";
    String tagURL = "https://lamp.ms.wits.ac.za/home/s2105624/tagretrieve.php?ccode=";
    String instrURL = "https://lamp.ms.wits.ac.za/home/s2105624/getInstructorName.php?Instructor_Username=";

    //Volley Request Queue
    private RequestQueue requestQueue;

    //For determining previous activity
    Bundle extras;

    //The request counter to send ?page=1, ?page=2 requests
    private int reviewCount = 1;
    @Override
    @Generated
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_course_home_page);
        super.onCreate(savedInstanceState);
        //To determine which activity we came from (BrowseCourses or MyCourses
        extras = getIntent().getExtras();
        if (extras != null) {
            String act = extras.getString("activity");
            if (act.contains("BrowseCourses")) {
                browse = true;
            } else if (act.contains("Dashboard")) {
                dashboard = true;

            } else {
                mycourses = true;
            }
        }

        progressBar = findViewById(R.id.courseHomePageProgressBar);
        relativeLayout = findViewById(R.id.CourseHomePageRelLayout);

        TextView courseName =(TextView)findViewById(R.id.courseName);
        TextView courseDescription =(TextView)findViewById(R.id.courseDescription);
        courseInstructor =(TextView)findViewById(R.id.courseInstructor);
        courseInstructor.setOnClickListener(new View.OnClickListener() {
            @Override
            @Generated
            public void onClick(View v) {
                createNewViewProfileDialog();
            }
        });
        RatingBar courseRating = (RatingBar)findViewById(R.id.courseRating);
        subscribe = (Button)findViewById(R.id.subscribe);
        outlineLayout = findViewById(R.id.courseOutline);
        review = (Button)findViewById(R.id.review);
        image = (ImageView)findViewById(R.id.courseImage);
        viewLesson = (Button)findViewById(R.id.viewLessons);


        if(!COURSE.IMAGE.equals("null")){
            Glide.with(this).load(COURSE.IMAGE).into(image);
        }
        courseName.setText(COURSE.NAME);
        courseDescription.setText(COURSE.DESCRIPTION);
        courseInstructor.setText("By: "+COURSE.INSTRUCTOR_NAME);
        courseRating.setRating(Float.parseFloat(COURSE.RATING));
        INSTRUCTOR.USERNAME = COURSE.INSTRUCTOR;
        addOutlineTopics(COURSE.OUTLINE);

        //Initializing Views
        recyclerView = (RecyclerView)findViewById(R.id.reviewRecyclerView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        recyclerViewTags = (RecyclerView)findViewById(R.id.tagsRecyclerView);
        recyclerViewTags.setHasFixedSize(true);
        layoutManagerTags = new LinearLayoutManager(this);
        recyclerViewTags.setLayoutManager(layoutManagerTags);

        //Initializing our Course list
        listReviewVs = new ArrayList<>();
        listTagVs = new ArrayList<>();
        tags = new ArrayList<>();
        requestQueue = Volley.newRequestQueue(this);

        //Calling methods to get data from server
        getData();
        getTagData();
        getInstrData();

        //Adding an scroll change listener to recyclerView
        recyclerView.setOnScrollChangeListener(this);
        recyclerViewTags.setOnScrollChangeListener(this);

        //initializing our adapters
        adapter = new ReviewCardAdapter(listReviewVs, this);
        adapterTags = new TagAdapter(listTagVs, this);

        //Adding adapter to recyclerview's
        recyclerView.setAdapter(adapter);
        recyclerViewTags.setAdapter(adapterTags);
        imgForum = findViewById(R.id.forumButton);
        imgForum.setOnClickListener(new View.OnClickListener() {
            @Override
            @Generated
            public void onClick(View v) {
                Intent intent = new Intent(CourseHomePage.this, ForumActivity.class);
                if(browse){
                    intent.putExtra("activity",""+BrowseCourses.class);
                }
                else if(mycourses){
                    intent.putExtra("activity",""+MyCourses.class);
                }
                else{
                    intent.putExtra("activity",""+Dashboard.class);
                }
                startActivity(intent);
                finish();
            }
        });

        //check a student's enrolment as the page loads
        try {
            doPostRequest("enrolment.php");
        } catch (IOException e) {
            e.printStackTrace();
        }

        subscribe.setOnClickListener(new View.OnClickListener() {
            @Override
            @Generated
            public void onClick(View v) {
                if(subscribe.getText().toString().trim().equals("SUBSCRIBE")){
                    //display forum if student is subscribed to course
                    imgForum.setVisibility(View.VISIBLE);
                    try {
                        doPostRequest("enrol.php");
                        subscribe.setText("SUBSCRIBED");
                        Toast toast = Toast.makeText(CourseHomePage.this, "Subscribed to "+COURSE.CODE, Toast.LENGTH_LONG);
                        toast.show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                else{
                    createNewViewDialog();
                }
            }
        });

        review.setOnClickListener(new View.OnClickListener() {
            @Override
            @Generated
            public void onClick(View view) {
                createNewViewDialogReview();
            }

        });

        viewLesson.setOnClickListener(new View.OnClickListener() {
            @Override
            @Generated
            public void onClick(View v) {
                Intent intent = new Intent(CourseHomePage.this, BrowseLessons.class);
                intent.putExtra("activity","student");
                startActivity(intent);
            //    finish();
            }
        });


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
        final ProgressBar progressBar = (ProgressBar) findViewById(R.id.tagsProgressBar);

        //Displaying ProgressBar
        progressBar.setVisibility(View.VISIBLE);
        setProgressBarIndeterminateVisibility(true);

        //JsonArrayRequest of volley
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(tagURL + COURSE.CODE,
                (response) -> {
                    //Creating an array of tags making use of taglist method
                    String[] taglist=taglist(response);
                    Log.i("Length", "Length: "+Integer.toString(taglist.length));
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
    private JsonArrayRequest getInstrDataFromServer(){

        //Initializing progressbar
        final ProgressBar progressBar = (ProgressBar) findViewById(R.id.reviewProgressBar);

        //Displaying ProgressBar
        progressBar.setVisibility(View.VISIBLE);
        setProgressBarIndeterminateVisibility(true);

        //JsonArrayRequest of volley
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(instrURL + COURSE.INSTRUCTOR,
                (response) -> {
                    //Calling method parseData to parse the json responce
                    parseInstrData(response);
                    //Hiding the progressBar
                    progressBar.setVisibility(View.GONE);

                },
                (error) -> {
                    //If an error occurs that means end of the list has been reached
                    progressBar.setVisibility(View.GONE);
                    //Toast.makeText(CourseHomePage.this, "No More Items Available", Toast.LENGTH_SHORT).show();
                });
        //Returning the request
        return jsonArrayRequest;
    }

    //This method will get Data from the web api
    private void getData(){
        //Adding the method to the queue by calling the method getDatafromServer
        requestQueue.add(getDataFromServer(reviewCount));
        //Incrementing the course counter
        reviewCount++;
    }

    private void getTagData(){
        //Adding the method to the queue by calling the method getTagData
        requestQueue.add(getTagDataFromServer());
    }

    private void getInstrData(){
        //Adding the method to the queue by calling the method getTagData
        requestQueue.add(getInstrDataFromServer());
    }

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

    //This method will parse json Data
    @Generated
    private void parseInstrData(JSONArray array){
        for (int i = 0; i< array.length(); i++){

            JSONObject json = null;
            try {
                //Getting json
                json = array.getJSONObject(i);

                //Adding data to the course object
                String fname = json.getString("Instructor_FName");
                String lname = json.getString("Instructor_LName");

                courseInstructor.setText("By: "+ fname + " " + lname);

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
            if (outlineTopics[i].length() > 0) {
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
    @Generated
    private void doPostRequest(String phpFile) throws IOException {
        OkHttpClient client = new OkHttpClient();

        HttpUrl.Builder urlBuilder = HttpUrl.parse("https://lamp.ms.wits.ac.za/~s2105624/" + phpFile).newBuilder();
        urlBuilder.addQueryParameter("studentNumber",USER.USERNAME);
        urlBuilder.addQueryParameter("courseCode", COURSE.CODE);
        String url = urlBuilder.build().toString();

        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            @Generated
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
            }

            @Override
            @Generated
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                final String responseData = response.body().string();
                CourseHomePage.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(responseData.trim().equals("subscribed")){
                            //display forum if student is subscribed to course
                            imgForum.setVisibility(View.VISIBLE);
                            subscribe.setText("SUBSCRIBED");
                        }
                        if(responseData.trim().equals("not subscribed")){
                            // don't display forum if student is not subscribed to course
                            imgForum.setVisibility(View.GONE);
                            subscribe.setText("SUBSCRIBE");
                           // btnUnsubscribe.setText("SUBSCRIBE");
                        }
                        //Hiding the progressBar
                        relativeLayout.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.GONE);
                    }
                });
            }
        });
    }
    @Generated
    public void createNewViewDialog(){
        dialogBuilder = new AlertDialog.Builder(this);
        final View viewPopUp = LayoutInflater.from(this)
                .inflate(R.layout.unsubscribe_dialog, null);

        btnUnsubscribe = (Button) viewPopUp.findViewById(R.id.unSubscribe);
        btnCancel = (Button) viewPopUp.findViewById(R.id.unsubscribeCancel);

        dialogBuilder.setView(viewPopUp);
        dialog = dialogBuilder.create();
        dialog.show();

        btnUnsubscribe.setOnClickListener(new View.OnClickListener() {
            @Override
            @Generated
            public void onClick(View v) {
                // don't display forum if student is not subscribed to course
                imgForum.setVisibility(View.GONE);
                subscribe.setText("SUBSCRIBE");
                try {
                    doPostRequest("unsubscribe.php");
                    Toast toast = Toast.makeText(CourseHomePage.this, "Unsubscribed to "+COURSE.CODE, Toast.LENGTH_LONG);
                    toast.show();
                    dialog.dismiss();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            @Generated
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

    }
    @Generated
    public void createNewViewDialogReview(){
        dialogBuilder = new AlertDialog.Builder(this);
        final View viewPopUp = LayoutInflater.from(this)
                .inflate(R.layout.review_dialog, null);

        dialogBuilder.setView(viewPopUp);
        dialog = dialogBuilder.create();
        dialog.show();

        btnReviewPostReview = (Button) viewPopUp.findViewById(R.id.dialogPostReview);
        rtbReviewRating = (RatingBar) viewPopUp.findViewById(R.id.dialogCourseRating);
        edtReviewDescription = (EditText) viewPopUp.findViewById(R.id.dialogCourseDescription);

        btnReviewPostReview.setOnClickListener(new View.OnClickListener() {
            @Override
            @Generated
            public void onClick(View v) {
                float postReviewRating = rtbReviewRating.getRating();
                String postReviewDescription = edtReviewDescription.getText().toString();
                if(postReviewDescription.isEmpty()){
                    Toast toast = Toast.makeText(CourseHomePage.this, "Review description can't be empty", Toast.LENGTH_LONG);
                    toast.show();
                }
                else {
                    try {
                        doPostReview("addReview.php", postReviewRating, postReviewDescription);
                        dialog.dismiss();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

    }
    @Generated
    public void createNewViewProfileDialog(){
        dialogBuilder = new AlertDialog.Builder(this);
        final View viewPopUp = LayoutInflater.from(this)
                .inflate(R.layout.view_profile_dialog, null);

        btnView = (Button) viewPopUp.findViewById(R.id.btnView);
        btnProfileCancel = (Button) viewPopUp.findViewById(R.id.btnViewCancel);

        dialogBuilder.setView(viewPopUp);
        dialog = dialogBuilder.create();
        dialog.show();

        btnView.setOnClickListener(new View.OnClickListener() {
            @Override
            @Generated
            public void onClick(View v) {
                INSTRUCTOR.USERNAME = extras.getString("username");
                Intent intent5 = new Intent(CourseHomePage.this,UserDetails.class);
                intent5.putExtra("userType","instructor");
                startActivity(intent5);
                dialog.dismiss();
            }
        });
        btnProfileCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            @Generated
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

    }
    @Generated
    private void doPostReview(String phpFile, float postReviewRating, String reviewDescription) throws IOException {
        OkHttpClient client = new OkHttpClient();

        HttpUrl.Builder urlBuilder = HttpUrl.parse("https://lamp.ms.wits.ac.za/~s2105624/" + phpFile).newBuilder();
        urlBuilder.addQueryParameter("studentNumber",USER.USERNAME);
        urlBuilder.addQueryParameter("courseCode", COURSE.CODE);
        urlBuilder.addQueryParameter("reviewRating", String.valueOf(postReviewRating));
        urlBuilder.addQueryParameter("reviewDescription", reviewDescription);
        String url = urlBuilder.build().toString();

        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            @Generated
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
            }

            @Override
            @Generated
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                final String responseData = response.body().string();
                CourseHomePage.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        updateRatings(String.valueOf(postReviewRating),reviewDescription);
                        overridePendingTransition(0, 0);
                        startActivity(getIntent());
                        overridePendingTransition(0, 0);
                        Toast toast = Toast.makeText(CourseHomePage.this, "Review posted", Toast.LENGTH_LONG);
                        toast.show();
                    }
                });
            }
        });
    }
    public void updateRatings(  String rating , String description){
        ReviewV reviewV = new ReviewV();
        reviewV.setReviewDescription(description);
        reviewV.setReviewRating(rating);
        reviewV.setStudentFName(USER.FNAME);
        reviewV.setStudentLName(USER.LNAME);
        listReviewVs.add(reviewV);
        float averageRating = 0;
        for(int i=0;i<listReviewVs.size();i++){
            averageRating = averageRating+Float.parseFloat(listReviewVs.get(i).getReviewRating());
        }
        COURSE.RATING = String.valueOf(averageRating/listReviewVs.size());
    }

    @Override
    @Generated
    public void onBackPressed(){
        if (browse){
            Intent intent = new Intent(CourseHomePage.this, BrowseCourses.class);
            startActivity(intent);
            finish();
        }else if (dashboard){
            Intent intent = new Intent(CourseHomePage.this, Dashboard.class);
            startActivity(intent);
            finish();
        }else{
            Intent intent = new Intent(CourseHomePage.this, MyCourses.class);
            startActivity(intent);
            finish();
        }
    }
}
