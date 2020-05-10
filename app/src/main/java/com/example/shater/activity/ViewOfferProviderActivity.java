package com.example.shater.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shater.LocationUtil.LocationHelper;
import com.example.shater.R;
import com.example.shater.fragment.OfferCustomerFragment;
import com.example.shater.helper.CacheJson;
import com.example.shater.models.receiverFromProvider;
import com.example.shater.models.userInfo;
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

import java.io.IOException;

public class ViewOfferProviderActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, ActivityCompat.OnRequestPermissionsResultCallback , OnMapReadyCallback {

    TextView tv_name , tv_price ;
    RatingBar rb_ratingService ;
    Button btn_acceptOrder , btn_pay , btn_ratePovider ;
    MapView map_Provider ;
    float numberRating ;
    receiverFromProvider reciver ;
    CacheJson cacheJson ;
    userInfo info ;
    DatabaseReference reference ;
    SharedPreferences preferences ;
    SharedPreferences.Editor editor ;
    final String PAY ="pay";
    final String RATE = "rate";
    boolean FROM;

    private Location mLastLocation;
    double latitude;
    double longitude;
    LocationHelper locationHelper;
    private GoogleMap gMap ;
    Marker marker;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_offer_provider);

        locationHelper=new LocationHelper(this);
        locationHelper.checkpermission();
        // check availability of play services
        if (locationHelper.checkPlayServices()) {
            // Building the GoogleApi client
            locationHelper.buildGoogleApiClient();
            mLastLocation=locationHelper.getLocation();
        }

        reference = FirebaseDatabase.getInstance().getReference();
        FROM = getIntent().getBooleanExtra("fromCustomerActivity" , false);
        preferences = getPreferences(Context.MODE_PRIVATE);
        editor = preferences.edit();
        Gson gson = new Gson();


            try {
                if (FROM){
                    reciver = (receiverFromProvider)new CacheJson().readObject(ViewOfferProviderActivity.this , "reciverFromProvider");
                }else {
                    reciver = gson.fromJson(getIntent().getStringExtra("reciverFromProvider"), receiverFromProvider.class);
                    new CacheJson().writeObject(ViewOfferProviderActivity.this ,"reciverFromProvider" ,reciver);
                }
                info = (userInfo) new CacheJson().readObject(ViewOfferProviderActivity.this , "userInfo");
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }






        tv_name = (TextView) findViewById(R.id.tv_name_pro) ;
        tv_price = (TextView) findViewById(R.id.tv_price_pro) ;
        rb_ratingService = (RatingBar) findViewById(R.id.rb_provider);
        btn_acceptOrder = (Button) findViewById(R.id.btn_acceptorder);
        btn_pay = (Button) findViewById(R.id.btn_pay);
        btn_ratePovider = (Button) findViewById(R.id.btn_rate);
        map_Provider = (MapView) findViewById(R.id.map_order);

        tv_name.setText("Name: "+reciver.getName_provider());
        tv_price.setText("Price: "+(int)reciver.getPrice());
        rb_ratingService.setRating(reciver.getMunStartRating());


        latitude = reciver.getLat_provider();
        longitude = reciver.getLng_provider();
        map_Provider.onCreate(savedInstanceState);
        map_Provider.getMapAsync(this);

        btn_acceptOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                setAcceptOrderInDatabase("provider/offer/"+reciver.getId_provider()+"/"+reciver.getId_offer());
                setAcceptOrderInDatabase("users/offer/"+info.getId()+"/"+reciver.getId_offer());
                editor.putBoolean(PAY , false);
                editor.putBoolean(RATE , false);
                editor.commit();

            }
        });

        btn_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new AlertDialog.Builder(ViewOfferProviderActivity.this).setTitle("the order is Accepted  ")
                        .setMessage("the order is Accepted but Please Choose the Payment method .").setNegativeButton("Cash", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        editor.putBoolean("pay" , false);
                        editor.commit();
                    }
                }).setPositiveButton("Cradit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Gson gson = new Gson();
                        String json = gson.toJson(reciver);
                        Intent intent = new Intent(ViewOfferProviderActivity.this , CraditActivity.class);
                        intent.putExtra("reciverFromProvider" , json);
                        startActivity(intent);
                    }
                }).show();

            }
        });

        btn_ratePovider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog();
            }
        });


    }

//    private void showDialogRating(){
//
//        final AlertDialog.Builder builder =new AlertDialog.Builder(this);
//      //  @SuppressLint("ServiceCast") LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAUNCHER_APPS_SERVICE);
//
//        final View customLayout = getLayoutInflater().inflate(R.layout.custom_ratingbar_in_dialog, null);
//        final RatingBar rb = customLayout.findViewById(R.id.rb_provider_dialog);
//        builder.setView(customLayout);
//         builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//             @Override
//             public void onClick(DialogInterface dialogInterface, int i) {
//                 numberRating = rb.getRating();
//                 setNumberRatingInDatabase("provider/offer/"+reciver.getId_provider()+"/"+reciver.getId_offer() , numberRating);
//                 setNumberRatingInDatabase("users/offer/"+reciver.getId_customer()+"/"+reciver.getId_offer() , numberRating);
//                 setNumberRatingInDatabaseInProvider("provider/info/"+reciver.getId_provider(), numberRating);
//                 editor.putBoolean("rate" , true);
//                 editor.commit();
//             }
//         });
//         builder.setNegativeButton("Later Time", new DialogInterface.OnClickListener() {
//             @Override
//             public void onClick(DialogInterface dialogInterface, int i) {
//                 editor.putBoolean("rate" , false);
//                 editor.commit();
//                 dialogInterface.dismiss();
//             }
//         });
//         builder.setCancelable(false);
//         AlertDialog dialog = builder.create();
//         dialog.show();
//    }
    private void showDialog(){


        final Dialog dialog =new Dialog(ViewOfferProviderActivity.this);
        dialog.setContentView(R.layout.custom_ratingbar_in_dialog);

        final RatingBar rbReview = (RatingBar) dialog.findViewById(R.id.rb_review);
        Button btnAdd  = (Button) dialog.findViewById(R.id.btn_comment);
        Button btnCancel = (Button) dialog.findViewById(R.id.btn_cancel);


        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                numberRating = rbReview.getRating();
                setNumberRatingInDatabase("provider/offer/"+reciver.getId_provider()+"/"+reciver.getId_offer() , numberRating);
                setNumberRatingInDatabase("users/offer/"+reciver.getId_customer()+"/"+reciver.getId_offer() , numberRating);
                setNumberRatingInDatabaseInProvider("provider/info/"+reciver.getId_provider(), numberRating);
                editor.putBoolean("rate" , true);
                editor.commit();
                Toast.makeText(ViewOfferProviderActivity.this, "Rating ="+rbReview.getRating(), Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.putBoolean("rate" , false);
                editor.commit();
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void setAcceptOrderInDatabase(String path){

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference(path);
        reference.child("accept_customer").setValue(1);
    }

    private void setNumberRatingInDatabase (String path , float rating){

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference(path);
        reference.child("rating_customer").setValue(1);
        reference.child("munStartRating").setValue(rating);
    }

    private void setNumberRatingInDatabaseInProvider (String path , float rating) {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference(path);
        reference.child("startRate").setValue(rating);
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
        map_Provider.onSaveInstanceState(outState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        locationHelper.checkPlayServices();
        map_Provider.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
        map_Provider.onStart();
    }

    @Override
    protected void onPause() {
        super.onPause();
        map_Provider.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        map_Provider.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        map_Provider.onLowMemory();
    }

    @Override
    public boolean isActivityTransitionRunning() {
        return super.isActivityTransitionRunning();
    }
}
