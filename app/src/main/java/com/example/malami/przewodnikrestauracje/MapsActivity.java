package com.example.malami.przewodnikrestauracje;


import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

public class MapsActivity extends FragmentActivity
        implements GoogleMap.OnMyLocationButtonClickListener,
        GoogleMap.OnMyLocationClickListener,
        OnMapReadyCallback {

    private GoogleMap mMap;
    Double szerokosc;
    Double dlugosc;
    String Sszerokosc, Sdlugosc;
    String nazwa, adres;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    public void onMapReady(GoogleMap map) {
        mMap = map;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        Sszerokosc = getIntent().getStringExtra("szerokosc");
        szerokosc= Double.parseDouble(Sszerokosc);
        Sdlugosc = getIntent().getStringExtra("dlugosc");
        dlugosc= Double.parseDouble(Sdlugosc);

        nazwa = getIntent().getStringExtra("nazwa");
        adres = getIntent().getStringExtra("adres");


        mMap.setMyLocationEnabled(true);
        mMap.setOnMyLocationButtonClickListener(this);
        mMap.setOnMyLocationClickListener(this);
        map.addMarker(new MarkerOptions()
                .position(new LatLng(szerokosc, dlugosc)).title(nazwa).snippet(adres));
        LatLng punkt = new LatLng( szerokosc, dlugosc);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom( punkt,18));
    }



    public void onMyLocationClick(@NonNull Location location) {
        Toast.makeText(this, "Twoja lokalizacja", Toast.LENGTH_LONG).show();
    }


    public boolean onMyLocationButtonClick() {
        return false;
    }
}