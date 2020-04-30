package com.example.safety20;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static com.example.safety20.R.id.button;
import static com.example.safety20.R.id.button2;

public class MainActivity extends AppCompatActivity {
    Button A,B;
    String ph1="",ph2="",message="";
    LocationManager locationManager;
    String lattitude="";
    String longitude="";
    Button btnSendSMS;

    private final int MY_PERMISSIONS_REQUEST_SEND_SMS = 1;
    private static final int REQUEST_LOCATION = 1;
    private static final int REQUEST_CALL = 1;
    private static int VIDEO_REQUEST = 101;



    private final String SENT = "SMS_SENT";
    private final String DELIVERED = "SMS_DELIVERED";
    PendingIntent sentPI, deliveredPI;
    BroadcastReceiver smsSentReceiver, smsDeliveredReceiver;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);

        sentPI = PendingIntent.getBroadcast(MainActivity.this, 0, new Intent(SENT), 0);
        deliveredPI = PendingIntent.getBroadcast(MainActivity.this, 0, new Intent(DELIVERED), 0);

        A = (Button) findViewById(button);
        btnSendSMS = (Button) findViewById(R.id.button4);
        B=  (Button) findViewById(button2);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("Justice").child("latitude").setValue("false");
        mDatabase.child("Justice").child("longitude").setValue("false");


        A.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Main2Activity.class);
                startActivity(intent);
            }
        });
        B.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* Intent intent1 = new Intent(getApplicationContext(), Main4Activity.class);
                startActivity(intent1);*/
            }
        });
        mDatabase.child("ph1").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ph1 = dataSnapshot.getValue(java.lang.String.class);
                Toast.makeText(getApplicationContext(), ph1, Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        mDatabase.child("ph2").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ph2 = dataSnapshot.getValue(java.lang.String.class);
                Toast.makeText(getApplicationContext(), ph2, Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });


        btnSendSMS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    buildAlertMessageNoGps();

                } else if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    getLocation();
                }
                String telNr = "6354373919";
                String n1=ph1.trim();
                String n2=ph2.trim();
                if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.SEND_SMS)
                        != PackageManager.PERMISSION_GRANTED)
                {
                    ActivityCompat.requestPermissions(MainActivity.this, new String [] {Manifest.permission.SEND_SMS},
                            MY_PERMISSIONS_REQUEST_SEND_SMS);
                }
                else
                {
                    sendvideo();
                    SmsManager sms = SmsManager.getDefault();
                    sms.sendTextMessage(telNr, null, message, sentPI, deliveredPI);

                    if(n1.length()==10 && n2.length()==10){
                        sms.sendTextMessage(n1, null, message, sentPI, deliveredPI);
                        sms.sendTextMessage(n2, null, message, sentPI, deliveredPI);


                    }

                }

            }
        });
    }
    @Override
    protected void onPause() {
        super.onPause();

        unregisterReceiver(smsSentReceiver);
        unregisterReceiver(smsDeliveredReceiver);
    }

    @Override
    protected void onResume() {
        super.onResume();

        //The deliveredPI PendingIntent does not fire in the Android emulator.
        //You have to test the application on a real device to view it.
        //However, the sentPI PendingIntent works on both, the emulator as well as on a real device.

        smsSentReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                switch (getResultCode())
                {
                    case Activity.RESULT_OK:
                        Toast.makeText(context, "SMS sent successfully!", Toast.LENGTH_SHORT).show();
                        break;

                    //Something went wrong and there's no way to tell what, why or how.
                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                        Toast.makeText(context, "Generic failure!", Toast.LENGTH_SHORT).show();
                        break;

                    //Your device simply has no cell reception. You're probably in the middle of
                    //nowhere, somewhere inside, underground, or up in space.
                    //Certainly away from any cell phone tower.
                    case SmsManager.RESULT_ERROR_NO_SERVICE:
                        Toast.makeText(context, "No service!", Toast.LENGTH_SHORT).show();
                        break;

                    //Something went wrong in the SMS stack, while doing something with a protocol
                    //description unit (PDU) (most likely putting it together for transmission).
                    case SmsManager.RESULT_ERROR_NULL_PDU:
                        Toast.makeText(context, "Null PDU!", Toast.LENGTH_SHORT).show();
                        break;

                    //You switched your device into airplane mode, which tells your device exactly
                    //"turn all radios off" (cell, wifi, Bluetooth, NFC, ...).
                    case SmsManager.RESULT_ERROR_RADIO_OFF:
                        Toast.makeText(context, "Radio off!", Toast.LENGTH_SHORT).show();
                        break;

                }

            }
        };

        smsDeliveredReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                switch(getResultCode())
                {
                    case Activity.RESULT_OK:
                        Toast.makeText(context, "SMS delivered!", Toast.LENGTH_SHORT).show();
                        break;

                    case Activity.RESULT_CANCELED:
                        Toast.makeText(context, "SMS not delivered!", Toast.LENGTH_SHORT).show();
                        break;
                }

            }
        };

        //register the BroadCastReceivers to listen for a specific broadcast
        //if they "hear" that broadcast, it will activate their onReceive() method
        registerReceiver(smsSentReceiver, new IntentFilter(SENT));
        registerReceiver(smsDeliveredReceiver, new IntentFilter(DELIVERED));
    }
    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                (MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);

        } else {
            Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

            Location location1 = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            Location location2 = locationManager.getLastKnownLocation(LocationManager. PASSIVE_PROVIDER);

            if (location != null) {
                double latti = location.getLatitude();
                double longi = location.getLongitude();
                lattitude = String.valueOf(latti);
                longitude = String.valueOf(longi);
                message="emergency\n"+lattitude+"\n"+longitude;
                mDatabase.child("Justice").child("latitude").setValue(lattitude);
                mDatabase.child("Justice").child("longitude").setValue(longitude);


            } else  if (location1 != null) {
                double latti = location1.getLatitude();
                double longi = location1.getLongitude();
                lattitude = String.valueOf(latti);
                longitude = String.valueOf(longi);
                message="emergency\n"+lattitude+"\n"+longitude;
                mDatabase.child("Justice").child("latitude").setValue(lattitude);
                mDatabase.child("Justice").child("longitude").setValue(longitude);



            } else  if (location2 != null) {
                double latti = location2.getLatitude();
                double longi = location2.getLongitude();
                lattitude = String.valueOf(latti);
                longitude = String.valueOf(longi);
                message="emergency\n"+lattitude+"\n"+longitude;
                mDatabase.child("Justice").child("latitude").setValue(lattitude);
                mDatabase.child("Justice").child("longitude").setValue(longitude);
            }else{

                Toast.makeText(this,"Unble to Trace your location",Toast.LENGTH_SHORT).show();

            }
        }
    }

    protected void buildAlertMessageNoGps() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Please Turn ON your GPS Connection")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }
    private void makePhoneCall() {
        String number = "180030009009";

        if (number.trim().length() > 0) {

            if (ContextCompat.checkSelfPermission(MainActivity.this,
                    Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL);

            } else {

                String dial = "tel:" + number;

                 //don't take from text field rather directly write number in number variable
                startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));

                }

            }

        }


    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CALL) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            }
            else {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
        }
    }
    public void sendvideo() {
        Intent videoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        if (videoIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(videoIntent, VIDEO_REQUEST);
            makePhoneCall();
        }
    }
}

