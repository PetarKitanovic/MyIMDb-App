package petarkitanovic.androidkurs.omiljeniglumci.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import petarkitanovic.androidkurs.omiljeniglumci.R;
import petarkitanovic.androidkurs.omiljeniglumci.models.ResultsMovieItem;

import static petarkitanovic.androidkurs.omiljeniglumci.net.MyServiceContract.IMAGEBASEURL;

public class AdapterSearch extends RecyclerView.Adapter<AdapterSearch.MyViewHolder>{
    private Context context;
    private List<ResultsMovieItem> searchItems;
    private OnItemClickListener listener;

    private String firstFour(String godina) {
        if (godina.length() > 4) {
            return godina.substring(0, 4);
        } else {
            return null;
        }

    }

    public AdapterSearch(Context context, List<ResultsMovieItem> searchItems, OnItemClickListener listener) {
        this.context = context;
        this.searchItems = searchItems;
        this.listener = listener;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_prikaz, parent, false);

        return new MyViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.imeFilma.setText(searchItems.get(position).getTitle());

        try {
            holder.godinaFilma.setText(firstFour(searchItems.get(position).getReleaseDate()));

        } catch (NullPointerException e) {
            e.printStackTrace();
        }
//        Picasso.with(context).load(searchItems.get(position).getPosterPath()).into(holder.slikaGlumca);
        if (searchItems.get(position).getPosterPath() == null) {
            holder.slikaGlumca.setImageDrawable(context.getDrawable(R.drawable.ic_image_white_24dp));
            holder.slikaGlumca.setScaleType(ImageView.ScaleType.FIT_CENTER);
        } else {
            Glide.with(holder.itemView)
                    .load(IMAGEBASEURL +  searchItems.get(position).getPosterPath())
                    .into(holder.slikaGlumca);
        }
    }

    @Override
    public int getItemCount() {
        return searchItems.size();
    }

    public ResultsMovieItem get(int position) {
        return searchItems.get(position);
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


        private TextView imeFilma, godinaFilma;
        private ImageView slikaGlumca;

        private long lastClickTime = System.currentTimeMillis();
        private static final long CLICK_TIME_INTERVAL = 1000;

        private OnItemClickListener vhListener;

        MyViewHolder(@NonNull View itemView, OnItemClickListener vhListener) {
            super(itemView);

            itemView.setClickable(true);
            itemView.setOnClickListener(this);

            slikaGlumca = itemView.findViewById(R.id.slikaGlumca);
            imeFilma = itemView.findViewById(R.id.imeGlumca);
            godinaFilma = itemView.findViewById((R.id.godinaFilma));
            this.vhListener = vhListener;

        }



        @Override
        public void onClick(View v) {
            long now = System.currentTimeMillis();
            if (now - lastClickTime < CLICK_TIME_INTERVAL){
                return;
            }
            lastClickTime = now;

            vhListener.onSearchMovieClick(getAdapterPosition());
        }

    }

    public interface OnItemClickListener {
        void onSearchMovieClick(int position);
    }


}
