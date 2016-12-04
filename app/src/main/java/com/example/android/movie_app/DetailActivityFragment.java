package com.example.android.movie_app;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailActivityFragment extends Fragment {

    public ArrayList<ReviewItem> reviewItems = new ArrayList<ReviewItem>();
    public String jsonUrlReview="";
    public String jsonUrlTrailer="";
    private JsonObjectRequest jsonObjectRequestReview;
    public AdapterReviewList adapterReviewList;
    private RequestQueue requestQueue;
    private JsonObjectRequest jsonObjectRequestTrailer;
    public String movieId;
    public DetailActivityFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail, container, false);
        //start intent info display details
        TextView originalTitle = (TextView) view.findViewById(R.id.original_title);
        ImageView imageViewUrl = (ImageView) view.findViewById(R.id.thumbnail);
        TextView overview = (TextView) view.findViewById(R.id.overview);
        TextView voteAverage = (TextView) view.findViewById(R.id.vote_average);
        TextView releaseDate = (TextView) view.findViewById(R.id.release_date);
        //Receive the sent Bundle
        //Receive the sent  String
        Realm intentRealm=Realm.getInstance(getContext());
        Bundle sentBundle = getArguments();
        //Receive the sent  String
        movieId = sentBundle.getString("id");
            ItemModel itemIntent=intentRealm.where(ItemModel.class).equalTo("movieId",movieId).findFirst();
            originalTitle.setText(itemIntent.getOriginalTitle());
            Picasso.with(getContext()).load("http://image.tmdb.org/t/p/" + "w500" +
                    itemIntent.getImageViewUrl()).into(imageViewUrl);
            overview.setText(itemIntent.getOverview());
            voteAverage.setText( "Vote: "+itemIntent.getVoteAverage()+"/10.0");
            releaseDate.setText(itemIntent.getReleaseDate());
            //end intent info display details
            //start and display trailer
            Button trailerButton = (Button) view.findViewById(R.id.trailer_button);
            trailerButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        Realm realm1=Realm.getInstance(getContext());
                        MovieTrailer movieTrailer=realm1.where(MovieTrailer.class).equalTo("movieId",getArguments().getString("id")).findFirst();
                        Intent intent = new Intent(Intent.ACTION_VIEW,
                                Uri.parse("http://www.youtube.com/watch?v=" +movieTrailer.getKey()));
                        startActivity(intent);
                    } catch (Exception e) {
                        Toast.makeText(getContext(), "No trailers available", Toast.LENGTH_SHORT).show();
                    }
                }
            });


            //end display trailer
            //start display Reviews
            ListView listView = (ListView) view.findViewById(R.id.reviews_list);
            adapterReviewList = new AdapterReviewList(getContext(), reviewItems);
            listView.setAdapter(adapterReviewList);
            adapterReviewList.notifyDataSetChanged();
            listView.setOnTouchListener(new View.OnTouchListener() {
                // Setting on Touch Listener for handling the touch inside ScrollView
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    // Disallow the touch request for parent scroll on touch of child view
                    v.getParent().requestDisallowInterceptTouchEvent(true);
                    return false;
                }

            });
            //end display Reviews
            //start assign favourite
            final ImageView favourite=(ImageView)view.findViewById(R.id.favouriteImage);
            final Realm realm;
            realm=Realm.getInstance(getContext());
            final ItemModel itemModel=realm.where(ItemModel.class).equalTo("movieId",movieId).findFirst();
            if(itemModel.getFavourite()) {
                favourite.setBackgroundColor(getResources().getColor(R.color.Gold));
            }
            favourite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(itemModel.getFavourite()) {
                        realm.beginTransaction();
                        itemModel.setFavourite(false);
                        realm.copyToRealmOrUpdate(itemModel);
                        realm.commitTransaction();
                        favourite.setBackgroundColor(getResources().getColor(R.color.Dark_Grey));
                    } else {
                        realm.beginTransaction();
                        itemModel.setFavourite(true);
                        realm.copyToRealmOrUpdate(itemModel);
                        realm.commitTransaction();
                        favourite.setBackgroundColor(getResources().getColor(R.color.Gold));
                    }
                }
            });
            //end assign favourite


        return view;
    }
    public void GetTrailerKey(){
        final MovieTrailer movie=new MovieTrailer();
        movie.setMovieId(movieId);
        jsonUrlTrailer="https://api.themoviedb.org/3/movie/"+movie.getMovieId()+"/videos?api_key=20cc796f209efabc74f71dbc1ea14bd5";
        requestQueue = Volley.newRequestQueue(getContext());
        jsonObjectRequestTrailer = new JsonObjectRequest(Request.Method.GET, jsonUrlTrailer, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Realm realmTrailer=Realm.getInstance(getContext());
                    realmTrailer.beginTransaction();
                    JSONArray arr = response.getJSONArray("results");
                    //the response JSON Object

                    JSONObject element = arr.getJSONObject(0);
                    movie.setKey(element.getString("key"));
                    movie.setTrailerId(element.getString("id"));
                    realmTrailer.copyToRealmOrUpdate(movie);
                    realmTrailer.commitTransaction();
                }
                // Try and catch are included to handle any errors due to JSON
                catch (JSONException e) {
                    // If an error occurs, this prints the error to the log
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(jsonObjectRequestTrailer);
    }
    public void GetReviews(){
        jsonUrlReview = "https://api.themoviedb.org/3/movie/"+ movieId +"/reviews?api_key=20cc796f209efabc74f71dbc1ea14bd5";
        requestQueue = Volley.newRequestQueue(getContext());
        jsonObjectRequestReview = new JsonObjectRequest(Request.Method.GET, jsonUrlReview, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray arr = response.getJSONArray("results");
                    //the response JSON Object
                    String author;
                    String content;
                    String reviewId;
                    Realm realmReviews=Realm.getInstance(getContext());
                    realmReviews.beginTransaction();
                    for (int i = 0; i < arr.length(); i++) {

                        JSONObject element = arr.getJSONObject(i);
                        author = element.getString("author");
                        content = element.getString("content");
                        reviewId=element.getString("id");
                        ReviewItem item = new ReviewItem(author, content);
                        item.setReviewId(reviewId);
                        item.setMovieId(movieId);
                        realmReviews.copyToRealmOrUpdate(item);
                    }
                    realmReviews.commitTransaction();
                    ShowReviews();
                }
                // Try and catch are included to handle any errors due to JSON
                catch (JSONException e) {
                    // If an error occurs, this prints the error to the log
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(jsonObjectRequestReview);
    }

    public void ShowReviews() {
        reviewItems.clear();
        Realm realm = Realm.getInstance(getContext());
        RealmResults<ReviewItem> results = realm.where(ReviewItem.class).equalTo("movieId",movieId).findAll();
        for (int i=0; i<results.size(); i++) {
            reviewItems.add(results.get(i));
            adapterReviewList.notifyDataSetChanged();
        }
        adapterReviewList.notifyDataSetChanged();
    }


    @Override
    public void onStart() {
        super.onStart();
        GetTrailerKey();
        GetReviews();
    }
}
