package rmit.ad.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import rmit.ad.myapplication.Adapter.WishlistAdapter;
import rmit.ad.myapplication.ModelClass.Item;

public class WishlistActivity extends BackgroundActivity {

    RecyclerView recyclerView;
    ImageView menu;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    WishlistAdapter wishlistAdapter;
    FirebaseFirestore db;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseUser currUser = firebaseAuth.getCurrentUser();
    ArrayList<Item> wishlistItems;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wishlist);

        recyclerView = findViewById(R.id.wishlistRecycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        wishlistItems = new ArrayList<Item>();
        wishlistAdapter = new WishlistAdapter(wishlistItems);
        recyclerView.setAdapter(wishlistAdapter);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        navigationView = (NavigationView) findViewById(R.id.navigationView);
        db = FirebaseFirestore.getInstance();

        //Set up displaying method while loading
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading....");
        progressDialog.show();

        menu = (ImageView) findViewById(R.id.menu);

        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        db.collection("Wishlist").document(currUser.getUid()).collection("Items").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        QuerySnapshot querySnapshot = task.getResult();
                        if(!querySnapshot.isEmpty()) {
                            for (QueryDocumentSnapshot dc : querySnapshot) {
                                Item i = dc.toObject(Item.class);
                                wishlistItems.add(i);
                                wishlistAdapter.notifyDataSetChanged();
                            }
                        }
                        if (progressDialog.isShowing())
                            progressDialog.dismiss();
                    }
                });
    }
}