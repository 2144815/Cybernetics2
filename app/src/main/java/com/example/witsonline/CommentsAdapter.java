package com.example.witsonline;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.MyViewHolder> {
    List<Comment> commentList;
    Context context;

    private RequestQueue requestQueue;
    String tutorURL = "https://lamp.ms.wits.ac.za/home/s2105624/getTutorState.php?studentNumber=";
    public CommentsAdapter(List<Comment> commentList, Context context) {
        this.commentList = commentList;
        this.context = context;
    }

    @NonNull
    @NotNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        //Inflator
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.one_line_comment, parent, false);
        MyViewHolder holder = new MyViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull MyViewHolder holder, int position) {
        //here we can create clickListerns and assign values
        //onBindViewHolder(): RecyclerView calls this method to associate a ViewHolder with data.
        // The method fetches the appropriate data and uses the data to fill in the view holder's layout. For example, if the RecyclerView displays a list of names,
        // the method might find the appropriate name in the list and fill in the view holder's TextView widget.
        holder.TheStudentName.setText(commentList.get((holder.getAdapterPosition())).getUserFullName());
        holder.TheAnswer.setText(commentList.get((holder.getAdapterPosition())).getComment());
        requestQueue = Volley.newRequestQueue(context);
        holder.upvote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                commentList.get((holder.getAdapterPosition())).setNoVotes(1);
                holder.NoVotes.setText(String.valueOf(commentList.get((holder.getAdapterPosition())).getNoVotes()));
            }
        });
        holder.downVote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                commentList.get((holder.getAdapterPosition())).setNoVotes(-1);
                holder.NoVotes.setText(String.valueOf(commentList.get((holder.getAdapterPosition())).getNoVotes()));
            }
        });
        getTutorStateData(commentList.get(holder.getAdapterPosition()).getUsername(), holder.role);

    }
    public void UpdateReplies(){

    }
   //This method will parse json Data


    @Override
    public int getItemCount() {
        return commentList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        //this is a reference to the one line layout
        TextView TheStudentName;
        TextView TheTime;
        TextView TheAnswer;
        TextView NoVotes;
        TextView role;
        AppCompatImageButton upvote;
        AppCompatImageButton downVote;


        public MyViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            TheStudentName = itemView.findViewById(R.id.tv_studentFullName);
            TheTime = itemView.findViewById(R.id.time);
            TheAnswer = itemView.findViewById(R.id.Answer);
            NoVotes = itemView.findViewById(R.id.tv_NoVotes);
            upvote = itemView.findViewById(R.id.btn_Upvote);
            downVote = itemView.findViewById(R.id.btn_downVote);
            role = itemView.findViewById(R.id.role);

        }
    }
    @Generated
    private void getTutorStateData(String username, TextView role){
        requestQueue.add(getTutorStateDataFromServer(username,role));
    }

    @Generated
    private JsonArrayRequest getTutorStateDataFromServer(String username,TextView role){
        //JsonArrayRequest of volley
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(tutorURL + username+ "&courseCode=" + COURSE.CODE,
                (response) -> {
                    //Calling method parseData to parse the json responce
                    parseTutorStateData(response,role);

                },
                (error) -> {
                    //Toast.makeText(CourseHomePage.this, "No More Items Available", Toast.LENGTH_SHORT).show();
                });
        //Returning the request
        return jsonArrayRequest;
    }

    private void parseTutorStateData(JSONArray array,TextView role){
        if(array.length()==0){
            role.setText("Instructor");
        }
        for (int i = 0; i< array.length(); i++){

            JSONObject json = null;
            try {
                //Getting json
                json = array.getJSONObject(i);

                //Adding data to the course object
                Integer tutorState = json.getInt("Tutor");
                if(tutorState==1){
                    role.setText("Tutor");
                }
                else {
                    role.setText("Student");
                }
                //Toast.makeText(this, ""+tutorState, Toast.LENGTH_LONG).show();
            } catch (JSONException e){
                e.printStackTrace();
            }

        }


    }

}
