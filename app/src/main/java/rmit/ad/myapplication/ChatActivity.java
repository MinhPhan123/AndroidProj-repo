package rmit.ad.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import rmit.ad.myapplication.messages.MessageList;
import rmit.ad.myapplication.messages.MessagesAdapter;

public class ChatActivity extends BackgroundActivity {
    private boolean dataSet = false;
    private RecyclerView messagesRecyclerView;
    private MessagesAdapter messagesAdapter;
    private final List<MessageList> messageLists = new ArrayList<>();


    DatabaseReference databaseReference = FirebaseDatabase.getInstance("https://androidproj-12477-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference();     //REALTIME DATABASE
    StorageReference storageReference = FirebaseStorage.getInstance().getReference();     //STORAGE
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();          //AUTH
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();   //FIREBASE FIRESTORE

    FirebaseUser user;
    String userID;

    private int  unseenMessage = 0;
    private String  lastMessage = "";
    private String chatKey="";
    ImageView backButton;

    private String UserID, email, full_name,phone_number;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        final CircleImageView userProfilePic = findViewById(R.id.userProfilePic);
        messagesRecyclerView = findViewById(R.id.messageRecyclerView);
        ImageView backButton = findViewById(R.id.backButton);

        //get the current user
        user = firebaseAuth.getCurrentUser();

        //fetch the data from firebase
        userID = firebaseAuth.getUid();          //get the user id that has been created automatically by firebase
        DocumentReference documentReference = firestore.collection("user").document(userID);
        //get the avatar of a specific user
        StorageReference profileReference = FirebaseStorage.getInstance().getReference().child("user_ava/"+userID+"/profile.jpg");

//        //fetch and display the data
//        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
//            @Override
//            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
//                String full_name = documentSnapshot != null ? documentSnapshot.getString("full_name") : null;
//                String email = documentSnapshot != null ? documentSnapshot.getString("email") : null;
//                String dob = documentSnapshot != null ? documentSnapshot.getString("dob") : null;
//                String address = documentSnapshot != null ? documentSnapshot.getString("address") : null;
//                String phone_number = documentSnapshot != null ? documentSnapshot.getString("phone_number") : null;
//            }
//        });

        //get intent data from MainActivity,class
        UserID = getIntent().getStringExtra("userID");
        email = getIntent().getStringExtra("email");
        full_name = getIntent().getStringExtra("full_name");
        phone_number = getIntent().getStringExtra("phone_number");


        messagesRecyclerView.setHasFixedSize(true);
        messagesRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        //set Adapter to recyclerview
        messagesAdapter = new MessagesAdapter(messageLists, ChatActivity.this);
        messagesRecyclerView.setAdapter(messagesAdapter);

        // to fetch data image data in Storage (FIRESTORE)
        try {
            final File localFile = File.createTempFile("avatar","jpg");
            profileReference.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    //Utility.showToast(ChatActivity.this,"Your avatar has been retrieved");
                    Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());

                    //set the profile picture of the user
                    userProfilePic.setImageBitmap(bitmap);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    //Utility.showToast(Profile.this,"Error occurred");
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }



        //Set up the ProgressDialog
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading......");
        progressDialog.show();

        //get the profile picture from realtime database firebase
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                final String profilePicUrl = snapshot.child("users").child(userID).child("profile_pic").getValue(String.class);

                if (profilePicUrl != null && !profilePicUrl.isEmpty()) {
                    //set profile picture to circle image view
                    Picasso.get().load(profilePicUrl).into(userProfilePic);
                }
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messageLists.clear();
                unseenMessage = 0;
                lastMessage = "";
                chatKey="";

                //loop through the data in branch "users"
                for(DataSnapshot dataSnapshot : snapshot.child("users").getChildren()){
                    final String getuserID = dataSnapshot.getKey();

                    dataSet = false;
                    //if (getuserID != null && !getuserID.equals(UserID)) {
                    if (!getuserID.equals(userID)) {      //userID
                        final String getFullname = dataSnapshot.child("fullname").getValue(String.class);
                        final String getProfile_pic = dataSnapshot.child("profile_pic").getValue(String.class);

                        databaseReference.child("chat").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                int getChatCount = (int) snapshot.getChildrenCount();

                                if (getChatCount > 0) {
                                    for (DataSnapshot dataSnapshot1 : snapshot.getChildren()) {

                                        final String getKey = dataSnapshot1.getKey();
                                        chatKey = getKey;


                                        if (dataSnapshot1.hasChild("user_1") && dataSnapshot1.hasChild("user_2") && dataSnapshot1.hasChild("messages")) {
                                            final String getUserOne = dataSnapshot1.child("user_1").getValue(String.class);
                                            final String getUserTwo = dataSnapshot1.child("user_2").getValue(String.class);

                                            //UserID
                                            if ((getUserOne != null && getUserTwo != null) && ((getUserOne.equals(getuserID) && getUserTwo.equals(UserID)) || (getUserOne.equals(UserID) && getUserTwo.equals(getuserID)))) {
                                                for (DataSnapshot chatDataSnapshot : dataSnapshot1.child("messages").getChildren()) {

                                                    //final long getMessageKey = Long.parseLong(chatDataSnapshot.getKey());
                                                    final long getMessageKey = Long.parseLong(Objects.requireNonNull(chatDataSnapshot.getKey()));
                                                    final long getLastSeenMessage = Long.parseLong(MemoryData.getLastMsgTS(ChatActivity.this, getKey));

                                                    lastMessage = chatDataSnapshot.child("msg").getValue(String.class);
                                                    if (getMessageKey > getLastSeenMessage) {
                                                        unseenMessage++;
                                                    }
                                                }
                                            }
                                        }
                                        //                                        final String getUserOne = dataSnapshot1.child("user_1").getValue(String.class);
                                        //                                        final String getUserTwo = dataSnapshot1.child("user_2").getValue(String.class);
                                        //
                                        //                                        if ((getUserOne != null && getUserTwo != null) && ((getUserOne.equals(getuserID) && getUserTwo.equals(userID)) || (getUserOne.equals(userID) && getUserTwo.equals(getuserID)))) {
                                        //                                            for(DataSnapshot chatDataSnapshot : dataSnapshot1.child("messages").getChildren()){
                                        //                                                //final long getMessageKey = Long.parseLong(chatDataSnapshot.getKey());
                                        //                                                final long getMessageKey = Long.parseLong(Objects.requireNonNull(chatDataSnapshot.getKey()));
                                        //                                                final long getLastSeenMessage = Long.parseLong(MemoryData.LastMsgTS(ChatActivity.this, getKey));
                                        //
                                        //                                                lastMessage = chatDataSnapshot.child("msg").getValue(String.class);
                                        //                                                if(getMessageKey > getLastSeenMessage){
                                        //                                                    unseenMessage++;
                                        //                                                }
                                        //
                                        //                                            }
                                        //
                                        //                                        }
                                    }
                                }
                                if (!dataSet) {
                                    dataSet = true;
                                    MessageList messageList = new MessageList(getuserID, getFullname, lastMessage, getProfile_pic, unseenMessage, chatKey);
                                    messageLists.add(messageList);
                                    messagesAdapter.updateData(messageLists);
                                }
                                //                                MessageList messageList = new MessageList(getuserID,getFullname, lastMessage, getProfile_pic, unseenMessage,chatKey);
                                //                                messageLists.add(messageList);
                                //                                messagesAdapter.updateData(messageLists);

                                //check this
                                //      messagesRecyclerView.setAdapter(new MessagesAdapter(messageLists, ChatActivity.this));
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                        //                        MessageList messageList = new MessageList(getFullname, lastMessage, getProfile_pic, unseenMessage);
                        //                        messageLists.add(messageList);
                    }

                }
//                messagesRecyclerView.setAdapter(new MessagesAdapter(messageLists, ChatActivity.this));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

}