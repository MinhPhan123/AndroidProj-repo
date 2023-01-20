package rmit.ad.myapplication.Adapter;

import static android.content.ContentValues.TAG;


import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import android.content.Intent;
import android.widget.Toast;

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
    int pos;

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
        Item item =  wishlist.get(position);
        Picasso.get().load(wishlist.get(position).getImage().get(0))
                .into(holder.itemImg);
        holder.itemName.setText(wishlist.get(position).getName());
        holder.itemPrice.setText("$ "+String.valueOf(wishlist.get(position).getPrice()));
        holder.item = item;

    }

    public void setClickListener(onClickInterface onClickInterface) {
        ProductViewAdapter.onClickInterface = onClickInterface;
    }

    @Override
    public int getItemCount() {
        return wishlist.size();
    }


    public class wishlistViewHolder extends RecyclerView.ViewHolder{
        ImageView itemImg;
        TextView itemName, itemPrice, itemID;
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser currUser = firebaseAuth.getCurrentUser();

        Item item;

        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        public wishlistViewHolder(@NonNull View itemView) {
            super(itemView);
            itemImg = itemView.findViewById(R.id.item_image);
            itemName = itemView.findViewById(R.id.item_name);
            itemPrice = itemView.findViewById(R.id.item_price);

            itemView.findViewById(R.id.buyBtn).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DocumentReference documentReference = firestore.collection("Shopping Cart").document(currUser.getUid()).collection("Items").document(item.getID());
                    documentReference.set(item).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Log.d("TAG", "onSuccess: item added to wishlist");
                            Toast.makeText(view.getContext(), "Successfully added into your cart", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d(TAG, "onFailure: " + e.toString());
                        }
                    });
                }
            });
            itemView.findViewById(R.id.removeBtn).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DocumentReference documentReference = firestore.collection("Wishlist").document(currUser.getUid()).collection("Items").document(item.getID());
                    documentReference.delete();
                    wishlist.remove(item);
                    notifyDataSetChanged();
                }
            });
        }
    }
}
