package com.example.shater.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.shater.LocationUtil.LocationHelper;
import com.example.shater.R;
import com.example.shater.models.receiverFromProvider;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;

public class ViewAcceptMyOffer extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, ActivityCompat.OnRequestPermissionsResultCallback , OnMapReadyCallback {

    TextView tv_name , tv_description ,tv_category ;
    MapView map_location;
    Button btn_done;
    receiverFromProvider reciver ;

    private Location mLastLocation;
    double latitude;
    double longitude;
    LocationHelper locationHelper;
    private GoogleMap gMap ;
    Marker marker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_accept_my_offer);
        locationHelper=new LocationHelper(this);
        locationHelper.checkpermission();
        // check availability of play services
        if (locationHelper.checkPlayServices()) {
            // Building the GoogleApi client
            locationHelper.buildGoogleApiClient();
            mLastLocation=locationHelper.getLocation();
        }

        reciver = new Gson().fromJson(getIntent().getStringExtra("reciverFromProvider"), receiverFromProvider.class);

        tv_name = (TextView) findViewById(R.id.tv_name_myoffer);
        tv_description = (TextView) findViewById(R.id.tv_description_myoffer);
        tv_category = (TextView) findViewById(R.id.tv_category_myoffer);
        map_location = (MapView) findViewById(R.id.map_myoffer);
        btn_done = (Button) findViewById(R.id.btn_done);

        latitude = reciver.getLat_provider();
        longitude = reciver.getLng_provider();
        map_location.onCreate(savedInstanceState);
        map_location.getMapAsync(this);

        // set data in textview
        tv_name.setText(reciver.getName_customer());
        tv_description.setText(reciver.getDescription());
        tv_category.setText(reciver.getCategory());


        btn_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(ViewAcceptMyOffer.this).setTitle("Done with Service")
                        .setMessage("Did the customer pay you ?").setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        setActionPaymentInDatabase("provider/offer/"+reciver.getId_provider()+"/"+reciver.getId_offer() , 2);
                        setActionPaymentInDatabase("users/offer/"+reciver.getId_customer()+"/"+reciver.getId_offer() , 2);
                        dialogInterface.dismiss();
                    }
                }).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        setActionPaymentInDatabase("provider/offer/"+reciver.getId_provider()+"/"+reciver.getId_offer() , 1);
                        setActionPaymentInDatabase("users/offer/"+reciver.getId_customer()+"/"+reciver.getId_offer() , 1);
                        dialogInterface.dismiss();
                    }
                }).show();
            }
        });
    }

    private void setActionPaymentInDatabase(String Path , int accapt_payment){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference(Path);
        reference.child("pay_customer").setValue(accapt_payment);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        gMap = googleMap ;
        gMap.setMinZoomPreference(12);
        gMap.setIndoorEnabled(true);
        UiSettings uiSettings =  gMap.getUiSettings();
        uiSettings.setIndoorLevelPickerEnabled(true);
        uiSettings.setMapToolbarEnabled(true);
        uiSettings.setMyLocationButtonEnabled(true);
        uiSettings.setZoomControlsEnabled(true);
        uiSettings.setCompassEnabled(true);
        if(longitude != 0.0 && latitude !=0.0){
            LatLng lng = new LatLng(latitude , longitude);
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(lng).title(reciver.getName_customer());
            if(marker != null)
                marker.remove();
            marker = gMap.addMarker(markerOptions);
            gMap.moveCamera(CameraUpdateFactory.newLatLng(lng));
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        locationHelper.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        // Once connected with google api, get the location
        mLastLocation=locationHelper.getLocation();
    }

    @Override
    public void onConnectionSuspended(int i) {
        locationHelper.connectApiClient();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

        Log.i("Connection failed:", " ConnectionResult.getErrorCode() = "
                + connectionResult.getErrorCode());
    }
    // Permission check functions
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        // redirects to utils
        locationHelper.onRequestPermissionsResult(requestCode,permissions,grantResults);

    }


    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        map_location.onSaveInstanceState(outState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        locationHelper.checkPlayServices();
        map_location.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
        map_location.onStart();
    }

    @Override
    protected void onPause() {
        super.onPause();
        map_location.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        map_location.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        map_location.onLowMemory();
    }

    @Override
    public boolean isActivityTransitionRunning() {
        return super.isActivityTransitionRunning();
    }
}
