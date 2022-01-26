package app.gasnikov.helper.fragments.helper.fragments;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.DatabaseReference.CompletionListener;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import app.gasnikov.helper.LocationService;
import app.gasnikov.helper.MainActivity;
import app.gasnikov.helper.Menu2;
import app.gasnikov.helper.Message;
import app.gasnikov.helper.MessageAdapter;
import app.gasnikov.helper.Message_Activity;
import app.gasnikov.helper.R;
import app.gasnikov.helper.Settings;
import app.gasnikov.helper.User;
import app.gasnikov.helper.UserAdapter;

public class Third extends Fragment {

    private FirebaseUser user;

    private String uid;
    private TextView email, fullname,blood_type,rh_factor,cd,ar;
    private Button logout;
    private Button set;
    private Button finish;
    FirebaseDatabase db = FirebaseDatabase.getInstance();

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.fragment_third_1, container, false);
        user = FirebaseAuth.getInstance().getCurrentUser();

        set=(Button)v.findViewById(R.id.settings);
            uid = user.getUid();
        email=(TextView) v.findViewById(R.id.yemail);
        fullname=(TextView) v.findViewById(R.id.yfullname);
        blood_type=(TextView)v.findViewById(R.id.blood_type_3);
        rh_factor=(TextView)v.findViewById(R.id.rh_factor_3);
        cd=(TextView)v.findViewById(R.id.cd_3);
        ar=(TextView)v.findViewById(R.id.ar_3);
        logout=(Button)v.findViewById(R.id.signout);
        finish=(Button)v.findViewById(R.id.calls);



        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog();
            }
        });
        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.getReference("Incidents").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(null).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getContext(), "Сall completed successfully", Toast.LENGTH_LONG).show();
                            db.getReference().child("Chats").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                                        Message message = snapshot1.getValue(Message.class);
                                        if (message != null) {
                                            if (message.getReceiver().equals(FirebaseAuth.getInstance().getCurrentUser().getUid()) || message.getSender().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                                                snapshot1.getRef().removeValue();
                                            }
                                        }
                                    }
                                }
                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        }
                    }
                });


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
        db.getReference("Users").child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User u=snapshot.getValue(User.class);
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

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




        return v;
    }
    private void dialog(){
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setCancelable(false);
        builder.setTitle("Are you sure you want to log out of your account?");
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

            }
        });
        builder.setPositiveButton("Log out", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                getActivity().stopService(
                        new Intent(getContext(), LocationService.class));
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);

            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }


    @Override
    public void onResume() {
        super.onResume();

    }


}
