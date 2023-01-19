package rmit.ad.myapplication.ModelClass;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class Item implements Parcelable {
    private String ID;
    private String category;
    private ArrayList<String> color;
    private String description;
    private double discount;
    private ArrayList<String> image;
    private String name;
    private double price;
    private int stockQuantity;

    //Constructor
    public Item() {}

    public Item(String ID, String category, ArrayList<String> color, String description, double discount, ArrayList<String> image, String name, double price, int stockQuantity) {
        this.ID = ID;
        this.category = category;
        this.color = color;
        this.description = description;
        this.discount = discount;
        this.image = image;
        this.name = name;
        this.price = price;
        this.stockQuantity = stockQuantity;
    }

    protected Item(Parcel in) {
        ID = in.readString();
        name = in.readString();
        category = in.readString();
        price = in.readDouble();
        discount = in.readDouble();
        description = in.readString();
        stockQuantity = in.readInt();
        image = in.createStringArrayList();
        color = in.createStringArrayList();
    }

    public static final Creator<Item> CREATOR = new Creator<Item>() {
        @Override
        public Item createFromParcel(Parcel in) {
            return new Item(in);
        }

        @Override
        public Item[] newArray(int size) {
            return new Item[size];
        }
    };

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

    public void setID(String ID) {
        this.ID = ID;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(ID);
        parcel.writeString(name);
        parcel.writeString(category);
        parcel.writeDouble(price);
        parcel.writeDouble(discount);
        parcel.writeString(description);
        parcel.writeInt(stockQuantity);
        parcel.writeStringList(image);
        parcel.writeStringList(color);
    }
}
