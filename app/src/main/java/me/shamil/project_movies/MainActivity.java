package me.shamil.project_movies;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    RecyclerView rvMovies;
    int PageNumber = 1;
    ArrayList<Movie> movies = new  ArrayList<Movie>();
    MoviesAdapter moviesAdapter ;
    String Url;
    public static  final int BY_POPULARITY =1;
    public static  final int BY_RATING =2;
    int ListFetchType = BY_POPULARITY;
    GridLayoutManager gridLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rvMovies = (RecyclerView) findViewById(R.id.rv_movie_list);
        gridLayoutManager = new GridLayoutManager(this, 2);
        rvMovies.setLayoutManager(gridLayoutManager);
        rvMovies.hasFixedSize();
        moviesAdapter = new MoviesAdapter(getBaseContext(),movies);
        rvMovies.setAdapter(moviesAdapter);

        rvMovies.addOnScrollListener(getScrollLsitner());
        getMovieList();


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Movies");
        setSupportActionBar(toolbar);

    }

    public void getMovieList() {

        String baseurl = "";
        if(ListFetchType == BY_RATING){
            baseurl = "http://api.themoviedb.org/3/movie/top_rated";
        }else {
            baseurl = "http://api.themoviedb.org/3/movie/popular";
        }

        Url = baseurl+"?api_key=a2626b5e7169bc1e7b9ff18888ff9131"+"&page="+Integer.toString(PageNumber);
        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, Url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        ApiResponse apiResponse = new Gson().fromJson(response, ApiResponse.class);
                        PageNumber = apiResponse.page +1;
                        for(Movie movie:apiResponse.results){
                            movies.add(movie);
                        }
                        moviesAdapter.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        queue.add(stringRequest);
    }

    public EndlessRecyclerOnScrollListener getScrollLsitner(){
        return new EndlessRecyclerOnScrollListener(gridLayoutManager) {
            @Override
            public void onLoadMore(int currentPage) {
                Log.d("here", "here");
                getMovieList();
            }
        };
    }
    private class MoviesAdapter extends RecyclerView.Adapter {
        ArrayList<Movie> movies = new ArrayList<Movie>();
        Context context;

        public MoviesAdapter(Context context, ArrayList<Movie> movies) {
            this.movies = movies;
            this.context = context;

        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.movie_item_view, parent, false);

            MovieViewHolder vh = new MovieViewHolder(v);
            return vh;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            MovieViewHolder movieViewHolder = (MovieViewHolder) holder;
            final Movie movie = movies.get(position);
            Log.d("Url", "http://image.tmdb.org/t/p/w185/"+movie.mPosterUrl);
            movieViewHolder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context , DetailedActivity.class);
                    intent.putExtra("movie",new Gson().toJson(movie));
                    startActivity(intent);
                }
            });
            movieViewHolder.textView.setText(movie.mTittle);
            Glide.with(context).load("http://image.tmdb.org/t/p/w185/"+movie.mPosterUrl).crossFade().into(movieViewHolder.imageView);
        }

        @Override
        public int getItemCount() {
            return movies.size();
        }
    }

    public class MovieViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textView;
        View view;

        public MovieViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            textView = (TextView) itemView.findViewById(R.id.tv_tittle);
            imageView = (ImageView) itemView.findViewById(R.id.iv_poster);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.main_activity_menu, menu);
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()) {
            case R.id.id_popularity:
                if(ListFetchType == BY_RATING){
                    ListFetchType = BY_POPULARITY;
                    movies.clear();
                    moviesAdapter.notifyDataSetChanged();
                    PageNumber = 1;
                  //  moviesAdapter = new MoviesAdapter(getBaseContext(),movies);
                  //  rvMovies.setAdapter(moviesAdapter);
                    rvMovies.addOnScrollListener(getScrollLsitner());

                    getMovieList();
                }
                break;
            case R.id.id_rating:
                if(ListFetchType == BY_POPULARITY){
                    ListFetchType = BY_RATING;
                    movies.clear();
                    moviesAdapter.notifyDataSetChanged();
                    PageNumber = 1;
                  //  moviesAdapter = new MoviesAdapter(getBaseContext(),movies);
                  //  rvMovies.setAdapter(moviesAdapter);
                    rvMovies.addOnScrollListener(getScrollLsitner());

                    getMovieList();
                }
                break;
        }
        return true;

    }
}
