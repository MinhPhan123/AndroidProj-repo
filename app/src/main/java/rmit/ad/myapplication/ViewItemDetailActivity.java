package rmit.ad.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
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

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_item_detail);

        ArrayList<Item> itemList = this.getIntent().getExtras().getParcelableArrayList("itemArrayList");
        String itemID = getIntent().getStringExtra("itemID");

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

                //Set up color spinner
                Spinner colorSpinner = findViewById(R.id.color_spinner);
                ArrayList<String> colorList = item.getColor();
                ArrayAdapter<String> colorAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, colorList);
                colorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                colorSpinner.setAdapter(colorAdapter);
                colorSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        selectedColor = parent.getItemAtPosition(position).toString();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });
            }
        }
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