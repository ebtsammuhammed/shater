package com.example.shater.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.shater.LocationUtil.LocationHelper;
import com.example.shater.R;
import com.example.shater.helper.CacheJson;
import com.example.shater.models.requestCustomerToService;
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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.shawnlin.numberpicker.NumberPicker;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class RequestActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, ActivityCompat.OnRequestPermissionsResultCallback , OnMapReadyCallback {
   // NumberPicker np_category ;
    Spinner sp_category;
    EditText edt_description , edt_time , edt_date , edt_search;
    Button btn_addImage , btn_addVideo , btn_confirm ;
    MapView mv_location ;
    ImageView img_addImage , imv_time ,imv_date , btn_search , btn_inputLocation ;
    VideoView vid_addVideo;
    ProgressBar pr_request ;
    LinearLayout ll_search;
    private int mYear , mMounth , mDay , mHour , mMinute ;
    String valueCategory , description , time , date , urlImage= null ;
    userInfo userInfo ;

    private StorageReference mStorageRef;
    private StorageTask task ;
    private DatabaseReference  databaseReference ;
    public Uri imageUri ;


    private Location mLastLocation;
    double latitude;
    double longitude;
    LocationHelper locationHelper;
    private GoogleMap gMap ;
    Marker marker;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request);

        locationHelper=new LocationHelper(this);
        locationHelper.checkpermission();
        // check availability of play services
        if (locationHelper.checkPlayServices()) {

            // Building the GoogleApi client
            locationHelper.buildGoogleApiClient();
        }

        mStorageRef = FirebaseStorage.getInstance().getReference("Images");

        // Get data of user from Cache
        userInfo = new userInfo();
        CacheJson cacheJson = new CacheJson();
        try {
            userInfo = (userInfo) cacheJson.readObject(RequestActivity.this , "userInfo");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        sp_category = (Spinner) findViewById(R.id.sp_categray);
        edt_description = (EditText) findViewById(R.id.edt_description);
        btn_addImage = (Button) findViewById(R.id.btn_addimage);
        btn_addVideo =(Button) findViewById(R.id.btn_addvideo);
        btn_confirm = (Button) findViewById(R.id.btn_confirm);
        mv_location = (MapView) findViewById(R.id.mv_location);
        img_addImage = (ImageView) findViewById(R.id.img_add);
        vid_addVideo = (VideoView) findViewById(R.id.vi_add);
        pr_request = (ProgressBar) findViewById(R.id.pb_request);


        edt_date = (EditText) findViewById(R.id.edt_date);
        edt_time = (EditText) findViewById(R.id.edt_time);
        imv_date = (ImageView) findViewById(R.id.imv_date);
        imv_time = (ImageView) findViewById(R.id.imv_time);

        btn_inputLocation =(ImageView) findViewById(R.id.btn_inputLocation);
        btn_search =(ImageView) findViewById(R.id.btn_search);
        edt_search = (EditText) findViewById(R.id.edt_search);
        ll_search = (LinearLayout) findViewById(R.id.ll_search);

        mv_location.onCreate(savedInstanceState);
        mv_location.getMapAsync(this);

        btn_inputLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getAddress();
            }
        });


        btn_addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                choosImage();
            }
        });

        // set string value in numberpicker
        final String [] category = {"Home decor","Technology","Repair","Painting","Parking shades","Electricity" ,"A/C Maintenance" , "Plumping",
                                            "Carpentry", "Moving Furniture" , "Floor tiling"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,category);
        sp_category.setAdapter(adapter);

        sp_category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                valueCategory = (String)adapterView.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                valueCategory = null;
            }
        });


        imv_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 final Calendar calendar = Calendar .getInstance();
                 mYear = calendar.get(Calendar.YEAR);
                 mMounth = calendar.get(Calendar.MONTH);
                 mDay = calendar.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog dialog = new DatePickerDialog(RequestActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int mounth, int day) {
                        String dayset = day+"";
                        if (dayset.length() == 1)
                            dayset = "0"+day;
                        int mou = mounth+1;
                        String mounthSet = mou+"";
                        if (mounthSet.length() == 1)
                            mounthSet ="0"+mounthSet ;
                        date = dayset +"/"+mounthSet+"/"+year ;
                        edt_date.setText(date);

                    }
                },mYear , mMounth , mDay );
               dialog.show();
            }
        });

        imv_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar calendar = Calendar .getInstance();
                mHour = calendar.get(Calendar.HOUR_OF_DAY);
                mMinute = calendar.get(Calendar.MINUTE);

                TimePickerDialog pickerDialog = new TimePickerDialog(RequestActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                        time = hour +":"+minute ;
                        edt_time.setText(time);
                    }
                },mHour , mMinute , false);
                pickerDialog.show();

            }
        });

        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // get description from edtitext
                description = edt_description.getText().toString().trim();
                if(task != null && task.isInProgress()){
                    Toast.makeText(RequestActivity.this, "upload in progress", Toast.LENGTH_SHORT).show();
                }
                else {
                    if (!TextUtils.isEmpty(valueCategory) && !TextUtils.isEmpty(date) && !TextUtils.isEmpty(time) && !TextUtils.isEmpty(description) &&
                            latitude != 0.0 && longitude !=0.0 ){
                         pr_request.setVisibility(View.VISIBLE);
                         pr_request.setIndeterminate(false);
                         pr_request.setProgress(0);
                        sentRequest(valueCategory , date , time , description ,latitude , longitude);


                    }
                    else {
                        Toast.makeText(RequestActivity.this, " please Insert All Data !!", Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });

        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String location = edt_search.getText().toString().trim();
                searchLocation(location);
            }
        });

    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        mv_location.onSaveInstanceState(outState);
    }

    // Get Imag from your device
    private void choosImage (){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent , 1);
    }

    private String getExtension(Uri uri){
        ContentResolver resolver = getContentResolver();
        MimeTypeMap typeMap = MimeTypeMap.getSingleton();
        return typeMap.getExtensionFromMimeType(resolver.getType(uri));
    }

    private void sentRequest (String valueCategory , String date , String time , String description  , double lat , double lng ){


        // insert data in file JSON of requestCustomerToService
        String imageId = System.currentTimeMillis()+"."+getExtension(imageUri) ;
        requestCustomerToService  toService = new requestCustomerToService();
        toService.setCategory(valueCategory);
        toService.setTime(time);
        toService.setDate(date);
        toService.setId_customer(userInfo.getId());
        toService.setDescription(description);
        toService.setEmail_customer(userInfo.getEmail());
        toService.setName_customer(userInfo.getName());
        toService.setPhone_customer(userInfo.getPhone_number());
        toService.setLat(lat);
        toService.setLng(lng);
        toService.setUrlImage(imageId);
        toService.setMakeOffer(false);


       // set data in firebasedatabase
       databaseReference = FirebaseDatabase.getInstance().getReference().child("Service").child(valueCategory);
       databaseReference.child(userInfo.getId()).setValue(toService);

       // upload image in firebasStorage
        StorageReference reference = mStorageRef.child(imageId);
        task =reference.putFile(imageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // Get a URL to the uploaded content
                       // Uri downloadUrl = taskSnapshot.getDownloadUrl();
                        Toast.makeText(RequestActivity.this, "image upload Successfully ", Toast.LENGTH_SHORT).show();
                        pr_request.setProgress(100);
                        onBackPressed();
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Toast.makeText(RequestActivity.this, "image upload Failure "+exception, Toast.LENGTH_SHORT).show();

                    }
                });
    }


    public void getAddress()
    {
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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        locationHelper.onActivityResult(requestCode, resultCode, data);

        if(requestCode==1 && resultCode == RESULT_OK && data != null && data.getData() != null){

            imageUri = data.getData();
            img_addImage.setVisibility(View.VISIBLE);
            img_addImage.setImageURI(imageUri);
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        locationHelper.checkPlayServices();
        mv_location.onResume();
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
    private void showMaps (){
        mv_location.getMapAsync(this);
    }


    public void searchLocation(String location) {

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
    protected void onStart() {
        super.onStart();
        mv_location.onStart();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mv_location.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mv_location.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mv_location.onLowMemory();
    }

    @Override
    public boolean isActivityTransitionRunning() {
        return super.isActivityTransitionRunning();
    }
}
