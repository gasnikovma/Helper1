package app.gasnikov.helper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class FPassword extends AppCompatActivity implements View.OnClickListener {
    private EditText email;
    private Button resetpassword;
    FirebaseAuth mauth;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_f_password);
        email=(EditText)findViewById(R.id.email);
        resetpassword=(Button) findViewById(R.id.resetpasssword);
        resetpassword.setOnClickListener(this);
        mauth=FirebaseAuth.getInstance();
        progressBar=findViewById(R.id.progressBar);
    }
    public void onBackPressed(){
        try{
            Intent intent = new Intent(FPassword.this,MainActivity.class);
            startActivity(intent);
            finish();
        }
        catch(Exception e){

        }

    }

    @Override
    public void onClick(View v) {
        resetpassword();
    }
    private void resetpassword(){
        String nemail=email.getText().toString().trim();
        if(nemail.isEmpty()){
            Toast.makeText(FPassword.this, "Enter your email address", Toast.LENGTH_LONG).show();
            return;
        }

        if(!android.util.Patterns.EMAIL_ADDRESS.matcher(nemail).matches()){
            email.setError("Please enter valid email");
            return;
        }
        progressBar.setVisibility(View.VISIBLE);
        mauth.sendPasswordResetEmail(nemail).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(FPassword.this, "Check your email address to reset your password", Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.GONE);
                }
                else {
                    Toast.makeText(FPassword.this, "Unsuccessful, try again", Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.GONE);
                }
            }
        });

    }
}