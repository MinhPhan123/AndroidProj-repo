package rmit.ad.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

import rmit.ad.myapplication.ModelClass.Image;
import rmit.ad.myapplication.ModelClass.ImageViewPagerAdapter;
import rmit.ad.myapplication.ModelClass.Item;

public class ViewItemDetailActivity extends AppCompatActivity {
    Item item;
    TextView itemNameText, itemIDText, itemPriceText, itemCategoryText, itemDiscountText,
             itemQuantityText, itemStockQuantityText, itemDescriptionText;
    Spinner colorSpinner;
    String itemID;
    String itemName;
    double itemPrice;
    Image firstImage;
    double itemDiscount;
    int selectedQuantity;
    String selectedColor;
    Button toggleButton;
    boolean isExpanded = false;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_item_detail);

        itemID = getIntent().getStringExtra("itemID");
        getItem(itemID);

        //Set up viewpager for image slider
        ViewPager viewPager = findViewById(R.id.view_pager_image);
        List<Image> imagesList = item.getImage();
        ImageViewPagerAdapter imageViewPagerAdapter = new ImageViewPagerAdapter(imagesList);
        viewPager.setAdapter(imageViewPagerAdapter);
        firstImage = imagesList.get(0);

        //Set up the name of the item
        itemNameText = findViewById(R.id.item_name);
        itemName = item.getName();
        itemNameText.setText(itemName);

        //Set up the ID of the item
        itemIDText = findViewById(R.id.item_ID);
        itemID = item.getID();
        itemIDText.setText(itemID);

        //Set up the price of the item
        itemPriceText = findViewById(R.id.item_price);
        itemPrice = item.getPrice();
        itemPriceText.setText(itemPrice + " $");

        //Set up the category of the item
        itemCategoryText = findViewById(R.id.item_category);
        itemCategoryText.setText("Category: " + item.getCategory());

        //Set up the discount of the item
        itemDiscountText = findViewById(R.id.item_discount);
        itemDiscount = item.getDiscount();
        itemDiscountText.setText("Discount: " + itemDiscount + " %");

        //Set up the stock quantity of the item
        //---------Code lai phan nay neu quantity = 0 thi display status------
        itemStockQuantityText = findViewById(R.id.item_stock_quantity);
        itemStockQuantityText.setText("Stock quantity: " + item.getStockQuantity());

        //Set up color spinner for selecting color
        colorSpinner = findViewById(R.id.color_spinner);
        setUpColorSpinner();

        //Set up the expandable description
        itemDescriptionText = findViewById(R.id.item_description);
        toggleButton = findViewById(R.id.toggle_button);
        toggleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleDescription();
            }
        });

        //Set up increase and decrease button for quantity of item
        AppCompatButton btnIncrease = findViewById(R.id.increase_button);
        AppCompatButton btnDecrease = findViewById(R.id.decrease_button);
        itemQuantityText = findViewById(R.id.edit_text_quantity);

        btnDecrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedQuantity > 0) {
                    selectedQuantity--;
                    itemQuantityText.setText(String.valueOf(selectedQuantity));
                }
            }
        });

        btnIncrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedQuantity++;
                itemQuantityText.setText(String.valueOf(selectedQuantity));
            }
        });

        //Add to cart button
        AppCompatButton btnAddToCart = findViewById(R.id.add_to_cart_button);
        btnAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ViewItemDetailActivity.this, ShoppingCartActivity.class);
                i.putExtra("itemID", itemID);
                i.putExtra("itemName",itemName);
                i.putExtra("itemImage",firstImage.getUrl());
                i.putExtra("itemPrice",itemPrice);
                i.putExtra("itemColor",selectedColor);
                i.putExtra("itemQuantity",selectedQuantity);
                startActivity(i);

            }
        });

    }

    private void getItem(String itemID) {
        if (isNetworkConnected()) {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            DocumentReference docRef = db.collection("items").document(itemID);
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot documentSnapshot = task.getResult();
                        if (documentSnapshot.exists()) {
                            item = documentSnapshot.toObject(Item.class);
                            setUpColorSpinner();
                        }
                        else {
                            Log.d("TAG", "No such document");
                        }
                    }
                    else {
                        Log.d("TAG", "get failed with ", task.getException());
                    }
                }
            });
        }
    }

    //Set up color spinner
    private void setUpColorSpinner() {
        List<String> colorList = item.getColor();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(ViewItemDetailActivity.this, android.R.layout.simple_spinner_item, colorList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        colorSpinner.setAdapter(adapter);
        colorSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedColor = parent.getItemAtPosition(position).toString();
                item.setSelectedColor(selectedColor);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private boolean isNetworkConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    @SuppressLint("SetTextI18n")
    private void toggleDescription() {
        if (isExpanded) {
            itemDescriptionText.setMaxLines(5);
            toggleButton.setText("Show More");
        } else {
            itemDescriptionText.setMaxLines(Integer.MAX_VALUE);
            toggleButton.setText("Show Less");
        }
        isExpanded = !isExpanded;
    }

}
