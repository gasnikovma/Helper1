package app.gasnikov.helper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView registernow,fpassword;
    private EditText email,password;
    private Button login;

    private FirebaseAuth mauth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        registernow=(TextView)findViewById(R.id.register);
        registernow.setOnClickListener(this);
        login=(Button)findViewById(R.id.resetpasssword);
        login.setOnClickListener(this);

        email=(EditText)findViewById(R.id.email);
        mauth=FirebaseAuth.getInstance();
        fpassword=(TextView)findViewById(R.id.fpassword);
        fpassword.setOnClickListener(this);

        password=(EditText)findViewById(R.id.password);
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.register){
            Intent intent = new Intent(this,Registration.class);
            startActivity(intent);
            finish();
        }
        if(v.getId()==R.id.resetpasssword){
            login();

        }
        if(v.getId()==R.id.fpassword){
            Intent intent = new Intent(this,FPassword.class);
            startActivity(intent);
            finish();
        }
    }
    private void login(){
        String nemail=email.getText().toString().trim();
        String npassword=password.getText().toString().trim();

        if(nemail.isEmpty()||npassword.isEmpty()){
            Toast.makeText(MainActivity.this, "All fields must be filled", Toast.LENGTH_LONG).show();
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
        mauth.signInWithEmailAndPassword(nemail,npassword)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //если успешно вошел
                        if(task.isSuccessful()){
                            FirebaseUser u=FirebaseAuth.getInstance().getCurrentUser();
                            if(u.isEmailVerified()) {
                                Intent i = new Intent(MainActivity.this, Menu.class);
                                startActivity(i);
                                finish();
                            }
                            else{
                                u.sendEmailVerification();
                                Toast.makeText(MainActivity.this, "Check your email address to confirm your account", Toast.LENGTH_LONG).show();

                            }




                                }
                        else {
                            Toast.makeText(MainActivity.this, "Authorization failed!", Toast.LENGTH_LONG).show();
                        }


                        }



});
    }
}