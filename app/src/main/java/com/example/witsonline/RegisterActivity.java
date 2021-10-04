package com.example.witsonline;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.util.PatternsCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.text.TextUtils;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;

public class RegisterActivity extends AppCompatActivity {

    private RadioGroup rgRegister;
    private TextInputEditText userText;
    private RadioButton rbStudent;
    private RadioButton rbInstructor;
    private ImageView image;
    private TextInputLayout username, firstName, lastName, email, password, confirmPass,bio;
    private Button register;
    public static final int IMAGE_REQUEST_CODE = 3;
    public static final int STORAGE_PERMISSION_CODE = 123;
    private Uri filePath;
    private Bitmap bitmap;
    private ArrayList<String> studentNumbers = new ArrayList<>();
    private ArrayList<String> instructorUsernames = new ArrayList<>();
    private Boolean instructorCheck=false;
    private boolean imgSelected = false;
    private String url = "https://lamp.ms.wits.ac.za/~s2105624/";
    private RequestQueue requestQueue;
    MD5Hash m;

    @Override
    @Generated
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Bundle extras = getIntent().getExtras();
        studentNumbers = extras.getStringArrayList("students");
        instructorUsernames = extras.getStringArrayList("instructors");
        image = findViewById(R.id.profileImage);
        requestStoragePermission();
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

        rbStudent = findViewById(R.id.student);
        rbInstructor = findViewById(R.id.instructor);
        userText = (TextInputEditText) findViewById(R.id.RegisterText);
        userText.setHint("Student Number");
        rgRegister = findViewById((R.id.rbUserType));
        if(rbInstructor.isChecked()){
            instructorCheck=true;
        }

        rgRegister.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            @Generated
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.student) {
                    userText.setHint("Student Number");
                    userText.setInputType(InputType.TYPE_CLASS_NUMBER);
                    userText.setText("");
                    username.setCounterMaxLength(10);
                    userText.setFilters(new InputFilter[] { new InputFilter.LengthFilter(10) });
                } else {
                    userText.setHint("Username");
                    userText.setInputType(InputType.TYPE_CLASS_TEXT);
                    username.setCounterMaxLength(20);
                    userText.setFilters(new InputFilter[] { new InputFilter.LengthFilter(20) });
                }
            }
        });

        username = findViewById(R.id.username);
        firstName = findViewById(R.id.firstName);
        lastName = findViewById(R.id.lastName);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        confirmPass = findViewById(R.id.confirmPassword);
        register = (Button) findViewById(R.id.buttonRegister);
        bio = findViewById(R.id.bio);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            @Generated
            public void onClick(View v) {

                //Remove errors on input layouts if errors are fixed
                username.setErrorEnabled(false);
                firstName.setErrorEnabled(false);
                lastName.setErrorEnabled(false);
                email.setErrorEnabled(false);
                password.setErrorEnabled(false);
                confirmPass.setErrorEnabled(false);
                bio.setErrorEnabled(false);

                String[] phpFile = {"studentRegister.php"}; // php file for students
                String[] userType = {"studentNumber"}; // students use student number to register
                if (rbInstructor.isChecked()) {
                    phpFile[0] = "instructorRegister.php";// php file for instructor
                    userType[0] = "username";//instructors use username to register
                    Log.d("HERE", phpFile[0]);
                } else {
                    phpFile[0] = "studentRegister.php";// php file for instructor
                    userType[0] = "studentNumber";//instructors use username to register
                    Log.d("HERE", phpFile[0]);
                }
                requestQueue = Volley.newRequestQueue(getApplicationContext());
                try {
                    processInfo(username, firstName, lastName, email,bio, password, confirmPass, phpFile[0], userType[0],instructorUsernames,studentNumbers,instructorCheck);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });

    }

    //Post request function
    private void doPostRequest(final TextInputLayout user, TextInputLayout name, TextInputLayout surname, TextInputLayout emailAdd,TextInputLayout bio, TextInputLayout pass, String phpFile, String userType) throws IOException {
        String bm = "nofile";
        if (imgSelected){
            bm = getStringImage(bitmap);
            if(userType.equals("username")){
                phpFile = "instructorRegisterImage.php";
            }
            else{
                //still need to create this php file
                phpFile = "studentRegisterImage.php";
            }
        }
        String finalBm = bm;
        StringRequest request = new StringRequest(Request.Method.POST, url+phpFile, new com.android.volley.Response.Listener<String>() {
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
                parameters.put(userType, user.getEditText().getText().toString());
                parameters.put("firstname", name.getEditText().getText().toString());
                parameters.put("lastname", surname.getEditText().getText().toString());
                parameters.put("email", emailAdd.getEditText().getText().toString());
                parameters.put("password", pass.getEditText().getText().toString());
                parameters.put("bio", bio.getEditText().getText().toString());
                if(!finalBm.equals("nofile")){
                    parameters.put("bitmap", getStringImage(bitmap));
                }
                return parameters;
            }
        };
        USER.regUser = user.getEditText().getText().toString().trim();
        USER.regPass = m.md5(pass.getEditText().getText().toString());
        if (USER.STUDENT){
            USER.regStudent = true;
        }
        requestQueue.add(request);
        Toast toast = Toast.makeText(RegisterActivity.this, "Registration successful", Toast.LENGTH_LONG);
        toast.show();
        Intent i = new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(i);
        finish();
    }

    // This function checks if a required text is empty or not
    public boolean isEmpty(TextInputLayout text) {
        boolean empty = false;
        if (text.getEditText().getText().toString().isEmpty()) {
            text.setError("Field can't be empty");
            empty = true;
        }

        return empty;
    }

    public boolean userExists(TextInputLayout text,ArrayList<String> InstructorNames,ArrayList<String> StudentNums,Boolean Instructor) {
        String usernameInput = text.getEditText().getText().toString().trim();
        boolean exists = false;
        if (Instructor==true) {
            for (int i = 0; i < InstructorNames.size(); i++) {
                if (usernameInput.equals(InstructorNames.get(i))) {
                    text.setError("Username already exists");
                    exists = true;
                }
            }
        } else {
            for (int i = 0; i < StudentNums.size(); i++) {
                if (usernameInput.equals(StudentNums.get(i))) {
                    text.setError("Username already exists");
                    exists = true;
                }
            }
        }
        return exists;
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
        if(confirmPass.getEditText().getText().toString().isEmpty()){
            confirmPass.setError("Confirm your password");
            valid = false;
        }
        if(password.getEditText().getText().toString().length()<5){
            password.setError("Password must be at least 5 characters long");
            valid = false;
        }
        else if(!password.getEditText().getText().toString().equals(confirmPass.getEditText().getText().toString())){
            confirmPass.setError("Passwords don't match");
            valid = false;
        }
        return valid;
    }

    //This function checks if a text contains digits only
    @Generated
    boolean isDigits(TextInputLayout text){
        return TextUtils.isDigitsOnly(text.getEditText().getText().toString());
    }

    //This function processes data for registration
    public boolean processInfo(TextInputLayout user, TextInputLayout name, TextInputLayout surname, TextInputLayout emailAdd, TextInputLayout bio, TextInputLayout pass, TextInputLayout confirmPass, String phpFile, String userType, ArrayList<String> InstructorNames, ArrayList<String> StudentNums, Boolean instructor) throws IOException {
        boolean valid = true;
        isEmpty(user);
        isEmpty(name);
        isEmpty(surname);
        isEmpty(emailAdd);
        isEmpty(pass);
        isEmpty(bio);
        userExists(user,InstructorNames,StudentNums,instructor);
        validateEmail(emailAdd);
        if(!(isEmpty(user) && isEmpty(name) && isEmpty(surname) && isEmpty(pass) &&isEmpty(bio) && userExists(user,InstructorNames,StudentNums,instructor)) && validatePassword(pass,confirmPass) && validateEmail(emailAdd)&&!isEmpty(bio) ){
            doPostRequest(user, name, surname, emailAdd,bio, pass, phpFile, userType);
        }
        else{
            valid = false;
        }

        return valid;
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
            imgSelected = true;
            try {
                ImageDecoder.Source source = ImageDecoder.createSource(this.getContentResolver(), filePath);
                bitmap = ImageDecoder.decodeBitmap(source);
                image.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{
            imgSelected = false;
        }
    }

    //getting the bitmap of image and encoding it as a string
    @Generated
    public String getStringImage(Bitmap bitmap) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] b = baos.toByteArray();
        final String temp = Base64.encodeToString(b, Base64.DEFAULT);
        Log.i("My_data_image", "" + temp);
        return temp;
    }

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
    @Override
    @Generated
    public void onBackPressed() {
        Intent i = new Intent(RegisterActivity.this,LoginActivity.class);
        startActivity(i);
        finish();

    }

}
