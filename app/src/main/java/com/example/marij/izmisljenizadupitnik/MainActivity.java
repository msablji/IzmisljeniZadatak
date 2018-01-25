package com.example.marij.izmisljenizadupitnik;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import static com.example.marij.izmisljenizadupitnik.R.layout.activity_main;

public class MainActivity extends AppCompatActivity {
    DBAdapter db = new DBAdapter(this);
    int notificationID=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(activity_main);


    }

    public void salji(View view)
    {

        EditText string1 = (EditText) findViewById(R.id.ime);
        EditText string2 = (EditText) findViewById(R.id.email);

        db.open();
        long id = db.insertContact(string1.getText().toString(), string2.getText().toString());

        db.close();
        displayNotification();
    }

    public void pogledaj(View view)
    {
        db.open();
       Cursor c = db.getAllContacts();
        if (c.moveToFirst())
        {
           do {
                DisplayContact(c);
            } while (c.moveToNext());
       }
       db.close();
    }
    public void DisplayContact(Cursor c)
    {
        Toast.makeText(this,
                "id: " + c.getString(0) + "\n" +
                        "Name: " + c.getString(1) + "\n" +
                        "Email:  " + c.getString(2),
                Toast.LENGTH_LONG).show();
    }




    protected void displayNotification()
    {
        //---PendingIntent to launch activity if the user selects
        // this notification---
        Intent i = new Intent(this, NotificationView.class);

        i.putExtra("notificationID", notificationID);


        PendingIntent pendingIntent =
                PendingIntent.getActivity(this, 0, i, 0);

        long[] vibrate = new long[] { 100, 250, 100, 500};


//za sve verzije
        NotificationManager nm = (NotificationManager)
                getSystemService(NOTIFICATION_SERVICE);





//ovako je i u starim verzijama, jedino dodano .setChannelId (za stare verzije to brisemo)

        Notification notif = new Notification.Builder(this)
                .setTicker("Uspješno ste dodali kontakt")

                .setSmallIcon(R.mipmap.ic_launcher)
                .setWhen(System.currentTimeMillis())
                .setShowWhen(true)
                .setContentIntent(pendingIntent)
                .setVibrate(vibrate)
                .build();

        nm.notify(notificationID, notif);
    }

}
