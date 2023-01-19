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

public class OngoingAdapter extends RecyclerView.Adapter<OngoingAdapter.ongoingViewHolder> {

    ArrayList<Item> itemList;

    public OngoingAdapter(ArrayList<Item> itemList) {
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public ongoingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_item_viewholder,parent,false);
        return new ongoingViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ongoingViewHolder holder, int position) {
        Picasso.get().load(itemList.get(position).getImage().get(0))
                .into(holder.itemImg);
        holder.itemName.setText(itemList.get(position).getName());
        holder.itemPrice.setText("$ "+String.valueOf(itemList.get(position).getPrice()));
        holder.orderStatus.setText("Delivering");
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class ongoingViewHolder extends RecyclerView.ViewHolder{
        ImageView itemImg;
        TextView itemName, itemPrice, orderStatus;


        public ongoingViewHolder(@NonNull View itemView) {
            super(itemView);

            itemImg = itemView.findViewById(R.id.item_image);
            itemName = itemView.findViewById(R.id.item_name);
            itemPrice = itemView.findViewById(R.id.item_price);
            orderStatus = itemView.findViewById(R.id.status);

        }
    }
}
