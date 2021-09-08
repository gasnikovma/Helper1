package app.gasnikov.helper.fragments.helper.fragments;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import app.gasnikov.helper.Map_Activity;
import app.gasnikov.helper.R;
import app.gasnikov.helper.Settings;

public class First extends Fragment {
    private FragmentActivity myContext;
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
    public void onAttach(Context context) {
        super.onAttach(context);
        myContext=(FragmentActivity) context;

    }
}