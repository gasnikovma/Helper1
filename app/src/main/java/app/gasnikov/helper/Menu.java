package app.gasnikov.helper;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;


import androidx.fragment.app.FragmentTransaction;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;

import app.gasnikov.helper.Notifications.Token;
import app.gasnikov.helper.fragments.helper.fragments.First;
import app.gasnikov.helper.fragments.helper.fragments.Second;
import app.gasnikov.helper.fragments.helper.fragments.Third;


public class Menu extends AppCompatActivity {
    private static final int PERMISSIONS_REQUEST_ENABLE_GPS =12345 ;
    Fragment first=new First();
    Fragment second=new Second();
    Fragment third=new Third();
    FirebaseDatabase db = FirebaseDatabase.getInstance();






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        change(first);

        Intent serviceIntent = new Intent(Menu.this, LocationService.class);
        if (android.os.Build.VERSION.SDK_INT >= 26){

           Menu.this.startForegroundService(serviceIntent);

        }else{
            Menu.this.startService(serviceIntent);
        }

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        getToken();


    }

    @Override
    protected void onResume() {
        super.onResume();
        if(isgpsenabled()==false){
            buildAlertMessageNoGps();
        }
    }


    private void change(Fragment f){
        if(f!=null){
            FragmentTransaction ft=getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.fragment_container,f);
            ft.commit();
        }
    }
    private void updateToken(String token){
        Token token1=new Token(token);
        db.getReference("Tokens").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(token1);
    }
    private void getToken(){
        FirebaseMessaging.getInstance().getToken().addOnSuccessListener(this::updateToken);
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
    private boolean isgpsenabled(){
        final LocationManager manager = (LocationManager)getSystemService( Context.LOCATION_SERVICE );
        if ( !manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
            return  false;
        }
        else return true;
    }
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = item -> {
        switch (item.getItemId()) {
            case R.id.current_calls:
                change(second);
                return true;
            case R.id.types_of_calls:
                change(first);
                return true;
            case R.id.settings:
                change(third);

                return true;
        }
        return false;
    };




}