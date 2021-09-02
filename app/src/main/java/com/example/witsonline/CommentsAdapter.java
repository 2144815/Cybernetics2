package com.example.witsonline;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
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
import java.util.HashMap;
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

    //to view student's profile
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private Button btnView, btnCancel;
    private HashMap<String, String> usernames = new HashMap<>();

    private RequestQueue requestQueue;

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
        final Comment comment = commentList.get(position);
        //here we can create clickListerns and assign values
        //onBindViewHolder(): RecyclerView calls this method to associate a ViewHolder with data.
        // The method fetches the appropriate data and uses the data to fill in the view holder's layout. For example, if the RecyclerView displays a list of names,
        // the method might find the appropriate name in the list and fill in the view holder's TextView widget.
        holder.TheStudentName.setText(commentList.get((holder.getAdapterPosition())).getUserFullName());
        holder.TheAnswer.setText(commentList.get((holder.getAdapterPosition())).getComment());
        requestQueue = Volley.newRequestQueue(context);
        holder.role.setText(comment.getUserRole());
        holder.id.setText(comment.getId());
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

        usernames.put(comment.getId(),comment.getUsername());


    }

    public void UpdateReplies() {

    }
    //This method will parse json Data

    @Generated
    public void createNewViewProfileDialog(TextView replyID, TextView role) {

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
                if (role.getText().toString().equals("Instructor")) {
                    INSTRUCTOR.USERNAME = usernames.get(replyID.getText().toString());
                    Intent intent5 = new Intent(context, UserDetails.class);
                    intent5.putExtra("userType", "instructor");
                    context.startActivity(intent5);
                    dialog.dismiss();
                } else {
                    STUDENT.number = usernames.get(replyID.getText().toString());
                    Intent intent5 = new Intent(context, UserDetails.class);
                    intent5.putExtra("userType", "student");
                    context.startActivity(intent5);
                    dialog.dismiss();
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
        TextView id;
        AppCompatImageButton upvote;
        AppCompatImageButton downVote;


        public MyViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            id = itemView.findViewById(R.id.replyID);
            role = itemView.findViewById(R.id.role);
            TheStudentName = itemView.findViewById(R.id.tv_studentFullName);
            TheStudentName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    createNewViewProfileDialog(id, role);
                }
            });
            TheTime = itemView.findViewById(R.id.time);
            TheAnswer = itemView.findViewById(R.id.Answer);
            NoVotes = itemView.findViewById(R.id.tv_NoVotes);
            upvote = itemView.findViewById(R.id.btn_Upvote);
            downVote = itemView.findViewById(R.id.btn_downVote);

        }
    }

}
