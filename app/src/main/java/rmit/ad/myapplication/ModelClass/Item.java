package rmit.ad.myapplication.ModelClass;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class Item implements Parcelable {
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
