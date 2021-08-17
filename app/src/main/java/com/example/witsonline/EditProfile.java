package com.example.witsonline;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.util.PatternsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class EditProfile extends AppCompatActivity implements View.OnScrollChangeListener, BottomNavigationView.OnNavigationItemSelectedListener {

    //This is for the logout pop up menu
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private Button btnLogout, btnCancel;

    //Volley Request Queue
    private RequestQueue requestQueue;

    //Creating Views
    private TextInputLayout user, firstName, lastName, email, password, confirmPass;
    private Button btnEditProfile;
    private Switch passwordSwitch;
    private RecyclerView recyclerView;

    //boolean for password update
    boolean passwordUpdateRequired = false;
    String passwordUpdate;

    //This is for the delay while loading the email
    ProgressBar progressBar;
    RelativeLayout relativeLayout;

    //URLs for php
    String studURL = "https://lamp.ms.wits.ac.za/home/s2105624/getStudentProfile.php?unum=";
    String instURL = "https://lamp.ms.wits.ac.za/home/s2105624/getInstructorProfile.php?unum=";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        //Initializing progressbar
        progressBar = findViewById(R.id.editProfileProgressBar);
        relativeLayout = findViewById(R.id.EditProfileLayout);
        progressBar.setVisibility(View.VISIBLE);

        //setup request queue
        requestQueue = Volley.newRequestQueue(this);

        //determine which navbar to use (student or instructor)
        BottomNavigationView dashboardBottomNavigation = findViewById(R.id.dashboardBottomNavigation);
        dashboardBottomNavigation.setOnNavigationItemSelectedListener(EditProfile.this);

        //set edit texts with user info
        user = findViewById(R.id.username);
        user.getEditText().setText(USER.USER_NUM);
        firstName = findViewById(R.id.firstName);
        firstName.getEditText().setText(USER.FNAME);
        lastName = findViewById(R.id.lastName);
        lastName.getEditText().setText(USER.LNAME);
        email = findViewById(R.id.email);
        passwordSwitch = findViewById(R.id.passwordSwitch);
        password = findViewById(R.id.password);
        confirmPass = findViewById(R.id.confirmPassword);

        passwordSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            @Generated
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (passwordSwitch.isChecked()){
                    password.setVisibility(View.VISIBLE);
                    confirmPass.setVisibility(View.VISIBLE);
                    passwordUpdateRequired = true;
                }
                else{
                    password.getEditText().setText("");
                    confirmPass.getEditText().setText("");
                    password.setError(null);
                    confirmPass.setError(null);
                    password.setVisibility(View.GONE);
                    confirmPass.setVisibility(View.GONE);
                    passwordUpdateRequired = false;
                }
            }
        });


        if (USER.STUDENT) {
            dashboardBottomNavigation.inflateMenu(R.menu.menu_student);
            dashboardBottomNavigation.getMenu().findItem(R.id.menuProfileStudent).setChecked(true);

            //set new hint for student
            user.setHint("Student Number");

            //set email
            getStudentData();


        } else {
            dashboardBottomNavigation.inflateMenu(R.menu.menu_instructor);
            dashboardBottomNavigation.getMenu().findItem(R.id.menuProfileInstructor).setChecked(true);

            //set email
            getInstructorData();

        }

        btnEditProfile = findViewById(R.id.buttonEditProfile);
        btnEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            @Generated
            public void onClick(View v) {
                boolean valid = true;
                if (passwordUpdateRequired){
                    passwordUpdate = "password";
                    //validate with password text inputs
                    if (isEmpty(firstName) | isEmpty(lastName) | validateEmail(email) | !validatePassword(password,confirmPass)){
                        valid = false;
                    }
                }
                else{
                    passwordUpdate = "nopassword";
                    //validate without password text inputs
                    if (isEmpty(firstName) | isEmpty(lastName) | validateEmail(email)){
                        valid = false;
                    }
                }

                if (valid){
                    //perfom the update

                }

                }
        });

    }

    // This function checks if a required text is empty or not
    public boolean isEmpty(TextInputLayout text) {
        boolean empty = false;
        if (text.getEditText().getText().toString().isEmpty()) {
            text.setError("Field can't be empty");
            empty = true;
        }
        else{
            text.setError(null);
        }
        return empty;
    }

    //this function validates email
    public boolean validateEmail(TextInputLayout text) {
        boolean valid = true;

        String emailInput = text.getEditText().getText().toString().trim();
        if (emailInput.isEmpty()) {
            text.setError("Field can't be empty");
            valid =  false;
        } else if (!PatternsCompat.EMAIL_ADDRESS.matcher(emailInput).matches()) {
            text.setError("Please enter a valid email address");
            valid = false;
        } else {
            text.setError(null);
            valid = true;
        }
        return valid;
    }

    //This function validates password only
    public boolean validatePassword(TextInputLayout password, TextInputLayout confirmPass){
        boolean valid = true;
        if (password.getEditText().getText().toString().isEmpty()){
            password.setError("Field can't be empty");
            valid = false;
        }
        else if (password.getEditText().getText().toString().length() < 5) {
            password.setError("Password must be at least 5 characters long");
            valid = false;
        }
        else{
            password.setError(null);
        }
        if (confirmPass.getEditText().getText().toString().isEmpty()) {
            confirmPass.setError("Confirm your password");
            valid = false;
        }
        else if (!password.getEditText().getText().toString().equals(confirmPass.getEditText().getText().toString())) {
            confirmPass.setError("Passwords don't match");
            valid = false;
        }
        else {
            confirmPass.setError(null);
        }

        return valid;
    }


    @Generated
    private void getStudentData(){
        //Adding the method to the queue by calling the method getTagData
        requestQueue.add(getStudentDataFromServer());
    }

    @Generated
    private JsonArrayRequest getStudentDataFromServer(){

        //Initializing progressbar
       // final ProgressBar progressBar = (ProgressBar) findViewById(R.id.editProfileProgressBar);

        //Displaying ProgressBar
       // progressBar.setVisibility(View.VISIBLE);
       // setProgressBarIndeterminateVisibility(true);


        //JsonArrayRequest of volley
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(studURL + USER.USER_NUM,
                (response) -> {
                    //Calling method parseData to parse the json response
                    parseStudentData(response);
                    //Hiding the progressBar
                    relativeLayout.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                },
                (error) -> {
                    //Hiding the progressBar
                    relativeLayout.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                    //If an error occurs that means end of the list has been reached
                    //Toast.makeText(CourseHomePage.this, "No More Items Available", Toast.LENGTH_SHORT).show();
                });
        //Returning the request
        return jsonArrayRequest;
    }

    //This method will parse json Data
    @Generated
    private void parseStudentData(JSONArray array){
        for (int i = 0; i< array.length(); i++){

            JSONObject json = null;
            try {
                //Getting json
                json = array.getJSONObject(i);

                //getting student profile data
                USER.EMAIL = json.getString("Student_Email");
                email.getEditText().setText(USER.EMAIL);

            } catch (JSONException e){
                e.printStackTrace();
            }

        }

    }

    @Generated
    private void getInstructorData(){
        //Adding the method to the queue by calling the method getTagData
        requestQueue.add(getInstructorDataFromServer());
    }

    @Generated
    private JsonArrayRequest getInstructorDataFromServer(){

        //Initializing progressbar
        //final ProgressBar progressBar = (ProgressBar) findViewById(R.id.editProfileProgressBar);

        //Displaying ProgressBar
        //progressBar.setVisibility(View.VISIBLE);
       // setProgressBarIndeterminateVisibility(true);


        //JsonArrayRequest of volley
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(instURL + USER.USER_NUM,
                (response) -> {
                    //Calling method parseData to parse the json response
                    parseInstructorData(response);
                    //Hiding the progressBar
                    relativeLayout.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                },
                (error) -> {
                    //Hiding the progressBar
                    relativeLayout.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                    //If an error occurs that means end of the list has been reached
                    //Toast.makeText(CourseHomePage.this, "No More Items Available", Toast.LENGTH_SHORT).show();
                });
        //Returning the request
        return jsonArrayRequest;
    }

    //This method will parse json Data
    @Generated
    private void parseInstructorData(JSONArray array){
        for (int i = 0; i< array.length(); i++){

            JSONObject json = null;
            try {
                //Getting json
                json = array.getJSONObject(i);

                //getting student profile data
                USER.EMAIL = json.getString("Instructor_Email");
                email.getEditText().setText(USER.EMAIL);

            } catch (JSONException e){
                e.printStackTrace();
            }

        }

    }

    @Generated
    public void createNewViewDialog(){
        dialogBuilder = new AlertDialog.Builder(this);
        final View viewPopUp = LayoutInflater.from(this)
                .inflate(R.layout.logout_dialog, null);

        btnLogout = (Button) viewPopUp.findViewById(R.id.btnLogout);
        btnCancel = (Button) viewPopUp.findViewById(R.id.btnLogoutCancel);

        dialogBuilder.setView(viewPopUp);
        dialog = dialogBuilder.create();
        dialog.show();

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            @Generated
            public void onClick(View v) {
                Intent intent5 = new Intent(EditProfile.this,LoginActivity.class);
                startActivity(intent5);
                finish();
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
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            // STUDENT MENU
            case R.id.menuHomeStudent :
                Intent intent = new Intent(EditProfile.this, Dashboard.class);
                startActivity(intent);
                finish();
                break;

            case R.id.menuMyCoursesStudent :
                Intent intent2 = new Intent(EditProfile.this, MyCourses.class);
                startActivity(intent2);
                finish();
                break;

            case R.id.menuBrowseCourses :
                Intent intent1 = new Intent(EditProfile.this, BrowseCourses.class);
                startActivity(intent1);
                finish();
                break;

            case R.id.menuLogOutStudent :
                createNewViewDialog();
                break;

            case R.id.menuHomeInstructor :
                Intent intent5 = new Intent(EditProfile.this, Dashboard.class);
                startActivity(intent5);
                finish();
                break;

            case R.id.menuMyCoursesInstructor :
                Intent intent3 = new Intent(EditProfile.this, MyCourses.class);
                startActivity(intent3);
                finish();
                break;

            case R.id.menuCreateCourse :
                Intent intent4 = new Intent(EditProfile.this, CreateCourse.class);
                startActivity(intent4);
                finish();
                break;

            case R.id.menuLogOutInstructor :
                createNewViewDialog();
                break;

            case R.id.menuProfileStudent:
                break;

            case R.id.menuProfileInstructor:
                break;
        }

        return false;
    }

    //This method will check if the recyclerview has reached the bottom or not
    public boolean isLastItemDistplaying(RecyclerView recyclerView){
        if(recyclerView.getAdapter().getItemCount() != 0){
            int lastVisibleItemPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastCompletelyVisibleItemPosition();
            if (lastVisibleItemPosition != RecyclerView.NO_POSITION && lastVisibleItemPosition == recyclerView.getAdapter().getItemCount() -1){
                return true;
            }
        }
        return false;
    }

    @Override
    @Generated
    public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
        //if Scrolled at last then
        if(isLastItemDistplaying(recyclerView)){
            //Calling the method getData again
        }
    }
}
