package com.example.persona.Login;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.persona.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ResetPass extends AppCompatActivity {
    private EditText emaileditext;
    private Button resetpasswordbutton;
    private ProgressBar progressBar;

    FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forget_pass);

        emaileditext = (EditText) findViewById(R.id.emailforget);
        resetpasswordbutton = (Button) findViewById(R.id.resetButton);
        progressBar = (ProgressBar) findViewById(R.id.progressBarforget);

        auth = FirebaseAuth.getInstance();

        resetpasswordbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetPassword();
            }
        });

    }

    private void resetPassword() {
        String email = emaileditext.getText().toString().trim();
        if (email.isEmpty()){
            emaileditext.setError("Email field is empty, please input the email address!");
            emaileditext.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emaileditext.setError("Email Not found! Please input the correct email");
            emaileditext.requestFocus();
            return;
        }
        progressBar.setVisibility(View.VISIBLE);
        auth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(ResetPass.this, "Please check your email address to reset!", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(ResetPass.this, "Error! Please try again", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
}
