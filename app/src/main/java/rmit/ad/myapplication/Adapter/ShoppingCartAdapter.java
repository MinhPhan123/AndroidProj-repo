package rmit.ad.myapplication.Adapter;

import android.content.Context;
import android.util.Log;
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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import rmit.ad.myapplication.ModelClass.Item;
import rmit.ad.myapplication.R;

public class ShoppingCartAdapter extends RecyclerView.Adapter<ShoppingCartAdapter.shoppingCartViewHolder> {

    ArrayList<Item> shoppingCart;

    public ShoppingCartAdapter(ArrayList<Item> shoppingCart) {
        this.shoppingCart = shoppingCart;
    }

    @NonNull
    @Override
    public shoppingCartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v =   LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_viewholder,parent,false);
        return new shoppingCartViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull shoppingCartViewHolder holder, int position) {
        Item item = shoppingCart.get(position);
        Picasso.get().load(shoppingCart.get(position).getImage().get(0))
                .into(holder.itemImg);
        holder.itemName.setText(shoppingCart.get(position).getName());
        holder.itemPrice.setText("$ "+String.valueOf(shoppingCart.get(position).getPrice()));
        holder.item = item;
    }

    @Override
    public int getItemCount() {
        return shoppingCart.size();
    }

    public class shoppingCartViewHolder extends RecyclerView.ViewHolder{
        Item item;
        ImageView itemImg;
        TextView itemName, itemPrice;
        FirebaseFirestore db;
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser currUser = firebaseAuth.getCurrentUser();
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        public shoppingCartViewHolder(@NonNull View itemView) {
            super(itemView);
            itemImg = itemView.findViewById(R.id.item_image);
            itemName = itemView.findViewById(R.id.item_name);
            itemPrice = itemView.findViewById(R.id.item_price);

            itemView.findViewById(R.id.removeBtn).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DocumentReference documentReference = firestore.collection("Shopping Cart").document(currUser.getUid()).collection("Items").document(item.getID());
                    documentReference.delete();
                    shoppingCart.remove(item);
                    notifyDataSetChanged();
                }
            });
        }
    }
}
