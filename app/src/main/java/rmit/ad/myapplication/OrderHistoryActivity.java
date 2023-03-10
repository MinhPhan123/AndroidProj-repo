package rmit.ad.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import rmit.ad.myapplication.Adapter.OrderHistoryAdapter;
import rmit.ad.myapplication.Adapter.WishlistAdapter;
import rmit.ad.myapplication.ModelClass.Item;

public class OrderHistoryActivity extends BackgroundActivity {

    RecyclerView recyclerView;
    ImageView back;
    OrderHistoryAdapter orderHistoryAdapter;
    FirebaseFirestore db;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseUser currUser = firebaseAuth.getCurrentUser();
    ArrayList<Item> historyItems;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);

        recyclerView = findViewById(R.id.orderHistoryRecycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        historyItems = new ArrayList<Item>();
        orderHistoryAdapter = new OrderHistoryAdapter(historyItems);
        recyclerView.setAdapter(orderHistoryAdapter);
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

        db.collection("Order History").document(currUser.getUid()).collection("Items").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                        if (!list.isEmpty()) {
                            for (DocumentSnapshot d : list) {
                                Item i = d.toObject(Item.class);
                                historyItems.add(i);
                            }
                            orderHistoryAdapter.notifyDataSetChanged();
                        }
                        if (progressDialog.isShowing())
                            progressDialog.dismiss();
                    }
                });
    }
}