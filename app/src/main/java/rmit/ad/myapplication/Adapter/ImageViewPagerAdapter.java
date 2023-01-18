package rmit.ad.myapplication.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import rmit.ad.myapplication.R;

public class ImageViewPagerAdapter extends PagerAdapter {

    ArrayList<String> imageList;
    Context context;
    LayoutInflater layoutInflater;

    public ImageViewPagerAdapter(Context context, ArrayList<String> imageList) {
        this.context = context;
        this.imageList = imageList;
        layoutInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = layoutInflater.inflate(R.layout.item_image, container, false);
        ImageView imageView = view.findViewById(R.id.image_item);
        Glide.with(context).load(imageList.get(position)).into(imageView);
        container.addView(view, 0);
        return view;
    }

    @Override
    public int getCount() {
        if (imageList != null) {
            return imageList.size();
        }
        return 0;
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
