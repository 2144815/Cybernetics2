package com.example.witsonline;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.MyViewHolder> {
    List<Comment> commentList;
    Context context;

    public CommentsAdapter(List<Comment> commentList, Context context) {
        this.commentList = commentList;
        this.context = context;
    }

    @NonNull
    @NotNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        //Inflator
        View view = LayoutInflater.from( parent.getContext()).inflate( R.layout.one_line_comment,parent,false );
        MyViewHolder holder = new MyViewHolder( view );

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull MyViewHolder holder, int position) {
        //here we can create clickListerns and assign values
        //onBindViewHolder(): RecyclerView calls this method to associate a ViewHolder with data.
        // The method fetches the appropriate data and uses the data to fill in the view holder's layout. For example, if the RecyclerView displays a list of names,
        // the method might find the appropriate name in the list and fill in the view holder's TextView widget.
        holder.TheStudentName.setText( commentList.get((holder.getAdapterPosition())).getUserFullName());
        holder.TheAnswer.setText( commentList.get((holder.getAdapterPosition())).getComment());


        holder.upvote.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                commentList.get((holder.getAdapterPosition())).setNoVotes(1);
                holder.NoVotes.setText( String.valueOf(commentList.get((holder.getAdapterPosition())).getNoVotes()) );
            }
        } );

        holder.downVote.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                commentList.get((holder.getAdapterPosition())).setNoVotes( -1 );
                holder.NoVotes.setText( String.valueOf(commentList.get((holder.getAdapterPosition())).getNoVotes()) );
            }
        } );


    }

    @Override
    public int getItemCount() {
        return commentList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder{
        //this is a reference to the one line layout
        TextView TheStudentName;
        TextView TheTime;
        TextView TheAnswer;
        TextView NoVotes;
        AppCompatImageButton upvote;
        AppCompatImageButton downVote;



        public MyViewHolder(@NonNull @NotNull View itemView) {
            super( itemView );
            TheStudentName = itemView.findViewById( R.id.tv_studentFullName );
            TheTime = itemView.findViewById( R.id.time );
            TheAnswer = itemView.findViewById( R.id.Answer );
            NoVotes = itemView.findViewById( R.id.tv_NoVotes );
            upvote = itemView.findViewById( R.id.btn_Upvote );
            downVote = itemView.findViewById( R.id.btn_downVote );

        }
    }
}
