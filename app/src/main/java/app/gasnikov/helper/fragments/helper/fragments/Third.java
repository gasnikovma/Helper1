package app.gasnikov.helper.fragments.helper.fragments;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import app.gasnikov.helper.MainActivity;
import app.gasnikov.helper.Menu2;
import app.gasnikov.helper.R;
import app.gasnikov.helper.Settings;
import app.gasnikov.helper.User;
public class Third extends Fragment {

    private FirebaseUser user;

    private String uid;
    private TextView email, fullname,blood_type,rh_factor,cd,ar;
    private Button logout;
    private Button set;
    private Button ia;
    FirebaseFirestore db = FirebaseFirestore.getInstance();




    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.fragment_third_1, container, false);
        user = FirebaseAuth.getInstance().getCurrentUser();

        set=(Button)v.findViewById(R.id.settings);
        uid = user.getUid();
        email=(TextView) v.findViewById(R.id.yemail);
        ia=(Button)v.findViewById(R.id.ia);
        fullname=(TextView) v.findViewById(R.id.yfullname);
        blood_type=(TextView)v.findViewById(R.id.blood_type_3);
        rh_factor=(TextView)v.findViewById(R.id.rh_factor_3);
        cd=(TextView)v.findViewById(R.id.cd_3);
        ar=(TextView)v.findViewById(R.id.ar_3);
        logout=(Button)v.findViewById(R.id.signout);



        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);

            }
        });
        ia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(), Menu2.class);
                startActivity(intent);

            }
        });
        set.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Settings.class);
                intent.putExtra("blood_type",blood_type.getText().toString());
                intent.putExtra("rh_factor",rh_factor.getText().toString());
                intent.putExtra("cd",cd.getText().toString());
                intent.putExtra("ar",ar.getText().toString());
                startActivity(intent);

            }
        });
        DocumentReference docRef = db.collection("Users").document(uid);
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                User u = documentSnapshot.toObject(User.class);
                if (u != null) {
                    String nemail = u.email;
                    String nfullname = u.fullname;
                    String nblood_type = u.blood_type;
                    String nrh_factor = u.rh_factor;
                    String ncd = u.cd;
                    String nar = u.ar;
                    email.setText(nemail);
                    fullname.setText(nfullname);
                    blood_type.setText(nblood_type);
                    rh_factor.setText(nrh_factor);
                    cd.setText(ncd);
                    ar.setText(nar);
                }
            }
        });



        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        DocumentReference docRef1 = db.collection("Users").document(uid);
        docRef1.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                User u = documentSnapshot.toObject(User.class);
                if (u != null) {
                    String nemail = u.email;
                    String nfullname = u.fullname;
                    String nblood_type = u.blood_type;
                    String nrh_factor = u.rh_factor;
                    String ncd = u.cd;
                    String nar = u.ar;
                    email.setText(nemail);
                    fullname.setText(nfullname);
                    blood_type.setText(nblood_type);
                    rh_factor.setText(nrh_factor);
                    cd.setText(ncd);
                    ar.setText(nar);
                }
                else {
                    Toast.makeText(getActivity(),"Try again",Toast.LENGTH_LONG).show();
                }
            }
        });



    }


}
