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

import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

public class a_discussion extends AppCompatActivity {
    List<Comment> commentList = new ArrayList<Comment>();
    private ImageButton send;
    private EditText Answer;
    private Discussion discussion;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    private String webLink = "https://lamp.ms.wits.ac.za/home/s2105624/getReplies.php?page=";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_adiscussion );
        //assign values

        discussion = (Discussion) getIntent().getSerializableExtra("theData");


        send = (ImageButton)findViewById(R.id.btn_Send );
        Answer = (EditText)findViewById( R.id.editTextAnswer ) ;
        recyclerView = (RecyclerView)findViewById( R.id.comments );
        recyclerView.setHasFixedSize( true );
        //use a liner layout manager
        layoutManager = new LinearLayoutManager( this );
        recyclerView.setLayoutManager( layoutManager );
        //specify an adapter
        mAdapter = new CommentsAdapter(commentList,this);
        recyclerView.setAdapter( mAdapter );

        //updating a_discussion card



        send.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(Answer.getText().toString().equals( "" )){
                    Toast toast = Toast.makeText(a_discussion.this, "Answer cannot be empty", Toast.LENGTH_LONG);
                    toast.show();
                }
                else {

                    AddComment(Answer.getText().toString());
                    Answer.setText( "" );
                }

            }
        } );





    }

    private void AddComment(String comment) {
        Comment c1 = new Comment( USER.FNAME+" "+USER.LNAME,1,comment,"15h00" );
        commentList.add(c1);
    }
    public void onBackPressed(){
        Intent intent = new Intent(this, ForumActivity.class);
        startActivity(intent);
        finish();
    }
}
