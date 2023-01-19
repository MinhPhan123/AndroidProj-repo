package rmit.ad.myapplication.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import rmit.ad.myapplication.ModelClass.Item;
import rmit.ad.myapplication.R;

public class OrderHistoryAdapter extends RecyclerView.Adapter<OrderHistoryAdapter.orderHistoryViewHolder>{

    ArrayList<Item> itemList;

    public OrderHistoryAdapter(ArrayList<Item> itemList) {
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public orderHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_item_viewholder,parent,false);
        return new orderHistoryViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull orderHistoryViewHolder holder, int position) {
        Picasso.get().load(itemList.get(position).getImage().get(0))
                .into(holder.itemImg);
        holder.itemName.setText(itemList.get(position).getName());
        holder.itemPrice.setText("$ "+String.valueOf(itemList.get(position).getPrice()));
        holder.orderStatus.setText("Delivered");
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class orderHistoryViewHolder extends RecyclerView.ViewHolder{
        ImageView itemImg;
        TextView itemName, itemPrice, orderStatus;

        public orderHistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            itemImg = itemView.findViewById(R.id.item_image);
            itemName = itemView.findViewById(R.id.item_name);
            itemPrice = itemView.findViewById(R.id.item_price);
            orderStatus = itemView.findViewById(R.id.status);
        }
    }
}
