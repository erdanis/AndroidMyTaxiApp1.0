package com.example.mytaxiapp10;


import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;

import android.os.IBinder;

import android.content.BroadcastReceiver;
import android.content.Context;


import android.util.Log;
import android.Manifest;
import android.location.Location;
import android.app.Notification;
import android.content.pm.PackageManager;


import androidx.annotation.RequiresApi;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;


public class TrackingService extends Service {

    DatabaseReference ref;
    DatabaseReference refjobs;
    String messageaddress;
    String msgfulladdress;
    String msggen;
    String msgcomment;
    String msgnumtelf;
    String msgcab;
    int msgstatus;

    private void createdriverdata (String name, double latitud, double longitud, double speed, int ranking, int status)
    {
        drivers newdriver = new drivers(name,latitud,longitud,ranking,status,speed);
        ref.setValue(newdriver);
    };

    private void createNotificationChannel() {

        Intent fullScreenIntent = new Intent(this, NewJobActivity.class);
        fullScreenIntent.putExtra("msgaddress",messageaddress);
        fullScreenIntent.putExtra("msgfulladdress",msgfulladdress);
        fullScreenIntent.putExtra("msggen",msggen);
        fullScreenIntent.putExtra("msgcomment",msgcomment);
        fullScreenIntent.putExtra("msgnumtelf",msgnumtelf);
        fullScreenIntent.putExtra("msgcab",msgcab);
        fullScreenIntent.putExtra("msgstatus",msgstatus);
        PendingIntent fullScreenPendingIntent = PendingIntent.getActivity(this, 0,
                fullScreenIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "NOTIF2")
                .setSmallIcon(R.drawable.new_job_received)
                .setContentTitle("TIENE NUEVO TRABAJO")
                .setContentText(messageaddress)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setFullScreenIntent(fullScreenPendingIntent, true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
// notificationId is a unique int for each notification that you must define
        notificationManager.notify(3, builder.build());

    };

    private static final String TAG = TrackingService.class.getSimpleName();
    public TrackingService() {}
    //String fireuser;
    String useremail;

    public int onStartCommand(Intent intent, int flags, int startId) {


        if (intent != null && intent.hasExtra("user")) {
            //fireuser = intent.getStringExtra("userid");
            useremail = intent.getStringExtra("user");
            final String capnumber = useremail.substring(6, useremail.indexOf("@"));

           // Log.d("serviceDEBUG","string recibido " + useremail);
            if (!useremail.isEmpty()){
                ref = FirebaseDatabase.getInstance().getReference().child("drivers").child(capnumber+"");
                 ref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NotNull DataSnapshot dataSnapshot) {
                        // This method is called once with the initial value and again
                        // whenever data at this location is updated.
                        //drivers value = dataSnapshot.getValue(drivers.class);
                        if (dataSnapshot.exists()) {
                            requestLocationUpdates();
                        }
                        else
                        {
                           // Log.d("TESTING", "Creando Data....");
                            createdriverdata(capnumber,0.00,0.00,0.00,0,1);
                        }
                    }
                    @Override
                    public void onCancelled(@NotNull DatabaseError error) {
                        // Failed to read value
                        Log.w("TESTING", "Failed to read value.", error.toException());
                    }
                });

            }
            refjobs = FirebaseDatabase.getInstance().getReference().child("recentjobs").child(capnumber+"");

            ValueEventListener jobListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    if (dataSnapshot.exists()) {
                    newjob job = dataSnapshot.getValue(newjob.class);

                    messageaddress = job.addressname;
                    msgfulladdress = job.fulladdress;
                    msgcomment = job.comentario;
                    msgnumtelf = job.numTelf.substring(6,10);
                    msggen = job.genero;
                    msgcab = job.cab;
                    msgstatus = job.status;
                    //Log.d("NewJob", "ALGO ESTA PASANDO!");
                    if(msgstatus==0) createNotificationChannel();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    // Getting Post failed, log a message
                    Log.w("NewJob", "newJob:onCancelled", databaseError.toException());
                    // ...
                }
            };
            refjobs.addValueEventListener(jobListener);


        }
        return Service.START_NOT_STICKY;
    }

    @Override
    public void onDestroy(){
        useremail="";
    }
    @Override
    public IBinder onBind(Intent intent) {
        return null;
        // TODO: Return the communication channel to the service.
    }


    @Override
    public void onCreate() {
        super.onCreate();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            buildNotification();
        else
            startForeground(1, new Notification());
        //buildNotification();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void buildNotification() {

        NotificationChannel chan = new NotificationChannel(getString(R.string.app_name), getString(R.string.tracking_enabled_notif), NotificationManager.IMPORTANCE_HIGH);
        chan.setLightColor(Color.BLUE);
        chan.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        assert manager != null;
        manager.createNotificationChannel(chan);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, getString(R.string.app_name));
        Notification notification = notificationBuilder.setOngoing(true)
                .setSmallIcon(R.drawable.tracking_enabled)
                .setContentTitle(getString(R.string.tracking_enabled_notif))
                .setPriority(NotificationManager.IMPORTANCE_HIGH)
                .setCategory(Notification.CATEGORY_SERVICE)
                .build();
        startForeground(2, notification);

        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "NEW_JOB_CHANNEL";
            String description = "MENSAJE_CHANNEL";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel("NOTIF2", name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

    }

    protected BroadcastReceiver stopReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //Unregister the BroadcastReceiver when the notification is tapped//
            unregisterReceiver(stopReceiver);
//Stop the Service//
            stopSelf();
        }
    };

    private void requestLocationUpdates() {
        LocationRequest request = new LocationRequest();

        request.setInterval(10000);

        request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        FusedLocationProviderClient client = LocationServices.getFusedLocationProviderClient(this);
        //final String path = getString(R.string.firebase_path);
        int permission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);

        if (permission == PackageManager.PERMISSION_GRANTED) {

            client.requestLocationUpdates(request, new LocationCallback() {
                @Override
                public void onLocationResult(LocationResult locationResult) {

                Location location = locationResult.getLastLocation();
                   if (location != null) {

                       double speed = location.getSpeed();
                       double latitud = location.getLatitude();
                       double longitud = location.getLongitude();

                       if(!useremail.isEmpty()) {
                       ref.child("latitud").setValue(latitud);
                       ref.child("longitud").setValue(longitud);
                       ref.child("speed").setValue(speed);}

                }
            }
        }, null);
    }
}
}

