package com.techmania.notificationprocedures;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {

    Button buttonCounter;
    ConstraintLayout constraintLayout;

    private final static String CHANNEL_ID = "1";
    private final static String CHANNEL_NAME = "Notification Procedures";
    private final static int NOTIFICATION_ID = 1;

    int counter = 0;

    NotificationCompat.Builder builder;
    NotificationManagerCompat compat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonCounter = findViewById(R.id.buttonCounter);
        constraintLayout = findViewById(R.id.constraintLayout);

        buttonCounter.setOnClickListener(v -> {

            counter++;
            buttonCounter.setText(String.valueOf(counter));

            if (counter == 5){

                sendNotification();

            }

        });

    }

    public void sendNotification(){

        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(MainActivity.this,0,intent,PendingIntent.FLAG_IMMUTABLE);

        //action button
        Intent actionIntent = new Intent(MainActivity.this,Receiver.class);
        actionIntent.putExtra("toast","This is a notification message");
        PendingIntent actionPending = PendingIntent.getBroadcast(MainActivity.this,1,actionIntent,PendingIntent.FLAG_IMMUTABLE);

        //dismiss button
        Intent dismissIntent = new Intent(MainActivity.this,Receiver.class);
        PendingIntent dismissPending = PendingIntent.getBroadcast(MainActivity.this,2,dismissIntent,PendingIntent.FLAG_IMMUTABLE);

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.android);
        String text = getResources().getString(R.string.big_text);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){

            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
            NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            manager.createNotificationChannel(channel);

        }

        builder = new NotificationCompat.Builder(MainActivity.this,CHANNEL_ID)
                .setSmallIcon(R.drawable.notification_icon)
                .setContentTitle("Notification Title")
                .setContentText("Notification Text")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .addAction(R.drawable.notification_icon,"Toast Message",actionPending)
                .addAction(R.drawable.notification_icon,"Dismiss",dismissPending)
                .setLargeIcon(bitmap)
                .setStyle(new NotificationCompat.BigPictureStyle().bigPicture(bitmap).bigLargeIcon(null));
        //.setStyle(new NotificationCompat.BigTextStyle().bigText(text))

        compat = NotificationManagerCompat.from(MainActivity.this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){

            if (ContextCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED){

                    /*
                    In an educational UI, explain to the user why your app requires this
                    permission for a specific feature to behave as expected, and what
                    features are disabled if it's declined. In this UI, include a
                    "cancel" or "no thanks" button that lets the user continue
                    using your app without granting the permission.
                    So we show a snackbar message for this. If you want you can create a dialog message
                    */
                if (shouldShowRequestPermissionRationale(android.Manifest.permission.POST_NOTIFICATIONS)){

                    Snackbar.make(
                            constraintLayout,
                            "Please allow the permission to take notification",
                            Snackbar.LENGTH_LONG
                    ).setAction("Allow", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.POST_NOTIFICATIONS},3);
                            }
                    }).show();

                }else {
                    ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.POST_NOTIFICATIONS},3);
                }

            }else {
                compat.notify(NOTIFICATION_ID,builder.build());
            }


        }else {
            compat.notify(NOTIFICATION_ID,builder.build());
        }

    }

    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 3 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){

            compat.notify(NOTIFICATION_ID,builder.build());

        }else {

            Snackbar.make(
                    constraintLayout,
                    "Please allow the permission to take notification",
                    Snackbar.LENGTH_LONG).setAction("Allow", new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
                @Override
                public void onClick(View v) {
                    ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.POST_NOTIFICATIONS},3);
                }
            }).show();

        }

    }
}