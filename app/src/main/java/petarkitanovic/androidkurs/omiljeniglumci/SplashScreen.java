package petarkitanovic.androidkurs.omiljeniglumci;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;

import java.util.Timer;
import java.util.TimerTask;

public class SplashScreen extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

            setContentView(R.layout.splash_screen);

            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    startActivity(new Intent(SplashScreen.this, MainActivity.class));
                    finish();
                }
            }, 1500);

    }
}
