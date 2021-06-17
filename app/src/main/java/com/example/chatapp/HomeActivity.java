package com.example.chatapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(getResources().getString(R.string.home));
        setSupportActionBar(toolbar);


        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);

        List<ChatUser> chatUsers = new ArrayList<>();

        FirebaseDatabase.getInstance().getReference("Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                chatUsers.clear();

                for (DataSnapshot child : snapshot.getChildren()) {
                    User user = child.getValue(User.class);

                    if (!child.getKey().equals(FirebaseAuth.getInstance().getCurrentUser().getUid()))
                        chatUsers.add(new ChatUser(child.getKey(), user.getImageUrl(), user.getName()));
                }

                ChatAdapter chatAdapter = new ChatAdapter(HomeActivity.this, chatUsers);

                chatAdapter.setCustomClickListener(new CustomClickListener() {
                    @Override
                    public void onClick(int position) {
                        Intent intent = new Intent(HomeActivity.this, MessageActivity.class);
                        intent.putExtra("userId", chatUsers.get(position).getId());
                        startActivity(intent);
                    }
                });

                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(HomeActivity.this);

                recyclerView.setLayoutManager(linearLayoutManager);
                recyclerView.setAdapter(chatAdapter);
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });

    }

    class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {

        private Context context;
        private List<ChatUser> users;
        private CustomClickListener customClickListener;

        public void setCustomClickListener(CustomClickListener customClickListener) {
            this.customClickListener = customClickListener;
        }

        public ChatAdapter(Context context, List<ChatUser> users) {
            this.context = context;
            this.users = users;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.chat_item, parent, false), customClickListener);
        }

        @Override
        public void onBindViewHolder(HomeActivity.ChatAdapter.ViewHolder holder, int position) {
            ChatUser chatUser = users.get(position);

            holder.name.setText(chatUser.getName());

            if (!chatUser.getImage().equals("default"))
                Glide.with(context).load(chatUser.getImage()).into(holder.profileImage);
        }

        @Override
        public int getItemCount() {
            return users.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {

            CircleImageView profileImage;
            TextView name;

            public ViewHolder(View itemView, CustomClickListener customClickListener) {
                super(itemView);

                profileImage = itemView.findViewById(R.id.profile_image);
                name = itemView.findViewById(R.id.name);

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        customClickListener.onClick(getAdapterPosition());
                    }
                });
            }

        }
    }

    public interface CustomClickListener {
        void onClick(int position);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.logout_menu, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.logout) {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(HomeActivity.this, StartActivity.class));
            finish();

            return true;
        }

        return false;

    }
}