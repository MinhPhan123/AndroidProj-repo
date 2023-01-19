package rmit.ad.myapplication.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import rmit.ad.myapplication.ModelClass.Item;
import rmit.ad.myapplication.R;
import rmit.ad.myapplication.ViewItemDetailActivity;

public class ProductViewAdapter extends RecyclerView.Adapter<ProductViewAdapter.ProductViewHolder>
{
    Context context;
    ArrayList<Item> itemArrayList;
    static onClickInterface onClickInterface;

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

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ProductViewAdapter.ProductViewHolder holder, @SuppressLint("RecyclerView") int position)
    {
        Item item = itemArrayList.get(position);

        Picasso.get().load(item.getImage().get(0))
                .into(holder.product_image);
        holder.product_title.setText(item.getName());
        holder.product_description.setText("$ "+ String.valueOf(item.getPrice()));

    }

    @Override
    public int getItemCount(){
        return itemArrayList.size();
    }

    public void setClickListener(onClickInterface onClickInterface) {
        ProductViewAdapter.onClickInterface = onClickInterface;
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        TextView product_title, product_description;
        ImageView product_image;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            product_image = (ImageView) itemView.findViewById(R.id.product_image);
            product_title = (TextView) itemView.findViewById(R.id.product_title);
            product_description = (TextView) itemView.findViewById(R.id.product_description);
            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View view) {
            if (onClickInterface != null) {
                onClickInterface.setClick(view, getAdapterPosition());
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setFilteredList(ArrayList<Item> filteredList)
    {
        this.itemArrayList = filteredList;
        notifyDataSetChanged();
    }




}
