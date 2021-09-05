package com.example.witsonline;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class StudentCardAdapter extends RecyclerView.Adapter<StudentCardAdapter.ViewHolder>{

    //For the dialog to view student's profile
    private Context context;
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private Button btnView, btnCancel;

    //List to store all Courses
    ArrayList<StudentV> studentVs;

    public StudentCardAdapter(ArrayList<StudentV> requestVs, Context context){
        super();
        //Getting all requests
        this.studentVs = requestVs;
        this.context = context;

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @NonNull
    @Override
    @Generated
    public StudentCardAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.student_card, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    @Generated
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //Getting the particular item from the list
        final StudentV studentV = studentVs.get(position);

        //Showing data on the views

        holder.studentNumber.setText(studentV.getStudentNumber());
        holder.studentName.setText(studentV.getStudentFName() +" "+ studentV.getStudentLName());
        holder.tutorState.setText(studentV.getTutorState());
        if(studentV.getTutorState().equals("1")){
            holder.assignTutor.setText("Un-assign Tutor");

        }
    }

    @Generated
    public void createNewViewProfileDialog(TextView studNumber){
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
                STUDENT.number = studNumber.getText().toString();
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
    public int getItemCount() { return studentVs.size(); }


    @Generated
    public class ViewHolder extends RecyclerView.ViewHolder{
        //views
        public TextView studentName;
        public TextView studentNumber;
        public Button assignTutor;
        public TextView tutorState;

        //Initializing Views

        public ViewHolder(View itemView){
            super(itemView);
            studentNumber = (TextView) itemView.findViewById(R.id.studentCardNumber);
            studentName = (TextView) itemView.findViewById(R.id.studentCardName);
            studentName.setOnClickListener(new View.OnClickListener() {
                @Override
                @Generated
                public void onClick(View v) {
                    createNewViewProfileDialog(studentNumber);
                }
            });
            assignTutor = (Button) itemView.findViewById(R.id.assignTutorButton);
            tutorState = (TextView) itemView.findViewById(R.id.studentCardTutor);

            assignTutor.setOnClickListener(new View.OnClickListener() {
                @Override
                @Generated
                public void onClick(View v) {
                    STUDENT.number = studentNumber.getText().toString();
                    if(tutorState.getText().toString().equals("1")){
                        try {
                            doPostRequest("unAssignTutor.php");
                            COURSE.TUTORS.remove(STUDENT.number);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    else{
                        try {
                            doPostRequest("assignTutor.php");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    Intent i = new Intent(context, BrowseStudents.class);
                    context.startActivity(i);
                }
            });

        }
    }
    @Generated
    private void doPostRequest(String phpFile) throws IOException {
        OkHttpClient client = new OkHttpClient();

        HttpUrl.Builder urlBuilder = HttpUrl.parse("https://lamp.ms.wits.ac.za/~s2105624/" + phpFile).newBuilder();
        urlBuilder.addQueryParameter("studentNumber",STUDENT.number);
        urlBuilder.addQueryParameter("courseCode", COURSE.CODE);
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
                        Toast.makeText(context, "Update successful", Toast.LENGTH_SHORT).show();
                        /*
                        if (btnViewDialogSubscribe != null) {
                            if (responseData.trim().equals("subscribed")) {
                                btnViewDialogSubscribe.setText("UNSUBSCRIBE");
                            }
                            if(progressBar!=null){
                                btnViewDialogSubscribe.setVisibility(View.VISIBLE);
                                btnViewDialogViewCourse.setVisibility(View.VISIBLE);
                                progressBar.setVisibility(View.GONE);
                            }
                        }*/

                    }
                });
            }
        });
    }
}
