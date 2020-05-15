package petarkitanovic.androidkurs.omiljeniglumci.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import petarkitanovic.androidkurs.omiljeniglumci.DetaljiFilma;
import petarkitanovic.androidkurs.omiljeniglumci.R;
import petarkitanovic.androidkurs.omiljeniglumci.database.FavoriteMovies;

import static petarkitanovic.androidkurs.omiljeniglumci.net.MyServiceContract.IMAGEBASEURL;

public class AdapterOmiljenih extends RecyclerView.Adapter<AdapterOmiljenih.MyViewHolder> {

    private Context context;
    private List<FavoriteMovies> favoriteMovies;

    private String firstFour(String godina) {
        if (godina == null) {
            return "";
        }
        if (godina.length() > 4) {
            return godina.substring(0, 4);
        } else {
            return "";
        }
    }

    public AdapterOmiljenih(Context context, List<FavoriteMovies> favoriteMovies) {
        this.context = context;
        this.favoriteMovies = favoriteMovies;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.omiljeni_filmovi, parent, false);

        return new MyViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {

        holder.filmNaziv.setText(favoriteMovies.get(position).getmMovieName());
        holder.filmDate.setText(firstFour(favoriteMovies.get(position).getmDate()));
        holder.filmTrajanje.setText(favoriteMovies.get(position).getmRuntime() + "min");

        if (favoriteMovies.get(position).getmRating() == null) {
            holder.filmZvezdica.setVisibility(View.GONE);
            holder.filmOcena.setVisibility(View.GONE);
        } else {
            holder.filmOcena.setText(favoriteMovies.get(position).getmRating());
        }

//        Picasso.with(context).load(searchItems.get(position).getPosterPath()).into(holder.slikaGlumca);

        if (favoriteMovies.get(position).getmImage() == null) {
            holder.filmSlika.setImageResource(R.drawable.ic_image_white_24dp);
            holder.filmSlika.setScaleType(ImageView.ScaleType.FIT_CENTER);
        } else {
            Glide.with(holder.itemView)
                    .load(IMAGEBASEURL + favoriteMovies.get(position).getmImage())
                    .into(holder.filmSlika);
        }


        holder.constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (SystemClock.elapsedRealtime() - holder.lastClickTime >= 1000) {
                    holder.lastClickTime = SystemClock.elapsedRealtime();
                    final Intent intent = new Intent(context, DetaljiFilma.class);
                    intent.putExtra("id", favoriteMovies.get(position).getmId());
                    context.startActivity(intent);
                }
            }
        });

    }

    public FavoriteMovies get(int position) {
        return favoriteMovies.get(position);
    }

    @Override
    public int getItemCount() {

        return favoriteMovies.size();

    }


    static class MyViewHolder extends RecyclerView.ViewHolder {


        private TextView filmNaziv, filmDate, filmTrajanje, filmOcena;
        private ImageView filmSlika, filmZvezdica;
        private ConstraintLayout constraintLayout;

        private long lastClickTime;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);

            filmNaziv = itemView.findViewById(R.id.film_naziv);
            filmDate = itemView.findViewById(R.id.film_date);
            filmTrajanje = itemView.findViewById(R.id.film_trajanje);
            filmOcena = itemView.findViewById(R.id.film_ocena);
            filmSlika = itemView.findViewById(R.id.film_slika);
            filmZvezdica = itemView.findViewById(R.id.film_zvezdica);
            constraintLayout = itemView.findViewById(R.id.omiljeni_constraint_layout);

            lastClickTime = 0;

        }

    }
}