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

import petarkitanovic.androidkurs.omiljeniglumci.GlumciDetails;
import petarkitanovic.androidkurs.omiljeniglumci.R;
import petarkitanovic.androidkurs.omiljeniglumci.model_movie.Cast;

import static petarkitanovic.androidkurs.omiljeniglumci.net.MyServiceContract.IMAGEBASEURL;

public class AdapterGlumci extends RecyclerView.Adapter<AdapterGlumci.MyViewHolder> {

    private Context context;
    private List<Cast> searchItems;

    public AdapterGlumci(Context context, List<Cast> searchItems) {
        this.context = context;
        this.searchItems = searchItems;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.rv_prikaz_glumaca, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {

        holder.imeGlumca.setText(searchItems.get(position).getName());
//        Picasso.with(context).load(searchItems.get(position).getPosterPath()).into(holder.slikaGlumca);

        if (searchItems.get(position).getProfilePath() == null){
            holder.slikaGlumca.setImageResource(R.drawable.glava_silueta);
            holder.slikaGlumca.setScaleType(ImageView.ScaleType.CENTER_CROP);
        }else{
            Glide.with(holder.itemView)
                    .load(IMAGEBASEURL + searchItems.get(position).getProfilePath())
                    .into(holder.slikaGlumca);
        }


        holder.karakter.setText(searchItems.get(position).getCharacter());

        holder.glavni_cardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (SystemClock.elapsedRealtime() - holder.lastClickTime >= 1000) {
                    holder.lastClickTime = SystemClock.elapsedRealtime();
                    final Intent intent = new Intent(context, GlumciDetails.class);
                    intent.putExtra("id", searchItems.get(position).getId());
                    context.startActivity(intent);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        int limit = 15;
        if (searchItems.size() > limit) {
            return limit;
        } else {
            return searchItems.size();
        }

    }


    static class MyViewHolder extends RecyclerView.ViewHolder {


        private TextView imeGlumca;
        private TextView karakter;
        private ImageView slikaGlumca;
        private CardView glavni_cardview;
        private long lastClickTime ;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);

            slikaGlumca = itemView.findViewById(R.id.iv_slika_glumca);
            imeGlumca = itemView.findViewById(R.id.tv_ime_glumca);
            karakter = itemView.findViewById(R.id.tv_uloga_glumca);
            glavni_cardview = itemView.findViewById(R.id.glumciCardView);
            lastClickTime = 0;

        }

    }
}
