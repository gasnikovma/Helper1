package app.gasnikov.helper;

import android.Manifest;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;

import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


import android.provider.Settings;
import android.util.Log;

import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.microedition.khronos.egl.EGLDisplay;

public class Map_Activity extends AppCompatActivity {
    private Boolean lp = false;
    private EditText search;
    private static final int L_P_REQUEST_CODE = 12345;
    public static final int PERMISSIONS_REQUEST_ENABLE_GPS = 9003;
    public static final int ERROR_DIALOG_REQUEST = 9001;
    private static final String TAG = "MapActivity";
    private FusedLocationProviderClient fusedLocationProviderClient;
    private GoogleMap map;
    private UserLocation userLocation;

    FirebaseFirestore db = FirebaseFirestore.getInstance();




    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_map);
        search = (EditText) findViewById(R.id.input);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);


    }
    private void startLocationService(){
        if(!isLocationServiceRunning()){
            Intent serviceIntent = new Intent(this, LocationService.class);
//        this.startService(serviceIntent);

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O){

                Map_Activity.this.startForegroundService(serviceIntent);
            }else{
                startService(serviceIntent);
            }
        }
    }

    private boolean isLocationServiceRunning() {
        ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)){
            if("app.gasnikov.helper".equals(service.service.getClassName())) {
                Log.d(TAG, "isLocationServiceRunning: location service is already running.");
                return true;
            }
        }
        Log.d(TAG, "isLocationServiceRunning: location service is not running.");
        return false;
    }
    //step one(retrieve the user detailes)
    private void getUser(){
        if(userLocation==null){
            userLocation=new UserLocation();
            DocumentReference user=db.collection("Users").document(FirebaseAuth.getInstance().getCurrentUser().getUid());
            user.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if(task.isSuccessful()){
                        User user=task.getResult().toObject(User.class);
                        userLocation.setUser(user);
                        getDeviceLocation();

                    }
                }
            });
        }
        else {
            getDeviceLocation();
        }
    }
    //step two(getting the gps coord)
    private void getDeviceLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            Task location = fusedLocationProviderClient.getLastLocation();
            location.addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    if(task.isSuccessful()){
                        Location curloc=(Location)task.getResult();
                        LatLng latLng=new LatLng(curloc.getLatitude(),curloc.getLongitude());
                        map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,15));

                        GeoPoint geoPoint = new GeoPoint(curloc.getLatitude(),curloc.getLongitude());
                        userLocation.setGeo_point(geoPoint);
                        userLocation.setTime_stamp(null);
                        saveUserLocation();
                        startLocationService();

                    }
                    else{
                        Toast.makeText(Map_Activity.this,"unable to get the current location",Toast.LENGTH_LONG).show();
                    }
                }
            });
        }

    }
    //step three(upload into firestore)
    private void saveUserLocation(){
        if(userLocation!=null){
            DocumentReference location=db.collection("User Locations").document(FirebaseAuth.getInstance().getCurrentUser().getUid());
            location.set(userLocation);

        }

    }

    private void init() {
        search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH || actionId == EditorInfo.IME_ACTION_DONE || event.getAction() == KeyEvent.ACTION_DOWN || event.getAction() == KeyEvent.KEYCODE_ENTER) {
                    String searchstr = search.getText().toString();
                    Geocoder geocoder = new Geocoder(Map_Activity.this);
                    List<Address> list = new ArrayList<>();
                    try {
                        list = geocoder.getFromLocationName(searchstr, 1);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (list.size() > 0) {
                        Address address = list.get(0);
                        // Toast.makeText(Map_Activity.this,address.toString(),Toast.LENGTH_LONG).show();
                    }


                }
                return false;
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode){
            case PERMISSIONS_REQUEST_ENABLE_GPS:{
                if(lp){
                    initMap();
                   getUser();
                }
                else{
                    getLocationPermission();
                }
            }
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.d(TAG, "CALLED");

        lp = false;
        switch (requestCode) {
            case L_P_REQUEST_CODE:
                if (grantResults.length > 0) {
                    for (int i = 0; i < grantResults.length; i++) {
                        if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                            lp = true;
                            initMap();
                            getUser();

                        }

                    }
                }

        }
    }
    private void initMap() {
        SupportMapFragment supportMapFragment = (SupportMapFragment)
                getSupportFragmentManager().findFragmentById(R.id.google_map);
        supportMapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull GoogleMap googleMap) {
                map = googleMap;
                if (ActivityCompat.checkSelfPermission(Map_Activity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(Map_Activity.this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                    map.setMyLocationEnabled(true);
                    map.getUiSettings().setMyLocationButtonEnabled(false);
                    init();
                    map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                        @Override
                        public void onMapClick(@NonNull LatLng latLng) {
                            //когда нажали на карту
                            MarkerOptions markerOptions = new MarkerOptions();

                            //установка позиции маркера
                            markerOptions.position(latLng);
                            markerOptions.title(getResources().getString(R.string.incident));
                            map.clear();
                            map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
                            map.addMarker(markerOptions);

                            return;
                        }


                    });
                }
            }
        });
    }

    private void getLocationPermission() {
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                lp = true;
                initMap();

                getUser();

            } else {
                ActivityCompat.requestPermissions(this, permissions, L_P_REQUEST_CODE);

            }
        } else {
            ActivityCompat.requestPermissions(this, permissions, L_P_REQUEST_CODE);

        }


    }
    private void buildAlertMessageNoGps(){
        final AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setMessage("This application requires GPS to work properly, do you want to enable it?").setCancelable(false).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent gps=new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivityForResult(gps,PERMISSIONS_REQUEST_ENABLE_GPS);
            }
        });
        final AlertDialog alertDialog=builder.create();
        alertDialog.show();
    }
    private boolean checkMapServices(){
        if(isServicesOK()){
            if(isMapsEnabled()){
                return true;
            }
        }
        return false;
    }

    public boolean isMapsEnabled(){
        final LocationManager manager = (LocationManager) getSystemService( Context.LOCATION_SERVICE );

        if ( !manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
            buildAlertMessageNoGps();
            return false;
        }
        return true;
    }
    public boolean isServicesOK(){
        Log.d(TAG, "isServicesOK: checking google services version");

        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(Map_Activity.this);

        if(available == ConnectionResult.SUCCESS){
            //everything is fine and the user can make map requests
            Log.d(TAG, "isServicesOK: Google Play Services is working");
            return true;
        }
        else if(GoogleApiAvailability.getInstance().isUserResolvableError(available)){
            //an error occured but we can resolve it
            Log.d(TAG, "isServicesOK: an error occured but we can fix it");
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(Map_Activity.this, available, ERROR_DIALOG_REQUEST);
            dialog.show();
        }else{
            Toast.makeText(this, "You can't make map requests", Toast.LENGTH_SHORT).show();
        }
        return false;
    }






        @Override
        protected void onResume() {
            super.onResume();
            if(checkMapServices()){
                if(lp){
                    initMap();

                    getUser();

                }
                else{
                    getLocationPermission();
                }
            }
        }
    }


