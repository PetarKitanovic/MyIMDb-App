package petarkitanovic.androidkurs.omiljeniglumci.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;

import java.util.List;
import java.util.Objects;

import petarkitanovic.androidkurs.omiljeniglumci.FullSlika;
import petarkitanovic.androidkurs.omiljeniglumci.R;
import petarkitanovic.androidkurs.omiljeniglumci.model_movie.Backdrop;

import static petarkitanovic.androidkurs.omiljeniglumci.net.MyServiceContract.IMAGEBASEURL;


public class SliderPagerAdapter extends PagerAdapter {

    private Context mContext;
    private List<Backdrop> mList;
    private long lastClickTime = 0;

    public SliderPagerAdapter(Context mContext, List<Backdrop> mList) {
        this.mContext = mContext;
        this.mList = mList;
    }


    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {


        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        @SuppressLint("InflateParams") View slideLayout = Objects.requireNonNull(inflater).inflate(R.layout.slide_item, null);


        ImageView slideImg = slideLayout.findViewById(R.id.slide_img);

        Glide.with(container)
                .load(IMAGEBASEURL + mList.get(position).getFilePath())
                .into(slideImg);

        slideImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SystemClock.elapsedRealtime() - lastClickTime >= 1000) {
                    lastClickTime = SystemClock.elapsedRealtime();

                    Intent intent = new Intent(mContext, FullSlika.class);
                    intent.putExtra("slika", mList.get(position).getFilePath());
                    mContext.startActivity(intent);
                }
            }
        });

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
