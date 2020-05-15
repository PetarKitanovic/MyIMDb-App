package petarkitanovic.androidkurs.omiljeniglumci.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import petarkitanovic.androidkurs.omiljeniglumci.DetaljiFilma;
import petarkitanovic.androidkurs.omiljeniglumci.R;
import petarkitanovic.androidkurs.omiljeniglumci.models.ResultsMovieItem;

import static petarkitanovic.androidkurs.omiljeniglumci.net.MyServiceContract.IMAGEBASEURL;

public class AdapterTopMovies  extends RecyclerView.Adapter<AdapterTopMovies.MyViewHolder> {

    private Context context;
    private List<ResultsMovieItem> searchItems;

    private String firstFour(String godina) {
        if (godina.length() > 4) {
            return godina.substring(0, 4);
        } else {
            return null;
        }

    }

    public AdapterTopMovies(Context context, List<ResultsMovieItem> searchItems) {
        this.context = context;
        this.searchItems = searchItems;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.prikaz_filmova, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {


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


        holder.glavni_cardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (SystemClock.elapsedRealtime() - holder.lastClickTime >= 1000) {
                    holder.lastClickTime = SystemClock.elapsedRealtime();
                    final Intent intent = new Intent(context, DetaljiFilma.class);
                    intent.putExtra("id", searchItems.get(position).getId());
                    context.startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return searchItems.size();
    }

    public ResultsMovieItem get(int position) {
        return searchItems.get(position);
    }


    static class MyViewHolder extends RecyclerView.ViewHolder{


        private TextView imeFilma, godinaFilma;
        private ImageView slikaGlumca;
        private CardView glavni_cardview;
        private long lastClickTime ;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);

            itemView.setClickable(true);

            slikaGlumca = itemView.findViewById(R.id.iv_slika_filma);
            imeFilma = itemView.findViewById(R.id.tv_naziv_filma);
            godinaFilma = itemView.findViewById((R.id.tv_godina_filma));
            glavni_cardview = itemView.findViewById(R.id.glavni_cardview);
            lastClickTime = 0;


        }
    }
}