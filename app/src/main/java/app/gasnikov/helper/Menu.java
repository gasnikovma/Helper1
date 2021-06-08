package app.gasnikov.helper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.os.Bundle;
import android.view.MenuItem;


import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import app.gasnikov.helper.fragments.First;
import app.gasnikov.helper.fragments.Second;
import app.gasnikov.helper.fragments.Third;

public class Menu extends AppCompatActivity {
    Fragment first=new First();
    Fragment second=new Second();
    Fragment third=new Third();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        change(first);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);


    }
    private void change(Fragment f){
        if(f!=null){
            FragmentTransaction ft=getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_container,f);
        ft.commit();
        }
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