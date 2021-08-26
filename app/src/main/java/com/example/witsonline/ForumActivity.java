package com.example.witsonline;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.style.TtsSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ForumActivity extends AppCompatActivity implements  View.OnScrollChangeListener{


    //Creating a list of Courses
    private ArrayList<Discussion> listDiscussions;
    //This is for the review pop up menu

    //CreatingViews
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;
    String webURL = "https://lamp.ms.wits.ac.za/home/s2105624/getDiscussions.php?page=";
    //Volley Request Queue
    private RequestQueue requestQueue;
    private  Button startDiscussion;

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


        //Initializing Views
        recyclerView = (RecyclerView)findViewById(R.id.discussionRecyclerView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        startDiscussion = (Button)findViewById( R.id.startDiscussion );

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
        getData();
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
            public void onClick(View view) {
                createNewViewDialogDiscussion();
            }
        });

    }


    @Generated
    private JsonArrayRequest getDataFromServer(int requestCount){
        //Initializing progressbar
        final ProgressBar progressBar = (ProgressBar) findViewById(R.id.discussionProgressBar);

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
        Log.d("HERE", "yes"+jsonArrayRequest.toString());
        return jsonArrayRequest;
    }

    //This method will get Data from the web api
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
                discussion.setDiscussionTopic(json.getString("discussionTopic"));
                discussion.setDiscussionStudent(json.getString("discussionStudentFName"));
                discussion.setDiscussionStatus(Integer.parseInt(json.getString("discussionStatus")));
                discussion.setDiscussionReplies(Integer.parseInt(json.getString("discussionReplies")));
            } catch (JSONException e){
                e.printStackTrace();
            }
            //Adding the request object to the list
            listDiscussions.add(discussion);
        }
        //Notifying the adapter that data has been added or changed
        adapter.notifyDataSetChanged();
    }
    //This is the dialog for adding new discussions in the forum
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
    @Override
    @Generated
    public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
        //if Scrolled at last then
        if(isLastItemDisplaying(recyclerView)){
            //Calling the method getData again
            getData();
        }
    }
    public void onBackPressed(){
            Intent intent = new Intent(ForumActivity.this, CourseHomePage.class);
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
}
