package com.example.witsonline;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputLayout;

import okhttp3.*;
import okio.Utf8;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
//import org.mindrot.jbcrypt.BCrypt;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.ArrayList;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public class UserDetails extends AppCompatActivity {

    private TextInputLayout user, firstName, lastName, email;
    private RequestQueue requestQueue;

    private String getStudentURL = "https://lamp.ms.wits.ac.za/home/s2105624/getStudentProfile.php?unum=";
    private String getInstructorURL = "https://lamp.ms.wits.ac.za/home/s2105624/getInstructorProfile.php?unum=";

    //for determining the type of user
    Bundle extras;

    @Override
    @Generated
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);

        requestQueue = Volley.newRequestQueue(this);

        user = findViewById(R.id.viewUser);
        firstName = findViewById(R.id.viewFirstName);
        lastName = findViewById(R.id.viewLastName);
        email = findViewById(R.id.viewEmail);

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
    }

    @Generated
    private void getStudentData() {
        //Adding the method to the queue by calling the method getTagData
        requestQueue.add(getStudentDataFromServer());
    }

    @Generated
    private JsonArrayRequest getStudentDataFromServer() {
        //JsonArrayRequest of volley
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(getStudentURL + STUDENT.number,
                (response) -> {
                    //Calling method parseData to parse the json response
                    parseStudentData(response);
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
    private void parseStudentData(JSONArray array) {
        for (int i = 0; i < array.length(); i++) {

            JSONObject json = null;
            try {
                //Getting json
                json = array.getJSONObject(i);

                //getting student profile data
                USER.EMAIL = json.getString("Student_Email");
                email.getEditText().setText(USER.EMAIL);
                USER.FNAME = json.getString("Student_FName");
                firstName.getEditText().setText(USER.FNAME);
                USER.LNAME = json.getString("Student_LName");
                lastName.getEditText().setText(USER.LNAME);

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
                USER.EMAIL = json.getString("Instructor_Email");
                email.getEditText().setText(USER.EMAIL);
                USER.FNAME = json.getString("Instructor_FName");
                firstName.getEditText().setText(USER.FNAME);
                USER.LNAME = json.getString("Instructor_LName");
                lastName.getEditText().setText(USER.LNAME);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
