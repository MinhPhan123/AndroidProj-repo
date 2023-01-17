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
    private List<Bitmap> image;
    private List<String> color;

    //Constructor
    public Item(String ID, String name, String category, double price, double discount, String description, int stockQuantity) {
        this.ID = ID;
        this.name = name;
        this.category = category;
        this.price = price;
        this.discount = discount;
        this.description = description;
        this.stockQuantity = stockQuantity;
        image = new ArrayList<>();
        color = new ArrayList<>();
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(int stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    public List<Bitmap> getImage() {
        return image;
    }

    public void setImage(List<Bitmap> image) {
        this.image = image;
    }

    public List<String> getColor() {
        return color;
    }

    public void setColor(List<String> color) {
        this.color = color;
    }
}
