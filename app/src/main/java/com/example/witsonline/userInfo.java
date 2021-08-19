package com.example.witsonline;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class userInfo extends AppCompatActivity {
    private TextView username;
    private TextView stuNumber;
    private TextView email;
    private TextView role;

    private String searchURL = "https://lamp.ms.wits.ac.za/home/s2105624/userInfo.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        username = findViewById(R.id.user_name);
        stuNumber = findViewById(R.id.user_student_number);
        email = findViewById(R.id.user_email);
        role = findViewById(R.id.user_role);
        //studentName = (TextView) itemView.findViewById(R.id.studentName);

        String FName=" ";
        String LName=" ";
        /*final
        String[] splitStr = username.split("\\s+");
        FName = splitStr[0];
        LName = splitStr[1];*/

    }
}