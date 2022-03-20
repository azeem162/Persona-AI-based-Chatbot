package com.example.persona.Notepad;

import android.content.Intent;
import android.content.res.Resources;


import android.os.Build;
import android.os.Bundle;

import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.persona.Login.Login;
import com.example.persona.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainNotes extends AppCompatActivity {

    private FirebaseAuth fAuth;
    private RecyclerView mNotesList;
    private GridLayoutManager gridLayoutManager;
    private FirebaseRecyclerAdapter<NoteModel,NoteViewHolder> adapter;
    private DatabaseReference fNotesDatabase;
    FirebaseUser firebaseUser;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.note_main);
        firebaseUser=fAuth.getInstance().getCurrentUser();
        mNotesList = (RecyclerView) findViewById(R.id.notes_list);
        fNotesDatabase = FirebaseDatabase.getInstance().getReference().child("Notes");
        gridLayoutManager = new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false);


        mNotesList.setHasFixedSize(true);
        mNotesList.setLayoutManager(gridLayoutManager);
        mNotesList.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));

        fAuth = FirebaseAuth.getInstance();
        if (fAuth.getCurrentUser() != null) {
            fNotesDatabase = FirebaseDatabase.getInstance().getReference().child("Notes").child(fAuth.getCurrentUser().getUid());
        }


        Query query = fNotesDatabase.orderByKey();
        fNotesDatabase.keepSynced(true);
        FirebaseRecyclerOptions<NoteModel> options =
                new FirebaseRecyclerOptions.Builder<NoteModel>()
                        .setQuery(query, NoteModel.class)
                        .build();
            adapter = new FirebaseRecyclerAdapter<NoteModel, NoteViewHolder>(options){
            @NonNull
            @Override
            public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.single_note_layout, parent, false);

                return new NoteViewHolder(view);
            }

            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            protected void onBindViewHolder(@NonNull NoteViewHolder holder, int position, @NonNull NoteModel model) {
                final String noteId = getRef(position).getKey();
                ImageView popupbutton=holder.itemView.findViewById(R.id.menupopbutton);
                int colourcode= RandomColor();
                holder.mView.setBackgroundColor(holder.itemView.getResources().getColor(colourcode,null));
                holder.setNoteTitle(model.getNoteTitle());
                holder.setNoteTime(model.getNoteTime());
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override public void onClick(View view) {
                        Intent intent=new Intent(view.getContext(),notedetails.class);
                        intent.putExtra("title",model.getNoteTitle());
                        intent.putExtra("content",model.getContent());
                        intent.putExtra("noteId",noteId);

                    }
                });
                fNotesDatabase.child(noteId).addValueEventListener(new ValueEventListener() {
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.hasChild("title") && dataSnapshot.hasChild("timestamp")) {
                            String content = dataSnapshot.child("content").getValue().toString();
                            String title = dataSnapshot.child("title").getValue().toString();
                            String timestamp = dataSnapshot.child("timestamp").getValue().toString();
                            holder.setNoteTitle(title);
                            holder.setNoteContent(content);
                            GetTimeAgo getTimeAgo = new GetTimeAgo();
                            holder.setNoteTime(getTimeAgo.getTimeAgo(Long.parseLong(timestamp), getApplicationContext()));

                            holder.itemView.setOnClickListener(new View.OnClickListener() {
                                @Override public void onClick(View view) {
                                    Intent intent=new Intent(view.getContext(),notedetails.class);
                                    intent.putExtra("title",model.getNoteTitle());
                                    intent.putExtra("content",model.getContent());
                                    intent.putExtra("noteId",noteId);

                                }
                            });
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }

                });

                popupbutton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        PopupMenu popupMenu=new PopupMenu(v.getContext(),v);
                        popupMenu.setGravity(Gravity.END);
                        popupMenu.getMenu().add("Edit").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {

                                Intent intent=new Intent(v.getContext(), EditNotes.class);
                                intent.putExtra("title",model.getNoteTitle());
                                intent.putExtra("content",model.getContent());
                                intent.putExtra("noteId",noteId);
                                v.getContext().startActivity(intent);
                                return false;
                            }
                        });

                        popupMenu.getMenu().add("Delete").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {

                            Query noteQuery = fNotesDatabase.child("Notes").child(firebaseUser.getUid()).child(noteId);
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {
                                noteQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    for (DataSnapshot noteSnapshot: dataSnapshot.getChildren()) {
                                        noteSnapshot.getRef().removeValue();
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                    Log.e("MainNotes", "onCancelled", databaseError.toException());
                                }
                            });


                                return false;
                            }
                        });

                        popupMenu.show();
                    }
                });

            }
        };
        mNotesList.setAdapter(adapter);

        updateUI();
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();

    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }


    public static class NoteViewHolder extends RecyclerView.ViewHolder {
        View mView;

        TextView textTitle, textTime, textContent;
        LinearLayout noteCard;

        public NoteViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }

        public void setNoteTitle(String title) {
            TextView textTitle = mView.findViewById(R.id.note_title);
            textTitle.setText(title);
        }

        public void setNoteContent(String content){
            TextView textContent = mView.findViewById(R.id.notecontent);
            textContent.setText(content);
        }

        public void setNoteTime(String time) {
            TextView textTime = mView.findViewById(R.id.note_time);
            textTime.setText(time);
        }

    }

    private void updateUI(){

        if (fAuth.getCurrentUser() != null){
            Log.i("MainNotes", "fAuth != null");
        } else {
            Intent startIntent = new Intent(MainNotes.this, Login.class);
            startActivity(startIntent);
            finish();
            Log.i("MainNotes", "fAuth == null");
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        getMenuInflater().inflate(R.menu.main_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        switch (item.getItemId()) {
            case R.id.Main_note_btn:
                Intent newIntent = new Intent(MainNotes.this, NewNote.class);
                startActivity(newIntent);
                break;
        }

        return true;
    }



    /**
     * Converting dp to pixel
     */
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

    private int RandomColor()
    {
        List<Integer> colorcode=new ArrayList<>();
        colorcode.add(R.color.gray);
        colorcode.add(R.color.pink);
        colorcode.add(R.color.lightgreen);
        colorcode.add(R.color.skyblue);
        colorcode.add(R.color.color1);
        colorcode.add(R.color.color2);
        colorcode.add(R.color.color3);

        colorcode.add(R.color.color4);
        colorcode.add(R.color.color5);
        colorcode.add(R.color.green);

        Random random=new Random();
        int number=random.nextInt(colorcode.size());
        return colorcode.get(number);



    }

}