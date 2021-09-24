package com.example.witsonline;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.style.TtsSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.resources.TextAppearance;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ForumActivity extends AppCompatActivity implements  View.OnScrollChangeListener{


    //Creating a list of Courses
    private ArrayList<Discussion> listDiscussions;

    //For storing the course tutors
    String tutorListURL = "https://lamp.ms.wits.ac.za/home/s2105624/getTutors.php?courseCode=";

    //progress bar for entire page
    private ProgressBar progressBar;
    private RelativeLayout relativeLayout;

    //CreatingViews
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;
    String webURL = "https://lamp.ms.wits.ac.za/home/s2105624/getDiscussions.php?page=";
    private String votesUrl = "https://lamp.ms.wits.ac.za/home/s2105624/getVotes.php";
    private String  votesInstUrl = "https://lamp.ms.wits.ac.za/home/s2105624/getVotesInstructor.php";
    //Volley Request Queue
    private RequestQueue requestQueue;
    private  Button startDiscussion;

    //for determining whether a student is a tutor or not
    String tutorURL = "https://lamp.ms.wits.ac.za/home/s2105624/getTutorState.php?studentNumber=";

    //TextViews
    private TextView topic;
    private TextView text;

    private boolean browse = false;
    private boolean mycourses = false;
    private boolean dashboard = false;

    Bundle extras;

    private Button postdiscussion;
    //The request counter to send ?page=1, ?page=2 requests
    private int discussionCount = 1;
    @Override
    @Generated
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_forum);
        super.onCreate(savedInstanceState);
        TextView courseCode =(TextView)findViewById(R.id.courseCode);
        courseCode.setText(COURSE.CODE);

        //initialising progress bar and relative layour for entire page
        progressBar = findViewById(R.id.ForumProgressBar);
        relativeLayout = findViewById(R.id.ForumRelLayout);

        Log.d("HERE","landed");
        //Initializing Views
        recyclerView = (RecyclerView)findViewById(R.id.discussionRecyclerView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        startDiscussion = (Button)findViewById( R.id.startDiscussion );
        USER.INSTRUCTOR_VOTES.clear();
        USER.VOTES.clear();

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
        //Initializing our Course list
        listDiscussions = new ArrayList<>();
        requestQueue = Volley.newRequestQueue(this);
        //Calling methods to get data from server
        //getTutorData();
        try {
            getCourseTutors();
        } catch (IOException e) {
            e.printStackTrace();
        }
        getVotesData();
        getInstVotesData();
        getData();
        //getTutorStateData();
        try {
            checkTutorState();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //Adding an scroll change listener to recyclerView
        recyclerView.setOnScrollChangeListener(this);

        //initializing our adapter
        adapter = new DiscussionCardAdapter(listDiscussions, this);

        //Adding adapter to recyclerview's
        recyclerView.setAdapter(adapter);
        if(!USER.STUDENT){
            startDiscussion.setVisibility(View.GONE);
        }
        startDiscussion.setOnClickListener(new View.OnClickListener() {
            @Override
            @Generated
            public void onClick(View view) {
                createNewViewDialogDiscussion();
            }
        });

    }


    @Generated
    private JsonArrayRequest getDataFromServer(int requestCount){
        //Initializing progressbar for the dicussions. The other one is for the page due to the header changing for students
        // or tutors and instructors
        final ProgressBar loadProgressBar = (ProgressBar) findViewById(R.id.discussionProgressBar);

        //Displaying ProgressBar
        loadProgressBar.setVisibility(View.VISIBLE);
        setProgressBarIndeterminateVisibility(true);

        //JsonArrayRequest of volley
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(webURL + String.valueOf(requestCount) + "&courseCode=" + COURSE.CODE,
                (response) -> {
                    //Calling method parseData to parse the json responce
                    parseData(response);
                    //Hiding the progressBar
                    if(listDiscussions.isEmpty()){
                        TextView noDiscussions = (TextView)findViewById(R.id.noDiscussionItems);
                        noDiscussions.setVisibility(View.VISIBLE);
                    }
                    relativeLayout.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                    loadProgressBar.setVisibility(View.GONE);
                },
                (error) -> {
                    //If an error occurs that means end of the list has been reached

                    if(listDiscussions.isEmpty()){
                        TextView noDiscussions = (TextView)findViewById(R.id.noDiscussionItems);
                        noDiscussions.setVisibility(View.VISIBLE);
                    }
                    relativeLayout.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                    loadProgressBar.setVisibility(View.GONE);
                    //Toast.makeText(CourseHomePage.this, "No More Items Available", Toast.LENGTH_SHORT).show();
                });
        return jsonArrayRequest;
    }

    //This method will get Data from the web api
    @Generated
    private void getData(){
        //Adding the method to the queue by calling the method getDatafromServer
        requestQueue.add(getDataFromServer(discussionCount));
        //Incrementing the course counter
        discussionCount++;
    }
    @Generated
    private void parseData(JSONArray array){
        for (int i = 0; i< array.length(); i++){
            // Creating the Course object
            Discussion discussion = new Discussion();
            JSONObject json = null;
            try {
                //Getting json
                json = array.getJSONObject(i);
                //Adding data to the course object
                String fullName = "";
                discussion.setDiscussionID(json.getString("discussionID"));
                discussion.setDiscussionTopic(json.getString("discussionTopic"));
                fullName = fullName + json.getString("discussionStudentFName");
                fullName = fullName + " " + json.getString("discussionStudentLName");
                discussion.setDiscussionStudent(fullName);
                discussion.setDiscussionStatus(Integer.parseInt(json.getString("discussionStatus")));
                discussion.setDiscussionReplies(Integer.parseInt(json.getString("discussionReplies")));
                discussion.setDiscussionText(json.getString("discussionText"));
                discussion.setDiscussionStudentNumber(json.getString("discussionStudentNumber"));
                String date = json.getString("discussionTime");
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date dtDate = format.parse(date);
                discussion.setDiscussionDate(dtDate);
                Log.d("HERE",discussion.getDiscussionText());

            } catch (JSONException | ParseException e){
                e.printStackTrace();
                Log.d("HERE","error occured");

            }
            //Adding the request object to the list
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
            //Log.d("HERE",format.format(discussion.getDiscussionDate()));
            listDiscussions.add(discussion);
        }
        //Notifying the adapter that data has been added or changed
        adapter.notifyDataSetChanged();
    }

    //This is the dialog for adding new discussions in the forum
    @Generated
    public void createNewViewDialogDiscussion(){
        AlertDialog.Builder dialogBuilder= new AlertDialog.Builder(this);
        final View viewPopUp = LayoutInflater.from(this)
                .inflate(R.layout.discussion_dialog, null);

        dialogBuilder.setView(viewPopUp);
        AlertDialog dialog = dialogBuilder.create();
        dialog.show();

        postdiscussion = (Button) viewPopUp.findViewById(R.id.postDiscussion);
        topic = (TextView) viewPopUp.findViewById(R.id.discussionTopic);
        text = (TextView) viewPopUp.findViewById(R.id.discussionText);


        postdiscussion.setOnClickListener(new View.OnClickListener() {
            @Override
            @Generated
            public void onClick(View v) {
                String time = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss").format(Calendar.getInstance().getTime());
                String discussionTopic = topic.getText().toString();
                String discussionText = text.getText().toString();
                Log.d("HERE", time);
                if(discussionTopic.isEmpty()){
                    Toast toast = Toast.makeText(ForumActivity.this, "Topic can't be empty", Toast.LENGTH_LONG);
                    toast.show();
                }
                if(discussionText.isEmpty()){
                    Toast toast = Toast.makeText(ForumActivity.this, "Please enter a message to start the discussion", Toast.LENGTH_LONG);
                    toast.show();
                }
                if(!discussionText.isEmpty() && !discussionTopic.isEmpty()) {
                    try {
                        addDiscussion("addDiscussion.php", discussionTopic, discussionText, time);
                        dialog.dismiss();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });


    }
    //This function adds the discussion the database
    @Generated
    private void addDiscussion (String phpFile, String topic , String text , String time) throws IOException {
        OkHttpClient client = new OkHttpClient();
        HttpUrl.Builder urlBuilder = HttpUrl.parse("https://lamp.ms.wits.ac.za/~s2105624/" + phpFile).newBuilder();
        urlBuilder.addQueryParameter("studentNumber",USER.USERNAME);
        urlBuilder.addQueryParameter("courseCode", COURSE.CODE);
        urlBuilder.addQueryParameter("topic", topic);
        urlBuilder.addQueryParameter("text", text);
        urlBuilder.addQueryParameter("time", time);
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
                ForumActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    @Generated
                    public void run() {
                        Log.d("HERE",responseData.toString());
                        if(responseData.trim().equals("Successful")) {
                            Toast toast = Toast.makeText(ForumActivity.this, responseData, Toast.LENGTH_LONG);
                            toast.show();
                            Intent intent = new Intent(ForumActivity.this, ForumActivity.class);
                            startActivity(intent);
                            finish();
                        }
                        else{
                            Toast toast = Toast.makeText(ForumActivity.this, "Couldn't post your discussion ", Toast.LENGTH_LONG);
                            toast.show();
                        }
                    }
                });
            }
        });
    }


   /*
    @Generated
    private void getTutorStateData(){
        requestQueue.add(getTutorStateDataFromServer());
    }

    @Generated
    private JsonArrayRequest getTutorStateDataFromServer(){
        //JsonArrayRequest of volley
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(tutorURL + USER.USER_NUM + "&courseCode=" + COURSE.CODE,
                (response) -> {
                    //Calling method parseData to parse the json responce
                    parseTutorStateData(response);

                },
                (error) -> {
                    //Toast.makeText(CourseHomePage.this, "No More Items Available", Toast.LENGTH_SHORT).show();
                });
        //Returning the request
        return jsonArrayRequest;
    }

    //This method will parse json Data
    @Generated
    private void parseTutorStateData(JSONArray array){
        for (int i = 0; i< array.length(); i++){

            JSONObject json = null;
            try {
                //Getting json
                json = array.getJSONObject(i);

                //Adding data to the course object
                int tutorState = json.getInt("Tutor");
                //Toast.makeText(this, ""+tutorState, Toast.LENGTH_LONG).show();

                if (tutorState == 1){
                    //if student is a tutor, they can't post discussions
                    startDiscussion.setVisibility(View.GONE);
                }


            } catch (JSONException e){
                e.printStackTrace();
            }

        }
    }

    //This method will get Data from the web api
    @Generated
    private void getTutorData() {
        //Adding the method to the queue by calling the method getDatafromServer
        requestQueue.add(getTutorDataFromServer());
    }

    @Generated
    private JsonArrayRequest getTutorDataFromServer() {

        //JsonArrayRequest of volley
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(tutorListURL + COURSE.CODE,
                (response) -> {
                    //Calling method parseData to parse the json responce
                    parseTutorData(response);

                },
                (error) -> {
                    //If an error occurs that means end of the list has been reached
                    //Toast.makeText(CourseHomePage.this, "No More Items Available", Toast.LENGTH_SHORT).show();
                });
        return jsonArrayRequest;
    }
    @Generated
    public void parseTutorData(JSONArray array) {

        for (int i = 0; i < array.length(); i++) {

            JSONObject json = null;
            try {
                //Getting json
                json = array.getJSONObject(i);

                //Adding data to the course object
                String studentNumber = json.getString("Enrolment_Student");
                COURSE.TUTORS.add(studentNumber);


            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

   */

    @Generated
    private void checkTutorState() throws IOException {
        OkHttpClient client = new OkHttpClient();

        HttpUrl.Builder urlBuilder = HttpUrl.parse("https://lamp.ms.wits.ac.za/~s2105624/getTutorState.php").newBuilder();
        urlBuilder.addQueryParameter("studentNumber", USER.USER_NUM);
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
                ForumActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    @Generated
                    public void run() {
                        try {
                            JSONArray all = new JSONArray(responseData);
                            for (int i = 0; i < all.length();i++){
                                JSONObject obj = all.getJSONObject(i);
                                //Adding data to the course object
                                int tutorState = obj.getInt("Tutor");
                                //Toast.makeText(this, ""+tutorState, Toast.LENGTH_LONG).show();

                                if (tutorState == 1){
                                    //if student is a tutor, they can't post discussions
                                    startDiscussion.setVisibility(View.GONE);
                                }

                            }

                        }
                        catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }

    private void getVotesData() {
        requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(getVotesDataFromServer());
    }

    @Generated
    private JsonArrayRequest getVotesDataFromServer() {
        //JsonArrayRequest of volley
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(votesUrl + "?username="+USER.USERNAME,
                (response) -> {
                    //Calling method parseData to parse the json responce
                    parseVotesData(response);


                },
                (error) -> {
                    //Toast.makeText(CourseHomePage.this, "No More Items Available", Toast.LENGTH_SHORT).show();
                });
        //Returning the request
        return jsonArrayRequest;
    }

    //This method will parse json Data
    @Generated
    private void parseVotesData(JSONArray array) {
        JSONObject json = null;
        for(int i=0;i<array.length();i++){
            try {
                //Getting json
                json = array.getJSONObject(i);
                //Adding data to the course object
                USER.VOTES.put(json.getString("Vote_ReplyId"), json.getInt("Vote_Int"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void getInstVotesData() {
        requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(getInstVotesDataFromServer());
    }

    @Generated
    private JsonArrayRequest getInstVotesDataFromServer() {
        //JsonArrayRequest of volley
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(votesInstUrl + "?username="+USER.USERNAME,
                (response) -> {
                    //Calling method parseData to parse the json responce
                    parseInstVotesData(response);

                },
                (error) -> {
                    //Toast.makeText(CourseHomePage.this, "No More Items Available", Toast.LENGTH_SHORT).show();
                });
        //Returning the request
        return jsonArrayRequest;
    }

    //This method will parse json Data
    @Generated
    private void parseInstVotesData(JSONArray array) {
        JSONObject json = null;
        for(int i=0;i<array.length();i++){
            try {
                //Getting json
                json = array.getJSONObject(i);
                //Adding data to the course object
                USER.INSTRUCTOR_VOTES.put(json.getString("Vote_ReplyId"), json.getInt("Vote_Int"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Generated
    private void getCourseTutors() throws IOException {
        OkHttpClient client = new OkHttpClient();

        HttpUrl.Builder urlBuilder = HttpUrl.parse("https://lamp.ms.wits.ac.za/~s2105624/getTutors.php").newBuilder();
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
                ForumActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    @Generated
                    public void run() {
                        try {
                            JSONArray all = new JSONArray(responseData);
                            for (int i = 0; i < all.length();i++){
                                JSONObject obj = all.getJSONObject(i);
                                //Adding data to the course object
                                String studentNumber = obj.getString("Enrolment_Student");
                                COURSE.TUTORS.add(studentNumber);
                            }
                        }
                        catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }

    //This method will check if the recyclerview has reached the bottom or not
    public boolean isLastItemDisplaying(RecyclerView recyclerView){
        if(recyclerView.getAdapter().getItemCount() != 0){
            int lastVisibleItemPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastCompletelyVisibleItemPosition();
            if (lastVisibleItemPosition != RecyclerView.NO_POSITION && lastVisibleItemPosition == recyclerView.getAdapter().getItemCount() -1){
                return true;
            }
        }
        return false;
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
    public void onBackPressed(){
        Intent intent = new Intent(ForumActivity.this, CourseHomePage.class);
        Intent intent2 = new Intent(ForumActivity.this, CourseHomePageInstructor.class);
        if(USER.STUDENT) {
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
            }
        else{
            startActivity(intent2);
        }
        finish();
    }
}
