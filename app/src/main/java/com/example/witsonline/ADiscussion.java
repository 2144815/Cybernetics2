package com.example.witsonline;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ADiscussion extends AppCompatActivity implements View.OnScrollChangeListener {

    //to check if user is student or instructor
    private final Matcher m = Pattern.compile("-?\\d+").matcher("");

    //to view student's profile
    private android.app.AlertDialog.Builder alertDialogBuilder;
    private android.app.AlertDialog dialog;
    private Button btnView, btnCancel;

    private RelativeLayout relativeLayout;
    private ProgressBar progressBar;
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
    private String webLink = "https://lamp.ms.wits.ac.za/home/s2105624/getAllReplies.php?page=";
    private boolean browse = false;
    private boolean mycourses = false;
    private boolean dashboard = false;
    Bundle extras;
    Menu menu;

    @Override
    @Generated
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adiscussion);

        //get list of tutors
        requestQueue = Volley.newRequestQueue(this);
        //getTutorData();

        //initialize relative layout and progress bar
        relativeLayout = findViewById(R.id.repliesRelativeLayout);
        progressBar = findViewById(R.id.commentProgressBar);
        progressBar.setVisibility(View.VISIBLE);


        //assign values
        send = (ImageButton) findViewById(R.id.btn_Send);
        Answer = (EditText) findViewById(R.id.editTextAnswer);
        studentName = (TextView) findViewById(R.id.tv_studentName);
        question = (TextView) findViewById(R.id.question);
        NoAnswers = (TextView) findViewById(R.id.NoReplies);
        studentName.setText(DISCUSSIONS.DISCUSSION_STUDENT);
        studentName.setOnClickListener(new View.OnClickListener() {
            @Override
            @Generated
            public void onClick(View v) {
                createNewViewProfileDialog();
            }
        });
        question.setText(DISCUSSIONS.DISCUSSION_TEXT);
        if (DISCUSSIONS.DISCUSSION_NUM_REPLIES.equals("1")){
            NoAnswers.setText(" " + DISCUSSIONS.DISCUSSION_NUM_REPLIES + " Answer");
        }else{
            NoAnswers.setText(" " + DISCUSSIONS.DISCUSSION_NUM_REPLIES + " Answers");
        }
        TextView txtTime = (TextView) findViewById(R.id.tvTime);
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
        recyclerView = (RecyclerView) findViewById(R.id.comments);
        recyclerView.setHasFixedSize(true);
        //use a liner layout manager
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        //specify an adapter
        recyclerView.setAdapter(mAdapter);
        commentList = new ArrayList<>();
        //Calling methods to get data from server

        getData();

        //Adding an scroll change listener to recyclerView
        recyclerView.setOnScrollChangeListener(this);
        mAdapter = new CommentsAdapter(commentList, this);
        recyclerView.setAdapter(mAdapter);
        //Closed discussion
        if (DISCUSSIONS.getDiscussionStatus().equals("Closed")) {
            Answer.setText("This discussion is closed.");
            Answer.setEnabled(false);
            send.setEnabled(false);
            send.setImageResource(R.drawable.ic_baseline_send_24_grey);
        } else {
            send.setOnClickListener(new View.OnClickListener() {
                @Override
                @Generated
                public void onClick(View v) {
                    String time = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss").format(Calendar.getInstance().getTime());
                    if (Answer.getText().toString().equals("")) {
                        Toast toast = Toast.makeText(ADiscussion.this, "Answer cannot be empty", Toast.LENGTH_LONG);
                        toast.show();
                    } else {
                        try {
                            String phpFile = "addComment.php";
                            String phpFile2 = "addInstructorComment.php";
                            if (USER.STUDENT) {
                                addComment(phpFile, Answer.getText().toString(), time, "studentNumber");
                            } else {
                                addComment(phpFile2, Answer.getText().toString(), time, "username");
                            }

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        Answer.setText("");
                    }
                }
            });
        }

    }

    //This method will get Data from the web api
    @Generated
    private void getData() {
        //Adding the method to the queue by calling the method getDatafromServer
        requestQueue.add(getDataFromServer(commentsCount));
        //Incrementing the course counter
        commentsCount++;
    }

    @Generated
    private JsonArrayRequest getDataFromServer(int requestCount) {
        //Initializing progressbar
        //final ProgressBar progressBar = (ProgressBar) findViewById(R.id.commentProgressBar);

        //Displaying ProgressBar
        //progressBar.setVisibility(View.VISIBLE);
        setProgressBarIndeterminateVisibility(true);

        //JsonArrayRequest of volley
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(webLink + String.valueOf(requestCount) + "&discussionID=" + DISCUSSIONS.DISCUSSION_ID,
                (response) -> {
                    //Calling method parseData to parse the json responce
                    //getTutorData();
                    parseData(response);
                    /*final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            // Do something after 0.5s = 500ms
                        }
                    }, 500); */
                    relativeLayout.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                },
                (error) -> {
                    relativeLayout.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                    //If an error occurs that means end of the list has been reached
                    //Toast.makeText(CourseHomePage.this, "No More Items Available", Toast.LENGTH_SHORT).show();
                });
        return jsonArrayRequest;
    }


    @Generated
    private void parseData(JSONArray array) {

        for (int i = 0; i < array.length(); i++) {
            //updating the number of replies
            //DISCUSSIONS.updateNoReplies();
            comment = new Comment();
            JSONObject json = null;
            try {
                //Getting json
                json = array.getJSONObject(i);
                //Adding data to the course object
                String id = json.getString("reply_ID");
                comment.setId(id);
                String fullName = "";
                fullName = fullName + json.getString("userFname");
                fullName = fullName + " " + json.getString("userLname");
                comment.setUserFullName(fullName);
                comment.setNoVotes(Integer.parseInt(json.getString("reply_Votes")));
                comment.setComment(json.getString("reply_Text"));
                String username = json.getString("username");
                comment.setUsername(username);
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                comment.setTime(format.parse(json.getString("reply_Time")));
                Log.d("HERE",format.format(comment.getTime()));

                //check if instructor or student
                if (byRegex(username,m)) {
                    //figure out if student is a tutor
                    if (COURSE.TUTORS.contains(username)){
                        comment.setUserRole("Tutor");
                    }else{
                        comment.setUserRole("Student");
                    }
                } else {
                    comment.setUserRole("Instructor");
                }

            } catch (JSONException | ParseException e) {
                e.printStackTrace();
            }
            commentList.add(comment);
            //commentList.sort(Comparator.comparing(o -> o.getTime()));
            mAdapter.notifyDataSetChanged();

        }

        //Notifying the adapter that data has been added or changed
    }
    @Generated
    public boolean byRegex(String str, Matcher m) {
        return m.reset(str).matches();
    }

    @Generated
    private void addComment(String phpFile, String text, String time, String user) throws IOException {
        OkHttpClient client = new OkHttpClient();
        HttpUrl.Builder urlBuilder = HttpUrl.parse("https://lamp.ms.wits.ac.za/~s2105624/" + phpFile).newBuilder();
        urlBuilder.addQueryParameter(user, USER.USERNAME);
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
                    @Generated
                    public void run() {
                        if (responseData.trim().equals("Successful")) {
                            Toast toast = Toast.makeText(ADiscussion.this, responseData, Toast.LENGTH_LONG);
                            toast.show();
                            DISCUSSIONS.updateNoReplies();
                            Intent intent = new Intent(ADiscussion.this, ADiscussion.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast toast = Toast.makeText(ADiscussion.this, "Couldn't post your answer ", Toast.LENGTH_LONG);
                            toast.show();
                        }
                    }
                });

            }
        });
    }

    @Generated
    public void createNewViewProfileDialog(){
        alertDialogBuilder= new android.app.AlertDialog.Builder(this);
        final View viewPopUp = LayoutInflater.from(this)
                .inflate(R.layout.view_profile_dialog, null);

        btnView = (Button) viewPopUp.findViewById(R.id.btnView);
        btnCancel = (Button) viewPopUp.findViewById(R.id.btnViewCancel);

        alertDialogBuilder.setView(viewPopUp);
        dialog = alertDialogBuilder.create();
        dialog.show();

        btnView.setOnClickListener(new View.OnClickListener() {
            @Override
            @Generated
            public void onClick(View v) {
                STUDENT.number = DISCUSSIONS.DISCUSSION_STUDENT_NUMBER;
                Intent intent5 = new Intent(ADiscussion.this,UserDetails.class);
                intent5.putExtra("userType","student");
                startActivity(intent5);
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
    @Generated
    public void onBackPressed() {
        Intent intent = new Intent(this, ForumActivity.class);
        if (browse) {
            intent.putExtra("activity", "" + BrowseCourses.class);
        } else if (mycourses) {
            intent.putExtra("activity", "" + MyCourses.class);
        } else {
            intent.putExtra("activity", "" + Dashboard.class);
        }
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate( R.menu.sort_comments_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull @NotNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.menu_sortByVotes:
                // sort by votes
                Collections.sort(commentList, Comment.CommentVotesComparator);
                mAdapter.notifyDataSetChanged();
                Toast.makeText( ADiscussion.this, "Sorted by votes", Toast.LENGTH_SHORT ).show();
                return true;

            case R.id.menu_sortByRoles:
                //sort by Roles
                Collections.sort(commentList, Comment.CommentRolesComparator);
                mAdapter.notifyDataSetChanged();
                Toast.makeText( ADiscussion.this, "Sorted by Roles", Toast.LENGTH_SHORT ).show();
                return true;

            case R.id.menu_sortByDate:
                //sort by date(Default)

                Collections.sort(commentList, Comment.CommentDatesComparator);
                mAdapter.notifyDataSetChanged();
                Toast.makeText( ADiscussion.this, "Sorted by Date", Toast.LENGTH_SHORT ).show();
                return true;
        }

        return super.onOptionsItemSelected( item );
    }
}
