package rmit.ad.myapplication;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import rmit.ad.myapplication.Adapter.ImageViewPagerAdapter;
import rmit.ad.myapplication.ModelClass.Item;

public class ViewItemDetailActivity extends BackgroundActivity {
    TextView itemDescriptionText;
    int selectedQuantity = 0;
    String selectedColor;
    Button toggleButton;
    boolean isExpanded = false;
    Item item;
    ImageSlider imageSlider;
    ImageView btnBack, wishList;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseUser currUser = firebaseAuth.getCurrentUser();
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_item_detail);



        //Implementation of back method
        btnBack = (ImageView) findViewById(R.id.back_button);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                startActivity(new Intent(ViewItemDetailActivity.this, MainActivity.class));
                finish();
            }
        });

        //Implementation of wishList method
        wishList = (ImageView) findViewById(R.id.add_wishlist);
        wishList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                DocumentReference documentReference = firestore.collection("Wishlist").document(currUser.getUid()).collection("Items").document(item.getID());
                documentReference.set(item).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Log.d("TAG", "onSuccess: item added to wishlist");
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d(TAG, "onFailure: " + e.toString());
                            }
                        });
            }
        });

        item = (Item) getIntent().getExtras().get("item");


        //Set up viewpager for image slider
        /*
        ViewPager viewPager = findViewById(R.id.view_pager_image);
        ArrayList<String> imageList = item.getImage();
        ImageViewPagerAdapter imageViewPagerAdapter = new ImageViewPagerAdapter(this, imageList);
        viewPager.setAdapter(imageViewPagerAdapter);
        viewPager.setCurrentItem(0);

         */

        imageSlider = findViewById(R.id.image_slider_item);
        ArrayList<SlideModel> slideModels = new ArrayList<>();
        slideModels.add(new SlideModel(item.getImage().get(0),ScaleTypes.FIT));
        slideModels.add(new SlideModel(item.getImage().get(1),ScaleTypes.FIT));
        imageSlider.setImageList(slideModels,ScaleTypes.FIT);

        //Set up the name of the item
        TextView itemNameText = findViewById(R.id.item_name);
        itemNameText.setText(item.getName());

        //Set up the price of the item
        TextView itemPriceText = findViewById(R.id.item_price);
        itemPriceText.setText(item.getPrice() + " $");

        //Set up the category of the item
        TextView itemCategoryText = findViewById(R.id.item_category);
        itemCategoryText.setText("Category: " + item.getCategory());

        //Set up the discount of the item
        TextView itemDiscountText = findViewById(R.id.item_discount);
        itemDiscountText.setText("Discount: " + item.getDiscount() + " %");

        //Set up the stock quantity of the item
        TextView itemStockQuantityText = findViewById(R.id.item_stock_quantity);
        if (item.getStockQuantity() <= 0) {
            itemStockQuantityText.setText("Stock quantity: Out of stock");
        }
        else{
            itemStockQuantityText.setText("Stock quantity: Available");
        }

        //Set up the expandable description
        itemDescriptionText = findViewById(R.id.item_description);
        itemDescriptionText.setText(item.getDescription());
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
        TextView itemQuantityText = findViewById(R.id.edit_text_quantity);

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

        //Set up color selection
        ArrayList<String> colorList = item.getColor();
        selectColor(colorList);

        AppCompatButton btnAddToCart = findViewById(R.id.add_to_cart_button);
        btnAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DocumentReference documentReference = firestore.collection("Shopping Cart").document(currUser.getUid()).collection("Items").document(item.getID());
                documentReference.set(item).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d("TAG", "onSuccess: item added to wishlist");
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "onFailure: " + e.toString());
                    }
                });
            }
        });
    }

    @SuppressLint("SetTextI18n")
    public void toggleDescription() {
        if (isExpanded) {
            itemDescriptionText.setMaxLines(5);
            toggleButton.setText("Show More");
        } else {
            itemDescriptionText.setMaxLines(Integer.MAX_VALUE);
            toggleButton.setText("Show Less");
        }
        isExpanded = !isExpanded;
    }

    public void selectColor(ArrayList<String> colors) {
        RadioGroup radioGroup = findViewById(R.id.color_group);
        radioGroup.removeAllViews();
        for (String color : colors) {
            RadioButton radioButton = new RadioButton(this);
            radioButton.setId(View.generateViewId());
            radioButton.setText(color);
            radioButton.setOnClickListener(v -> {
                RadioButton rb = (RadioButton) v;
                selectedColor = rb.getText().toString();
                //do something with the selected color
            });
            radioGroup.addView(radioButton);
        }
    }


}