package com.example.witsonline;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DiscussionCardAdapter extends RecyclerView.Adapter<DiscussionCardAdapter.ViewHolder> {
    private Context context;

    //List to store all Courses
    ArrayList<Discussion> discussions;


    //to view student's profile
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private Button btnView, btnCancel;
    private HashMap<String,String> studentNums = new HashMap<>();


    //Constructor of this class
    public DiscussionCardAdapter(ArrayList<Discussion> discussions, Context context){
        super();
        //Getting all requests
        this.discussions = discussions;
        this.context = context;
    }

    @NonNull
    @Override
    @Generated
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.discussion_card, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    @Generated
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //Getting the particular item from the list
        final Discussion discussion = discussions.get(position);
        studentNums.put(discussion.getDiscussionID(),discussion.getDiscussionStudentNumber());
        //Toast.makeText(context, studentNumbers.get(discussion.getDiscussionID()), Toast.LENGTH_SHORT).show();

        //Showing data on the views
        holder.setIsRecyclable(false);
        holder.startedBy.setText(discussion.getDiscussionStudent());
      //  holder.numberOfReplies.setText(discussion.getDiscussionReplies());
        holder.status.setText(discussion.getDiscussionStatus());
        holder.text.setText(discussion.getDiscussionText());
        holder.topic.setText(discussion.getDiscussionTopic());
        holder.id.setText(discussion.getDiscussionID());

        //Toast.makeText(context, discussion.getDiscussionStudentNumber(), Toast.LENGTH_SHORT).show();


    }

    @Generated
    public void createNewViewProfileDialog(TextView discussionID){

        dialogBuilder = new AlertDialog.Builder(context);
        final View viewPopUp = LayoutInflater.from(context)
                .inflate(R.layout.view_profile_dialog, null);

        btnView = (Button) viewPopUp.findViewById(R.id.btnView);
        btnCancel = (Button) viewPopUp.findViewById(R.id.btnViewCancel);

        dialogBuilder.setView(viewPopUp);
        dialog = dialogBuilder.create();
        dialog.show();

        btnView.setOnClickListener(new View.OnClickListener() {
            @Override
            @Generated
            public void onClick(View v) {
                STUDENT.number = studentNums.get(discussionID.getText().toString());
                Intent intent5 = new Intent(context,UserDetails.class);
                intent5.putExtra("userType","student");
                context.startActivity(intent5);
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


    @Override
    @Generated
    public int getItemCount() {
        return discussions.size();
    }
    @Generated
    class ViewHolder extends RecyclerView.ViewHolder{
        //Views
        public TextView topic;
        public TextView startedBy;
        public TextView numberOfReplies;
        public TextView status;
        public TextView text;
        public TextView id;
        public TextView time;


        //Initializing Views
        public ViewHolder(View itemView) {
            super(itemView);
            topic = (TextView) itemView.findViewById(R.id.topic);
            text = (TextView) itemView.findViewById(R.id.text);
            id = (TextView) itemView.findViewById(R.id.discussionID);
            startedBy = (TextView) itemView.findViewById(R.id.startedBy);
            startedBy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    createNewViewProfileDialog(id);
                }
            });
            numberOfReplies = (TextView) itemView.findViewById(R.id.numberOfReplies);
            status = (TextView) itemView.findViewById(R.id.status);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                @Generated
                public void onClick(View view) {
                    DISCUSSIONS.DISCUSSION_ID = id.getText().toString();
                    DISCUSSIONS.DISCUSSION_STUDENT = startedBy.getText().toString();
                    DISCUSSIONS.DISCUSSION_TEXT = text.getText().toString();
                    DISCUSSIONS.DISCUSSION_TOPIC = topic.getText().toString();
                    Intent i = new Intent(context, ADiscussion.class);
                    context.startActivity(i);
                }

        });
    }
}}
