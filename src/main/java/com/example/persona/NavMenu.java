package com.example.persona;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.persona.ChatroomBot.ChatRoom;
import com.example.persona.Login.Login;
import com.example.persona.Login.User;
import com.example.persona.Notepad.MainNotes;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class NavMenu extends AppCompatActivity {
    DrawerLayout drawerLayout;
    private FirebaseUser FUser;
    private DatabaseReference Freference;
    private String userId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nav_menu);


        FUser = FirebaseAuth.getInstance().getCurrentUser();
        Freference = FirebaseDatabase.getInstance().getReference("Users");
        userId = FUser.getUid();

        final TextView welcome = (TextView) findViewById(R.id.welcome);
        final TextView emailaddresstextview = (TextView) findViewById(R.id.useremail);
        final TextView fullnametextview = (TextView) findViewById(R.id.userfullname);
        final TextView agetextview = (TextView) findViewById(R.id.userage);

        Freference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userProfile = snapshot.getValue(User.class);
                if (userProfile != null){
                    String email = userProfile.email;
                    String name = userProfile.fullname;
                    String age = userProfile.age;
                    welcome.setText("Welcome My friend !");
                    emailaddresstextview.setText(email);
                    fullnametextview.setText(name);
                    agetextview.setText(age);

                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(NavMenu.this, "Something wrong", Toast.LENGTH_SHORT).show();
            }
        });



        // Assigning variable
        drawerLayout = findViewById(R.id.drawer_layout);
    }
    public void ClickMenu(View view){
        // Opening the drawer
        openDrawer(drawerLayout);

    }

    public static void openDrawer(DrawerLayout drawerLayout) {
        drawerLayout.openDrawer(GravityCompat.START);
    }
    public void clickLogo(View view){
        closeDrawer(drawerLayout);
    }

    public static void closeDrawer(DrawerLayout drawerLayout) {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){

            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }

    public void ClickChatroom(View view) {
        redirect(this, ChatRoom.class);
    }

    public void Clicknotepad (View view) {
        redirect(this, MainNotes.class);
    }

    public void ClickProfile (View view){

        recreate();
    }

    public void ClickLogout (View view){

        logout(this);
    }

    public void ClickHomePage (View view){
        redirect(this, MainActivity.class);
    }

    public void logout(Activity activity) {
        // give alert to user
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("Logout");
        builder.setMessage("Do you want to logout?");
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(NavMenu.this, Login.class);
                startActivity(intent);
                finish();


            }
        });
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();

    }

    public static void redirect(Activity activity, Class aClass) {
        // initialize the intent
        Intent intent = new Intent(activity,aClass);
        // set the flag
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        // start the activity
        activity.startActivity(intent);
    }

    @Override
    protected void onPause() {
        super.onPause();
        //close the drawer
        closeDrawer(drawerLayout);
    }
}
