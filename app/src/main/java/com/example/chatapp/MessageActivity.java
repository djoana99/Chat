package com.example.chatapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        EditText msg = findViewById(R.id.enter_msg);
        ImageView send = findViewById(R.id.send);


        RecyclerView recyclerView = findViewById(R.id.recycler_view);

        List<Message> chatMessages = new ArrayList<>();

        FirebaseDatabase.getInstance().getReference("Chats")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {

                        chatMessages.clear();

                        for (DataSnapshot child : snapshot.getChildren()) {
                            Message message = child.getValue(Message.class);

                            if (message.getSender().equals(FirebaseAuth.getInstance().getUid()) ||
                                         message.getReceiver().equals(FirebaseAuth.getInstance().getUid())) {
                                chatMessages.add(message);
                            }
                        }

                        recyclerView.setHasFixedSize(true);

                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MessageActivity.this);
                        linearLayoutManager.setStackFromEnd(true);

                        recyclerView.setLayoutManager(linearLayoutManager);

                        MessageAdapter messageAdapter = new MessageAdapter(MessageActivity.this, chatMessages);

                        recyclerView.setAdapter(messageAdapter);


                    }

                    @Override
                    public void onCancelled(DatabaseError error) {

                    }
                });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!TextUtils.isEmpty(msg.getText().toString())) {
                    Map<String, String> map = new HashMap<>();
                    map.put("sender", FirebaseAuth.getInstance().getCurrentUser().getUid());
                    map.put("receiver", getIntent().getExtras().getString("userId"));
                    map.put("message", msg.getText().toString());

                    FirebaseDatabase.getInstance().getReference("Chats").push()
                            .setValue(map);
                }

            }
        });

    }

    class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {

        private Context context;
        private List<Message> messages;

        public MessageAdapter(Context context, List<Message> messages) {
            this.context = context;
            this.messages = messages;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            switch (viewType) {
                case 1:
                    return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.sender, parent, false));
                case 2:
                    return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.receiver, parent, false));
            }

            return null;
        }

        @Override
        public int getItemViewType(int position) {

            if (FirebaseAuth.getInstance().getCurrentUser().getUid().equals(messages.get(position).getSender())) {
                return 1;
            }

            return 2;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            Message message = messages.get(position);

            holder.msg.setText(message.getMessage());
        }

        @Override
        public int getItemCount() {
            return messages.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {

            TextView msg;

            public ViewHolder(View itemView) {
                super(itemView);

                msg = itemView.findViewById(R.id.msg);
            }

        }
    }

}