package com.example.shater.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shater.LocationUtil.LocationHelper;
import com.example.shater.R;
import com.example.shater.helper.CacheJson;
import com.example.shater.models.providerInfo;
import com.example.shater.models.receiverFromProvider;
import com.example.shater.models.requestCustomerToService;
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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MakeOfferActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, ActivityCompat.OnRequestPermissionsResultCallback , OnMapReadyCallback {
    TextView tv_name , tv_catogery , tv_description , tv_dateAndtime ;
    ImageView img_make , btn_input , btn_search ;
    EditText edt_price , edt_search ;
    MapView map_location;
    Button btn_sendOrder ;
    ProgressBar pr_request ;
    LinearLayout ll_search ;
    requestCustomerToService toService ;
    receiverFromProvider reciver ;
    CacheJson cacheJson ;
    providerInfo info ;
    DatabaseReference reference ;
    private StorageReference mStorageRef;
    boolean fromCustomerActivity;

    private Location mLastLocation;
    double latitude;
    double longitude;
    LocationHelper locationHelper;
    private GoogleMap gMap ;
    Marker marker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_offer);
        locationHelper=new LocationHelper(this);
        locationHelper.checkpermission();
        // check availability of play services
        if (locationHelper.checkPlayServices()) {

            // Building the GoogleApi client
            locationHelper.buildGoogleApiClient();
        }

        reference = FirebaseDatabase.getInstance().getReference();
        cacheJson =  new CacheJson();
        Gson gson = new Gson();
        fromCustomerActivity = getIntent().getBooleanExtra("fromCustomerActivity" , true);
        mStorageRef = FirebaseStorage.getInstance().getReference();



        try {
            info = (providerInfo) cacheJson.readObject(MakeOfferActivity.this , "providerInfo");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }


        tv_name = (TextView) findViewById(R.id.tv_namecust_make);
        tv_catogery = (TextView) findViewById(R.id.tv_catagory_make);
        tv_description = (TextView) findViewById(R.id.tv_description_make);
        tv_dateAndtime = (TextView) findViewById(R.id.tv_date_make);
        img_make = (ImageView) findViewById(R.id.img_make);
        btn_input = (ImageView) findViewById(R.id.btn_inputLocation_make);
        btn_search = (ImageView) findViewById(R.id.btn_search_make);
        edt_price = (EditText) findViewById(R.id.edt_price_make);
        edt_search = (EditText) findViewById(R.id.edt_search_make);
        map_location = (MapView) findViewById(R.id.map_make);
        btn_sendOrder = (Button) findViewById(R.id.btn_sendOrder_make);
        pr_request = (ProgressBar) findViewById(R.id.pb_make);

        ll_search = (LinearLayout) findViewById(R.id.ll_search_make);

        if(fromCustomerActivity){
            toService = gson.fromJson(getIntent().getStringExtra("RequestCustomer"),requestCustomerToService.class);
            tv_name.setText(toService.getName_customer());
            tv_description.setText(toService.getDescription());
            tv_catogery.setText(toService.getCategory());
            tv_dateAndtime.setText("Date:"+toService.getDate()+" Time:"+toService.getTime());
            StorageReference reference =mStorageRef.child("Images/"+toService.getUrlImage());
            downloadImage(reference);

        }else {
            reciver = gson.fromJson(getIntent().getStringExtra("reciverFromProvider"), receiverFromProvider.class);

            tv_name.setText(reciver.getName_customer());
            tv_description.setText(reciver.getDescription());
            tv_catogery.setText(reciver.getCategory());
            tv_dateAndtime.setText("Date:"+reciver.getDate());
            StorageReference reference =mStorageRef.child("Images/"+reciver.getUrlImage());
            downloadImage(reference);
        }

        map_location.onCreate(savedInstanceState);
        map_location.getMapAsync(this);

        btn_input.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getAddress();
            }
        });

        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String location = edt_search.getText().toString().trim();
                searchLocation(location);
            }
        });


        btn_sendOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(fromCustomerActivity && latitude !=0.0 && longitude != 0.0){
                    pr_request.setVisibility(View.VISIBLE);
                    pr_request.setIndeterminate(false);
                    pr_request.setProgress(0);
                    String Id = reference.push().getKey();

                    reciver = new receiverFromProvider(info.getId(), toService.getId_customer(), toService.getName_customer()
                            , toService.getPhone_customer(),Id, info.getName() , info.getEmail() , info.getPhone_number()
                            , tv_catogery.getText().toString().trim(), tv_description.getText().toString().trim()
                            , latitude , longitude , toService.getUrlImage() , info.getStartRate(),
                            Float.parseFloat(edt_price.getText().toString().trim()),0,0,0 ,toService.getDate() , info.getExperience());

                    sendOfferToCustomer(reciver , toService.getId_customer() , Id);
                    putInMyOffer(reciver , info.getId() , Id);
                    putAcceptInServiceDatabase("Service/"+toService.getCategory()+"/"+toService.getId_customer());
                    pr_request.setProgress(100);
                    onBackPressed();
                    finish();
                }else {
                    pr_request.setVisibility(View.VISIBLE);
                    pr_request.setIndeterminate(false);
                    pr_request.setProgress(0);
                    String path1 = "users/offer/"+reciver.getId_customer()+"/"+reciver.getId_offer();
                    String path2 = "provider/offer/"+reciver.getId_provider()+"/"+reciver.getId_offer();
                    changePriceAndSend(path1 , Float.parseFloat(edt_price.getText().toString().trim()));
                    changePriceAndSend(path2, Float.parseFloat(edt_price.getText().toString().trim()));
                    putAcceptInServiceDatabase("Service/"+reciver.getCategory()+"/"+reciver.getId_customer());
                    pr_request.setProgress(100);
                    onBackPressed();
                    finish();
                }


            }
        });

    }
    private void sendOfferToCustomer (Object object , String id,  String idOffer){

        reference = FirebaseDatabase.getInstance().getReference("users").child("offer");
        reference.child(id).child(idOffer).setValue(object);
        Toast.makeText(this, "Send Offer", Toast.LENGTH_SHORT).show();

    }
    private void putAcceptInServiceDatabase (String path){

        reference = FirebaseDatabase.getInstance().getReference(path);
        reference.child("makeOffer").setValue(true);
        Toast.makeText(this, "Send Offer to Service", Toast.LENGTH_SHORT).show();

    }
    private void putInMyOffer(Object object , String idprovider , String idChild){
        reference = FirebaseDatabase.getInstance().getReference("provider").child("offer");
        reference.child(idprovider).child(idChild).setValue(object);
        Toast.makeText(this, "Put offer in myOffer", Toast.LENGTH_SHORT).show();


    }
    private void changePriceAndSend(String path , float price){
        reference = FirebaseDatabase.getInstance().getReference(path);
        reference.child("price").setValue(price);
        Toast.makeText(this, "Change and Send Price", Toast.LENGTH_SHORT).show();

    }
    private void downloadImage(StorageReference m ){
        final long ONE_MEGABYTE = 1024*1024*5 ;
        m.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes , 0 , bytes.length);
                img_make.setVisibility(View.VISIBLE);
                img_make.setImageBitmap(bitmap);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MakeOfferActivity.this, "onFailure to"+e, Toast.LENGTH_SHORT).show();
            }
        });

    }
    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        map_location.onSaveInstanceState(outState);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        locationHelper.onActivityResult(requestCode, resultCode, data);
    }
    @Override
    protected void onResume() {
        super.onResume();
        locationHelper.checkPlayServices();
        map_location.onResume();
    }
    private void getAddress(){
        mLastLocation=locationHelper.getLocation();

        if (mLastLocation != null) {

            new AlertDialog.Builder(this).setTitle("Your Location ")
                    .setMessage("Do you want to place your current location ?").setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    ll_search.setVisibility(View.VISIBLE);
                    dialogInterface.dismiss();
                }
            }).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    ll_search.setVisibility(View.GONE);
                    latitude = mLastLocation.getLatitude();
                    longitude = mLastLocation.getLongitude();
                    showMaps();
                    dialogInterface.dismiss();
                }
            }).show();



        } else {
            Toast.makeText(this, "Couldn't get the location. Make sure location is enabled on the device", Toast.LENGTH_SHORT).show();
        }
    }

    private void showMaps (){
        map_location.getMapAsync(this);
    }

    private void searchLocation(String location){

        List<Address> addressList =new ArrayList<Address>();

        if (location != null || !location.equals("")) {
            Geocoder geocoder = new Geocoder(this);
            try {
                addressList = geocoder.getFromLocationName(location, 1);

            } catch (IOException e) {
                e.printStackTrace();
            }
            Address address = addressList.get(0);
            LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
            latitude = address.getLatitude() ;
            longitude = address.getLongitude() ;
            if(marker != null)
                marker.remove();
            marker = gMap.addMarker(new MarkerOptions().position(latLng).title(location));
            gMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
            Toast.makeText(getApplicationContext(),address.getLatitude()+" "+address.getLongitude(),Toast.LENGTH_LONG).show();
        }
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
            markerOptions.position(lng).title("your location");
            if(marker != null)
                marker.remove();
            marker = gMap.addMarker(markerOptions);
            gMap.moveCamera(CameraUpdateFactory.newLatLng(lng));
        }
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
