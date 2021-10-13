package com.example.witsonline;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class QuizActivity extends AppCompatActivity implements View.OnScrollChangeListener {

    //this page will also be used for students so we need these variables
    private boolean browse = false;
    private boolean mycourses = false;
    private boolean dashboard = false;
    Bundle extras;

    //page views
    private TextView courseCode;
    private Button createQuiz;

    //create quiz dialog
    private Button dialogCreateQuiz;
    private Button cancel;
    private TextInputLayout dialogQuizName;

    //getting the quizzes
    //CreatingViews
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;
    String webURL = "https://lamp.ms.wits.ac.za/home/s2105624/quizFeed.php?page=";
    //Volley Request Queue
    private RequestQueue requestQueue;
    //The request counter to send ?page=1, ?page=2 requests
    private int quizCount = 1;
    //Creating a list of Courses
    private ArrayList<QuizV> listQuizVs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        //which activity we came from before course home page
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


        //Initializing Views
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        //Initializing our Course list
        listQuizVs = new ArrayList<>();
        requestQueue = Volley.newRequestQueue(this);

        //Calling method getData to fetch data
        getData();

        //Adding an scroll change listener to recyclerView
        recyclerView.setOnScrollChangeListener(QuizActivity.this);

        //initializing our adapter
        adapter = new QuizCardAdapter(listQuizVs, this);

        //Adding adapter to recyclerview
        recyclerView.setAdapter(adapter);


        //heading
        courseCode =(TextView)findViewById(R.id.quizCourseCode);
        courseCode.setText(COURSE.CODE);

        //create quiz
        createQuiz = findViewById(R.id.btnCreateQuiz);
        if(USER.STUDENT){
            createQuiz.setVisibility(View.GONE);
        }
        createQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNewQuizDialog();
            }
        });

    }


    //This method will get Data from the web api
    @Generated
    public void getData() {
        //Adding the method to the queue by calling the method getDatafromServer
        requestQueue.add(getDataFromServer(quizCount));
        //Incrementing the course counter
        quizCount++;
    }

    //This method would return a JsonArrayRequest that will be added to the request queue
    @Generated
    private JsonArrayRequest getDataFromServer(int requestCount) {
        //Initializing progressbar
        final ProgressBar progressBar = (ProgressBar) findViewById(R.id.quizProgressBar);

        //Displaying ProgressBar
        progressBar.setVisibility(View.VISIBLE);
        setProgressBarIndeterminateVisibility(true);

        //JsonArrayRequest of volley
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(webURL + String.valueOf(requestCount) + "&courseCode=" + COURSE.CODE,
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
                    if (listQuizVs.isEmpty()) {
                        TextView noCourses = (TextView) findViewById(R.id.noCourseItems);
                        noCourses.setVisibility(View.VISIBLE);
                    }

                },
                (error) -> {
                    progressBar.setVisibility(View.GONE);
                    //If an error occurs that means end of the list has been reached or we unable to get an courses
                    if (listQuizVs.isEmpty()) {
                        TextView noQuizzes = (TextView) findViewById(R.id.noQuizItems);
                        noQuizzes.setVisibility(View.VISIBLE);
                    } else {
                        Toast.makeText(QuizActivity.this, "No More Items Available", Toast.LENGTH_SHORT).show();
                    }
                });
        //Returning the request
        return jsonArrayRequest;
    }


    //This method will parse json Data
    @Generated
    private void parseData(JSONArray array) throws JSONException {
        for (int i = 0; i < array.length(); i++) {
            // Creating the Quiz object
            QuizV quizV = new QuizV();
            JSONObject json = null;
            try {
                //Getting json
                json = array.getJSONObject(i);

                //Adding data to the quiz object
                quizV.setQuizID(json.getString("quizID"));
                quizV.setQuizName(json.getString("quizName"));
                quizV.setQuizMarkAlloc(json.getInt("quizMarkAlloc"));
                quizV.setQuizNumQuestions(json.getInt("quizNumQuestions"));
                quizV.setQuizVisibility(json.getInt("quizVisibility"));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            listQuizVs.add(quizV);
            adapter.notifyDataSetChanged();
        }
    }


    //This is the dialog for adding new discussions in the forum
    @Generated
    public void createNewQuizDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        final View viewPopUp = LayoutInflater.from(this)
                .inflate(R.layout.create_quiz_dialog, null);

        dialogBuilder.setView(viewPopUp);
        AlertDialog dialog = dialogBuilder.create();
        dialog.show();

        dialogCreateQuiz = (Button) viewPopUp.findViewById(R.id.dialogCreateQuiz);
        cancel = (Button) viewPopUp.findViewById(R.id.cancelQuizCreation);
        dialogQuizName = (TextInputLayout) viewPopUp.findViewById(R.id.dialogQuizName);

        dialogCreateQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            @Generated
            public void onClick(View v) {
                String quizName = dialogQuizName.getEditText().getText().toString().trim();
                if (quizName.isEmpty()){
                    dialogQuizName.setError("Quiz name can't be empty");
                }
                else{
                    dialogQuizName.setError(null);

                    try {
                        addQuiz("addQuiz.php",quizName);
                        dialog.dismiss();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    //This function adds the discussion the database
    @Generated
    private void addQuiz (String phpFile, String quizName) throws IOException {
        OkHttpClient client = new OkHttpClient();
        HttpUrl.Builder urlBuilder = HttpUrl.parse("https://lamp.ms.wits.ac.za/~s2105624/" + phpFile).newBuilder();
        urlBuilder.addQueryParameter("courseCode", COURSE.CODE);
        urlBuilder.addQueryParameter("quizName", quizName);
        urlBuilder.addQueryParameter("markAlloc","0");
        urlBuilder.addQueryParameter("numQuestions", "0");
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
                QuizActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    @Generated
                    public void run() {
                        Log.d("HERE",responseData.toString());
                        if(responseData.trim().equals("Successful")) {
                            Toast toast = Toast.makeText(QuizActivity.this,"Successful", Toast.LENGTH_LONG);
                            toast.show();
                            Intent intent = new Intent(QuizActivity.this, QuizActivity.class);
                            startActivity(intent);
                            finish();
                        }
                        else{
                            Toast toast = Toast.makeText(QuizActivity.this, "Couldn't add your quiz ", Toast.LENGTH_LONG);
                            toast.show();
                        }
                    }
                });
            }
        });
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
        if (isLastItemDistplaying(recyclerView)) {
            //Calling the method getData again
            getData();
        }
    }

    @Generated
    public void onBackPressed(){
        Intent intent = new Intent(QuizActivity.this, CourseHomePage.class);
        Intent intent2 = new Intent(QuizActivity.this, CourseHomePageInstructor.class);
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