package petarkitanovic.androidkurs.omiljeniglumci.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import petarkitanovic.androidkurs.omiljeniglumci.FullVideo;
import petarkitanovic.androidkurs.omiljeniglumci.R;
import petarkitanovic.androidkurs.omiljeniglumci.model_movie.Result;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.VideoViewHolder> {

    private Context context;
    private List<Result> youtubeVideoList;

    public VideoAdapter(Context context, List<Result> youtubeVideoList) {
        this.context = context;
        this.youtubeVideoList = youtubeVideoList;
    }

    @NonNull
    @Override
    public VideoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.video_view, parent, false);

        return new VideoViewHolder(view);

    }

    @Override
    public void onBindViewHolder(VideoViewHolder holder, final int position) {

        holder.videoWeb.loadUrl("https://www.youtube.com/embed/" + youtubeVideoList.get(position).getKey());
        holder.videoWeb.setBackgroundColor(255);

        holder.imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, FullVideo.class);
                intent.putExtra("key",youtubeVideoList.get(position).getKey());
                context.startActivity(intent);
            }
        });

        holder.nazivVidea.setText(youtubeVideoList.get(position).getName());

    }

    @Override
    public int getItemCount() {
        return youtubeVideoList.size();
    }

    class VideoViewHolder extends RecyclerView.ViewHolder {

        WebView videoWeb;
        ImageButton imageButton;
        TextView nazivVidea;

        @SuppressLint("SetJavaScriptEnabled")
        VideoViewHolder(View itemView) {
            super(itemView);

            videoWeb = itemView.findViewById(R.id.videoWebView);
            videoWeb.getSettings().setJavaScriptEnabled(true);

            imageButton = itemView.findViewById(R.id.imagePlayButton);
            nazivVidea = itemView.findViewById(R.id.naziv_videa);

        }
    }
}