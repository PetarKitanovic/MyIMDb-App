package petarkitanovic.androidkurs.omiljeniglumci.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;

import java.util.List;
import java.util.Objects;

import petarkitanovic.androidkurs.omiljeniglumci.R;
import petarkitanovic.androidkurs.omiljeniglumci.model_movie.Backdrop;

import static petarkitanovic.androidkurs.omiljeniglumci.net.MyServiceContract.IMAGEBASEURL;


public class SliderPagerAdapter extends PagerAdapter {

    private Context mContext;
    private List<Backdrop> mList;


    public SliderPagerAdapter(Context mContext, List<Backdrop> mList) {
        this.mContext = mContext;
        this.mList = mList;
    }


    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {


        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        @SuppressLint("InflateParams") View slideLayout = Objects.requireNonNull(inflater).inflate(R.layout.slide_item, null);

        ImageView slideImg = slideLayout.findViewById(R.id.slide_img);

        Glide.with(container)
                .load(IMAGEBASEURL + mList.get(position).getFilePath())
                .into(slideImg);

        container.addView(slideLayout);
        return slideLayout;

    }

    @Override
    public int getCount() {

        if (mList.size() > 10) {
            return 10;
        } else {
            return mList.size();
        }

    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == o;
    }


    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
