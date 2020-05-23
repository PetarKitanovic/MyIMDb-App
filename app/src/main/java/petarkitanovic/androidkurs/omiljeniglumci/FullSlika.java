package petarkitanovic.androidkurs.omiljeniglumci;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Objects;

import static petarkitanovic.androidkurs.omiljeniglumci.net.MyServiceContract.FULLIMAGEURL;
import static petarkitanovic.androidkurs.omiljeniglumci.net.MyServiceContract.IMAGEBASEURL;

public class FullSlika extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        hideSystemUI();

        setContentView( R.layout.activity_full_slika );

        String slika = Objects.requireNonNull(getIntent().getExtras()).getString("slika");

        PhotoView photoView = findViewById(R.id.photoView);
        Glide.with(FullSlika.this)
                .load(FULLIMAGEURL + slika)
                .into(photoView);


        FloatingActionButton floatingActionButton = findViewById(R.id.floatingActionButton);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void hideSystemUI() {
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);
    }
}