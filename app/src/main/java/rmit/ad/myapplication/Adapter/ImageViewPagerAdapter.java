package rmit.ad.myapplication.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import rmit.ad.myapplication.ModelClass.Item;
import rmit.ad.myapplication.R;

public class ImageViewPagerAdapter extends PagerAdapter {

    ArrayList<Item> itemArrayList;

    public ImageViewPagerAdapter(ArrayList<Item> itemArrayList) {
        this.itemArrayList = itemArrayList;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        Item item = itemArrayList.get(position);
        ArrayList<String> imageList = item.getImage();
        View view = LayoutInflater.from(container.getContext()).inflate(R.layout.item_image, container, false);
        for (String image : imageList) {
            ImageView imageView = view.findViewById(R.id.image_item);
            Picasso.get().load(image).into(imageView);
            container.addView(view);
        }

        return view;
    }

    @Override
    public int getCount() {
        if (itemArrayList == null || itemArrayList.size() == 0) {
            return 0;
        }
        int count = 0;
        for (Item item : itemArrayList) {
            count += item.getImage().size();
        }
        return count;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position,@NonNull Object object) {
        container.removeView((View) object);
    }
}
