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

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
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

import rmit.ad.myapplication.Adapter.OngoingAdapter;
import rmit.ad.myapplication.Adapter.OrderHistoryAdapter;
import rmit.ad.myapplication.Adapter.WishlistAdapter;
import rmit.ad.myapplication.ModelClass.Item;

public class OngoingOrderActivity extends BackgroundActivity {

    RecyclerView recyclerView;
    ImageView back;

    FirebaseFirestore db;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseUser currUser = firebaseAuth.getCurrentUser();
    ArrayList<Item> ongoingItems;
    OngoingAdapter ongoingAdapter;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ongoing_order);

        recyclerView = findViewById(R.id.ongoingRecycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        ongoingItems = new ArrayList<Item>();
        ongoingAdapter = new OngoingAdapter(ongoingItems);
        recyclerView.setAdapter(ongoingAdapter);
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

        db.collection("Ongoing Orders").document(currUser.getUid()).collection("Items").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                        if (!list.isEmpty()) {
                            for (DocumentSnapshot d : list) {
                                Item i = d.toObject(Item.class);
                                ongoingItems.add(i);
                            }
                            ongoingAdapter.notifyDataSetChanged();
                        }
                        if (progressDialog.isShowing())
                            progressDialog.dismiss();
                    }
                });
    }
}