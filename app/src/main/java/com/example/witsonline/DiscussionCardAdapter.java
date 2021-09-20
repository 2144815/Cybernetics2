package com.example.witsonline;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RatingBar;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class DiscussionCardAdapter extends RecyclerView.Adapter<DiscussionCardAdapter.ViewHolder> {
    private Context context;

    //List to store all Courses
    ArrayList<Discussion> discussions;
    private PopupMenu popup;

    //to view student's profile
    private AlertDialog.Builder dialogBuilder;
    public TextInputEditText EditedTopic;
    public TextInputEditText EditedText;
    public String Discussion_id;
    public Button postEditDiscussion;
    private AlertDialog dialog;
    private Button btnView, btnCancel;
    private HashMap<String, String> studentNums = new HashMap<>();

    private RequestQueue requestQueue;
    private String discussionUpdateURL = "https://lamp.ms.wits.ac.za/home/s2105624/updateDiscussion.php";

    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");

    //Constructor of this class
    public DiscussionCardAdapter(ArrayList<Discussion> discussions, Context context) {
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
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        //Getting the particular item from the list
        final Discussion discussion = discussions.get(position);
        studentNums.put(discussion.getDiscussionID(), discussion.getDiscussionStudentNumber());

        //Showing data on the views
        holder.setIsRecyclable(false);
        holder.startedBy.setText(discussion.getDiscussionStudent());
        //  holder.numberOfReplies.setText(discussion.getDiscussionReplies());
        holder.status.setText(discussion.getDiscussionStatus());
        holder.text.setText(discussion.getDiscussionText());
        holder.topic.setText(discussion.getDiscussionTopic());
        holder.id.setText(discussion.getDiscussionID());
        holder.time.setText(format.format(discussion.getDiscussionDate()));
        holder.numberOfReplies.setText(Integer.toString(discussion.getDiscussionReplies()));

        /*if (discussion.getDiscussionStatus().equals( "Closed" ) && USER.FNAME.equals( discussion.getDiscussionStudent()) == false){
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
        } */

        if (USER.USER_NUM.equals(discussion.getDiscussionStudentNumber())) {
            holder.menu.setOnClickListener(new View.OnClickListener() {
                @Override
                @Generated
                public void onClick(View v) {
                    showPopupMenu(holder.menu, position, holder);
                }
            });
        } else {
            holder.menu.setVisibility(View.GONE);
        }
    }

    @Generated
    public void createNewViewProfileDialog(TextView discussionID) {

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
                Intent intent5 = new Intent(context, UserDetails.class);
                intent5.putExtra("userType", "student");
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

    @Generated
    private void showPopupMenu(View v, int position, ViewHolder holder) {
        PopupMenu popup = new PopupMenu(v.getContext(), v);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.discussion_menu, popup.getMenu());

        if (holder.status.getText().equals("Closed")) {
            popup.getMenu().findItem(R.id.Open_Close_DiscChoice).setTitle("Open Discussion");
            popup.getMenu().findItem(R.id.Edit_DiscChoice).setVisible(false);
        }

        popup.getMenu().findItem(R.id.Edit_DiscChoice).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                androidx.appcompat.app.AlertDialog.Builder dialogBuilder = new androidx.appcompat.app.AlertDialog.Builder(context);

                final View viewPopUp = LayoutInflater.from(context)
                        .inflate(R.layout.edit_discussion_dialog, null);

                dialogBuilder.setView(viewPopUp);
                androidx.appcompat.app.AlertDialog dialog = dialogBuilder.create();

                requestQueue = Volley.newRequestQueue(context);

                postEditDiscussion = (Button) viewPopUp.findViewById(R.id.postEditDiscussion);
                EditedTopic = (TextInputEditText) viewPopUp.findViewById(R.id.EditDiscussionTopic);
                EditedText = (TextInputEditText) viewPopUp.findViewById(R.id.EditDiscussionText);

                EditedTopic.setText(holder.topic.getText());
                EditedText.setText(holder.text.getText());
                Discussion_id = holder.id.getText().toString();

                dialog.show();

                postEditDiscussion.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(!EditedTopic.getText().toString().isEmpty() && !EditedText.getText().toString().isEmpty()) {
                            StringRequest request = new StringRequest(Request.Method.POST, discussionUpdateURL, new Response.Listener<String>() {
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
                                    parameters.put("Discussion_Student", USER.USER_NUM);
                                    parameters.put("Discussion_Id", Discussion_id);
                                    parameters.put("Discussion_Topic", EditedTopic.getText().toString());
                                    parameters.put("Discussion_Text", EditedText.getText().toString());
                                    return parameters;
                                }
                            };
                            requestQueue.add(request);
                            Toast.makeText(context, "Discussion updated successfully", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                            Intent intent = new Intent(context, ForumActivity.class);
                            context.startActivity(intent);
                        }else {
                            Toast.makeText(context, "Please fill in all details", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                return true;
            }
        });
        popup.setOnMenuItemClickListener(new DiscMenuClickListener(position, context, holder, discussions));
        popup.show();
    }

    @Override
    @Generated
    public int getItemCount() {
        return discussions.size();
    }

    @Generated
    class ViewHolder extends RecyclerView.ViewHolder {
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
            time = (TextView) itemView.findViewById(R.id.timeHolder);
            numberOfReplies = (TextView) itemView.findViewById(R.id.numberOfReplies);
            startedBy.setOnClickListener(new View.OnClickListener() {
                @Override
                @Generated
                public void onClick(View v) {
                    createNewViewProfileDialog(id);
                }
            });
            numberOfReplies = (TextView) itemView.findViewById(R.id.numberOfReplies);
            status = (TextView) itemView.findViewById(R.id.status);
            menu = itemView.findViewById(R.id.disc_menu);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                @Generated
                public void onClick(View view) {
                    DISCUSSIONS.DISCUSSION_ID = id.getText().toString();
                    DISCUSSIONS.DISCUSSION_STUDENT = startedBy.getText().toString();
                    DISCUSSIONS.DISCUSSION_STUDENT_NUMBER = studentNums.get(DISCUSSIONS.DISCUSSION_ID);
                    DISCUSSIONS.DISCUSSION_TEXT = text.getText().toString();
                    DISCUSSIONS.DISCUSSION_TOPIC = topic.getText().toString();
                    DISCUSSIONS.DISCUSSION_NUM_REPLIES = numberOfReplies.getText().toString();
                    try {
                        DISCUSSIONS.DISCUSSION_DATE = format.parse(time.getText().toString());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    if ((status.getText().toString()).equals("Closed")) {
                        DISCUSSIONS.DISCUSSION_STATUS = 0;
                    } else {
                        DISCUSSIONS.DISCUSSION_STATUS = 1;
                    }

                    Intent i = new Intent(context, ADiscussion.class);
                    context.startActivity(i);
                }

            });
        }
    }
}
