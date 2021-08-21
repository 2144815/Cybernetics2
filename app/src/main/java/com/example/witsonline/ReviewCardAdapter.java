package com.example.witsonline;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ReviewCardAdapter extends RecyclerView.Adapter<ReviewCardAdapter.ViewHolder> {

    //For the dialog to view student's profile
    private Context context;
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private Button btnView, btnCancel;

    //List to store all Courses
    ArrayList<ReviewV>  reviewVs;

    //Constructor of this class
    public ReviewCardAdapter(ArrayList<ReviewV> requestVs, Context context ){
        super();
        //Getting all requests
        this.reviewVs = requestVs;
        this.context = context;
    }

    @Generated
    public void createNewViewDialog(){
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
                Intent intent5 = new Intent(context,UserDetails.class);
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


    @RequiresApi(api = Build.VERSION_CODES.M)
    @NonNull
    @Override
    @Generated
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.review_card, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    @Generated
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //Getting the particular item from the list
        final ReviewV reviewV =  reviewVs.get(position);

        //Showing data on the view
        holder.studentName.setText(reviewV.getStudentFName() +" "+ reviewV.getStudentLName());
        holder.reviewDescription.setText(reviewV.getReviewDescription());
        if(reviewV.getReviewRating()!=null) {
            holder.reviewRating.setRating(Float.parseFloat(reviewV.getReviewRating()));
        }

    }

    @Override
    @Generated
    public int getItemCount() {
        return reviewVs.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        //Views
        public TextView studentName;
        public TextView reviewDescription;
        public RatingBar reviewRating;

        //Initializing Views
        @RequiresApi(api = Build.VERSION_CODES.M)
        public ViewHolder(View itemView) {
            super(itemView);
            studentName = (TextView) itemView.findViewById(R.id.studentName);
            studentName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Intent intent = new Intent(v.getContext(),UserDetails.class);
                    //v.getContext().startActivity(intent);
                    createNewViewDialog();
                }
            });
            reviewDescription = (TextView) itemView.findViewById(R.id.reviewDescription);
            reviewRating = (RatingBar) itemView.findViewById(R.id.reviewRating);

        }
    }
}
