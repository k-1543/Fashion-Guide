package com.fashionapp.fashionapp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import android.os.Handler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback
{

    private static double RADIUS = 1000;
    public  static LatLng lt = new LatLng(0.0,0.0);
    double latitude = 0, longitude = 0;
    static String buttonpressed, iduser, email, pass;
    private static final String TAG = "Welcome";
    public static GoogleMap mMap;
    private FusedLocationProviderClient fused;
    private LocationRequest request;
    Button search;
    static Shops sp = new Shops();
    static String id;
    static int flag = 0;
    LocationCallback mLocationCallback = new LocationCallback()
    {
        @SuppressLint("MissingPermission")
        @Override
        public void onLocationResult(LocationResult locationResult) {
            for (Location location : locationResult.getLocations()) {
                // Update UI with location data
                lt= new LatLng(location.getLatitude(), location.getLongitude());
                mMap.setMyLocationEnabled(true);
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        Log.d(TAG, "onCreate: act1");
        Bundle bundle = getIntent().getExtras();
        buttonpressed = bundle.getString("button");
        iduser = bundle.getString("iduser");
        email = bundle.getString("email");
        pass = bundle.getString("pass");
        if (TextUtils.isEmpty(buttonpressed)) {
            buttonpressed = "boutique";
        }
        Log.d(TAG, "onCreate: "+buttonpressed);
        search = findViewById(R.id.search);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Log.d(TAG, "onClick: Search");
                EditText location = findViewById(R.id.loc);
                String loc = location.getText().toString();
                int number;

                if (loc.matches("[0-9]+") && loc.length() > 2)
                {
                    number = Integer.parseInt(loc);
                    RADIUS = number;
                    loadplaces(buttonpressed);
                }
                List<Address> addressList = null;
                MarkerOptions opt = new MarkerOptions();
                Log.d(TAG, "onClick: "+ loc);
                if(!loc.equals("")) {
                    Geocoder geocoder = new Geocoder(MapsActivity.this);
                    try
                    {
                        addressList = geocoder.getFromLocationName(loc, 50);
                        Log.d(TAG, "onClick: "+ addressList);
                    }
                    catch (IOException e)
                    {
                        e.printStackTrace();
                    }
                    mMap.clear();
                    for (int i = 0; i < addressList.size(); i++)
                    {
                        Address myAdd = addressList.get(i);
                        LatLng latLng = new LatLng(myAdd.getLatitude(), myAdd.getLongitude());
                        opt.position(latLng).title("Searched").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));
                        mMap.addMarker(opt);
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
                    }
                }
            }
        });
        fused = LocationServices.getFusedLocationProviderClient(this);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }
    @Override
    public void onMapReady(GoogleMap googleMap)
    {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        request = new LocationRequest();
        request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        request.setFastestInterval(100);
        request.setInterval(100);
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
        {
            //Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show();
            fused.requestLocationUpdates(request, mLocationCallback, Looper.myLooper());
            mMap.setMyLocationEnabled(true);
        }
        else
        {
            check();
        }
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                loadplaces(buttonpressed);
            }
        }, 3000);
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker)
            {
                final String info = marker.getTitle();
                Log.d(TAG, "onMarkerClick: " + info);
                marker.showInfoWindow();
                final Marker m = marker;
                final LatLng lat = marker.getPosition();
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run()
                    {
                        String uri = "http://maps.google.com/maps?saddr=" + lt.latitude + "," + lt.longitude + "&daddr=" + lat.latitude + "," + lat.longitude;
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                        intent.setPackage("com.google.android.apps.maps");
                        startActivity(intent);
                        String shop = "{\"shops\" : {\"group\" : [ null, 5, 3, 4 ],\"name\" : \""+ m.getTitle() +"\" }}";
                        DatabaseReference Db;
                        Db = FirebaseDatabase.getInstance().getReference("shops");
                        DatabaseReference db = FirebaseDatabase.getInstance().getReference("credentials");
                        id = Db.push().getKey();
                        db.child(iduser).child("visited").setValue(id);
                        sp.id = id;
                        sp.lat = Double.toString(lt.latitude);
                        sp.lng = Double.toString(lt.longitude);
                        sp.name = m.getTitle();
                        Db.child(id).setValue(sp);
                        Db.child(id).child("group").child("val1").setValue("0");
                        Db.child(id).child("group").child("val2").setValue("0");
                        Db.child(id).child("group").child("val3").setValue("0");
                        Db.child(id).child("group").child("val4").setValue("0");

                    }
                }, 3000);
                return true;
            }
        });
    }
    private void loadplaces(String buttonpressed)
    {
        Object dataTransfer[] = new Object[2];
        GetNearbyPlacesData getNearbyPlacesData = new GetNearbyPlacesData();
        switch (buttonpressed)
        {
            case "boutique":
                mMap.clear();
                String url = getUrl(latitude, longitude, "clothing_store");
                dataTransfer[0] = mMap;
                dataTransfer[1] = url;
                getNearbyPlacesData.execute(dataTransfer);
                Toast.makeText(this, "Showing Boutique Shops", Toast.LENGTH_SHORT).show();
            break;
            case "saloon":
                mMap.clear();
                url = getUrl(latitude, longitude, "beauty_salon");
                dataTransfer[0] = mMap;
                dataTransfer[1] = url;
                getNearbyPlacesData.execute(dataTransfer);
                Toast.makeText(this, "Showing Saloons", Toast.LENGTH_SHORT).show();
            break;
            case "tailor":
                mMap.clear();
                url = getUrl(latitude, longitude, "clothing_store");
                dataTransfer[0] = mMap;
                dataTransfer[1] = url;
                getNearbyPlacesData.execute(dataTransfer);
                Toast.makeText(this, "Showing Tailor Shops", Toast.LENGTH_SHORT).show();
            break;
            case "foot":
                mMap.clear();
                url = getUrl(latitude, longitude, "shoe_store");
                dataTransfer[0] = mMap;
                dataTransfer[1] = url;
                getNearbyPlacesData.execute(dataTransfer);
                Toast.makeText(this, "Showing Footwear Shops", Toast.LENGTH_SHORT).show();
            break;

        }
    }

    private String getUrl(double latitude, double longitude, String nearbyPlace)
    {

        latitude = lt.latitude;
        longitude = lt.longitude;
        Log.d(TAG, "getUrl: "+latitude+longitude);
        StringBuilder googlePLaceUrl = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        googlePLaceUrl.append("location=" + latitude + "," + longitude);
        googlePLaceUrl.append("&radius=" + RADIUS);
        googlePLaceUrl.append("&type=" + nearbyPlace);
        googlePLaceUrl.append("&type=" + nearbyPlace);
        googlePLaceUrl.append("&sensor=true");
        googlePLaceUrl.append("&key=AIzaSyD1Ar_8eJkObPphXtE7kFIplr_mRNNns98");
        Log.d(TAG, "getUrl: " + googlePLaceUrl);
        return googlePLaceUrl.toString();
    }

    public static Map<String, Object> jsonToMap(JSONObject json) throws JSONException {
        Map<String, Object> retMap = new HashMap<String, Object>();

        if(json != JSONObject.NULL) {
            retMap = toMap(json);
        }
        return retMap;
    }

    public static Map<String, Object> toMap(JSONObject object) throws JSONException {
        Map<String, Object> map = new HashMap<String, Object>();

        Iterator<String> keysItr = object.keys();
        while(keysItr.hasNext()) {
            String key = keysItr.next();
            Object value = object.get(key);

            if(value instanceof JSONArray) {
                value = toList((JSONArray) value);
            }

            else if(value instanceof JSONObject) {
                value = toMap((JSONObject) value);
            }
            map.put(key, value);
        }
        return map;
    }

    public static List<Object> toList(JSONArray array) throws JSONException {
        List<Object> list = new ArrayList<Object>();
        for(int i = 0; i < array.length(); i++) {
            Object value = array.get(i);
            if(value instanceof JSONArray) {
                value = toList((JSONArray) value);
            }

            else if(value instanceof JSONObject) {
                value = toMap((JSONObject) value);
            }
            list.add(value);
        }
        return list;
    }

    private void check()
    {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user asynchronously -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(this)
                        .setTitle("Location Permission Needed")
                        .setMessage("This app needs the Location permission, please accept to use location functionality")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(MapsActivity.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        1 );
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(MapsActivity.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        1 );
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        fused.requestLocationUpdates(request, mLocationCallback, Looper.myLooper());
                        mMap.setMyLocationEnabled(true);
                    }

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_LONG).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    @Override
    public void onBackPressed()
    {
        Intent intent = new Intent(this, Home.class);
        intent.putExtra("iduser", iduser);
        intent.putExtra("email", email);
        intent.putExtra("pass", pass);
        startActivity(intent);
        finish();
    }
}
