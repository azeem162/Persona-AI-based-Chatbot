package com.example.persona.Login;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.persona.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterNewUser extends AppCompatActivity implements View.OnClickListener {
    private TextView banner;
    private Button registerUser;
    private EditText edittextUsersName, edittextUsersAge, editTextUsersEmail, edittextUserPass;
    private ProgressBar PBar;

    private FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registerpage);
        fAuth = FirebaseAuth.getInstance();
        banner = (TextView) findViewById(R.id.banner);
        banner.setOnClickListener(this);

        registerUser = (Button) findViewById(R.id.register);
        registerUser.setOnClickListener(this);

        editTextUsersEmail = (EditText) findViewById(R.id.email);
        edittextUsersName = (EditText) findViewById(R.id.fullName);
        edittextUsersAge = (EditText) findViewById(R.id.age);
        edittextUserPass = (EditText) findViewById(R.id.password);


        PBar = (ProgressBar) findViewById(R.id.progressBar);
    }


    private void registerUser() {
        String name = edittextUsersName.getText().toString().trim();
        String age = edittextUsersAge.getText().toString().trim();
        String pass = edittextUserPass.getText().toString().trim();
        String email = editTextUsersEmail.getText().toString().trim();

        if (name.isEmpty()) {
            edittextUsersName.setError("This Field is empty, Please fill it!");
            edittextUsersName.requestFocus();
            return;
        }
        if (age.isEmpty()) {
            edittextUsersAge.setError("This Field is empty, Please fill it!");
            edittextUsersAge.requestFocus();
            return;
        }
        if(pass.isEmpty()){
            edittextUserPass.setError("This Field is empty, Please fill it!");
            edittextUserPass.requestFocus();
            return;
        }
        if(pass.length() < 8){
            edittextUserPass.setError("Password is required to be 8 letters at least");
            edittextUserPass.requestFocus();
            return;
        }
        if (email.isEmpty()) {
            editTextUsersEmail.setError("This Field is empty, Please fill it!");
            editTextUsersEmail.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            editTextUsersEmail.setError("Please Provide the correct email!");
            return;
        }


        PBar.setVisibility(View.VISIBLE);
        fAuth.createUserWithEmailAndPassword(email,pass)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            com.example.persona.Login.User user = new com.example.persona.Login.User(name, age, email);
                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    if(task.isSuccessful()){
                                        Toast.makeText(RegisterNewUser.this, "Registration Success", Toast.LENGTH_SHORT).show();
                                        PBar.setVisibility(View.GONE);
                                    }else{
                                        Toast.makeText(RegisterNewUser.this, "Failed to register the user account, Try again", Toast.LENGTH_SHORT).show();
                                        PBar.setVisibility(View.GONE);
                                    }
                                }
                            });
                        }else{
                            Toast.makeText(RegisterNewUser.this, "Failed to register the user account", Toast.LENGTH_SHORT).show();
                            PBar.setVisibility(View.GONE);
                        }
                    }
                });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.banner:
                startActivity(new Intent(this, com.example.persona.Login.Login.class));
                break;
            case R.id.register:
                registerUser();
                break;
        }
    }
}
