package com.fashionapp.fashionapp;

import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import static android.support.constraint.Constraints.TAG;

public class GetNearbyPlacesData extends AsyncTask<Object, String,String>
{
    private String googlePlacesData;
    private GoogleMap mMap;


    @Override
    protected String doInBackground(Object... objects)
    {
        String url;
        mMap = (GoogleMap) objects[0];
        url = (String) objects[1];

        DownloadUrl downloadUrl = new DownloadUrl();
        try
        {
            googlePlacesData = downloadUrl.readUrl(url);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return googlePlacesData;
    }

    @Override
    protected void onPostExecute(String s) {
        List<HashMap<String ,String >> nearbyPlaceList;
        DataParser parser = new DataParser();
        Log.d(TAG, "onPostExecute: "+s);
        nearbyPlaceList = parser.Parse(s);
        showNearbyPlaces(nearbyPlaceList);
    }

    private void showNearbyPlaces(List<HashMap<String, String>> nearbyPlaceList)
    {
        int i;
        for(i = 0;i<nearbyPlaceList.size();i++)
        {
            MarkerOptions opt = new MarkerOptions();
            HashMap<String,String> googlePlace = nearbyPlaceList.get(i);
            String placeName = googlePlace.get("place_name");
            String vicinity = googlePlace.get("vicinity");
            double lat = Double.parseDouble(googlePlace.get("lat"));
            double lng = Double.parseDouble(googlePlace.get("lng"));
            LatLng latLng = new LatLng(lat, lng);
            opt.position(latLng);
            opt.title(placeName);
            final int min = 0;
            final int max = 2;
            final int random = new Random().nextInt((max - min) + 1) + min;
            if(random == 0)
            {
                opt.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
            }
            else if(random == 1)
            {
                opt.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
            }
            else
            {
                opt.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));
            }
            mMap.addMarker(opt);
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
        }
        if(i==0)
        {
            MarkerOptions opt = new MarkerOptions();
            opt.position(new LatLng(0,0));
            opt.title("No Results");
            mMap.addMarker(opt).showInfoWindow();
            mMap.animateCamera(CameraUpdateFactory.newLatLng(new LatLng(0,0)));
        }
    }
}