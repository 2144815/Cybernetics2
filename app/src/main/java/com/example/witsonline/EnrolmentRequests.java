package com.example.witsonline;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class EnrolmentRequests extends AppCompatActivity implements View.OnScrollChangeListener{

    //Creating a list of Courses
    private ArrayList<RequestV> listRequestVs;



    //CreatingViews
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;
    String webURL = "https://lamp.ms.wits.ac.za/home/s2105624/tutorRequestFeed.php?page=";

    //Volley Request Queue
    private RequestQueue requestQueue;

    //The request counter to send ?page=1, ?page=2 requests
    private int courseCount = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enrolment_requests);

        //Initializing Views
        recyclerView = (RecyclerView) findViewById(R.id.tutorEnrolmentRequestRecyclerVew);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        //Initializing our Course list
        listRequestVs = new ArrayList<>();
        requestQueue = Volley.newRequestQueue(this);

        //Calling method getData to fetch data
        getData();

        //Adding an scroll change listener to recyclerView
        recyclerView.setOnScrollChangeListener(this);

        //initializing our adapter
        adapter = new RequestAdapter(listRequestVs, this);

        //Adding adapter to recyclerview
        recyclerView.setAdapter(adapter);


    }
    //Request to get json from server we are passing an integer here
    //This integer will used to specify the page number for the request ?page = requestCount
    //This method would return a JsonArrayRequest that will be added to the request queue
    @Generated
    private JsonArrayRequest getDataFromServer(int requestCount) {
        //Initializing progressbar
        final ProgressBar progressBar = (ProgressBar) findViewById(R.id.tutorEnrolmentRequestProgressBar);

        //Displaying ProgressBar
        progressBar.setVisibility(View.VISIBLE);
        setProgressBarIndeterminateVisibility(true);

        //JsonArrayRequest of volley
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(webURL + String.valueOf(requestCount) + "&courseCode=" + COURSE.CODE,
                (response) -> {
                    //Calling method parseData to parse the json response
                    try {
                        parseData(response);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    //Hiding the progressBar
                    progressBar.setVisibility(View.GONE);
                    // This is when we got a response but it an empty array
                    if (listRequestVs.isEmpty()) {
                        TextView noRequests = (TextView) findViewById(R.id.tutorEnrolmentNoRequests);
                        noRequests.setVisibility(View.VISIBLE);
                    }

                },
                (error) -> {
                    progressBar.setVisibility(View.GONE);
                    //If an error occurs that means end of the list has been reached or we unable to get an courses
                    if (listRequestVs.isEmpty()) {
                        TextView noRequests = (TextView) findViewById(R.id.tutorEnrolmentNoRequests);
                        noRequests.setVisibility(View.VISIBLE);
                    }
                });
        //Returning the request
        return jsonArrayRequest;
    }

    //This method will get Data from the web api
    @Generated
    public void getData() {
        //Adding the method to the queue by calling the method getDatafromServer
        requestQueue.add(getDataFromServer(courseCount));
        //Incrementing the course counter
        courseCount++;
    }



    //This method will parse json Data
    @Generated
    private void parseData(JSONArray array) throws JSONException {
        for (int i = 0; i < array.length(); i++) {
            // Creating the Course object
            RequestV requestV = new RequestV();
            JSONObject json = null;
            try {
                //Getting json
                json = array.getJSONObject(i);

                //Adding data to the course object
                requestV.setStudentNumber(json.getString("studentNumber"));
                requestV.setStudentLName(json.getString("studentLName"));
                requestV.setStudentFName(json.getString("studentFName"));
                requestV.setCourseName(json.getString("courseName"));
                requestV.setCourseCode(json.getString("courseCode"));
                requestV.setStudentImageUrl(json.getString("studentImage"));

                listRequestVs.add(requestV);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            //Adding the request object to the list
            //    if (json.getString("courseInstructor").equals("richard.klein")) {
            adapter.notifyDataSetChanged();
            //     }

            /*
            if ((USER.COURSES != null) && (USER.COURSES.contains(json.getString("courseCode")))) {
                listCourseVs.add(courseV);
                adapter.notifyDataSetChanged();
            }
            */

            //listCourseVs.add(courseV);

            //Notifying the adapter that data has been added or changed
            //adapter.notifyDataSetChanged();
        }
    }

    //This method will check if the recyclerview has reached the bottom or not
    public boolean isLastItemDisplaying(RecyclerView recyclerView) {
        if (recyclerView.getAdapter().getItemCount() != 0) {
            int lastVisibleItemPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastCompletelyVisibleItemPosition();
            if (lastVisibleItemPosition != RecyclerView.NO_POSITION && lastVisibleItemPosition == recyclerView.getAdapter().getItemCount() - 1) {
                return true;
            }
        }
        return false;
    }


    @Override
    @Generated
    public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
        //if Scrolled at last then
        if (isLastItemDisplaying(recyclerView)) {
            //Calling the method getData again
            getData();
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(EnrolmentRequests.this , CourseHomePage.class);
        startActivity(intent);
        super.onBackPressed();
    }
}