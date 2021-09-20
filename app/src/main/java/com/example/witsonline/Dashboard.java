package com.example.witsonline;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Objects;


public class Dashboard extends AppCompatActivity implements View.OnScrollChangeListener, BottomNavigationView.OnNavigationItemSelectedListener {
    TextView name;
    //This is for the delay while loading user full name
    ProgressBar progressBarPage;
    private RelativeLayout relativeLayout; // for the entire page
    private RelativeLayout relativeLayoutName;

    //Creating a list of Requests
    private ArrayList<RequestV> listRequestVs;

    //Volley Request Queue
    private RequestQueue requestQueue4request;

    //The request counter to send ?page=1, ?page=2 requests
    private int requestsCount = 1;

    //CreatingViews
    private RecyclerView requestRecyclerView;
    private RecyclerView.LayoutManager requestLayoutManager;
    private RecyclerView.Adapter requestAdapter;
    String webURL = "https://lamp.ms.wits.ac.za/home/s2105624/requestFeed.php?page=";

    //delay for loading featured courses
    private LinearLayout featuredCourses;  // for the featured course


    //This is for the logout pop up menu
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private Button btnLogout, btnCancel;

    //Creating Views
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;

    //to check if student is subscribed to featured course
    private RequestQueue requestQueue;
    String subURL = "https://lamp.ms.wits.ac.za/~s2105624/checkFeatCourses.php?studentNumber=";


    String URL = "https://lamp.ms.wits.ac.za/home/s2105624/";

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onBackPressed() {

    }

    @SuppressLint("SetTextI18n")
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    @Generated
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        name = findViewById(R.id.textViewStudentName);

        //display the user's name and surname
        getName(USER.USER_NUM);

        //Initializing progressbar
        progressBarPage = findViewById(R.id.dashboardProgressBar);
        progressBarPage.setVisibility(View.VISIBLE);

        relativeLayout = findViewById(R.id.DashboardLayout);
        relativeLayoutName = findViewById(R.id.dashboardRelLayout);

        //Initializing Views
        requestRecyclerView = (RecyclerView) findViewById(R.id.requestRecyclerView);
        requestRecyclerView.setHasFixedSize(true);
        requestLayoutManager = new LinearLayoutManager(this);
        requestRecyclerView.setLayoutManager(requestLayoutManager);

        //Initializing our Course list
        listRequestVs = new ArrayList<>();
        requestQueue4request = Volley.newRequestQueue(this);

        //Adding an scroll change listener to recyclerView
        requestRecyclerView.setOnScrollChangeListener(this);

        //initializing our adapters
        requestAdapter = new RequestAdapter(listRequestVs, this);

        //Adding adapter to recyclerview's
        requestRecyclerView.setAdapter(requestAdapter);

        //NO ACTION BAR ON THIS ACTIVITY
        Objects.requireNonNull(getSupportActionBar()).hide();

        BottomNavigationView dashboardBottomNavigation = findViewById(R.id.dashboardBottomNavigation);
        dashboardBottomNavigation.setOnNavigationItemSelectedListener(Dashboard.this);

        featuredCourses = findViewById(R.id.dashboardFeaturedCourses);
        if (USER.STUDENT) {
            dashboardBottomNavigation.inflateMenu(R.menu.menu_student);
            dashboardBottomNavigation.getMenu().findItem(R.id.menuHomeStudent).setChecked(true);

            String getFeaturedCoursesMethod = "getFeaturedCourses";
            ArrayList<CourseV> courses = new ArrayList<CourseV>();
            getFeaturedCourses(URL, getFeaturedCoursesMethod, courses);

            final ProgressBar progressBar = (ProgressBar) findViewById(R.id.requestProgressBar);
            progressBar.setVisibility(View.GONE);
        } else {
            dashboardBottomNavigation.inflateMenu(R.menu.menu_instructor);
            dashboardBottomNavigation.getMenu().findItem(R.id.menuHomeInstructor).setChecked(true);

            featuredCourses.setVisibility(LinearLayout.VISIBLE);
            TextView topic = findViewById(R.id.txtFeaturedCourses);
            topic.setText("Course Enrolment Requests");
            getData();
        }

    }

    //Request to get json from server we are passing an integer here
    //This integer will used to specify the page number for the request ?page = requestCount
    //This method would return a JsonArrayRequest that will be added to the request queue
    @Generated
    private JsonArrayRequest getDataFromServer(int requestCount) {
        //Initializing progressbar
        final ProgressBar progressBar = (ProgressBar) findViewById(R.id.requestProgressBar);

        //Displaying ProgressBar
        progressBar.setVisibility(View.VISIBLE);
        setProgressBarIndeterminateVisibility(true);

        //JsonArrayRequest of volley
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(webURL + String.valueOf(requestCount) + "&instructorCode=" + USER.USER_NUM,
                (response) -> {
                    //Calling method parseData to parse the json responce
                    try {
                        parseData(response);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    //Hiding the progressBar
                    progressBar.setVisibility(View.GONE);
                    // This is when we got a response but it an empty array
                    if (listRequestVs.isEmpty()) {
                        TextView topic = findViewById(R.id.txtFeaturedCourses);
                        topic.setText("No Enrolment Requests Available");
                    }

                    relativeLayout.setVisibility(View.VISIBLE);
                    relativeLayoutName.setVisibility(View.VISIBLE);
                    progressBarPage.setVisibility(View.GONE);

                },
                (error) -> {
                    progressBar.setVisibility(View.GONE);
                    //If an error occurs that means end of the list has been reached or we unable to get an courses
                    if (listRequestVs.isEmpty()) {
                        TextView topic = findViewById(R.id.txtFeaturedCourses);
                        topic.setText("No Enrolment Requests Available");
                    }
                    //else {
                    //Toast.makeText(Dashboard.this, "No More Items Available", Toast.LENGTH_SHORT).show();
                    //}

                    relativeLayout.setVisibility(View.VISIBLE);
                    relativeLayoutName.setVisibility(View.VISIBLE);
                    progressBarPage.setVisibility(View.GONE);
                });
        //Returning the request
        return jsonArrayRequest;
    }

    //This method will get Data from the web api
    @Generated
    public void getData() {
        //Adding the method to the queue by calling the method getDatafromServer
        requestQueue4request.add(getDataFromServer(requestsCount));
        //Incrementing the course counter
        requestsCount++;
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

                listRequestVs.add(requestV);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            //Adding the request object to the list
            //    if (json.getString("courseInstructor").equals("richard.klein")) {

            requestAdapter.notifyDataSetChanged();
            //     }

        }
    }

    @Generated
    public void createNewViewDialog() {
        dialogBuilder = new AlertDialog.Builder(this);
        final View viewPopUp = LayoutInflater.from(this)
                .inflate(R.layout.logout_dialog, null);

        btnLogout = (Button) viewPopUp.findViewById(R.id.btnLogout);
        btnCancel = (Button) viewPopUp.findViewById(R.id.btnLogoutCancel);

        dialogBuilder.setView(viewPopUp);
        dialog = dialogBuilder.create();
        dialog.show();

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            @Generated
            public void onClick(View v) {
                Intent intent5 = new Intent(Dashboard.this, LoginActivity.class);
                startActivity(intent5);
                finish();
                dialog.dismiss();
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
    private void getName(String user) {
        String URL = "https://lamp.ms.wits.ac.za/home/s2105624/";

        String getStudentNameMethod = "getStudentName";
        String getInstructorNameMethod = "getInstructorName";

        if (USER.STUDENT) {
            PHPRequestBuilder requestBuilder = new PHPRequestBuilder(URL, getStudentNameMethod);

            ArrayList<String> parameter = new ArrayList<String>();
            parameter.add("Student_Number");
            parameter.add(user);

            ArrayList<ArrayList<String>> Parameters = new ArrayList<ArrayList<String>>();
            Parameters.add(parameter);

            requestBuilder.doBuild(Parameters);
            requestBuilder.doRequest(Dashboard.this, response -> addName(response));
            getSubData();
        } else {
            PHPRequestBuilder requestBuilder = new PHPRequestBuilder(URL, getInstructorNameMethod);

            ArrayList<String> parameter = new ArrayList<String>();
            parameter.add("Instructor_Username");
            parameter.add(user);

            ArrayList<ArrayList<String>> Parameters = new ArrayList<ArrayList<String>>();
            Parameters.add(parameter);

            requestBuilder.doBuild(Parameters);
            requestBuilder.doRequest(Dashboard.this, response -> addName(response));
        }
    }

    @Generated
    private void addName(String JSON) throws JSONException {
        JSONObject NAMES = new JSONObject(JSON);

        String FName;
        String LName;

        if (USER.STUDENT) {
            FName = NAMES.getString("Student_FName");
            LName = NAMES.getString("Student_LName");
        } else {
            FName = NAMES.getString("Instructor_FName");
            LName = NAMES.getString("Instructor_LName");
        }

        USER.FNAME = FName;
        USER.LNAME = LName;

        name.setText(FName + " " + LName);
    }

    @Generated
    private void displayFeaturedCourses() {
        //Initializing Views
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        //Initializing our Course list
        //Creating a list of Courses
        if (USER.FEATURED_COURSES != null) {
            ArrayList<CourseV> listCourseVs = USER.FEATURED_COURSES;

            //Adding an scroll change listener to recyclerView
            recyclerView.setOnScrollChangeListener(this);

            //initializing our adapter
            adapter = new CourseCardAdapter(listCourseVs, this);

            //Adding adapter to recyclerview
            recyclerView.setAdapter(adapter);
        }
        featuredCourses.setVisibility(LinearLayout.VISIBLE);

    }

    @Override
    @Generated
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            // STUDENT MENU
            case R.id.menuHomeStudent:
                break;

            case R.id.menuMyCoursesStudent:
                Intent intent = new Intent(Dashboard.this, MyCourses.class);
                startActivity(intent);
                finish();
                break;

            case R.id.menuBrowseCourses:
                Intent intent1 = new Intent(Dashboard.this, BrowseCourses.class);
                startActivity(intent1);
                finish();
                break;

            case R.id.menuLogOutStudent:
                createNewViewDialog();
                break;

            case R.id.menuHomeInstructor:
                break;

            case R.id.menuMyCoursesInstructor:
                Intent intent3 = new Intent(Dashboard.this, MyCourses.class);
                startActivity(intent3);
                finish();
                break;

            case R.id.menuCreateCourse:
                Intent intent4 = new Intent(Dashboard.this, CreateCourse.class);
                startActivity(intent4);
                finish();
                break;

            case R.id.menuLogOutInstructor:
                createNewViewDialog();
                break;

            case R.id.menuProfileStudent:
                Intent intent5 = new Intent(Dashboard.this, EditProfile.class);
                startActivity(intent5);
                finish();
                break;

            case R.id.menuProfileInstructor:
                Intent intent6 = new Intent(Dashboard.this, EditProfile.class);
                startActivity(intent6);
                finish();
                break;
        }

        return false;
    }

    //This method will check if the recyclerview has reached the bottom or not
    public boolean isLastItemDistplaying(RecyclerView recyclerView) {
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
        if (isLastItemDistplaying(requestRecyclerView)) {
            //Calling the method getData again
            getData();
        }
        //if(isLastItemDistplaying(requestRecyclerView)){
        //  getData();
        //}
    }

    @Generated
    private void getFeaturedCourses(String URL, String method, ArrayList<CourseV> courses) {
        PHPRequestBuilder requestBuilder = new PHPRequestBuilder(URL, method);
        requestBuilder.doRequest(Dashboard.this, new ResponseHandler() {
            @Override
            @Generated
            public void processResponse(String response) throws JSONException {
                setFeaturedCourses(response, courses);
            }
        });

    }

    @Generated
    private void setFeaturedCourses(String JSON, ArrayList<CourseV> courses) throws JSONException {
        JSONArray featuredCourses = new JSONArray(JSON);
        for (int index = 0; index < featuredCourses.length(); index++) {
            JSONObject featuredCourse = featuredCourses.getJSONObject(index);
            CourseV course = new CourseV();

            course.setCourseCode(featuredCourse.getString("Course_Code"));
            course.setCourseName(featuredCourse.getString("Course_Name"));
            course.setCourseDescription(featuredCourse.getString("Course_Description"));
            course.setCourseOutline(featuredCourse.getString("Course_Outline"));
            course.setImageUrl(featuredCourse.getString("Course_Image"));
            course.setCourseRating(featuredCourse.getString("Course_Rating"));
            course.setCourseVisibility(featuredCourse.getString("Course_Visibility"));
            course.setCourseInstructor(featuredCourse.getString("Course_Instructor"));
            String instName = "";
            instName += featuredCourse.getString("Instructor_FName") + " ";
            instName += featuredCourse.getString("Instructor_LName");
            course.setInstName(instName);

            courses.add(course);
        }

        USER.FEATURED_COURSES = courses;
        displayFeaturedCourses();
    }

    @Generated
    private void getSubData() {
        requestQueue = Volley.newRequestQueue(Dashboard.this);
        requestQueue.add(getSubDataFromServer());
    }

    @Generated
    private JsonArrayRequest getSubDataFromServer() {
        //JsonArrayRequest of volley
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(subURL + USER.USER_NUM,
                (response) -> {
                    //Calling method parseData to parse the json responce
                    parseSubData(response);

                },
                (error) -> {
                    //Toast.makeText(CourseHomePage.this, "No More Items Available", Toast.LENGTH_SHORT).show();
                });
        //Returning the request
        return jsonArrayRequest;
    }

    //This method will parse json Data
    @Generated
    private void parseSubData(JSONArray array) {
        if (USER.SUBSCRIBED_TO_FEAT_COURSE != null) {
            USER.SUBSCRIBED_TO_FEAT_COURSE.clear();
        } else {
            USER.SUBSCRIBED_TO_FEAT_COURSE = new HashSet<>();
        }

        for (int i = 0; i< array.length(); i++){
            // Creating the Course object
            JSONObject json = null;
            try {
                //Getting json
                json = array.getJSONObject(i);
                String course = json.getString("Enrolment_Course");
                USER.SUBSCRIBED_TO_FEAT_COURSE.add(course);
                //Toast.makeText(Dashboard.this, course+"subscribed", Toast.LENGTH_SHORT).show();

            } catch (JSONException e){
                e.printStackTrace();
            }
        }

        relativeLayout.setVisibility(View.VISIBLE);
        relativeLayoutName.setVisibility(View.VISIBLE);
        progressBarPage.setVisibility(View.GONE);


        /*
        if (array.toString().equals("[]")) {
            USER.SUBSCRIBED_TO_FEAT_COURSE.put(courseCode, "false");
            //Toast.makeText(Dashboard.this, "not subscribed", Toast.LENGTH_SHORT).show();
        } else {
            USER.SUBSCRIBED_TO_FEAT_COURSE.put(courseCode, "true");
            //Toast.makeText(Dashboard.this, "subscribed", Toast.LENGTH_SHORT).show();
        }

         */


    }
}