package rmit.ad.myapplication.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import rmit.ad.myapplication.ModelClass.Item;
import rmit.ad.myapplication.R;

public class ProductViewAdapter extends RecyclerView.Adapter<ProductViewAdapter.ProductViewHolder>
{
    Context context;
    ArrayList<Item> itemArrayList;

    public ProductViewAdapter(Context context, ArrayList<Item> itemArrayList) {
        this.context = context;
        this.itemArrayList = itemArrayList;
    }

    @NonNull
    @Override
    public ProductViewAdapter.ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View v = LayoutInflater.from(context).inflate(R.layout.product_menu_layout,parent,false);
        return new ProductViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewAdapter.ProductViewHolder holder, int position)
    {
        Item item = itemArrayList.get(position);

        //Picasso.get().load("https://firebasestorage.googleapis.com/v0/b/androidproj-12477.appspot.com/o/sample_picture%2Fsample1.jpg?alt=media&token=53ef9cb0-f2c2-4d22-b0e6-ad5fed5dec4c")
        Picasso.get().load(item.getImage().get(0))
                .into(holder.product_image);
        holder.product_title.setText(item.getName());
        holder.product_description.setText("$ "+String.valueOf(item.getPrice()));
    }

    @Override
    public int getItemCount(){
        return itemArrayList.size();
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder
    {
        TextView product_title, product_description;
        ImageView product_image;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            product_image = (ImageView) itemView.findViewById(R.id.product_image);
            product_title = (TextView) itemView.findViewById(R.id.product_title);
            product_description = (TextView) itemView.findViewById(R.id.product_description);

        }
    }
}
