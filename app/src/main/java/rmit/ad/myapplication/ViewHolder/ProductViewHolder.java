package rmit.ad.myapplication.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import rmit.ad.myapplication.Interface.ItemClickListener;
import rmit.ad.myapplication.R;

public class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
{
    public TextView product_title, product_description;
    public ImageView product_image;
    public ItemClickListener listner;

    public ProductViewHolder(@NonNull View itemView) {
        super(itemView);

        product_image = (ImageView) itemView.findViewById(R.id.product_image);
        product_title = (TextView) itemView.findViewById(R.id.product_title);
        product_description = (TextView) itemView.findViewById(R.id.product_description);
    }

    public void setItemClickListener(ItemClickListener listener)
    {
        this.listner = listner;
    }

    @Override
    public void onClick(View view) {
        listner.onCLick(view, getAdapterPosition(),false);
    }
}
