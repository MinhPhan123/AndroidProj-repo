package rmit.ad.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import rmit.ad.myapplication.ModelClass.Image;
import rmit.ad.myapplication.ModelClass.ImageViewPagerAdapter;
import rmit.ad.myapplication.ModelClass.Item;

public class ViewItemDetail extends AppCompatActivity {
    private Item item;
    private Spinner colorSpinner;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_item_detail);

        //Set up viewpager for image slider
        ViewPager viewPager = findViewById(R.id.view_pager_image);
        List<Image> imagesList = item.getImage();
        ImageViewPagerAdapter imageViewPagerAdapter = new ImageViewPagerAdapter(imagesList);
        viewPager.setAdapter(imageViewPagerAdapter);

        //Set up the name of the item
        TextView itemNameText = findViewById(R.id.item_name);
        itemNameText.setText(item.getName());

        //Set up the ID of the item
        TextView itemIDText = findViewById(R.id.item_ID);
        itemIDText.setText(item.getID());

        //Set up the category of the item
        TextView itemCategoryText = findViewById(R.id.item_category);
        itemCategoryText.setText("Category: " + item.getCategory());

        //Set up the discount of the item
        TextView itemDiscountText = findViewById(R.id.item_discount);
        itemDiscountText.setText("Discount: " + item.getDiscount() + " %");

        //Set up the stock quantity of the item
        //---------Code lai phan nay neu quantity = 0 thi display status------
        TextView itemStockQuantityText = findViewById(R.id.item_stock_quantity);
        itemStockQuantityText.setText("Stock quantity: " + item.getStockQuantity());

        //Set up color spinner for selecting color
        colorSpinner = findViewById(R.id.color_spinner);
        String itemID = getIntent().getStringExtra("itemID");
        getItem(itemID);
        setUpColorSpinner();

    }

    private void getItem(String itemID) {

    }

    //Set up color spinner
    private void setUpColorSpinner() {
        List<String> colorList = item.getColor();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(ViewItemDetail.this, android.R.layout.simple_spinner_item, colorList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        colorSpinner.setAdapter(adapter);
        colorSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedColor = parent.getItemAtPosition(position).toString();
                item.setSelectedColor(selectedColor);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }
}