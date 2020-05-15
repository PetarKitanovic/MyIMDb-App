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
import petarkitanovic.androidkurs.omiljeniglumci.model_actor.Cast;

import static petarkitanovic.androidkurs.omiljeniglumci.net.MyServiceContract.IMAGEBASEURL;

public class AdapterFilmoviGlumca extends RecyclerView.Adapter<AdapterFilmoviGlumca.MyViewHolder> {

    private Context context;
    private List<petarkitanovic.androidkurs.omiljeniglumci.model_actor.Cast> searchItems;


    public AdapterFilmoviGlumca(Context context, List<petarkitanovic.androidkurs.omiljeniglumci.model_actor.Cast> searchItems) {
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


        holder.nazivFIlma.setText(searchItems.get(position).getTitle());

        holder.karakter.setText(searchItems.get(position).getCharacter());

//        Picasso.with(context).load(searchItems.get(position).getPosterPath()).into(holder.slikaFilma);

        if (searchItems.get(position).getPosterPath() == null){
            holder.slikaFilma.setImageResource(R.drawable.ic_image_white_24dp);
            holder.slikaFilma.setScaleType(ImageView.ScaleType.FIT_CENTER);
        }else {
            Glide.with(holder.itemView)
                    .load(IMAGEBASEURL + searchItems.get(position).getPosterPath())
                    .into(holder.slikaFilma);
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
        int limit = 15;
        if (searchItems.size() > limit) {
            return limit;
        } else {
            return searchItems.size();
        }
    }

    public Cast get(int position) {
        return searchItems.get(position);
    }


    static class MyViewHolder extends RecyclerView.ViewHolder {


        private TextView nazivFIlma;
        private TextView karakter;
        private ImageView slikaFilma;
        private CardView glavni_cardview;
        private long lastClickTime;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);

            slikaFilma = itemView.findViewById(R.id.iv_slika_glumca);
            nazivFIlma = itemView.findViewById(R.id.tv_ime_glumca);
            karakter = itemView.findViewById(R.id.tv_uloga_glumca);
            glavni_cardview = itemView.findViewById(R.id.glumciCardView);
            lastClickTime = 0;

        }

    }
}
