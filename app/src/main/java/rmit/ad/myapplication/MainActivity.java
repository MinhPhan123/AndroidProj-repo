package rmit.ad.myapplication;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class MainActivity extends BackgroundActivity {

    String userID, full_name,email,dob,address,phone_number, profileURL;

    Uri imageUri;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    StorageReference storageReference = FirebaseStorage.getInstance().getReference();
    DatabaseReference databaseReference = FirebaseDatabase.getInstance("https://androidproj-12477-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference();
    FirebaseUser user;
    GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
    GoogleSignInClient gsc;

    ImageSlider imageSlider;

    private ImageView bed, cabinet, chair, clock, desk, sofa, menu;
    ImageView wishlist, shoppingCart;
    ImageView searchIcon;
    DrawerLayout drawerLayout;
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        gsc = GoogleSignIn.getClient(this,gso);
        //Get the data from Google account
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if(account != null) {
            String google_name = account.getDisplayName();
            String google_mail = account.getEmail();
            createAccountInFireBase(google_name,google_mail);
        }

        //Create a list for images Slide
        imageSlider = findViewById(R.id.imageSlider);
        ArrayList<SlideModel> slideModels = new ArrayList<>();
        slideModels.add(new SlideModel("https://firebasestorage.googleapis.com/v0/b/androidproj-12477.appspot.com/o/AutoCaroselPic%2Fcaro1.jpg?alt=media&token=5260d800-1367-430a-8b55-7a4065103fb2", ScaleTypes.FIT));
        slideModels.add(new SlideModel("https://firebasestorage.googleapis.com/v0/b/androidproj-12477.appspot.com/o/AutoCaroselPic%2Fcaro2.jpg?alt=media&token=6293dd5f-9dbe-4323-8bb2-e4e7b659e390", ScaleTypes.FIT));
        slideModels.add(new SlideModel("https://firebasestorage.googleapis.com/v0/b/androidproj-12477.appspot.com/o/AutoCaroselPic%2Fcaro3.jpg?alt=media&token=723480e5-1ddb-4a19-9d17-a791743f50ab", ScaleTypes.FIT));
        slideModels.add(new SlideModel("https://firebasestorage.googleapis.com/v0/b/androidproj-12477.appspot.com/o/AutoCaroselPic%2Fcaro4.jpg?alt=media&token=066455d6-f048-4e12-b34b-9184a1487c83", ScaleTypes.FIT));
        slideModels.add(new SlideModel("https://firebasestorage.googleapis.com/v0/b/androidproj-12477.appspot.com/o/AutoCaroselPic%2Fcaro5.jpg?alt=media&token=cb75f7a3-04bb-4d8b-9d2b-fb79a25890ac", ScaleTypes.FIT));
        slideModels.add(new SlideModel("https://firebasestorage.googleapis.com/v0/b/androidproj-12477.appspot.com/o/AutoCaroselPic%2Fcaro6.jpg?alt=media&token=584fcd6e-30cc-4fb9-8d0d-215b9656af43", ScaleTypes.FIT));
        slideModels.add(new SlideModel("https://firebasestorage.googleapis.com/v0/b/androidproj-12477.appspot.com/o/AutoCaroselPic%2Fcaro7.jpg?alt=media&token=ed6399d8-e49c-44e4-99b3-e691bb33bd0f", ScaleTypes.FIT));
        slideModels.add(new SlideModel("https://firebasestorage.googleapis.com/v0/b/androidproj-12477.appspot.com/o/AutoCaroselPic%2Fcaro8.jpg?alt=media&token=cc47749c-5091-4e4d-9c69-fd81d9e75e41", ScaleTypes.FIT));

        imageSlider.setImageList(slideModels,ScaleTypes.FIT);

        //Navigation drawer declare
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        navigationView = (NavigationView) findViewById(R.id.navigationView);
        menu = (ImageView) findViewById(R.id.menu);

        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        //Start search activity
        searchIcon = findViewById(R.id.searchIcon);
        searchIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                startActivity(intent);
            }
        });

        //Check category press
        bed = (ImageView) findViewById(R.id.bed);
        cabinet = (ImageView) findViewById(R.id.cabinet);
        chair = (ImageView) findViewById(R.id.chair);
        clock = (ImageView) findViewById(R.id.clock);
        desk = (ImageView) findViewById(R.id.desk);
        sofa = (ImageView) findViewById(R.id.sofa);
        wishlist = findViewById(R.id.toWishlist);
        shoppingCart = findViewById(R.id.toCart);


        //Start activity base on category for the item list display
        bed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(MainActivity.this, ItemList.class);
                intent.putExtra("category", "Beds");
                startActivity(intent);
            }
        });

        cabinet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(MainActivity.this, ItemList.class);
                intent.putExtra("category", "Cabinets");
                startActivity(intent);
            }
        });

        chair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(MainActivity.this, ItemList.class);
                intent.putExtra("category", "Chairs");
                startActivity(intent);
            }
        });

        clock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(MainActivity.this, ItemList.class);
                intent.putExtra("category", "Clocks");
                startActivity(intent);
            }
        });

        desk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(MainActivity.this, ItemList.class);
                intent.putExtra("category", "Desks");
                startActivity(intent);
            }
        });

        sofa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(MainActivity.this, ItemList.class);
                intent.putExtra("category", "Sofas");
                startActivity(intent);
            }
        });

        wishlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, WishlistActivity.class);
                startActivity(intent);
            }
        });

        shoppingCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, ShoppingCartActivity.class);
                startActivity(i);
            }
        });


    }

    // check if the data of this google user is exits - then no need to create the data in cloud firestore
    // else create a new user using google fullname and email
    private void createAccountInFireBase(String google_name, String google_mail) {

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        userID = firebaseAuth.getUid();
        //https://firebase.google.com/docs/firestore/query-data/get-data#get_a_document

        DocumentReference documentReference = firestore.collection("user").document(userID);

        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if(documentSnapshot.exists()){
                        //do nothing
                    } else {
                        userID = firebaseAuth.getUid();
                        StorageReference fileRef = storageReference.child("user_ava/"+userID+"/profile.jpg");

                        Map<String, Object> user = new HashMap<>();
                        user.put("email", google_mail);
                        user.put("full_name", google_name);
                        user.put("dob", null);
                        user.put("phone_number", null);
                        user.put("address", null);
                        documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Log.d("TAG", "onSuccess: user profile is created for" + userID);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d(TAG, "onFailure: " + e.toString());
                            }
                        });
                    }
                } else {
                    Log.d(TAG, "onFailure: ");
                }
            }
        });

    }

    //function for logout for the sidebar
    public void logout(View view){
        gsc.signOut();                                          //google sign out
        FirebaseAuth.getInstance().signOut();                   //email password sign out
        startActivity(new Intent(getApplicationContext(),LoginActivity.class));
        finish();
    }

    //move to profile activity using the sidebar
    public void profile(View view){
        startActivity(new Intent(getApplicationContext(),Profile.class));
        finish();
    }

    public void chatbox(View view){
        //get the current user
        user = firebaseAuth.getCurrentUser();

        //fetch the data from firebase
        userID = firebaseAuth.getUid();          //get the user id that has been created automatically by firebase
        DocumentReference documentReference = firestore.collection("user").document(userID);

        //fetch and display the data
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
                full_name = documentSnapshot != null ? documentSnapshot.getString("full_name") : null;
                email = documentSnapshot != null ? documentSnapshot.getString("email") : null;
                dob = documentSnapshot != null ? documentSnapshot.getString("dob") : null;
                address = documentSnapshot != null ? documentSnapshot.getString("address") : null;
                phone_number = documentSnapshot != null ? documentSnapshot.getString("phone_number") : null;
            }
        });

        //get the avatar URL
        StorageReference fileRef = storageReference;
        fileRef.child("user_ava/"+userID+"/profile.jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                // Got the download URL for 'users/me/profile.png'
                profileURL = uri.toString();
                System.out.println(profileURL);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
                Toast.makeText(MainActivity.this,"Can not ", Toast.LENGTH_SHORT).show();
            }
        });

        //check if the user has already login using the ID which has been store in the data
        if(MemoryData.getData(this).isEmpty()){
            Intent intent = new Intent(MainActivity.this, ChatActivity.class);
            intent.putExtra("userID",MemoryData.getData(this));
            intent.putExtra("full_name",MemoryData.getName(this));
            intent.putExtra("email",email);
            intent.putExtra("phone_number",phone_number);
//          intent.putExtra("profile_pic",profileURL);              Realtime database can't now store an URL
            startActivity(intent);
        }


        //create data in realtime database for chatting function
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child("users").hasChild(userID)){
                    Toast.makeText(MainActivity.this,"User already exits", Toast.LENGTH_SHORT).show();
                } else {
                    String URL = profileURL;
                    //create a realtime database for each user based on their id
                    databaseReference.child("users").child(userID).child("fullname").setValue(full_name);
                    databaseReference.child("users").child(userID).child("userID").setValue(userID);
                    databaseReference.child("users").child(userID).child("email").setValue(email);
                    databaseReference.child("users").child(userID).child("phone_number").setValue(phone_number);
                    databaseReference.child("users").child(userID).child("profile_pic").setValue("https://firebasestorage.googleapis.com/v0/b/androidproj-12477.appspot.com/o/user_ava%2Fntjvh22DAshXeQoKbNQr4wXycaz2%2Fprofile.jpg?alt=media&token=3ae064d6-287f-446a-a2da-43ed152ff4bf");

                    //save ID to memory
                    MemoryData.saveData(userID, MainActivity.this);

                    //save name to memory
                    MemoryData.saveName(full_name, MainActivity.this);

                    Toast.makeText(MainActivity.this,"Successfully created", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(MainActivity.this, ChatActivity.class);
                    intent.putExtra("userID",userID);
                    intent.putExtra("email",email);
                    intent.putExtra("phone_number",phone_number);
                    intent.putExtra("full_name",full_name);
//                    intent.putExtra("profile_pic",profileURL);
                    startActivity(intent);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //nothing
            }
        });
        startActivity(new Intent(getApplicationContext(),ChatActivity.class));
        finish();
    }


    public void ongoing(View view){
        startActivity(new Intent(getApplicationContext(),OngoingOrderActivity.class));//change the activity name
        finish();
    }

    public void history(View view){
        startActivity(new Intent(getApplicationContext(),OrderHistoryActivity.class));
        finish();
    }

    public void home(View v){
        startActivity(new Intent(getApplicationContext(),MainActivity.class));
        finish();
    }

}
