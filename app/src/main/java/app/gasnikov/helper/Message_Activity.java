package app.gasnikov.helper;

import androidx.annotation.NonNull;
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




import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Message_Activity extends AppCompatActivity {
    private TextView username;
    FirebaseDatabase db = FirebaseDatabase.getInstance();
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

        db.getReference("Users").child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User u= snapshot.getValue(User.class);
                if(u!=null){
                    username.setText(u.getFullname());
                }
                readMsg(FirebaseAuth.getInstance().getCurrentUser().getUid(),uid);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });









    }
    private void sendMsg( String sender, String receiver, String message){
        HashMap<String,Object> msg=new HashMap<>();
        msg.put("receiver",receiver);
        msg.put("sender",sender);
        msg.put("message",message);
        db.getReference().child("Chats").push().setValue(msg);
    }
   private  void readMsg(String mid,String yid){
        messageList=new ArrayList<>();
        db.getReference().child("Chats").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messageList.clear();
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    Message message = snapshot1.getValue(Message.class);
                    if(message.getSender()==null){
                        Toast.makeText(Message_Activity.this,"Ппц",Toast.LENGTH_LONG).show();
                    }
                   if (message.getReceiver().equals(mid) && message.getSender().equals(yid) || message.getSender().equals(mid) && message.getReceiver().equals(yid)) {
                            messageList.add(message);
                        }


                        messageAdapter = new MessageAdapter(Message_Activity.this, messageList);
                        recyclerView.setAdapter(messageAdapter);


                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }



                });


            }



}
