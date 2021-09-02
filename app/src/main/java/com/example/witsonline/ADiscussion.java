package com.example.witsonline;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ADiscussion  extends AppCompatActivity implements  View.OnScrollChangeListener{
    List<Comment> commentList = new ArrayList<Comment>();
    private ImageButton send;
    private EditText Answer;
    private TextView studentName;
    private TextView question;
    private TextView NoAnswers;
    private Discussion discussion;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private int commentsCount = 1;
    private Comment comment;
    private RequestQueue requestQueue;
    String tutorURL = "https://lamp.ms.wits.ac.za/home/s2105624/getTutorState.php?studentNumber=";
    private String webLink = "https://lamp.ms.wits.ac.za/home/s2105624/getReplies.php?page=";
    private boolean browse = false;
    private boolean mycourses = false;
    private boolean dashboard = false;
    Bundle extras;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_adiscussion );
        //assign values
        send = (ImageButton)findViewById(R.id.btn_Send );
        Answer = (EditText)findViewById( R.id.editTextAnswer);
        studentName = (TextView)findViewById(R.id.tv_studentName);
        question = (TextView)findViewById(R.id.question);
        NoAnswers = (TextView)findViewById( R.id.NoReplies );
        studentName.setText(DISCUSSIONS.DISCUSSION_STUDENT);
        question.setText(DISCUSSIONS.DISCUSSION_TEXT);
        NoAnswers.setText(" "+DISCUSSIONS.DISCUSSION_NUM_REPLIES+" Answers");

        TextView txtTime = (TextView)findViewById(R.id.tvTime);
        SimpleDateFormat dtDate = new SimpleDateFormat("dd-MMM-yyyy");
        SimpleDateFormat dtTime = new SimpleDateFormat("HH:mm");
        String strTime = dtDate.format(DISCUSSIONS.DISCUSSION_DATE) + '\n' + dtTime.format(DISCUSSIONS.DISCUSSION_DATE);
        txtTime.setText(strTime);
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
       // question.setText(DISCUSSION.DISCUSSION_TEXT);
        recyclerView = (RecyclerView)findViewById( R.id.comments );
        recyclerView.setHasFixedSize(true);
        //use a liner layout manager
        layoutManager = new LinearLayoutManager( this );
        recyclerView.setLayoutManager( layoutManager );
        //specify an adapter
        recyclerView.setAdapter( mAdapter );
        commentList = new ArrayList<>();
        requestQueue = Volley.newRequestQueue(this);
        //Calling methods to get data from server
        getData();
        //Adding an scroll change listener to recyclerView
        recyclerView.setOnScrollChangeListener(this);
        mAdapter = new CommentsAdapter(commentList,this);
        recyclerView.setAdapter(mAdapter);
        //
        if(DISCUSSIONS.getDiscussionStatus().equals( "Closed" )  ){
            Answer.setText("This discussion is closed.");
            Answer.setEnabled( false );
            send.setEnabled( false );
            send.setImageResource(R.drawable.ic_baseline_send_24_grey);
        }
        else {
            send.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String time = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss").format(Calendar.getInstance().getTime());
                    if(Answer.getText().toString().equals( "" )){
                        Toast toast = Toast.makeText(ADiscussion.this, "Answer cannot be empty", Toast.LENGTH_LONG);
                        toast.show();
                    }
                    else {
                        try {
                            String phpFile = "addComment.php";
                            String phpFile2 = "addInstructorComment.php";
                            if(USER.STUDENT) {
                                addComment(phpFile, Answer.getText().toString(), time,"studentNumber");
                            }
                            else{
                                addComment(phpFile2, Answer.getText().toString(), time,"username");
                            }

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        Answer.setText("");
                    }
                }
            } );
        }


    }
    @Generated
    private JsonArrayRequest getDataFromServer(int requestCount){
        //Initializing progressbar
        final ProgressBar progressBar = (ProgressBar) findViewById(R.id.commentProgressBar);

        //Displaying ProgressBar
        progressBar.setVisibility(View.VISIBLE);
        setProgressBarIndeterminateVisibility(true);

        //JsonArrayRequest of volley
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(webLink + String.valueOf(requestCount) + "&discussionID="+DISCUSSIONS.DISCUSSION_ID ,
                (response) -> {
                    //Calling method parseData to parse the json responce
                    parseData(response);
                    progressBar.setVisibility(View.GONE);
                    //Hiding the progressBar
                },
                (error) -> {
                    progressBar.setVisibility(View.GONE);
                    //If an error occurs that means end of the list has been reached
                    //Toast.makeText(CourseHomePage.this, "No More Items Available", Toast.LENGTH_SHORT).show();
                });
        return jsonArrayRequest;
    }

    //This method will get Data from the web api
    private void getData(){
        //Adding the method to the queue by calling the method getDatafromServer
        requestQueue.add(getDataFromServer(commentsCount));
        //Incrementing the course counter
        commentsCount++;
    }
    @Generated
    private void parseData(JSONArray array){
        for (int i = 0; i< array.length(); i++){
            // Creating the Course object
            comment = new Comment();
            JSONObject json = null;
            try {
                //Getting json
                json = array.getJSONObject(i);
                //Adding data to the course object
                String fullName = "";
                fullName = fullName + json.getString("userFname");
                fullName = fullName + " " + json.getString("userLname");
                comment.setUserFullName(fullName);
                comment.setComment(json.getString("reply_Text"));
                comment.setUsername(json.getString("username"));
            } catch (JSONException e){
                e.printStackTrace();
            }
            //Adding the request object to the listb
           // getTutorStateData(comment.getUsername());
            commentList.add(comment);
            mAdapter.notifyDataSetChanged();

        }
        //Notifying the adapter that data has been added or changed
    }
    //This method will parse json Data


    private void addComment (String phpFile , String text , String time, String user) throws IOException {
        OkHttpClient client = new OkHttpClient();
        HttpUrl.Builder urlBuilder = HttpUrl.parse("https://lamp.ms.wits.ac.za/~s2105624/" + phpFile).newBuilder();
        urlBuilder.addQueryParameter(user,USER.USERNAME);
        urlBuilder.addQueryParameter("discussionID", DISCUSSIONS.DISCUSSION_ID);
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


                ADiscussion.this.runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        if(responseData.trim().equals("Successful")) {
                            Toast toast = Toast.makeText(ADiscussion.this, responseData, Toast.LENGTH_LONG);
                            toast.show();
                            DISCUSSIONS.updateNoReplies();
                            Intent intent = new Intent(ADiscussion.this, ADiscussion.class);
                            startActivity(intent);
                            finish();
                        }
                        else{
                            Toast toast = Toast.makeText(ADiscussion.this, "Couldn't post your answer ", Toast.LENGTH_LONG);
                            toast.show();
                        }
                    }
                });

            }
        });
    }

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
        Intent intent = new Intent(this, ForumActivity.class);
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
