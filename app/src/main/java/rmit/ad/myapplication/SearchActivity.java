package rmit.ad.myapplication;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import rmit.ad.myapplication.Adapter.ProductViewAdapter;
import rmit.ad.myapplication.Adapter.onClickInterface;
import rmit.ad.myapplication.ModelClass.Item;

public class SearchActivity extends AppCompatActivity implements onClickInterface {

    ImageView back;

    SearchView searchView;
    String searchInput;
    RecyclerView recycler_view;
    ArrayList<Item> itemArrayList;
    ProductViewAdapter productAdapter;
    FirebaseFirestore db;
    ProgressDialog progressDialog;

    ArrayList<Item> filteredList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        //Implementation of back method
        back = (ImageView) findViewById(R.id.backSearch);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                startActivity(new Intent(SearchActivity.this, MainActivity.class));
                finish();
            }
        });

        //Set up displaying method while loading
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading....");
        progressDialog.show();

        recycler_view = findViewById(R.id.recycler_view1);
        recycler_view.setHasFixedSize(true);
        recycler_view.setLayoutManager(new GridLayoutManager(this, 2));

        db = FirebaseFirestore.getInstance();
        itemArrayList = new ArrayList<Item>();
        productAdapter = new ProductViewAdapter(SearchActivity.this, itemArrayList);

        recycler_view.setAdapter(productAdapter);
        productAdapter.setClickListener(this);
        EventChangeListener();
        filteredList = itemArrayList;

        searchView = findViewById(R.id.search_product_name);
        searchView.clearFocus();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener()
        {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterList(newText);
                productAdapter.setClickListener(SearchActivity.this);
                return true;
            }
        });

    }

    private void filterList(String text)
    {
        filteredList = new ArrayList<>();
        for (Item item : itemArrayList)
        {
            if(item.getName().toLowerCase().contains(text.toLowerCase()))
            {
                filteredList.add(item);
            }

            if(filteredList.isEmpty())
            {
                Toast.makeText(this,"No Data Found", Toast.LENGTH_SHORT).show();
            }
            else {
                productAdapter.setFilteredList(filteredList);

            }
        }
    }

    private void EventChangeListener()
    {
        db.collection("item").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {

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
                                Toast toast = Toast.makeText(getApplicationContext(), "No Result Found.", Toast.LENGTH_SHORT);
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
        Item item = filteredList.get(position);
        Intent intent = new Intent(SearchActivity.this, ViewItemDetailActivity.class);
        intent.putExtra("item",item);
        startActivity(intent);
        finish();
    }
}