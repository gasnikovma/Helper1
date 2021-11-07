package app.gasnikov.helper.fragments.helper.fragments;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

import app.gasnikov.helper.LocationService;
import app.gasnikov.helper.Map_Activity;
import app.gasnikov.helper.Menu;
import app.gasnikov.helper.R;

public class First extends Fragment {
    private FragmentActivity myContext;
    private static final int PERMISSIONS_REQUEST_ENABLE_GPS =12345 ;

    CardView[] card = new CardView[6];
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_first, container, false);
        card[0]=(CardView)v.findViewById(R.id.c1);
        card[1]=(CardView)v.findViewById(R.id.c2);
        card[2]=(CardView)v.findViewById(R.id.c3);
        card[3]=(CardView)v.findViewById(R.id.c4);
        card[4]=(CardView)v.findViewById(R.id.c5);
        card[5]=(CardView)v.findViewById(R.id.c6);
        for(int i=0;i<6;i++) {

            card[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(getActivity(), Map_Activity.class);
                    startActivity(intent);
                }
            });
        }


        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        /*final LocationManager manager = (LocationManager) getActivity().getSystemService( Context.LOCATION_SERVICE );
        if ( !manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
            buildAlertMessageNoGps();
        }*/
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        myContext=(FragmentActivity) context;

    }
   /* private void buildAlertMessageNoGps(){
        final AlertDialog.Builder builder=new AlertDialog.Builder(getContext());
        builder.setMessage("This application requires GPS to work properly, do you want to enable it?").setCancelable(false).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent gps=new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivityForResult(gps,PERMISSIONS_REQUEST_ENABLE_GPS);
            }
        });
        final AlertDialog alertDialog=builder.create();
        alertDialog.show();
    }*/

}