package rmit.ad.myapplication.chat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

//import com.google.firebase.Timestamp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.checkerframework.checker.units.qual.C;
import java.sql.Timestamp;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.logging.SimpleFormatter;

import de.hdodenhof.circleimageview.CircleImageView;
import rmit.ad.myapplication.MemoryData;
import rmit.ad.myapplication.R;

public class Chat extends AppCompatActivity {
    private final List<ChatList> chatLists = new ArrayList<>();
    DatabaseReference databaseReference = FirebaseDatabase.getInstance("https://androidproj-12477-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference();
    private String chatKey;
    String userID = "";
    private RecyclerView chattingRecyclerView;
    private ChatAdapter chatAdapter;
    private boolean loadingFirstTime = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat2);

        final ImageView backBtn = findViewById(R.id.backBtn);
        final ImageView sendBtn = findViewById(R.id.sendBtn);
        final TextView full_name = findViewById(R.id.full_name);
        final EditText messageEditText = findViewById(R.id.messageEditText);
        final CircleImageView profile = findViewById(R.id.profile);
        chattingRecyclerView = findViewById(R.id.chattingRecyclerView);

        //get data from message adapter class
        final String getFullname = getIntent().getStringExtra("full_name");
        final String getProfile_pic = getIntent().getStringExtra("profile_pic");
        chatKey = getIntent().getStringExtra("chat_key");
        final String getId = getIntent().getStringExtra("userID");

        //get user ID from memory
        userID = MemoryData.getData(Chat.this);
        full_name.setText(getFullname);
        Picasso.get().load(getProfile_pic).into(profile);

        chattingRecyclerView.setHasFixedSize(true);
        chattingRecyclerView.setLayoutManager(new LinearLayoutManager(Chat.this));

        chatAdapter = new ChatAdapter(chatLists,Chat.this);
        chattingRecyclerView.setAdapter(chatAdapter);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (chatKey.isEmpty()) {
                    //generate the chat key
                    chatKey = "1";                       //default chatKey is 1
                    if (snapshot.hasChild("chat")) {
                        chatKey = String.valueOf(snapshot.child("chat").getChildrenCount() + 1);
                    }
                }
                if(snapshot.hasChild("chat")){
                    if(snapshot.child("chat").child(chatKey).hasChild("messages") &&  snapshot.child("chat").child(chatKey).hasChild("messages")){
                        chatLists.clear();
                        //snapshot.child("chat").child(chatKey).
                        for(DataSnapshot messagesSnapshot : snapshot.child("chat").child(chatKey).child("messages").getChildren()){
                            if(messagesSnapshot.hasChild("msg") && messagesSnapshot.hasChild("userID")){
                                final String messageTimestamps = messagesSnapshot.getKey();
                                final String getUserID = messagesSnapshot.child("userID").getValue(String.class);
                                final String getMsg = messagesSnapshot.child("msg").getValue(String.class);


                                long l = Long.parseLong(messageTimestamps);
                                Date d = new Date(l);
                                Timestamp timestamp = new Timestamp(Long.parseLong(messageTimestamps));
//                                Timestamp timestamp = new Timestamp(d);
                                Date date = new Date(timestamp.getTime());

                                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy ", Locale.getDefault());
                                SimpleDateFormat simpleTimeFormat = new SimpleDateFormat("hh:mm aa", Locale.getDefault());

                                ChatList chatList = new ChatList(getUserID,getFullname,getMsg,simpleDateFormat.format(date), simpleTimeFormat.format(date));
                                chatLists.add(chatList);

                                if (loadingFirstTime||Long.parseLong(messageTimestamps) > Long.parseLong(MemoryData.getLastMsgTS(Chat.this, chatKey))) {
                                    loadingFirstTime = false;
                                    MemoryData.saveLastMsgTS(messageTimestamps, chatKey, Chat.this);



//                                    long l=Long.parseLong(messageTimestamps);
//                                    Date d=new Date(l);
//                                    //Timestamp timestamp = new Timestamp(Long.parseLong(messageTimestamps));
//                                    Timestamp timestamp = new Timestamp(d);
//                                    Date date = new Date(String.valueOf(timestamp));
//
//                                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
//                                    SimpleDateFormat simpleTimeFormat = new SimpleDateFormat("hh:mm aa", Locale.getDefault());
//
//                                    ChatList chatList = new ChatList(getUserID,getFullname,getMsg,simpleDateFormat.format(date), simpleTimeFormat.format(date));
//                                    chatLists.add(chatList);

                                    chatAdapter.updateChatList(chatLists);
                                    chattingRecyclerView.scrollToPosition(chatLists.size() - 1);


                                }

                            }
                        }
                    }

                }

            }



            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String geTxMessage = messageEditText.getText().toString();
                //get current timestamps
                final String currentTimestamp = String.valueOf(System.currentTimeMillis()).substring(0, 10);

                MemoryData.saveLastMsgTS(currentTimestamp, chatKey, Chat.this);
                databaseReference.child("chat").child(chatKey).child("user_1").setValue(userID);
                databaseReference.child("chat").child(chatKey).child("user_2").setValue(getId);
                databaseReference.child("chat").child(chatKey).child("messages").child(currentTimestamp).child("msg").setValue(geTxMessage);
                databaseReference.child("chat").child(chatKey).child("messages").child(currentTimestamp).child("userID").setValue(userID);

                //clear edittext after send
                messageEditText.setText("");

            }
        });


        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

}