package com.example.sportconnect.teammenu;

import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.sportconnect.R;
import com.example.sportconnect.adapter.ChatListAdapter;
import com.example.sportconnect.adapter.ContactsAdapter;
import com.example.sportconnect.databinding.FragmentChatBinding;
import com.example.sportconnect.model.Chatlist;
import com.example.sportconnect.model.user.Users;
import com.example.sportconnect.view.contact.ContactsActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class ChatFragment extends Fragment {

    private static final String TAG = "ChatFragment";

    public ChatFragment() {
        // Required empty public constructor
    }

    private FirebaseUser firebaseUser;
    private DatabaseReference reference;
    private FirebaseFirestore firestore;
    private Handler handler = new Handler();
    private List<Chatlist> list;
    private RecyclerView recyclerView;
    private FragmentChatBinding binding;
    private  ArrayList<String> allUserId;
    private ChatListAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_chat, container, false);

        list = new ArrayList<>();
        allUserId = new ArrayList<>();


        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new ChatListAdapter(list,getContext());
        binding.recyclerView.setAdapter(adapter);


        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference();
        firestore = FirebaseFirestore.getInstance();



        if (firebaseUser!=null){
            getChatlist();
        }

        return binding.getRoot();
    }

    private void getChatlist() {
        binding.progressCircular.setVisibility(View.VISIBLE);
        list.clear();
        allUserId.clear();
        reference.child("ChatList").child(firebaseUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    String userId = Objects.requireNonNull(snapshot.child("chatid").getValue()).toString();
                    Log.d(TAG, "onDataChange: userid"+userId);

                    binding.progressCircular.setVisibility(View.GONE);
                    allUserId.add(userId);
                    //getUserData(userId);

                }
                //getUserInfo();

                if (firebaseUser!=null){
                    getContactList();
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //-------------------------------------------------
        //list.add(new Chatlist("11","LeBron James","Maybe my pain was motivation","31/12/2020","https://pyxis.nymag.com/v1/imgs/847/0f7/504c63a03d8a751a5cbeda0bc064306bb4-lebron-james.rsquare.w1200.jpg"));
        //list.add(new Chatlist("22","Anthony Davis","Only one music comes out of me","31/12/2020","https://www.gannett-cdn.com/presto/2020/01/07/USAT/a3830ac4-6dbd-4fac-8890-c04af2d5e9b5-davis.jpg"));
        //list.add(new Chatlist("33","LaMelo Ball","History is dependent on the new generation to write a new chapter","31/12/2020","https://2l7g9kgsh281akevs49v281d-wpengine.netdna-ssl.com/wp-content/uploads/2020/12/AP_20350174709817-scaled.jpg"));
        //recyclerView.setAdapter(new ChatListAdapter(list,getContext()));
        //-------------------------------------------------
    }


    private void getUserInfo() {

        handler.post(new Runnable() {
            @Override
            public void run() {
                for (String userId : allUserId){
                    firestore.collection("Users").document(userId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            Log.d(TAG, "onSuccess: ddd"+documentSnapshot.getString("userName"));
                            try {
                                //String userName = firebaseUser.getDisplayName();
                                //String userId = firebaseUser.getUid();
                                ///Uri imageProfile = firebaseUser.getPhotoUrl();

                                /*
                                Chatlist chat = new Chatlist(
                                        documentSnapshot.getString("userId"),
                                        documentSnapshot.getString("userName"),
                                        "this is description...",
                                        "",
                                        documentSnapshot.getString("imageProfile")
                                );
                                list.add(chat);*/
                            }catch (Exception e){
                                Log.d(TAG, "onSuccess: "+e.getMessage());
                            }
                            if (adapter!=null){
                                adapter.notifyItemInserted(0);
                                adapter.notifyDataSetChanged();

                                Log.d(TAG, "onSuccess: adapter"+adapter.getItemCount());
                            }

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d(TAG, "onFailure: Error L"+e.getMessage());
                        }
                    });
                }
            }
        });

        //binding.recyclerView.setAdapter(new ChatListAdapter(list,getContext()));
        //Log.d(TAG, "onDataChange: list "+list.size());

    }

    private void getContactList() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                //for (String userId : allUserId){
                    firestore.collection("Users").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                            for (QueryDocumentSnapshot snapshots : queryDocumentSnapshots){
                                //Users user = new Users();
                                //user.setUserId(userId);
                                //user.setBio(desc);
                                //user.setUserName(userName);
                                //user.setImageProfile(imageUrl);
                                String userId = snapshots.getString("userId");
                                String userName = snapshots.getString("userName");
                                String imageProfile = snapshots.getString("imageProfile");

                                Chatlist chat = new Chatlist();
                                chat.setUserId(userId);
                                chat.setUserName(userName);
                                chat.setDesc("this is description");
                                chat.setDate("");
                                chat.setUrlProfile(imageProfile);

                                /*
                                Chatlist chat = new Chatlist(
                                        userId,
                                        userName,
                                        "this is description...",
                                        "",
                                        imageProfile
                                );*/

                                if (userId != null && !userId.equals(firebaseUser.getUid())) {
                                    list.add(chat);
                                }

                            }
                            //binding.recyclerView.setAdapter(new ChatListAdapter(list,getContext()));
                            //Log.d(TAG, "onDataChange: list "+list.size());

                            if (adapter!=null){
                                adapter.notifyItemInserted(0);
                                adapter.notifyDataSetChanged();

                                Log.d(TAG, "onSuccess: adapter"+adapter.getItemCount());
                            }


                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d(TAG, "onFailure: Error L"+e.getMessage());
                        }
                    });
                //}

            }
        });

    }


}