package rmit.ad.myapplication.Adapter;

import static android.content.ContentValues.TAG;

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

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import rmit.ad.myapplication.ModelClass.Item;
import rmit.ad.myapplication.R;

public class WishlistAdapter extends RecyclerView.Adapter<WishlistAdapter.wishlistViewHolder> {

    ArrayList<Item> wishlist;
    Context context;

    public WishlistAdapter(ArrayList<Item> wishlist) {
        this.wishlist = wishlist;
    }

    @NonNull
    @Override
    public wishlistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v =   LayoutInflater.from(parent.getContext()).inflate(R.layout.wishlist_viewholder,parent,false);
        return new wishlistViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull wishlistViewHolder holder, int position) {
        Picasso.get().load(wishlist.get(position).getImage().get(0))
                .into(holder.itemImg);
        holder.itemName.setText(wishlist.get(position).getName());
        holder.itemPrice.setText("$ "+String.valueOf(wishlist.get(position).getPrice()));
        holder.itemID.setText(wishlist.get(position).getID());

    }

    @Override
    public int getItemCount() {
        return wishlist.size();
    }


    public static class wishlistViewHolder extends RecyclerView.ViewHolder{
        ImageView itemImg;
        TextView itemName, itemPrice, itemID;
        FirebaseFirestore db;
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser currUser = firebaseAuth.getCurrentUser();

        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        public wishlistViewHolder(@NonNull View itemView) {
            super(itemView);
            itemImg = itemView.findViewById(R.id.item_image);
            itemName = itemView.findViewById(R.id.item_name);
            itemPrice = itemView.findViewById(R.id.item_price);
            itemID = itemView.findViewById(R.id.itemID);

            itemView.findViewById(R.id.buyBtn).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
            itemView.findViewById(R.id.removeBtn).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
        }
    }
}
