package me.shamil.project_movies;


import com.google.gson.annotations.SerializedName;


public class Movie {

    @SerializedName("title")
    public String mTittle;

    @SerializedName("poster_path")
    public String mPosterUrl;

    @SerializedName("overview")
    public String mPlot;

    @SerializedName("vote_average")
    public float mUserRating;

    @SerializedName("release_date")
    public String mReleaseDate;

public void ConvertUrl(){
    mPosterUrl = "http://image.tmdb.org/t/p/w500/"+mPosterUrl;
}


}
