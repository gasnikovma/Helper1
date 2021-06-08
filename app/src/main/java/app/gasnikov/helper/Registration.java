package app.gasnikov.helper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class Registration extends AppCompatActivity implements View.OnClickListener {
    private FirebaseAuth mauth;
    private Button registernow;

    private EditText fullname,email,password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        mauth=FirebaseAuth.getInstance();

        registernow=(Button)findViewById(R.id.registeruser);
        registernow.setOnClickListener(this);
        fullname=(EditText)findViewById(R.id.fullname);
        email=(EditText)findViewById(R.id.email);
        password=(EditText)findViewById(R.id.password);


    }
    public void onBackPressed(){
        try{
            Intent intent = new Intent(Registration.this,MainActivity.class);
            startActivity(intent);
            finish();
        }
        catch(Exception e){

        }

    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.registeruser){
            registeruser();

        }

    }
    private void registeruser(){
        String nemail=email.getText().toString().trim();
        String npassword=password.getText().toString().trim();
        String nfullname=fullname.getText().toString().trim();
        if(nemail.isEmpty()||nfullname.isEmpty()||npassword.isEmpty()){
            Toast.makeText(Registration.this, "All fields must be filled", Toast.LENGTH_LONG).show();
            return;
        }

        if(!android.util.Patterns.EMAIL_ADDRESS.matcher(nemail).matches()){
            email.setError("Please enter valid email");
            return;
        }
        if(password.length()<6){
            password.setError("Password must be of 6 characters at least");
            return;
        }
        mauth.createUserWithEmailAndPassword(nemail,npassword)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //если был успешно зарегистрирован
                        if(task.isSuccessful()){
                            User user =new User(nfullname,nemail);
                            FirebaseDatabase.getInstance().getReference("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(Registration.this, "You have been successfully registered!", Toast.LENGTH_LONG).show();
                                    } else {
                                        Toast.makeText(Registration.this, "Registration failed!", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });

                        }
                        else{
                            Toast.makeText(Registration.this, "Registration failed!", Toast.LENGTH_LONG).show();
                        }

                    }
                });




    }
}