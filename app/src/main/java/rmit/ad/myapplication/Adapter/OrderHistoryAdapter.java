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

import rmit.ad.myapplication.ModelClass.Item;
import rmit.ad.myapplication.R;

public class OrderHistoryAdapter extends FirestoreRecyclerAdapter<Item, OrderHistoryAdapter.orderHistoryViewHolder> {

    Context context;

    public OrderHistoryAdapter(@NonNull FirestoreRecyclerOptions<Item> options, Context context) {
        super(options);
        this.context = context;
    }

    @NonNull
    @Override
    public orderHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.wishlist_viewholder,parent,false);
        return new orderHistoryViewHolder(v);
    }

    @Override
    protected void onBindViewHolder(@NonNull orderHistoryViewHolder holder, int position, @NonNull Item model) {
        Picasso.get().load(model.getImage().get(0))
                .into(holder.itemImg);
        holder.itemName.setText(model.getName());
        holder.itemPrice.setText("$ "+String.valueOf(model.getPrice()));
        holder.orderStatus.setText("Delivered");
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
