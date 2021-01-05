package com.example.sportconnect.view.chats;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;

import com.bumptech.glide.Glide;
import com.example.sportconnect.R;
import com.example.sportconnect.adapter.ChatsAdapter;
import com.example.sportconnect.databinding.ActivityChatsBinding;
import com.example.sportconnect.model.chat.Chats;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ChatsActivity extends AppCompatActivity {

    private ActivityChatsBinding binding;
    private FirebaseUser firebaseUser;
    private DatabaseReference reference;
    private String receiverId;
    private ChatsAdapter adapter;
    private List<Chats> list;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_chats);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference();


        Intent intent = getIntent();
        String userName = intent.getStringExtra("userName");
        receiverId = intent.getStringExtra("userId");
        String userProfile = intent.getStringExtra("userProfile");


        if(receiverId!=null){
            binding.tvUsername.setText(userName);
            Glide.with(this).load(userProfile).into(binding.imageProfile);
        }

        binding.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        binding.edMessage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(TextUtils.isEmpty(binding.edMessage.getText().toString())){
                    binding.btnSend.setImageDrawable(getDrawable(R.drawable.ic_baseline_keyboard_voice_24));
                } else {
                    binding.btnSend.setImageDrawable(getDrawable(R.drawable.ic_baseline_send_24));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        initBtnClick();

        list = new ArrayList<>();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL,false);
        layoutManager.setStackFromEnd(true);
        binding.recyclerView.setLayoutManager(layoutManager);
        /*
        if(!isVisible()){
            binding.recyclerView.smoothScrollToPosition(ChatsAdapter.getItemCount()-1);
        } */
        readChats();
    }

    private boolean isVisible(){
        LinearLayoutManager linearLayoutManager = (LinearLayoutManager) binding.recyclerView.getLayoutManager();
        int positionOfLastVisibleItem = linearLayoutManager.findLastCompletelyVisibleItemPosition();
        int itemCount = binding.recyclerView.getAdapter().getItemCount();
        return (positionOfLastVisibleItem>=itemCount);
    }
    private  void initBtnClick(){
        binding.btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(binding.edMessage.getText().toString())){
                    sendTextMessage(binding.edMessage.getText().toString());

                    binding.edMessage.setText("");
                }
            }
        });
        binding.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void sendTextMessage(String text){

        Date date = Calendar.getInstance().getTime();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        String today = formatter.format(date);

        Calendar currentDateTime = Calendar.getInstance();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat df = new SimpleDateFormat("hh:mm a");
        String currentTime = df.format(currentDateTime.getTime());

        Chats chats = new Chats(
                today+", "+currentTime,
                text,
                "TEXT",
                firebaseUser.getUid(),
                receiverId
        );

        reference.child("Chats").push().setValue(chats).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d("Send", "onSuccess: ");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("Send", "onFailure: "+e.getMessage());
            }
        });


        // Menambah chatlist
        DatabaseReference chatRef1 = FirebaseDatabase.getInstance().getReference("ChatList").child(firebaseUser.getUid()).child(receiverId);
        chatRef1.child("chatid").setValue(receiverId);

        //
        DatabaseReference chatRef2 = FirebaseDatabase.getInstance().getReference("ChatList").child(receiverId).child(firebaseUser.getUid());
        chatRef2.child("chatid").setValue(firebaseUser.getUid());

    }

    private void readChats(){
        try{
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
            reference.child("Chats").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    list.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                        Chats chats = snapshot.getValue(Chats.class);
                        if (chats.getSender().equals(firebaseUser.getUid()) && chats.getReceiver().equals(receiverId)
                                //chats != null && chats.getSender().equals(firebaseUser.getUid()) && chats.getReceiver().equals(receiverId)
                        || chats.getReceiver().equals(firebaseUser.getUid()) && chats.getSender().equals(receiverId)
                        ){
                            list.add(chats);
                        }

                    }
                    if (adapter!=null){
                        adapter.notifyDataSetChanged();
                    }else {
                        adapter = new ChatsAdapter(list,ChatsActivity.this);
                        binding.recyclerView.setAdapter(adapter);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}