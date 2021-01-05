package com.example.sportconnect.view.contact;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.example.sportconnect.R;
import com.example.sportconnect.adapter.ContactsAdapter;
import com.example.sportconnect.databinding.ActivityContactsBinding;
import com.example.sportconnect.model.user.Users;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ContactsActivity extends AppCompatActivity {

    private static final String TAG = "ContactsActivity";
    private ActivityContactsBinding binding;
    private List<Users> list = new ArrayList<>();
    private ContactsAdapter adapter;
    private FirebaseUser firebaseUser;
    private FirebaseFirestore firestore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_contacts);

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        firestore = FirebaseFirestore.getInstance();

        if (firebaseUser!=null){
            getContactList();
        }
    }

    private void getContactList() {
        firestore.collection("Users").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                for (QueryDocumentSnapshot snapshots : queryDocumentSnapshots){
                    String userId = snapshots.getString("userId");
                    String userName = snapshots.getString("userName");
                    String imageUrl = snapshots.getString("imageProfile");
                    String desc = snapshots.getString("bio");

                    Users user = new Users();
                    user.setUserId(userId);
                    user.setBio(desc);
                    user.setUserName(userName);
                    user.setImageProfile(imageUrl);

                    if (userId != null && !userId.equals(firebaseUser.getUid())) {
                        list.add(user);
                    }

                }
                adapter = new ContactsAdapter(list, ContactsActivity.this);
                binding.recyclerView.setAdapter(adapter);
            }
        });
    }
}