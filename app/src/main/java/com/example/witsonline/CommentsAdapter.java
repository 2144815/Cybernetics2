package com.example.witsonline;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.MyViewHolder> {
    List<Comment> commentList;
    Context context;

    //to edit replies
    public TextInputEditText EditedReply;
    public String Reply_id;
    public Button postEditComment;
    public Button btnEditComment;
    public Button btnCancelComment;

    //to view student's profile
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private Button btnView, btnCancel;
    private HashMap<String, String> usernames = new HashMap<>();
    private RequestQueue requestQueue;
    private String instReplyUpdateURL = "https://lamp.ms.wits.ac.za/home/s2105624/updateInstReply.php";
    private String studReplyUpdateURL = "https://lamp.ms.wits.ac.za/home/s2105624/updateStuReply.php";


    public CommentsAdapter(List<Comment> commentList, Context context) {
        this.commentList = commentList;
        this.context = context;
    }

    @NonNull
    @NotNull
    @Override
    @Generated
    public MyViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        //Inflator
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.one_line_comment, parent, false);
        MyViewHolder holder = new MyViewHolder(view);

        return holder;
    }

    @Override
    @Generated
    public void onBindViewHolder(@NonNull @NotNull MyViewHolder holder, int position) {
        final Comment comment = commentList.get(position);
        //here we can create clickListeners and assign values
        //onBindViewHolder(): RecyclerView calls this method to associate a ViewHolder with data.
        // The method fetches the appropriate data and uses the data to fill in the view holder's layout. For example, if the RecyclerView displays a list of names,
        // the method might find the appropriate name in the list and fill in the view holder's TextView widget.
        //edit reply
        if(!COURSE.INSTRUCTOR.equals(comment.getUsername())){
            if(USER.VOTES.containsKey(comment.getId())) {
                holder.votingStatus = USER.VOTES.get(comment.getId());
            }
        }
        else{
            if(USER.INSTRUCTOR_VOTES.containsKey(comment.getId())) {
                holder.votingStatus = USER.INSTRUCTOR_VOTES.get(comment.getId());
            }
        }
        if(holder.votingStatus==1){
            holder.upvote.setImageURI(Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE+
                    "://" + context.getResources().getResourcePackageName(R.drawable.ic_baseline_keyboard_arrow_up_gold_24)+
                    '/' + context.getResources().getResourceTypeName(R.drawable.ic_baseline_keyboard_arrow_up_gold_24) +
                    '/' + context.getResources().getResourceEntryName(R.drawable.ic_baseline_keyboard_arrow_up_gold_24) ));
        }
        if(holder.votingStatus==-1){
            holder.downVote.setImageURI(Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE+
                    "://" + context.getResources().getResourcePackageName(R.drawable.ic_baseline_keyboard_arrow_down_gold_24)+
                    '/' + context.getResources().getResourceTypeName(R.drawable.ic_baseline_keyboard_arrow_down_gold_24) +
                    '/' + context.getResources().getResourceEntryName(R.drawable.ic_baseline_keyboard_arrow_down_gold_24) ));
        }
        holder.TheStudentName.setText(commentList.get((holder.getAdapterPosition())).getUserFullName());
        holder.TheAnswer.setText(commentList.get((holder.getAdapterPosition())).getComment());
        requestQueue = Volley.newRequestQueue(context);
        holder.role.setText(comment.getUserRole());
        holder.id.setText(comment.getId());
        holder.NoVotes.setText((commentList.get(holder.getAdapterPosition())).getNoVotes().toString());
        SimpleDateFormat dtDate = new SimpleDateFormat("dd-MMM-yyyy");
        SimpleDateFormat dtTime = new SimpleDateFormat("HH:mm");
        String strTime = dtDate.format(comment.getTime()) + '\n' + dtTime.format(comment.getTime());
        holder.TheTime.setText(strTime);
        Log.d("VOTES", USER.VOTES.toString());
        holder.upvote.setOnClickListener(new View.OnClickListener() {
            @Override
            @Generated
            public void onClick(View v) {
                Log.d("VOTE", Integer.toString(holder.votingStatus ));
                if(holder.votingStatus ==0) {
                    try {
                        String phpFile = "addVote.php";
                        if (comment.getUsername().equals(COURSE.INSTRUCTOR)) {
                            phpFile = "addVoteInstructor.php";
                        }
                        doPostRequest(phpFile, holder.id.getText().toString(), "1");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    commentList.get((holder.getAdapterPosition())).setNoVotes(Integer.parseInt(holder.NoVotes.getText().toString()) + 1);
                    holder.NoVotes.setText(String.valueOf(commentList.get((holder.getAdapterPosition())).getNoVotes()));
                    holder.votingStatus=1;
                    holder.upvote.setImageURI(Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE+
                            "://" + context.getResources().getResourcePackageName(R.drawable.ic_baseline_keyboard_arrow_up_gold_24)+
                            '/' + context.getResources().getResourceTypeName(R.drawable.ic_baseline_keyboard_arrow_up_gold_24) +
                            '/' + context.getResources().getResourceEntryName(R.drawable.ic_baseline_keyboard_arrow_up_gold_24) ));
                    if(!COURSE.INSTRUCTOR.equals(comment.getUsername())){
                        USER.VOTES.replace(comment.getId(),1);
                    }
                    else{
                        USER.INSTRUCTOR_VOTES.replace(comment.getId(),1);
                    }

                }
                else if(holder.votingStatus ==-1) {
                    try {
                        String phpFile = "removeVote.php";
                        if (comment.getUsername().equals(COURSE.INSTRUCTOR)) {
                            phpFile = "removeVoteInstructor.php";
                        }
                        doPostRequest(phpFile, holder.id.getText().toString(), "1");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    holder.downVote.setImageURI(Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE+
                            "://" + context.getResources().getResourcePackageName(R.drawable.ic_baseline_keyboard_arrow_down_24)+
                            '/' + context.getResources().getResourceTypeName(R.drawable.ic_baseline_keyboard_arrow_down_24) +
                            '/' + context.getResources().getResourceEntryName(R.drawable.ic_baseline_keyboard_arrow_down_24) ));
                    commentList.get((holder.getAdapterPosition())).setNoVotes(Integer.parseInt(holder.NoVotes.getText().toString()) + 1);
                    holder.NoVotes.setText(String.valueOf(commentList.get((holder.getAdapterPosition())).getNoVotes()));
                    holder.votingStatus=0;
                    if(!COURSE.INSTRUCTOR.equals(comment.getUsername())){
                        USER.VOTES.replace(comment.getId(),0);
                    }
                    else{
                        USER.INSTRUCTOR_VOTES.replace(comment.getId(),0);
                    }
                }
            }


        });
        holder.downVote.setOnClickListener(new View.OnClickListener() {
            @Override
            @Generated
            public void onClick(View v) {
                Log.d("VOTE", Integer.toString(holder.votingStatus ));
                if(holder.votingStatus ==0) {
                    try {
                        String phpFile = "addVote.php";
                        if (comment.getUsername().equals(COURSE.INSTRUCTOR)) {
                            phpFile = "addVoteInstructor.php";
                        }
                        doPostRequest(phpFile, holder.id.getText().toString(), "-1");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    holder.downVote.setImageURI(Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE+
                            "://" + context.getResources().getResourcePackageName(R.drawable.ic_baseline_keyboard_arrow_down_gold_24)+
                            '/' + context.getResources().getResourceTypeName(R.drawable.ic_baseline_keyboard_arrow_down_gold_24) +
                            '/' + context.getResources().getResourceEntryName(R.drawable.ic_baseline_keyboard_arrow_down_gold_24) ));
                    commentList.get((holder.getAdapterPosition())).setNoVotes(Integer.parseInt(holder.NoVotes.getText().toString()) - 1);
                    holder.NoVotes.setText(String.valueOf(commentList.get((holder.getAdapterPosition())).getNoVotes()));
                    holder.votingStatus=-1;
                    if(!COURSE.INSTRUCTOR.equals(comment.getUsername())){
                        USER.VOTES.replace(comment.getId(),-1);
                    }
                    else{
                        USER.INSTRUCTOR_VOTES.replace(comment.getId(),-1);
                    }
                }
                else if(holder.votingStatus ==1) {
                    try {
                        String phpFile = "removeVote.php";
                        if (comment.getUsername().equals(COURSE.INSTRUCTOR)) {
                            phpFile = "removeVoteInstructor.php";
                        }
                        doPostRequest(phpFile, holder.id.getText().toString(), "-1");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    holder.upvote.setImageURI(Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE+
                            "://" + context.getResources().getResourcePackageName(R.drawable.ic_baseline_keyboard_arrow_up_24)+
                            '/' + context.getResources().getResourceTypeName(R.drawable.ic_baseline_keyboard_arrow_up_24) +
                            '/' + context.getResources().getResourceEntryName(R.drawable.ic_baseline_keyboard_arrow_up_24) ));

                    commentList.get((holder.getAdapterPosition())).setNoVotes(Integer.parseInt(holder.NoVotes.getText().toString()) - 1);
                    holder.NoVotes.setText(String.valueOf(commentList.get((holder.getAdapterPosition())).getNoVotes()));
                    holder.votingStatus=0;
                    if(!COURSE.INSTRUCTOR.equals(comment.getUsername())){
                        USER.VOTES.replace(comment.getId(),0);
                    }
                    else{
                        USER.INSTRUCTOR_VOTES.replace(comment.getId(),0);
                    }
                }
            }
        });

        //view profile
        usernames.put(comment.getId(),comment.getUsername());



    }

    @Generated
    public void getReplyDialog(TextView id, TextView TheAnswer) {

        dialogBuilder = new AlertDialog.Builder(context);
        final View viewPopUp = LayoutInflater.from(context)
                .inflate(R.layout.get_comment_dialog, null);

        btnEditComment = (Button) viewPopUp.findViewById(R.id.btnEdit);
        btnCancelComment = (Button) viewPopUp.findViewById(R.id.btnEditCancel);

        dialogBuilder.setView(viewPopUp);
        dialog = dialogBuilder.create();
        dialog.show();

        btnEditComment.setOnClickListener(new View.OnClickListener() {
            @Override
            @Generated
            public void onClick(View v) {
                androidx.appcompat.app.AlertDialog.Builder dialogBuilder = new androidx.appcompat.app.AlertDialog.Builder(context);
                final View viewPopUp = LayoutInflater.from(context)
                        .inflate(R.layout.edit_comment_dialog, null);

                dialogBuilder.setView(viewPopUp);
                androidx.appcompat.app.AlertDialog dialog = dialogBuilder.create();

                requestQueue = Volley.newRequestQueue(context);

                postEditComment = (Button) viewPopUp.findViewById(R.id.postEditComment);

                EditedReply = (TextInputEditText) viewPopUp.findViewById(R.id.EditCommentText);
                EditedReply.setText(TheAnswer.getText());

                Reply_id = id.getText().toString();

                dialog.show();

                postEditComment.setOnClickListener(new View.OnClickListener() {
                    @Override
                    @Generated
                    public void onClick(View view) {
                        if(!EditedReply.getText().toString().isEmpty()) {
                            if(USER.STUDENT) {
                                StringRequest request = new StringRequest(Request.Method.POST, studReplyUpdateURL, new Response.Listener<String>() {
                                    @Override
                                    @Generated
                                    public void onResponse(String response) {
                                        System.out.println(response);
                                    }
                                }, new Response.ErrorListener() {
                                    @Override
                                    @Generated
                                    public void onErrorResponse(VolleyError error) {
                                        System.out.println(error.getMessage());
                                    }
                                }) {
                                    @Override
                                    @Generated
                                    protected Map<String, String> getParams() throws AuthFailureError {
                                        Map<String, String> parameters = new HashMap<>();
                                        parameters.put("Reply_Student", USER.USER_NUM);
                                        parameters.put("Reply_Id", id.getText().toString());
                                        parameters.put("Reply_Discussion", DISCUSSIONS.DISCUSSION_ID);
                                        parameters.put("Reply_Text", EditedReply.getText().toString());
                                        return parameters;
                                    }
                                };
                                requestQueue.add(request);
                                Toast.makeText(context, "Reply edited successfully", Toast.LENGTH_SHORT).show();
                            } else {
                                StringRequest request = new StringRequest(Request.Method.POST, instReplyUpdateURL, new Response.Listener<String>() {
                                    @Override
                                    @Generated
                                    public void onResponse(String response) {
                                        System.out.println(response);
                                    }
                                }, new Response.ErrorListener() {
                                    @Override
                                    @Generated
                                    public void onErrorResponse(VolleyError error) {
                                        System.out.println(error.getMessage());
                                    }
                                }) {
                                    @Override
                                    @Generated
                                    protected Map<String, String> getParams() throws AuthFailureError {
                                        Map<String, String> parameters = new HashMap<>();
                                        parameters.put("Reply_Instructor", USER.USER_NUM);
                                        parameters.put("Reply_Id", id.getText().toString());
                                        parameters.put("Reply_Discussion", DISCUSSIONS.DISCUSSION_ID);
                                        parameters.put("Reply_Text", EditedReply.getText().toString());
                                        return parameters;
                                    }
                                };
                                requestQueue.add(request);
                                Toast.makeText(context, "Reply edited successfully", Toast.LENGTH_SHORT).show();
                            }
                            dialog.dismiss();
                            Intent intent = new Intent(context, ADiscussion.class);
                            context.startActivity(intent);
                        }else {
                            Toast.makeText(context, "Reply cannot be empty", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
        btnCancelComment.setOnClickListener(new View.OnClickListener() {
            @Override
            @Generated
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

    }
    private void doPostRequest(String phpFile, String id, String vote) throws IOException {
        OkHttpClient client = new OkHttpClient();

        HttpUrl.Builder urlBuilder = HttpUrl.parse("https://lamp.ms.wits.ac.za/~s2105624/" + phpFile).newBuilder();
        urlBuilder.addQueryParameter("username", USER.USERNAME);
        urlBuilder.addQueryParameter("reply", id);
        urlBuilder.addQueryParameter("vote", vote);
        String url = urlBuilder.build().toString();

        okhttp3.Request request = new okhttp3.Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            private Activity cont = (Activity) context;

            @Override
            @Generated
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
            }

            @Override
            @Generated
            public void onResponse(@NotNull Call call, @NotNull okhttp3.Response response) throws IOException {
                final String responseData = response.body().string();
                cont.runOnUiThread(new Runnable() {
                    @Override
                    @Generated
                    public void run() {
                        if (responseData.trim().equals("Successful")) {
                        }
                    }
                });
            }
        });
    }
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

    @Generated
    public class MyViewHolder extends RecyclerView.ViewHolder {
        //this is a reference to the one line layout
        TextView TheStudentName;
        TextView TheTime;
        TextView TheAnswer;
        TextView NoVotes;
        TextView role;
        TextView id;
        Integer votingStatus=0;
        AppCompatImageButton upvote;
        AppCompatImageButton downVote;


        public MyViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            id = itemView.findViewById(R.id.replyID);
            role = itemView.findViewById(R.id.role);
            TheStudentName = itemView.findViewById(R.id.tv_studentFullName);
            TheStudentName.setOnClickListener(new View.OnClickListener() {
                @Override
                @Generated
                public void onClick(View v) {
                    createNewViewProfileDialog(id, role);
                }
            });
            TheTime = itemView.findViewById(R.id.time);
            TheAnswer = itemView.findViewById(R.id.Answer);
            TheAnswer.setSoundEffectsEnabled(false);
            TheAnswer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    @Generated
                    public void onClick(View view) {
                        if (usernames.get(id.getText().toString()).equals(USER.USERNAME)) {
                        getReplyDialog(id, TheAnswer);
                        }
                        // Log.d("USERNAME", commentList.get(holder.getAdapterPosition()).getUsername());
                        // Log.d("HOLDER USERNAME", holder.TheStudentName.getText().toString());
                        // Log.d("USERNAME", comment.getUsername());
                        // Log.d("USER USERNAME", USER.USERNAME);
                    }
            });
            NoVotes = itemView.findViewById(R.id.tv_NoVotes);
            upvote = itemView.findViewById(R.id.btn_Upvote);
            downVote = itemView.findViewById(R.id.btn_downVote);
        }
    }

}
