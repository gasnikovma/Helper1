package app.gasnikov.helper.fragments.helper.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.protobuf.StringValue;

import java.util.ArrayList;
import java.util.List;

import app.gasnikov.helper.R;
import app.gasnikov.helper.User;
import app.gasnikov.helper.UserAdapter;


public class UsersFragment extends Fragment {
    private RecyclerView recyclerView;
    private UserAdapter userAdapter;
    private List<User> userList;

    FirebaseFirestore db = FirebaseFirestore.getInstance();



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_users,container,false);
        recyclerView=view.findViewById(R.id.rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        userList=new ArrayList<>();
        readusername();


        return view;
    }


    private void readusername() {


         db.collection("Users").get()
                 .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                     @Override
                     public void onComplete(@NonNull Task<QuerySnapshot> task) {
                         if (task.isSuccessful()) {
                             userList.clear();
                             for (QueryDocumentSnapshot document : task.getResult()) {
                                 User u = document.toObject(User.class);
                                 if (u!=null && !FirebaseAuth.getInstance().getCurrentUser().getUid().equals(u.getId())) {
                                     userList.add(u);



                                 }
                             }
                         } else {
                             Toast.makeText(getActivity(),"Try again",Toast.LENGTH_LONG).show();
                         }
                         userAdapter=new UserAdapter(getContext(),userList);
                         recyclerView.setAdapter(userAdapter);

                     }

            });

    }
}