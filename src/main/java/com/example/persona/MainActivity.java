package com.example.persona;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.persona.ChatroomBot.ChatRoom;
import com.example.persona.Login.Login;
import com.example.persona.Notepad.MainNotes;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu);
    }
    public void ClickChatroomMenu(View view) { redirectActivity(this, ChatRoom.class);}

    public void ClicknotepadMenu (View view) { redirectActivity(this, MainNotes.class);}

    public void ClickProfileMenu (View view){
        redirectActivity(this, NavMenu.class);
    }

    public void ClickLogoutMenu (View view){
        logoutMenu(this);
    }

    public void logoutMenu(Activity activity) {
        // give alert to user
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("Logout");
        builder.setMessage("Do you want to logout?");
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(MainActivity.this, Login.class);
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

    public static void redirectActivity(Activity activity,Class aClass) {
        // initialize the intent
        Intent intent = new Intent(activity,aClass);
        // set the flag
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        // start the activity
        activity.startActivity(intent);
    }
}


/*
    // Assigning variable
    drawerLayout = findViewById(R.id.drawer_layout);
}
    public void ClickMenu(View view) {
        // Opening the drawer
        NavMenu.openDrawer(drawerLayout);

    }

    public void clickLogo(View view) {
        NavMenu.closeDrawer(drawerLayout);
    }

    public void ClickProfile(View view) {
        NavMenu.redirectActivity(this, NavMenu.class);
    }

    public void ClickHomePage(View view) {
        recreate();
    }


    public void ClickChatroom(View view) {

        NavMenu.redirectActivity(this, ChatRoom.class);
    }
    public void Clicknotepad (View view) {

        NavMenu.redirectActivity(this, NoteActivity.class);
    }

    public void ClickLogout(View view) {
        logout(this);
    }
    public void logout(Activity activity) {
        // give alert to user
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("Logout");
        builder.setMessage("Are you sure you wanted to logout?");
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(MainActivity.this, Login.class);
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
    @Override
    protected void onPause() {
        super.onPause();
        //close the drawer
        NavMenu.closeDrawer(drawerLayout);
    }
}*/