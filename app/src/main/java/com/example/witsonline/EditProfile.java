package com.example.witsonline;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.util.PatternsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class EditProfile extends AppCompatActivity implements View.OnScrollChangeListener, BottomNavigationView.OnNavigationItemSelectedListener {

    //This is for the logout pop up menu
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private Button btnLogout, btnCancel;

    //Volley Request Queue
    private RequestQueue requestQueue;

    //Creating Views
    private TextInputLayout user, firstName, lastName, email,bio, password, confirmPass;
    private ImageView image;
    private Button btnEditProfile;
    private Switch passwordSwitch;
    private RecyclerView recyclerView;

    //for editing image
    public static final int IMAGE_REQUEST_CODE = 3;
    public static final int STORAGE_PERMISSION_CODE = 123;
    private boolean imgChanged = false;
    private Bitmap bitmap;
    private Uri filePath;

    //boolean for password update
    boolean passwordUpdateRequired = false;
    private String passwordUpdate;

    //This is for the delay while loading the email
    private ProgressBar progressBar;
    private RelativeLayout relativeLayout;

    //URLs for php
    private String studURL = "https://lamp.ms.wits.ac.za/home/s2105624/getStudentProfile.php?unum=";
    private String instURL = "https://lamp.ms.wits.ac.za/home/s2105624/getInstructorProfile.php?unum=";
    private String studUpdateURL = "https://lamp.ms.wits.ac.za/home/s2105624/studentProfileUpdate.php";
    private String instUpdateURL = "https://lamp.ms.wits.ac.za/home/s2105624/instructorProfileUpdate.php";


    @Override
    @Generated
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        requestStoragePermission();

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
        bio = findViewById(R.id.bio);
        image = findViewById(R.id.profileEditImage);
        passwordSwitch = findViewById(R.id.passwordSwitch);
        password = findViewById(R.id.password);
        confirmPass = findViewById(R.id.confirmPassword);

        //image on click
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            @Generated
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Complete action using"), IMAGE_REQUEST_CODE);
            }
        });

        passwordSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            @Generated
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (passwordSwitch.isChecked()) {
                    password.setVisibility(View.VISIBLE);
                    confirmPass.setVisibility(View.VISIBLE);
                    passwordUpdateRequired = true;
                } else {
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
                if (passwordUpdateRequired) {
                    passwordUpdate = "pass";
                    //validate with password text inputs
                    if (isEmpty(firstName) | isEmpty(lastName) | !validEmail(email) | !validPassword(password, confirmPass)) {
                        valid = false;
                    }
                    else{
                        valid = true;
                    }
                } else {
                    passwordUpdate = "nopass";
                    //validate without password text inputs
                    if (isEmpty(firstName) | isEmpty(lastName) | !validEmail(email) | isEmpty(bio)) {
                        valid = false;
                    }
                    else{
                        valid = true;
                    }
                }

                //store string version of image if updated
                String bm = "nofile";
                if (imgChanged){
                    bm = getStringImage(bitmap);
                }
                final String file = bm;

                if (valid) {
                    //perfom the update
                    if (USER.STUDENT) {
                        //update student
                        StringRequest request = new StringRequest(Request.Method.POST, studUpdateURL, new Response.Listener<String>() {
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
                                parameters.put("unum", USER.USER_NUM);
                                parameters.put("fname", firstName.getEditText().getText().toString());
                                parameters.put("lname", lastName.getEditText().getText().toString());
                                parameters.put("email", email.getEditText().getText().toString());
                                parameters.put("bio", bio.getEditText().getText().toString());
                                parameters.put("bitmap", file);
                                parameters.put("passreq", passwordUpdate);
                                parameters.put("pass", password.getEditText().getText().toString());
                                return parameters;
                            }
                        };
                        requestQueue.add(request);
                        Toast.makeText(EditProfile.this, "Profile update successful", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        //update instructor
                        StringRequest request = new StringRequest(Request.Method.POST, instUpdateURL, new Response.Listener<String>() {
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
                                parameters.put("unum", USER.USER_NUM);
                                parameters.put("fname", firstName.getEditText().getText().toString());
                                parameters.put("lname", lastName.getEditText().getText().toString());
                                parameters.put("email", email.getEditText().getText().toString());
                                parameters.put("bio", bio.getEditText().getText().toString());
                                parameters.put("bitmap", file);
                                parameters.put("passreq", passwordUpdate);
                                parameters.put("pass", password.getEditText().getText().toString());
                                return parameters;
                            }
                        };
                        requestQueue.add(request);
                        Toast.makeText(EditProfile.this, "Profile update successful", Toast.LENGTH_SHORT).show();
                    }
                }
        }
    });

}

    //getting and setting bitmap
    @Override
    @Generated
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMAGE_REQUEST_CODE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
        }
        if (filePath != null) {
            imgChanged = true;
            try {
                ImageDecoder.Source source = ImageDecoder.createSource(this.getContentResolver(), filePath);
                bitmap = ImageDecoder.decodeBitmap(source);
                image.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{
            imgChanged = false;
        }
    }

    //getting the bitmap of image and encoding it as a string
    @Generated
    private String getStringImage(Bitmap bitmap) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] b = baos.toByteArray();
        final String temp = Base64.encodeToString(b, Base64.DEFAULT);
        Log.i("My_data_image", "" + temp);
        return temp;
    }

    @Generated
    public void requestStoragePermission(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
            return;
        }
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.READ_EXTERNAL_STORAGE)){
            //can explain here why you need this permission.
        }
        //ask for permission
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},STORAGE_PERMISSION_CODE);
    }

    //This method will be called when user taps on allow or deny
    @Override
    @Generated
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){

        //Checking if request code is our request
        if (requestCode == STORAGE_PERMISSION_CODE){

            //if granted
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this, "Permission granted",Toast.LENGTH_LONG).show();
            }
            else{
                Toast.makeText(this, "Permission denied",Toast.LENGTH_LONG).show();
            }
        }
    }

    // This function checks if a required text is empty or not
    public boolean isEmpty(TextInputLayout text) {
        boolean empty = false;
        if (text.getEditText().getText().toString().isEmpty()) {
            text.setError("Field can't be empty");
            empty = true;
        } else {
            text.setError(null);
        }
        return empty;
    }

    //this function validates email
    public boolean validEmail(TextInputLayout text) {
        boolean valid = true;

        String emailInput = text.getEditText().getText().toString().trim();
        if (emailInput.isEmpty()) {
            text.setError("Field can't be empty");
            valid = false;
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
    public boolean validPassword(TextInputLayout password, TextInputLayout confirmPass) {
        boolean valid = true;
        if (password.getEditText().getText().toString().isEmpty()) {
            password.setError("Field can't be empty");
            valid = false;
        } else if (password.getEditText().getText().toString().length() < 5) {
            password.setError("Password must be at least 5 characters long");
            valid = false;
        } else {
            password.setError(null);
        }
        if (confirmPass.getEditText().getText().toString().isEmpty()) {
            confirmPass.setError("Confirm your password");
            valid = false;
        } else if (!password.getEditText().getText().toString().equals(confirmPass.getEditText().getText().toString())) {
            confirmPass.setError("Passwords don't match");
            valid = false;
        } else {
            confirmPass.setError(null);
        }

        return valid;
    }


    @Generated
    private void getStudentData() {
        //Adding the method to the queue by calling the method getTagData
        requestQueue.add(getStudentDataFromServer());
    }

    @Generated
    private JsonArrayRequest getStudentDataFromServer() {

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
    private void parseStudentData(JSONArray array) {
        for (int i = 0; i < array.length(); i++) {

            JSONObject json = null;
            try {
                //Getting json
                json = array.getJSONObject(i);

                //getting student profile data
                USER.EMAIL = json.getString("Student_Email");
                email.getEditText().setText(USER.EMAIL);
                if (!json.getString("Student_Bio").equals("null")){
                    bio.getEditText().setText(json.getString("Student_Bio"));
                }
                if(!json.getString("Student_Image").equals("null")){
                    Glide.with(this).load(json.getString("Student_Image")).into(image);
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
    private void parseInstructorData(JSONArray array) {
        for (int i = 0; i < array.length(); i++) {

            JSONObject json = null;
            try {
                //Getting json
                json = array.getJSONObject(i);

                //getting student profile data
                USER.EMAIL = json.getString("Instructor_Email");
                email.getEditText().setText(USER.EMAIL);
                if (!json.getString("Instructor_Bio").equals("null")) {
                    bio.getEditText().setText(json.getString("Instructor_Bio"));
                }
                if(!json.getString("Instructor_Image").equals("null")){
                    Glide.with(this).load(json.getString("Instructor_Image")).into(image);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

    }

    @Generated
    public void createNewViewDialog() {
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
                Intent intent5 = new Intent(EditProfile.this, LoginActivity.class);
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
            case R.id.menuHomeStudent:
                Intent intent = new Intent(EditProfile.this, Dashboard.class);
                startActivity(intent);
                finish();
                break;

            case R.id.menuMyCoursesStudent:
                Intent intent2 = new Intent(EditProfile.this, MyCourses.class);
                startActivity(intent2);
                finish();
                break;

            case R.id.menuBrowseCourses:
                Intent intent1 = new Intent(EditProfile.this, BrowseCourses.class);
                startActivity(intent1);
                finish();
                break;

            case R.id.menuLogOutStudent:
                createNewViewDialog();
                break;

            case R.id.menuHomeInstructor:
                Intent intent5 = new Intent(EditProfile.this, Dashboard.class);
                startActivity(intent5);
                finish();
                break;

            case R.id.menuMyCoursesInstructor:
                Intent intent3 = new Intent(EditProfile.this, MyCourses.class);
                startActivity(intent3);
                finish();
                break;

            case R.id.menuCreateCourse:
                Intent intent4 = new Intent(EditProfile.this, CreateCourse.class);
                startActivity(intent4);
                finish();
                break;

            case R.id.menuLogOutInstructor:
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
    public boolean isLastItemDistplaying(RecyclerView recyclerView) {
        if (recyclerView.getAdapter().getItemCount() != 0) {
            int lastVisibleItemPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastCompletelyVisibleItemPosition();
            if (lastVisibleItemPosition != RecyclerView.NO_POSITION && lastVisibleItemPosition == recyclerView.getAdapter().getItemCount() - 1) {
                return true;
            }
        }
        return false;
    }

    @Override
    @Generated
    public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
        //if Scrolled at last then
        if (isLastItemDistplaying(recyclerView)) {
            //Calling the method getData again
        }
    }


    @Override
    @Generated
    public void onBackPressed() {

    }
}
