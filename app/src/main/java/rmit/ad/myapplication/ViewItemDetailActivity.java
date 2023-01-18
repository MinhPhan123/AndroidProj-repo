package rmit.ad.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.ArrayList;

import rmit.ad.myapplication.Adapter.ImageViewPagerAdapter;
import rmit.ad.myapplication.ModelClass.Item;

public class ViewItemDetailActivity extends AppCompatActivity {
    TextView itemDescriptionText;
    int selectedQuantity = 0;
    String selectedColor;
    Button toggleButton;
    boolean isExpanded = false;
    String itemID;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_item_detail);

        ArrayList<Item> itemList = this.getIntent().getExtras().getParcelableArrayList("itemArrayList");
        itemID = getIntent().getStringExtra("itemID");

        for (Item item : itemList) {
            if (item.getID().equals(itemID)) {

                //Set up viewpager for image slider
                ViewPager viewPager = findViewById(R.id.view_pager_image);
                ImageViewPagerAdapter imageViewPagerAdapter = new ImageViewPagerAdapter(itemList);
                viewPager.setAdapter(imageViewPagerAdapter);

                //Set up the name of the item
                TextView itemNameText = findViewById(R.id.item_name);
                itemNameText.setText(item.getName());

                //Set up the ID of the item
                TextView itemIDText = findViewById(R.id.item_ID);
                itemID = item.getID();
                itemIDText.setText(itemID);

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
            }
        }

        AppCompatButton btnAddToCart = findViewById(R.id.add_to_cart_button);
        btnAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ViewItemDetailActivity.this, ShoppingCartActivity.class);
                i.putExtra("itemID", itemID);
                i.putExtra("itemSelectedColor", selectedColor);
                i.putExtra("itemSelectedQuantity", selectedQuantity);
                startActivity(i);
            }
        });
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

    private void selectColor(ArrayList<String> colors) {
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