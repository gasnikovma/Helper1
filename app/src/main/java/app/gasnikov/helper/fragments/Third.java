package app.gasnikov.helper.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import app.gasnikov.helper.R;
import app.gasnikov.helper.User;


public class Third extends Fragment {

    private FirebaseUser user;
    private DatabaseReference ref;
    private String uid;
    private TextView email, fullname, medind;





        @Override
        public View onCreateView (LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState){
            View v = inflater.inflate(R.layout.fragment_third, container, false);
            user = FirebaseAuth.getInstance().getCurrentUser();
            ref = FirebaseDatabase.getInstance().getReference("users");
            uid = user.getUid();

            email=(TextView) v.findViewById(R.id.yemail);
            fullname=(TextView) v.findViewById(R.id.yfullname);
            medind=(TextView) v.findViewById(R.id.ymedcal_condition);
            ref.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    User u=snapshot.getValue(User.class);
                    if(u!=null){
                        String nemail=u.email;
                        String nfullname =u.fullname;
                        // String nmedind=u.medind;
                        email.setText(nemail);
                        fullname.setText(nfullname);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(getActivity(),"Try again!",Toast.LENGTH_LONG).show();
                }
            });



            return v;
        }
    }
