package me.shamil.project_movies;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;

public class DetailedActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed);
        Intent intent = getIntent();
        Movie movie = new Gson().fromJson(intent.getStringExtra("movie"),Movie.class);
        Glide.with(this).load( "http://image.tmdb.org/t/p/w500/"+movie.mPosterUrl).into((ImageView)findViewById(R.id.image));
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(movie.mTittle);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        ((TextView) findViewById(R.id.rating)) .setText("Rating : "+Float.toString(movie.mUserRating)+"/10");
        ((TextView) findViewById(R.id.date)) .setText("Release Date :"+movie.mReleaseDate);
        ((TextView) findViewById(R.id.plot)) .setText(movie.mPlot);

    }

    @Override
    public void onBackPressed() {
        finish();
       // super.onBackPressed();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; goto parent activity.
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
