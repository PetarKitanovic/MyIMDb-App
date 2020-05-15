package petarkitanovic.androidkurs.omiljeniglumci;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;

public class FullVideo extends Activity {


    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.full_video);

        String key = getIntent().getStringExtra("key");

        WebView webView = findViewById(R.id.full_web_view);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setMediaPlaybackRequiresUserGesture(false);
        webView.loadUrl("https://www.youtube.com/embed/" + key);

    }
}
