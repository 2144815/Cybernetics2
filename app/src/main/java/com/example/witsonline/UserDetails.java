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

    private TextInputLayout user;

    //for determining the type of user
    Bundle extras;

    @Override
    @Generated
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);

        user = findViewById(R.id.viewUser);

        extras = getIntent().getExtras();
        String userType = extras.getString("userType");
        if (userType.equals("student")) {
            user.getEditText().setText(STUDENT.number);
        }
        else {
            user.setHint("Username"); //since this user is an instructor
            if (INSTRUCTOR.USERNAME != null) {
                user.getEditText().setText(INSTRUCTOR.USERNAME);
            } else {
                Toast.makeText(UserDetails.this, "null", Toast.LENGTH_SHORT).show();
            }
        }


    }
}