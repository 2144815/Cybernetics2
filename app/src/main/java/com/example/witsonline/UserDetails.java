package com.example.witsonline;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.textfield.TextInputLayout;

import okhttp3.*;
import okio.Utf8;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
//import org.mindrot.jbcrypt.BCrypt;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.ArrayList;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
@Generated
public class UserDetails extends AppCompatActivity {

    private TextInputLayout user, firstName, lastName, email, Bio;
    private RequestQueue requestQueue;

    String Biography;
    String profilePicUrl;

    //for viewing image
    public ImageView profileImage;
    public ImageView profileImageExtended;

    //This is for the delay while loading the email
    private ProgressBar progressBar;
    private androidx.constraintlayout.widget.ConstraintLayout constraintLayout;

    private String getStudentURL = "https://lamp.ms.wits.ac.za/home/s2105624/getStudentProfile.php?unum=";
    private String getInstructorURL = "https://lamp.ms.wits.ac.za/home/s2105624/getInstructorProfile.php?unum=";

    //for determining the type of user
    Bundle extras;

    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    @Generated
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);

        //Initializing progressbar
        progressBar = findViewById(R.id.userDetailsProgressBar);
        constraintLayout = findViewById(R.id.userDetailsLayout);
        progressBar.setVisibility(View.VISIBLE);

        requestQueue = Volley.newRequestQueue(this);

        user = findViewById(R.id.viewUser);
        firstName = findViewById(R.id.viewFirstName);
        lastName = findViewById(R.id.viewLastName);
        email = findViewById(R.id.viewEmail);
        Bio = findViewById(R.id.viewUserBio);
        profileImage = findViewById(R.id.profileImage);

        extras = getIntent().getExtras();
        String userType = extras.getString("userType");
        if (userType.equals("student")) {
            user.getEditText().setText(STUDENT.number);
            getStudentData();
        }
        else {
            user.setHint("Username"); //since this user is an instructor
            if (INSTRUCTOR.USERNAME != null) {
                user.getEditText().setText(INSTRUCTOR.USERNAME);
                getInstructorData();
            } else {
                Toast.makeText(UserDetails.this, "null", Toast.LENGTH_SHORT).show();
            }
        }

        //image on click
        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            @Generated
            public void onClick(View v) {
                //Intent intent = new Intent(UserDetails.this, viewProfilePic.class);
                //startActivity(intent);
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    @Generated
    private void getStudentData() {
        //Adding the method to the queue by calling the method getTagData
        requestQueue.add(getStudentDataFromServer());
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    @Generated
    private JsonArrayRequest getStudentDataFromServer() {
        //JsonArrayRequest of volley
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(getStudentURL + STUDENT.number,
                (response) -> {
                    //Calling method parseData to parse the json response
                    parseStudentData(response);
                    //Hiding the progressBar
                    constraintLayout.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                },
                (error) -> {
                    //If an error occurs that means user was not found or does not exist
                    Toast.makeText(UserDetails.this, "User not found", Toast.LENGTH_SHORT).show();
                });
        //Returning the request
        return jsonArrayRequest;
    }

    //This method will parse json Data
    @RequiresApi(api = Build.VERSION_CODES.P)
    @Generated
    private void parseStudentData(JSONArray array) {
        for (int i = 0; i < array.length(); i++) {

            JSONObject json = null;
            try {
                //Getting json
                json = array.getJSONObject(i);

                //getting student profile data
                STUDENT.email = json.getString("Student_Email");
                email.getEditText().setText(STUDENT.email);
                STUDENT.fName = json.getString("Student_FName");
                firstName.getEditText().setText(STUDENT.fName);
                STUDENT.lName = json.getString("Student_LName");
                lastName.getEditText().setText(STUDENT.lName);
                Biography = json.getString("Student_Bio");
                profilePicUrl = json.getString("Student_Image");
                if(Biography.equals("null")){
                    Bio.getEditText().setText("Bio has not been set");
                }else{
                    Bio.getEditText().setText(Biography);
                }
                if(!profilePicUrl.equals("null")){
                    Glide.with(this).load(profilePicUrl).into(profileImage);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Generated
    private void getInstructorData() {
        //Adding the method to the queue by calling the method getTagData
        requestQueue.add(getInstructorDataFromServer());
    }

    @Generated
    private JsonArrayRequest getInstructorDataFromServer() {
        //JsonArrayRequest of volley
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(getInstructorURL + INSTRUCTOR.USERNAME,
                (response) -> {
                    //Calling method parseData to parse the json response
                    parseInstructorData(response);
                    //Hiding the progressBar
                    constraintLayout.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                },
                (error) -> {
                    //If an error occurs that means user was not found or does not exist
                    Toast.makeText(UserDetails.this, "USER NOT FOUND OR DOES NOT EXIST", Toast.LENGTH_SHORT).show();
                });
        //Returning the request
        return jsonArrayRequest;
    }

    //This method will parse json Data
    @Generated
    private void parseInstructorData(JSONArray array) {
        for (int i = 0; i < array.length(); i++) {

            JSONObject json = null;
            try {
                //Getting json
                json = array.getJSONObject(i);

                //getting student profile data
                INSTRUCTOR.EMAIL = json.getString("Instructor_Email");
                email.getEditText().setText(INSTRUCTOR.EMAIL);
                INSTRUCTOR.FNAME = json.getString("Instructor_FName");
                firstName.getEditText().setText(INSTRUCTOR.FNAME);
                INSTRUCTOR.LNAME = json.getString("Instructor_LName");
                lastName.getEditText().setText(INSTRUCTOR.LNAME);
                Biography = json.getString("Instructor_Bio");
                if (Biography.equals("null")) {
                    Bio.getEditText().setText("Bio has not been set");
                } else {
                    Bio.getEditText().setText(Biography);
                }
                profilePicUrl = json.getString("Instructor_Image");
                if(!profilePicUrl.equals("null")){
                    Glide.with(this).load(profilePicUrl).into(profileImage);

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}