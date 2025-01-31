package petarkitanovic.androidkurs.omiljeniglumci;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Objects;

public class Biografija extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.cela_biografija);

        setupActionBar();

        TextView biografija = findViewById(R.id.cela_biografija);


        String biografijaGlumca = Objects.requireNonNull(getIntent().getExtras()).getString("biografija");

        biografija.setText(biografijaGlumca);

    }

    public void setupActionBar(){
        final ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setTitle("Biography");
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.back_btn);

            actionBar.setHomeButtonEnabled(true);
            actionBar.show();

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.movie_details_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.action_whishlist:
                Intent intent = new Intent(Biografija.this,OmiljeniFilmovi.class);
                startActivity(intent);
                break;
        }

        return super.onOptionsItemSelected(item);
    }


}
