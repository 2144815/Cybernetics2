package com.example.witsonline;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class EditProfile extends AppCompatActivity implements View.OnScrollChangeListener, BottomNavigationView.OnNavigationItemSelectedListener {

    //This is for the logout pop up menu
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private Button btnLogout, btnCancel;

    //Creating Views
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        //determine which navbar to use (student or instructor)
        BottomNavigationView dashboardBottomNavigation = findViewById(R.id.dashboardBottomNavigation);
        dashboardBottomNavigation.setOnNavigationItemSelectedListener(EditProfile.this);

        if (USER.STUDENT) {
            dashboardBottomNavigation.inflateMenu(R.menu.menu_student);
            dashboardBottomNavigation.getMenu().findItem(R.id.menuProfileStudent).setChecked(true);

        } else {
            dashboardBottomNavigation.inflateMenu(R.menu.menu_instructor);
            dashboardBottomNavigation.getMenu().findItem(R.id.menuProfileInstructor).setChecked(true);
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
