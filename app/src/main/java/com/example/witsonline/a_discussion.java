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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class a_discussion  extends AppCompatActivity implements  View.OnScrollChangeListener{
    List<Comment> commentList = new ArrayList<Comment>();
    private ImageButton send;
    private EditText Answer;
    private TextView studentName;
    private TextView question;
    private Discussion discussion;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private int commentsCount = 1;
    private RequestQueue requestQueue;
    private String webLink = "https://lamp.ms.wits.ac.za/home/s2105624/getReplies.php?page=";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_adiscussion );
        //assign values


        send = (ImageButton)findViewById(R.id.btn_Send );
        Answer = (EditText)findViewById( R.id.editTextAnswer );
        studentName = (TextView)findViewById(R.id.tv_studentName);
        question = (TextView)findViewById(R.id.question);

        studentName.setText(DISCUSSION.DISCUSSION_STUDENT);
        question.setText(DISCUSSION.DISCUSSION_TEXT);
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
        send.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(Answer.getText().toString().equals( "" )){
                    Toast toast = Toast.makeText(a_discussion.this, "Answer cannot be empty", Toast.LENGTH_LONG);
                    toast.show();
                }
                else {
                    Answer.setText( "" );
                }

            }
        } );

    }
    @Generated
    private JsonArrayRequest getDataFromServer(int requestCount){
        //Initializing progressbar
        final ProgressBar progressBar = (ProgressBar) findViewById(R.id.commentProgressBar);

        //Displaying ProgressBar
        progressBar.setVisibility(View.VISIBLE);
        setProgressBarIndeterminateVisibility(true);

        //JsonArrayRequest of volley
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(webLink + String.valueOf(requestCount) + "&discussionID="+DISCUSSION.DISCUSSION_ID ,
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
            Comment comment = new Comment();
            JSONObject json = null;
            try {
                //Getting json
                json = array.getJSONObject(i);
                //Adding data to the course object
                comment.setUserFullName(json.getString("reply_studentFname"));
                comment.setComment(json.getString("reply_Text"));
            } catch (JSONException e){
                e.printStackTrace();
            }
            //Adding the request object to the list
            commentList.add(comment);
        }
        //Notifying the adapter that data has been added or changed
        mAdapter.notifyDataSetChanged();
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
        startActivity(intent);
        finish();
    }
}
