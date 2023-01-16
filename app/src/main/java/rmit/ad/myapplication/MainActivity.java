package rmit.ad.myapplication;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    String userID;
    private ImageView bed, cabinet, chair, clock, desk, sofa, menu;

    DrawerLayout drawerLayout;
    NavigationView navigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Get the data from Google account
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if(account != null) {
            String google_name = account.getDisplayName();
            String google_mail = account.getEmail();
            createAccountInFireBase(google_name,google_mail);
        }

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




        //Check category press
        bed = (ImageView) findViewById(R.id.bed);
        cabinet = (ImageView) findViewById(R.id.cabinet);
        chair = (ImageView) findViewById(R.id.chair);
        clock = (ImageView) findViewById(R.id.clock);
        desk = (ImageView) findViewById(R.id.desk);
        sofa = (ImageView) findViewById(R.id.sofa);

        //Start activity base on category for the item list display
        bed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(MainActivity.this, ItemList.class);
                intent.putExtra("category", "bed");
                startActivity(intent);
            }
        });

        cabinet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(MainActivity.this, ItemList.class);
                intent.putExtra("category", "cabinet");
                startActivity(intent);
            }
        });

        chair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(MainActivity.this, ItemList.class);
                intent.putExtra("category", "chair");
                startActivity(intent);
            }
        });

        clock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(MainActivity.this, ItemList.class);
                intent.putExtra("category", "clock");
                startActivity(intent);
            }
        });

        desk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(MainActivity.this, ItemList.class);
                intent.putExtra("category", "desk");
                startActivity(intent);
            }
        });

        sofa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(MainActivity.this, ItemList.class);
                intent.putExtra("category", "sofa");
                startActivity(intent);
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
                        DocumentReference documentReference = firestore.collection("user").document(userID);
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
}