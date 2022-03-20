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

import com.example.persona.MainActivity;
import com.example.persona.NavMenu;
import com.example.persona.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity implements View.OnClickListener {
    private TextView reg, resetpass;
    private EditText editTextUMail, editTextUpass;
    private Button Signin;

    private FirebaseAuth mAuth;
    private ProgressBar Pbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        reg = (TextView) findViewById(R.id.register);
        reg.setOnClickListener(this);
        Signin = (Button) findViewById(R.id.signIn);
        Signin.setOnClickListener(this);
        editTextUMail = (EditText) findViewById(R.id.emaillogin);
        editTextUpass = (EditText) findViewById(R.id.password_user);
        Pbar = (ProgressBar) findViewById(R.id.progressBar);
        mAuth = FirebaseAuth.getInstance();
        resetpass = (TextView) findViewById(R.id.forgotpassword);
        resetpass.setOnClickListener(this);
    }



    public void ULogin() {
        String email = editTextUMail.getText().toString();
        String password = editTextUpass.getText().toString();

        if (email.isEmpty()) {
            editTextUMail.setError("This Field is required, Please fill it!");
            editTextUMail.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextUMail.setError("Invalid email address!");
            return;
        }
        if (password.isEmpty()) {
            editTextUpass.setError("You cannot empty your password!");
            editTextUpass.requestFocus();
            return;
        }
        if (password.length() < 8) {
            editTextUpass.setError("Invalid password!");
            editTextUpass.requestFocus();
            return;
        }


        Pbar.setVisibility(View.GONE);

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    if (user.isEmailVerified()) {
                        Intent homepage = new Intent(Login.this, MainActivity.class);
                        homepage.putExtra("key", email);
                        startActivity(homepage);
                    } else {
                        user.sendEmailVerification();
                        Toast.makeText(Login.this, "Please check your email to verify", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(Login.this, "Failed to Login", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.register:
                startActivity(new Intent(this, RegisterNewUser.class));
                break;
            case R.id.signIn:
                ULogin();
                break;
            case R.id.forgotpassword:
                startActivity(new Intent(this, ResetPass.class));
                break;
        }
    }

    public void onBackPressed() {
        // empty because it used to prevent user to go back after logout
    }


}
