package rmit.ad.myapplication.ModelClass;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import java.util.List;

import rmit.ad.myapplication.R;


public class ImageViewPagerAdapter extends PagerAdapter {

    //List of images
    private List<Image> imagesList;

    //ViewPager constructor
    public ImageViewPagerAdapter(List<Image> imagesList) {
        this.imagesList = imagesList;

    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = LayoutInflater.from(container.getContext()).inflate(R.layout.item_image, container,false);

        // referencing the image view from the item_image.xml file
        ImageView imageView = view.findViewById(R.id.image_item);

        //Setting the image
        Image image = imagesList.get(position);
        imageView.setImageResource(image.getUrl());

        container.addView(view);
        return view;
    }


    @Override
    public int getCount() {
        if (imagesList != null) {
            return imagesList.size();
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
