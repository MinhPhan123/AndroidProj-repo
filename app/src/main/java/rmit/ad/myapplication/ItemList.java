package rmit.ad.myapplication;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import rmit.ad.myapplication.Adapter.ProductViewAdapter;
import rmit.ad.myapplication.Adapter.onClickInterface;
import rmit.ad.myapplication.ModelClass.Item;

public class ItemList extends BackgroundActivity implements onClickInterface{

    private String categoryName;
    RecyclerView recycler_view;
    ImageView back, searchIcon, toCart, toWishlist;

    ArrayList<Item> itemArrayList;
    ProductViewAdapter productAdapter;
    FirebaseFirestore db;
    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_list);

        //Start search activity
        searchIcon = findViewById(R.id.searchIcon1);
        searchIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(ItemList.this, SearchActivity.class);
                startActivity(intent);
                finish();
            }
        });

        //Implementation of back method
        back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                startActivity(new Intent(ItemList.this, MainActivity.class));
                finish();
            }
        });

        toCart = findViewById(R.id.toCart);
        toCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ItemList.this, ShoppingCartActivity.class));
                finish();
            }
        });

        toWishlist = findViewById(R.id.toWishlist);
        toWishlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ItemList.this, WishlistActivity.class));
                finish();
            }
        });

        //Receive category
        categoryName = getIntent().getExtras().get("category").toString();

        //Set up displaying method while loading
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading....");
        progressDialog.show();

        recycler_view = findViewById(R.id.recycler_view);
        recycler_view.setHasFixedSize(true);
        recycler_view.setLayoutManager(new GridLayoutManager(this, 2));

        db = FirebaseFirestore.getInstance();
        itemArrayList = new ArrayList<Item>();
        productAdapter = new ProductViewAdapter(ItemList.this, itemArrayList);

        recycler_view.setAdapter(productAdapter);
        productAdapter.setClickListener(this);

        EventChangeListener();


    }

    private void EventChangeListener()
    {
        db.collection("item").whereEqualTo("category", categoryName).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {

                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onComplete(@Nullable Task<QuerySnapshot> task){

                        if(!task.isSuccessful())
                        {
                            if(progressDialog.isShowing())
                                progressDialog.dismiss();
                            Log.e("Firestore error", String.valueOf(task.getException()));
                            return;
                        }
                        else {
                            QuerySnapshot querySnapshot = task.getResult();

                            if(!querySnapshot.isEmpty()) {
                                for (QueryDocumentSnapshot dc : querySnapshot) {
                                    itemArrayList.add(dc.toObject(Item.class));
                                    productAdapter.notifyDataSetChanged();
                                }
                            }
                            else
                            {
                                Toast toast = Toast.makeText(getApplicationContext(), "There is no item in "+categoryName, Toast.LENGTH_SHORT);
                                toast.show();
                            }
                            if (progressDialog.isShowing())
                                progressDialog.dismiss();
                        }
                    }
                });
    }

    @Override
    public void setClick(View view, int position) {
        Item item = itemArrayList.get(position);
        Intent intent = new Intent(ItemList.this, ViewItemDetailActivity.class);
        intent.putExtra("item",item);
        startActivity(intent);
    }
}