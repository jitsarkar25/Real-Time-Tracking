package com.example.deva.afinal;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import android.location.LocationListener;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.deva.afinal.*;

import java.util.HashMap;
import java.util.Map;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, LocationListener {

    LocationManager locationManager;
    Firebase reference1, reference2;
    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //getLocation();
        Firebase.setAndroidContext(this);
        reference1 = new Firebase("https://chatapp-176e0.firebaseio.com/messages/" + UserDetails.username + "_" + UserDetails.chatWith);
        reference2 = new Firebase("https://chatapp-176e0.firebaseio.com/messages/" + UserDetails.chatWith + "_" + UserDetails.username);

        Log.d("reference2",reference2.toString());
        /*
        reference1.child(UserDetails.username + "_" + UserDetails.chatWith).removeValue();
        reference2.child(UserDetails.chatWith + "_" + UserDetails.username).removeValue();*/


        reference2.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                Map map = dataSnapshot.getValue(Map.class);

                //Log.d("dataSnap",dataSnapshot.toString());
                //Log.d("String:-",s);
                String message = map.get("message").toString();
                String userName = map.get("user").toString();



                    String coords[]=message.split(" ");
                    Log.d("dekho",coords[0]);
                    Log.d("dekho",coords[1]);
                    Toast.makeText(getApplicationContext(),"cords"+coords[0]+coords[1],Toast.LENGTH_LONG).show();
                    //mMap.clear();
                        double lat = Double.parseDouble(coords[0]);
                        double lng = Double.parseDouble(coords[1]);
                        LatLng sydney = new LatLng(lat, lng);
                        mMap.addMarker(new MarkerOptions().title(coords[0]+" "+coords[1]).position(sydney).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));


            }


            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
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

        // Add a marker in Sydney and move the camera
        //String passedArg = getIntent().getExtras().getString("arg");
        //enteredValue.setText(passedArg);



        getLocation();

    }



    @Override
    public void onLocationChanged(Location location) {
        Log.d("Final",location.getLatitude()+" "+location.getLongitude());
        Map<String, String> map = new HashMap<String, String>();
        map.put("message", location.getLatitude()+" "+location.getLongitude());
        map.put("user", UserDetails.username);
        //reference1.child(UserDetails.username + "_" + UserDetails.chatWith).setValue(null);
        //reference2.child(UserDetails.chatWith + "_" + UserDetails.username).setValue(null);
        reference1.push().setValue(map);
        LatLng sydney = new LatLng(location.getLatitude(), location.getLongitude());
        mMap.addMarker(new MarkerOptions().position(sydney).title("Your Location"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        Log.d("reference1",reference1.toString());
        //reference2.push().setValue(map);

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    void getLocation() {
        try {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 5, (android.location.LocationListener) this);
        }
        catch(SecurityException e) {
            e.printStackTrace();
        }
    }
}
