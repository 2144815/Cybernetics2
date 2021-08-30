package com.example.witsonline;



import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DiscussionCardAdapter extends RecyclerView.Adapter<DiscussionCardAdapter.ViewHolder> {
    private Context context;

    //List to store all Courses
    ArrayList<Discussion> discussions;
    private PopupMenu popup;


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
        if (discussion.getDiscussionStatus().equals( "Closed" ) && USER.FNAME.equals( discussion.getDiscussionStudent()) == false){
            holder.menu.setEnabled( false );
        }
        else if (discussion.getDiscussionStatus().equals( "Open" ) && USER.FNAME.equals( discussion.getDiscussionStudent()) == false){
            holder.menu.setEnabled( false );
        }
        else{
            holder.menu.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showPopupMenu(holder.menu, position,holder);
                }
            } );
        }



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


    private void showPopupMenu(View v, int position,ViewHolder holder) {
        PopupMenu popup = new PopupMenu(v.getContext(),v );
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.discussion_menu, popup.getMenu());

        if(holder.status.getText().equals( "Closed" )){
            popup.getMenu().findItem(R.id.Open_Close_DiscChoice).setTitle( "Open Discussion" );

        }


        
        popup.setOnMenuItemClickListener(new DiscMenuClickListener(position, context,holder,discussions));
        popup.show();


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
        public AppCompatImageButton menu;


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

            menu = itemView.findViewById( R.id.disc_menu );

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                @Generated
                public void onClick(View view) {
                    DISCUSSIONS.DISCUSSION_ID = id.getText().toString();
                    DISCUSSIONS.DISCUSSION_STUDENT = startedBy.getText().toString();
                    DISCUSSIONS.DISCUSSION_TEXT = text.getText().toString();
                    DISCUSSIONS.DISCUSSION_TOPIC = topic.getText().toString();
                    if((status.getText().toString()).equals( "Closed" )){
                        DISCUSSIONS.DISCUSSION_STATUS = 0;
                    }
                    else{
                        DISCUSSIONS.DISCUSSION_STATUS = 1;
                    }

                    Intent i = new Intent(context, ADiscussion.class);
                    context.startActivity(i);
                }

        });
    }
}}
