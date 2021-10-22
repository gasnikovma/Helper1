package app.gasnikov.helper;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.google.android.gms.tasks.OnSuccessListener;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Message_Activity extends AppCompatActivity {
    private TextView username;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
private Toolbar toolbar;
private Intent intent;
private ImageButton send;
private EditText txt_send;
private MessageAdapter messageAdapter;
private List<Message> messageList;
private String uid;
private RecyclerView recyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        toolbar=findViewById(R.id.toolbar);
        send=findViewById(R.id.send);
        txt_send=findViewById(R.id.txt_send);
        recyclerView=findViewById(R.id.recycler_view);
        LinearLayoutManager llm= new LinearLayoutManager(getApplicationContext());
        
        recyclerView.setLayoutManager(llm);


        setSupportActionBar(toolbar);//заменяем toolbar на actionbar и гарантируем что не null
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        username=findViewById(R.id.username);
        intent=getIntent();
         uid=intent.getStringExtra("getuserid");
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg=txt_send.getText().toString();
                if(!msg.equals("")){
                    sendMsg(FirebaseAuth.getInstance().getCurrentUser().getUid(),uid,msg);

                }
                else{
                    Toast.makeText(Message_Activity.this,"Empty field",Toast.LENGTH_LONG).show();
                }
                txt_send.setText("");
            }
        });

        DocumentReference docRef = db.collection("Users").document(uid);
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                User u = documentSnapshot.toObject(User.class);
                if (u != null) {
                    username.setText(u.getFullname());

                }
                readMsg(FirebaseAuth.getInstance().getCurrentUser().getUid(),uid);
            }


        });









    }
    private void sendMsg(final String sender,final String receiver,final String message){
        HashMap<String,Object> msg=new HashMap<>();
        msg.put("receiver",receiver);
        msg.put("sender",sender);
        msg.put("message",message);
        db.collection("Chats").document(uid).set(msg);
    }
    private  void readMsg(String mid,String recid){
        messageList=new ArrayList<>();
        db.collection("Chats").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    return;
                }
                    messageList.clear();
                    for (QueryDocumentSnapshot document : value) {
                        Message message = document.toObject(Message.class);
                        if ((message.getReceiver().equals(FirebaseAuth.getInstance().getCurrentUser().getUid()) && message.getSender().equals(uid))||(message.getSender().equals(FirebaseAuth.getInstance().getCurrentUser().getUid()) && message.getReceiver().equals(uid))) {
                            messageList.add(message);
                        }



                    }
                messageAdapter = new MessageAdapter(getApplicationContext(), messageList);
                recyclerView.setAdapter(messageAdapter);

                }

                });


            }



}
