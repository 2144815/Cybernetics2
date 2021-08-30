package com.example.witsonline;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class DiscMenuClickListener implements PopupMenu.OnMenuItemClickListener  {
    private int position;
    private Context context;
    private DiscussionCardAdapter.ViewHolder holder;
    private Button yes;
    private Button no;
    private TextView question;
    private ArrayList<Discussion> DiscList;


    Dialog mDialog;
    public DiscMenuClickListener(int position, Context context, DiscussionCardAdapter.ViewHolder Holder, ArrayList<Discussion> discList) {
        this.position = position;
        this.context = context;
        this.holder = Holder;
        this.DiscList = discList;

    }



    @Override
    public boolean onMenuItemClick(MenuItem item) {
        AlertDialog.Builder dialogBuilder= new AlertDialog.Builder(context);

        final View viewPopUp = LayoutInflater.from(context)
                .inflate(R.layout.open_close_disc_dialog, null);

        dialogBuilder.setView(viewPopUp);
        AlertDialog dialog = dialogBuilder.create();
        //dialog.requestWindowFeature( Window.FEATURE_NO_TITLE);
        yes = (Button)viewPopUp.findViewById(R.id.btn_yes);
        no = (Button)viewPopUp.findViewById( R.id.btn_no );
        question = (TextView)viewPopUp.findViewById( R.id.dialog_question );

        if(holder.status.getText().equals( "Open" )){

            switch (item.getItemId()){
                case R.id.Open_Close_DiscChoice:
                    dialog.show();
                    yes.setOnClickListener( new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            updateDiscStatus("Closed");
                            Intent i = new Intent(context, ForumActivity.class);
                            context.startActivity(i);


                        }
                    } );
                    no.setOnClickListener( new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent i = new Intent(context, ForumActivity.class);
                            context.startActivity(i);
                        }
                    } );
                    return  true;

                default:
                    //
            }
        }
        else{
            question.setText("Do you want to open this discussion?");
            switch (item.getItemId()){
                case R.id.Open_Close_DiscChoice:
                    dialog.show();
                    yes.setOnClickListener( new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            updateDiscStatus("Open");
                            int duration = Toast.LENGTH_SHORT;
                            Toast toast = Toast.makeText(context,"Discussion Open!", duration);
                            toast.show();
                            Intent i = new Intent(context, ForumActivity.class);
                            context.startActivity(i);
                        }
                    } );
                    no.setOnClickListener( new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent i = new Intent(context, ForumActivity.class);
                            context.startActivity(i);
                        }
                    } );
            }
        }
        return false;
    }
    private void updateDiscStatus(String NewStatus) {

        if( NewStatus.equals("Closed")){
            CloseDiscussion("updateDiscStatus.php","0");
        }
        else if(NewStatus.equals( "Open" )){
            OpenDisussion("updateDiscStatus.php","1");
        }


    }

    private void OpenDisussion(String phpFile, String newStatus) {

        OkHttpClient client = new OkHttpClient();
        HttpUrl.Builder urlBuilder = HttpUrl.parse("https://lamp.ms.wits.ac.za/~s2105624/" + phpFile).newBuilder();
        urlBuilder.addQueryParameter("discussionID",DiscList.get(position).getDiscussionID());
        urlBuilder.addQueryParameter("discStatus", newStatus);
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
                Log.d("Database Response",responseData.toString());


            }
        });
    }

    private void CloseDiscussion(String phpFile, String newStatus) {
        OkHttpClient client = new OkHttpClient();
        HttpUrl.Builder urlBuilder = HttpUrl.parse("https://lamp.ms.wits.ac.za/~s2105624/" + phpFile).newBuilder();
        urlBuilder.addQueryParameter("discussionID",DiscList.get(position).getDiscussionID());
        urlBuilder.addQueryParameter("discStatus", newStatus);
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
                Log.d("Database Response",responseData.toString());

            }
        });
    }
}
