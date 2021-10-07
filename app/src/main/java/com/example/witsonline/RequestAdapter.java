package com.example.witsonline;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
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

import com.bumptech.glide.Glide;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class RequestAdapter extends  RecyclerView.Adapter<RequestAdapter.ViewHolder>{
    private Context context;
    //List to store all Courses
    ArrayList<RequestV> requestVs;
    //Constructor of this class

    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private Button btnViewDialogSubscribe;
    private Button btnUnsubscribe, btnCancel;
    private TextView unsubscribeText;
    private Button btnView, btnCancelViewProfile;
    public RequestAdapter(ArrayList<RequestV> requestVs, Context context){
        super();
        //Getting all requests
        this.requestVs = requestVs;
        this.context = context;
    }

    @NonNull
    @Override
    @Generated
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.request_card, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);

        return viewHolder;
    }

    @Override
    @Generated
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //Getting the particular item from the list
        final RequestV requestV = requestVs.get(position);

        //Showing data on the views
        holder.setIsRecyclable(false);
        holder.courseName.setText(requestV.getCourseName());
        holder.courseCode.setText(requestV.getCourseCode());
        holder.studentName.setText(requestV.getStudentFName() + " " + requestV.getStudentLName());
        holder.studentNumber.setText(requestV.getStudentNumber());
        if (!requestV.getStudentImageUrl().equals("null")) {
            Glide.with(context).load(requestV.getStudentImageUrl()).into(holder.image);
        }
    }

    @Override
    @Generated
    public int getItemCount() {
        return requestVs.size();
    }
    @Generated
    class ViewHolder extends RecyclerView.ViewHolder{
        //Views
        public TextView courseName;
        public TextView courseCode;
        public TextView studentName;
        public TextView studentNumber;
        public Button acceptRequest;
        public Button declineRequest;
        public ImageView image;


        //Initializing Views
        public ViewHolder(View itemView) {
            super(itemView);
            courseName = (TextView) itemView.findViewById(R.id.requestCourse);
            courseCode = (TextView) itemView.findViewById(R.id.requestCourseCode);
            studentNumber = (TextView) itemView.findViewById(R.id.requestStudentNumber);
            studentName = (TextView) itemView.findViewById(R.id.requestStudentName);
            studentName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    createNewViewProfileDialog(studentNumber);
                }
            });
            acceptRequest = (Button) itemView.findViewById(R.id.acceptRequestButton);
            declineRequest = (Button) itemView.findViewById(R.id.declineRequestButton);
            image = (ImageView) itemView.findViewById(R.id.requestStudentImage);

            acceptRequest.setOnClickListener(new View.OnClickListener() {
                @Override
                @Generated
                public void onClick(View v) {
                    String studentNo = studentNumber.getText().toString();
                    String courseNo = courseCode.getText().toString();
                    createNewViewDialogUnsubscribe("accept","acceptRequest.php", studentNo, courseNo);

                }
            });

            declineRequest.setOnClickListener(new View.OnClickListener() {
                @Override
                @Generated
                public void onClick(View v) {
                    String studentNo = studentNumber.getText().toString();
                    String courseNo = courseCode.getText().toString();
                    createNewViewDialogUnsubscribe("decline","declineRequest.php", studentNo, courseNo);
                }
            });
        }
    }

    @Generated
    private void doPostRequest(String phpFile, String studentCode, String courseCode) throws IOException {
        OkHttpClient client = new OkHttpClient();

        HttpUrl.Builder urlBuilder = HttpUrl.parse("https://lamp.ms.wits.ac.za/~s2105624/" + phpFile).newBuilder();
        urlBuilder.addQueryParameter("studentNumber",studentCode);
        urlBuilder.addQueryParameter("courseCode", courseCode);
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

                    }
                });
            }
        });
    }

    @Generated
    public void createNewViewDialogUnsubscribe(String acceptOr, String phpFile, String studentCode, String courseCode) {
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

        if (acceptOr.equals("accept")) {
            unsubscribeText.setText("Are you sure you want to accept request?");
            btnUnsubscribe.setText("Accept");
        } else {
            unsubscribeText.setText("Are you sure you want to decline request?");
            btnUnsubscribe.setText("Decline");
        }


        btnUnsubscribe.setOnClickListener(new View.OnClickListener() {
            @Override
            @Generated
            public void onClick(View v) {
                try {
                    doPostRequest(phpFile, studentCode, courseCode);
                    Toast toast = Toast.makeText(context, "Updated", Toast.LENGTH_LONG);
                    toast.show();
                    dialog.dismiss();

                    if(USER.STUDENT){
                        Intent intent = new Intent(context, EnrolmentRequests.class);
                        context.startActivity(intent);
                    }
                    else {
                        Intent intent = new Intent(context, Dashboard.class);
                        context.startActivity(intent);
                    }
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
    public void createNewViewProfileDialog(TextView studentNumber){
        dialogBuilder = new AlertDialog.Builder(context);
        final View viewPopUp = LayoutInflater.from(context)
                .inflate(R.layout.view_profile_dialog, null);

        btnView = (Button) viewPopUp.findViewById(R.id.btnView);
        btnCancelViewProfile = (Button) viewPopUp.findViewById(R.id.btnViewCancel);

        dialogBuilder.setView(viewPopUp);
        dialog = dialogBuilder.create();
        dialog.show();

        btnView.setOnClickListener(new View.OnClickListener() {
            @Override
            @Generated
            public void onClick(View v) {
                STUDENT.number = studentNumber.getText().toString();
                Intent intent5 = new Intent(context,UserDetails.class);
                intent5.putExtra("userType","student");
                context.startActivity(intent5);
                dialog.dismiss();
            }
        });
        btnCancelViewProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            @Generated
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

    }
}
