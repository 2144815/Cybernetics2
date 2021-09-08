package com.example.witsonline;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class CourseCardAdapter extends RecyclerView.Adapter<CourseCardAdapter.ViewHolder> {
    private Context context;
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private ProgressBar progressBar;
    private Button btnViewDialogSubscribe, btnViewDialogViewCourse;
    private Button btnUnsubscribe, btnCancel;
    private HashMap <String,String> courseVisibilites = new HashMap<>(); // for storing course visibility
    private Button btnView, btnProfileCancel; // for viewing instructor's profile
    private HashMap <String,String> instUsernames = new HashMap<>(); // for viewing instructor's profile
    //List to store all Courses
    ArrayList<CourseV> coursesVs;


    //For determining if student is a tutor
    String tutorURL = "https://lamp.ms.wits.ac.za/home/s2105624/getTutorState.php?studentNumber=";
    private RequestQueue requestQueue;
    private boolean tutor;
    private TextView unsubscribeText;

    //Constructor of this class
    public CourseCardAdapter(ArrayList<CourseV> requestVs, Context context){
        super();
        //Getting all requests
        this.coursesVs = requestVs;
        this.context = context;
    }

    @NonNull
    @Override
    @Generated
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.course_card, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);

        return viewHolder;
    }

    @Override
    @Generated
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //Getting the particular item from the list
        final CourseV courseV = coursesVs.get(position);

        //Showing data on the views
        holder.setIsRecyclable(false);
        holder.courseName.setText(courseV.getCourseName());
        holder.courseDescription.setText(courseV.getCourseDescription());
        holder.courseVisibility.setText(courseV.getCourseVisibility());
        //holder.courseInstructor.setText(courseV.getCourseInstructor());
        holder.courseInstructor.setText(courseV.getInstName());
        holder.courseCode.setText(courseV.getCourseCode());
        holder.courseRatingBar.setRating(Float.parseFloat(courseV.getCourseRating()));
        holder.courseOutline = courseV.getCourseOutline();
        holder.courseRating= courseV.getCourseRating();
        holder.courseImage =courseV.getImageUrl();
        if(!holder.courseImage.equals("null")){
            Glide.with(context).load(holder.courseImage).into(holder.image);
        }
        String visibility = courseV.getCourseVisibility();
        if(visibility.equals("Public")){
            holder.courseLock.setVisibility(View.GONE);
        }


        //add course to visibility hash map
       // if (!courseVisibilites.containsKey(courseV.getCourseCode()) && courseV.getCourseVisibility() != null){
       //     courseVisibilites.put(courseV.getCourseCode(),courseV.getCourseVisibility());
        //    //Toast.makeText(context, courseV.getCourseCode(), Toast.LENGTH_SHORT).show();
       // }


        //for view profile, we need the instructor's username
        instUsernames.put(courseV.getCourseCode(),courseV.getCourseInstructor());

    }

    @Override
    @Generated
    public int getItemCount() {
        return coursesVs.size();
    }
    @Generated
    class ViewHolder extends RecyclerView.ViewHolder{
        //Views
        public TextView courseName;
        public TextView courseDescription;
        public TextView courseInstructor;
        public TextView courseCode;
        public String courseOutline;
        public String courseRating;
        public String courseImage;
        public ImageView image;
        public RatingBar courseRatingBar;
        public ImageView courseLock;
        public TextView courseVisibility;

        //Initializing Views
        public ViewHolder(View itemView) {
            super(itemView);
            courseName = (TextView) itemView.findViewById(R.id.courseName);
            courseDescription = (TextView) itemView.findViewById(R.id.courseDescription);
            courseInstructor = (TextView) itemView.findViewById(R.id.courseInstructor);
            courseCode = (TextView) itemView.findViewById(R.id.codeContainer);
            courseRatingBar = (RatingBar)itemView.findViewById(R.id.courseRating);
            courseVisibility = (TextView) itemView.findViewById(R.id.course_Visibility);
            image = (ImageView)itemView.findViewById(R.id.courseImage) ;
            courseLock = (ImageView)itemView.findViewById(R.id.lock_icon);
            courseInstructor.setOnClickListener(new View.OnClickListener() {
                @Override
                @Generated
                public void onClick(View v) {
                    if (USER.STUDENT){
                        createNewViewProfileDialog(courseCode);
                    }
                    else{
                        COURSE.NAME = courseName.getText().toString();
                        COURSE.OUTLINE = courseOutline;
                        COURSE.IMAGE = courseImage;
                        COURSE.RATING =courseRating;
                        //COURSE.INSTRUCTOR = courseInstructor.getText().toString();
                        COURSE.INSTRUCTOR_NAME = courseInstructor.getText().toString();
                        COURSE.CODE = courseCode.getText().toString();
                        COURSE.DESCRIPTION = courseDescription.getText().toString();
                        if (COURSE.TUTORS != null){
                            COURSE.TUTORS.clear();
                        }else{
                            COURSE.TUTORS = new HashSet<>();
                        }

                        Intent i = new Intent(context, CourseHomePageInstructor.class);
                        context.startActivity(i);
                    }
                }
            });
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                @Generated
                public void onClick(View view) {

                    COURSE.NAME = courseName.getText().toString();
                    COURSE.OUTLINE = courseOutline;
                    COURSE.IMAGE = courseImage;
                    COURSE.RATING =courseRating;
                    //COURSE.INSTRUCTOR = courseInstructor.getText().toString();
                    COURSE.INSTRUCTOR_NAME = courseInstructor.getText().toString();
                    COURSE.CODE = courseCode.getText().toString();
                    COURSE.DESCRIPTION = courseDescription.getText().toString();
                    if (COURSE.TUTORS != null){
                        COURSE.TUTORS.clear();
                    }else{
                        COURSE.TUTORS = new HashSet<>();
                    }

                    if(USER.STUDENT){
                        try {
                            doPostRequest("enrolment.php");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        createNewViewDialog(courseCode);
                    }
                    else{
                        Intent i = new Intent(context, CourseHomePageInstructor.class);
                        context.startActivity(i);
                    }
                }
            });
        }
    }

    @Generated
    public void createNewViewProfileDialog(TextView courseCode){
        dialogBuilder = new AlertDialog.Builder(context);
        final View viewPopUp = LayoutInflater.from(context)
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
                INSTRUCTOR.USERNAME = instUsernames.get(courseCode.getText());
                Intent intent5 = new Intent(context,UserDetails.class);
                intent5.putExtra("userType","instructor");
                context.startActivity(intent5);
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
    public void createNewViewDialog(TextView courseCode){
        dialogBuilder = new AlertDialog.Builder(context);
        final View viewPopUp = LayoutInflater.from(context)
                .inflate(R.layout.subscribe_dialog, null);

        btnViewDialogSubscribe = (Button) viewPopUp.findViewById(R.id.viewSubscribe);
        btnViewDialogViewCourse = (Button) viewPopUp.findViewById(R.id.viewViewCourse);
        progressBar = viewPopUp.findViewById(R.id.progressBarSubscribe);
        progressBar.setVisibility(View.VISIBLE);
        dialogBuilder.setView(viewPopUp);
        dialog = dialogBuilder.create();
        dialog.show();
        try {
            doPostRequest("enrolment.php");
        } catch (IOException e) {
            e.printStackTrace();
        }
        btnViewDialogSubscribe.setOnClickListener(new View.OnClickListener() {
            @Override
            @Generated
            public void onClick(View v) {
                if(btnViewDialogSubscribe.getText().toString().trim().equals("SUBSCRIBE")){
                    try {
                        doPostRequest("enrol.php");
                        btnViewDialogSubscribe.setText("UNSUBSCRIBE");
                        Toast toast = Toast.makeText(context, "Subscribed to "+ COURSE.CODE, Toast.LENGTH_LONG);
                        toast.show();
                        dialog.dismiss();
                        Intent intent = new Intent(context,BrowseCourses.class);
                        intent.putExtra("activity",""+context);
                        context.startActivity(intent);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                else{
                    dialog.dismiss();
                    createNewViewDialogUnsubscribe();
                }
            }
        });
       btnViewDialogViewCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            @Generated
            public void onClick(View v) {
                //INSTRUCTOR.USERNAME = instUsernames.get(courseCode.getText());
                dialog.dismiss();
                Intent i = new Intent(context, CourseHomePage.class);
                i.putExtra("activity",""+context);
                i.putExtra("username",instUsernames.get(courseCode.getText()));
                context.startActivity(i);
            }
        });

    }
    @Generated
    public void createNewViewDialogUnsubscribe(){
        dialogBuilder = new AlertDialog.Builder(context);
        final View viewPopUp = LayoutInflater.from(context)
                .inflate(R.layout.unsubscribe_dialog, null);
        //relativeLayout = findViewById(R.id.CourseHomeInstructorLayout);
        btnUnsubscribe = (Button) viewPopUp.findViewById(R.id.unSubscribe);
        btnCancel = (Button) viewPopUp.findViewById(R.id.unsubscribeCancel);
        dialogBuilder.setView(viewPopUp);
        dialog = dialogBuilder.create();
        dialog.show();

        unsubscribeText = viewPopUp.findViewById(R.id.unsubscribeText);

        if (tutor){
            unsubscribeText.setText("You are a tutor for this course. Are you sure you want to unsubscribe?");
        }
        else{
            unsubscribeText.setText("Are you sure you want to unsubscribe?");
        }


        btnUnsubscribe.setOnClickListener(new View.OnClickListener() {
            @Override
            @Generated
            public void onClick(View v) {
                try {
                    doPostRequest("unsubscribe.php");
                    btnViewDialogSubscribe.setText("SUBSCRIBE");
                    Toast toast = Toast.makeText(context, "Unsubscribed to "+ COURSE.CODE, Toast.LENGTH_LONG);
                    toast.show();
                    dialog.dismiss();
                    Intent intent = new Intent(context,MyCourses.class);
                    intent.putExtra("activity",""+context);
                    context.startActivity(intent);
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
            private Activity cont = (Activity)context;

            @Override
            @Generated
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
            }

            @Override
            @Generated
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                final String responseData = response.body().string();
                cont.runOnUiThread(new Runnable() {
                    @Override
                    @Generated
                    public void run() {
                        if (btnViewDialogSubscribe != null) {
                            if (responseData.trim().equals("subscribed")) {
                                btnViewDialogSubscribe.setText("UNSUBSCRIBE");
                                getTutorStateData();
                            }
                            if(progressBar!=null){
                                btnViewDialogSubscribe.setVisibility(View.VISIBLE);
                                btnViewDialogViewCourse.setVisibility(View.VISIBLE);
                                progressBar.setVisibility(View.GONE);
                            }
                        }
                        if (btnUnsubscribe != null) {
                            if (responseData.trim().equals("unsubscribed")) {
                                btnUnsubscribe.setText("SUBSCRIBE");
                            }
                            if(progressBar!=null) {
                                btnUnsubscribe.setVisibility(View.VISIBLE);
                                btnViewDialogViewCourse.setVisibility(View.VISIBLE);
                                progressBar.setVisibility(View.GONE);
                            }
                        }

                    }
                });
            }
        });
    }
    @Generated
    private void getTutorStateData(){
        requestQueue = Volley.newRequestQueue(context);
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
                    tutor = true;
                }
                else{
                    tutor = false;
                }

            } catch (JSONException e){
                e.printStackTrace();
            }

        }

    }

}
