package rmit.ad.myapplication;

import static java.lang.String.valueOf;

import androidx.appcompat.app.AppCompatActivity;

import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.badge.BadgeUtils;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import rmit.ad.myapplication.Adapter.ShoppingCartAdapter;
import rmit.ad.myapplication.Adapter.WishlistAdapter;
import rmit.ad.myapplication.ModelClass.Item;

public class ShoppingCartActivity extends BackgroundActivity {

    RecyclerView recyclerView;
    ImageView back;
    TextView totalPrice;
    ShoppingCartAdapter shoppingCartAdapter;
    Button toPayment;

    FirebaseFirestore db;
    DocumentReference docRef;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseUser currUser = firebaseAuth.getCurrentUser();
    ArrayList<Item> cartItems;
    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_cart);

        toPayment = findViewById(R.id.payment);
        totalPrice = findViewById(R.id.totalPrice);
        recyclerView = findViewById(R.id.cartRecycler);
        cartItems = new ArrayList<Item>();
        shoppingCartAdapter = new ShoppingCartAdapter(cartItems);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(shoppingCartAdapter);
        db = FirebaseFirestore.getInstance();


        //Set up displaying method while loading
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading....");
        progressDialog.show();

        back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
            }
        });

        toPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), PaymentActivity.class));
                finish();
            }
        });

        db.collection("Shopping Cart").document(currUser.getUid()).collection("Items").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                        if (!list.isEmpty()) {
                            double price = 0;
                            for (DocumentSnapshot d : list) {
                                Item i = d.toObject(Item.class);
                                price += i.getPrice();
                                cartItems.add(i);
                            }

                            shoppingCartAdapter.notifyDataSetChanged();
                            totalPrice.setText("$ " + String.format("%.2f",price));
                        }
                        if (progressDialog.isShowing())
                            progressDialog.dismiss();
                    }
                });
    }
}