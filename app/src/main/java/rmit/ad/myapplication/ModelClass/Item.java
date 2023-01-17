package rmit.ad.myapplication.ModelClass;

import android.graphics.Bitmap;
import java.util.ArrayList;
import java.util.List;

public class Item {
    private String ID;
    private String name;
    private String category;
    private double price;
    private double discount;
    private String description;
    private int stockQuantity;
    private ArrayList<String> image;
    private ArrayList<String> color;

    //Constructor
    public Item() {}

    public Item(String ID, String name, String category, double price, double discount, String description, int stockQuantity, ArrayList<String> image, ArrayList<String> color) {
        this.ID = ID;
        this.name = name;
        this.category = category;
        this.price = price;
        this.discount = discount;
        this.description = description;
        this.stockQuantity = stockQuantity;
        this.image = new ArrayList<>(image);
        this.color = new ArrayList<>(color);
    }

    public String getID() {
        return ID;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public double getPrice() {
        return price;
    }

    public double getDiscount() {
        return discount;
    }

    public String getDescription() {
        return description;
    }

    public int getStockQuantity() {
        return stockQuantity;
    }

    public ArrayList<String> getImage() {
        return image;
    }

    public ArrayList<String> getColor() {
        return color;
    }
}
