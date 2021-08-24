package com.example.witsonline;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.util.ArrayList;

public class DiscussionCardAdapter extends RecyclerView.Adapter<DiscussionCardAdapter.ViewHolder> {
    private Context context;

    //List to store all Courses
    ArrayList<Discussion> discussions;

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

        //Showing data on the views
        holder.setIsRecyclable(false);
        holder.startedBy.setText(discussion.getDiscussionStudent());
      //  holder.numberOfReplies.setText(discussion.getDiscussionReplies());
        holder.status.setText(discussion.getDiscussionStatus());
        holder.topic.setText(discussion.getDiscussionTopic());

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


        //Initializing Views
        public ViewHolder(View itemView) {
            super(itemView);
            topic = (TextView) itemView.findViewById(R.id.topic);
            startedBy = (TextView) itemView.findViewById(R.id.startedBy);
            numberOfReplies = (TextView) itemView.findViewById(R.id.numberOfReplies);
            status = (TextView) itemView.findViewById(R.id.status);
            /*  itemView.setOnClickListener(new View.OnClickListener() {
              @Override
                @Generated
                public void onClick(View view) {
                    LESSON.Code = COURSE.CODE;
                    LESSON.Text = lessonText.getText().toString();
                    LESSON.Name = lessonName.getText().toString();
                    LESSON.Resource = lessonResource.getText().toString();
                    LESSON.Url = lessonUrl.getText().toString();
                    LESSON.ID = lessonID.getText().toString();

                    if(USER.STUDENT){
                        Intent i = new Intent(context, LessonPage.class);
                        context.startActivity(i);
                    }
                    else{
                        Intent i = new Intent(context, LessonPageInstructor.class);
                        context.startActivity(i);
                    }
                }
            });*/

        }
    }
}
