package com.example.police20;

import androidx.fragment.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private DatabaseReference mDatabase;
    //private static final LatLngBounds BOUNDS_INDIA = new LatLngBounds(new LatLng(23.63936, 68.14712), new LatLng(28.20453, 97.34466));
    SupportMapFragment mapFragment;
    String lati;
    String longi;
    String level;
    Marker n;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mapFragment=(SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        lati="false";
        longi="false";
        level="false";

        mDatabase.child("Justice").child("latitude").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                lati = dataSnapshot.getValue(java.lang.String.class);


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {



            }
        });

        mDatabase.child("Justice").child("longitude").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                longi = dataSnapshot.getValue(java.lang.String.class);


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        //mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(BOUNDS_INDIA, 0));


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.


    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        MarkerOptions m= new MarkerOptions().position(new LatLng(28.7041,77.1025)).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)).title("tap to get the alerts");
        n= mMap.addMarker(m);

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {

                if (!lati.equals("false") && !longi.equals("false")) {
                    Double la = Double.parseDouble(lati);
                    Double lo = Double.parseDouble(longi);
                    // Add a marker in Sydney and move the camera
                    n.setPosition(new LatLng(la,lo));
                    n.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
                    LatLng posi = new LatLng(la, lo);
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(posi));
                }


            }
        });



    }
}
