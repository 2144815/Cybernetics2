package com.example.witsonline;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Permissions extends AppCompatActivity {
    CheckBox tutorRequests;

    String FetchUrl = "https://lamp.ms.wits.ac.za/~s2105624/load_permissions.php";
    String PermissionString;
    AlertDialog dialog;
    Button yes;
    Button no;
    TextView dialog_text;

    JSONObject jsonObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView(R.layout.permissions);
        tutorRequests = findViewById( R.id.checkBox_enrolment_requests );


        Bundle extras = getIntent().getExtras();
        PermissionString = extras.getString( "P" );
        try {
            parsePermissionData( PermissionString );
        } catch (JSONException e) {
            e.printStackTrace();
        }

        AlertDialog.Builder dialogBuilder= new AlertDialog.Builder(this);

        final View viewPopUp = LayoutInflater.from(this)
                .inflate(R.layout.allow_permission_dialog, null);
        dialogBuilder.setView(viewPopUp);
        dialog = dialogBuilder.create();
        yes = (Button)viewPopUp.findViewById( R.id.btn_permission_yes );
        no = (Button)viewPopUp.findViewById( R.id.btn_permission_no );
        dialog_text = (TextView)viewPopUp.findViewById( R.id.dialog_question_permission );



        tutorRequests.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    onCheckboxClicked( v );
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
            }
        } );

    }



    public void onCheckboxClicked(View view) throws IOException, JSONException {
        // Is the view now checked?
        boolean checked = ((CheckBox) view).isChecked();

        // Check which checkbox was clicked
        switch(view.getId()) {
            case R.id.checkBox_enrolment_requests:
     

                if (checked){
                    if(CheckCourse(COURSE.CODE)){
                        dialog_text.setText( "Do you want to enable this permission?" );
                        dialog.show();
                        yes.setOnClickListener( new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                try {
                                    doPostRequest( "Update_Tutor_Permission.php", "1" );
                                    getPermissionDataFromServer();

                                } catch (IOException e) {
                                    e.printStackTrace();
                                }


                                Toast.makeText( Permissions.this, "Permission enabled", Toast.LENGTH_SHORT ).show();
                                dialog.dismiss();
                            }
                        } );
                        no.setOnClickListener( new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ((CheckBox)findViewById( R.id.checkBox_enrolment_requests )).setChecked( false );
                                dialog.dismiss();

                            }
                        } );




                    }
                    else{

                        dialog.show();
                        yes.setOnClickListener( new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                try {
                                    doPostRequest( "addPermission.php" ,"1");
                                    getPermissionDataFromServer();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                                Toast.makeText( Permissions.this, "Permission enabled", Toast.LENGTH_SHORT ).show();
                                dialog.dismiss();
                            }
                        } );
                        no.setOnClickListener( new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ((CheckBox)findViewById( R.id.checkBox_enrolment_requests )).setChecked( false );
                                dialog.dismiss();
                            }
                        } );
                        parsePermissionData( PermissionString );

                    }

                }
                else if(!checked) {
                    dialog_text.setText( "Do you want to disable this permission?" );
                    dialog.show();

                    yes.setOnClickListener( new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            try {
                                doPostRequest( "Update_Tutor_Permission.php","0" );
                                getPermissionDataFromServer();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            Toast.makeText( Permissions.this, "Permission disabled", Toast.LENGTH_SHORT ).show();
                            dialog.dismiss();
                        }
                    } );
                    no.setOnClickListener( new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ((CheckBox)findViewById( R.id.checkBox_enrolment_requests )).setChecked( true );
                            dialog.dismiss();
                        }
                    } );

                }
        }
    }

    private boolean CheckCourse(String course) throws JSONException {
        boolean exists = false;
        getPermissionDataFromServer();
        JSONObject json = new JSONObject(PermissionString);
        JSONArray jsonArray = json.getJSONArray( "myArr" );
        for (int i = 0; i< jsonArray.length(); i++){

            JSONObject j = jsonArray.getJSONObject( i );
            if(j.getString("courseCode").equals(course)){
                exists = true;
                break;
            }
        }
        return exists;
    }

    @Generated
    private void doPostRequest(String phpFile, String b) throws IOException {
        OkHttpClient client = new OkHttpClient();

        HttpUrl.Builder urlBuilder = HttpUrl.parse("https://lamp.ms.wits.ac.za/~s2105624/" + phpFile).newBuilder();
        urlBuilder.addQueryParameter("CourseCode",COURSE.CODE);
        urlBuilder.addQueryParameter("Permission", b );
        String url = urlBuilder.build().toString();

        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            @Generated
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
            }

            @Override
            @Generated
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                final String responseData = response.body().string();
                if(response.isSuccessful()){

                    Permissions.this.runOnUiThread( new Runnable() {
                        @Override
                        public void run() {

                        }
                    } );
                }
                else{
                    Permissions.this.runOnUiThread( new Runnable() {
                        @Override
                        public void run() {

                        }
                    } );

                }
            }
        });
    }
    private String getPermissionDataFromServer() {
        final String[] PermissionData = new String[1];
        OkHttpClient client = new OkHttpClient();

        HttpUrl.Builder urlBuilder = HttpUrl.parse("https://lamp.ms.wits.ac.za/~s2105624/" + "load_permissions.php").newBuilder();
        String url = urlBuilder.build().toString();

        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            @Generated
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
            }

            @Override
            @Generated
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String responseData = response.body().string();
                if(response.isSuccessful()){

                    Permissions.this.runOnUiThread( new Runnable() {
                        @Override
                        public void run() {
                            //Toast.makeText( CourseHomePageInstructor.this, "Retrieval Successful", Toast.LENGTH_SHORT ).show();
                            UpdateData(responseData) ;

                        }
                    } );
                }
                else{
                    Permissions.this.runOnUiThread( new Runnable() {
                        @Override
                        public void run() {

                        }
                    } );

                }
            }
        });
        return PermissionData[0];
    }

    private void UpdateData(String responseData) {
        PermissionString = responseData;
    }


    private void parsePermissionData(String response) throws JSONException {
        JSONObject json = new JSONObject(response);
        JSONArray jsonArray = json.getJSONArray( "myArr" );
        for (int i = 0; i< jsonArray.length(); i++){

            JSONObject j = jsonArray.getJSONObject( i );

            if(j.getString("courseCode").equals( COURSE.CODE )){
                if(j.getString( "PermissionStatus" ).equals("1")){
                    ((CheckBox)findViewById( R.id.checkBox_enrolment_requests)).setChecked( true );
                }
                else{
                    ((CheckBox)findViewById( R.id.checkBox_enrolment_requests)).setChecked( false );
                }
            }
        }
    }


}
