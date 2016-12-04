package com.example.android.movie_app;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by Amr on 10/22/2016.
 */
public class PostersFragment extends Fragment {

    private RecyclerView mRecyclerView;
    public Adapter myAdapter;
    public ArrayList<ItemModel> itemList = new ArrayList<ItemModel>();
    public JsonObjectRequest jsonObjectRequest;
    private String jsonUrl = "";
    public RequestQueue requestQueue;
    public Realm myRealm;
    public int type;
    public RealmResults<ItemModel> results;
    private PosterListener mListener;

    void setSelectedMovie(PosterListener posterListener) {
        this.mListener = posterListener;
    }
    public PostersFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            UpdateData();
            myAdapter.notifyDataSetChanged();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.posters_list, container, false);
        setHasOptionsMenu(true);


        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        myRealm = Realm.getInstance(getContext());
        myAdapter = new Adapter(getContext(), itemList, mListener);
        mRecyclerView.setAdapter(myAdapter);
        myAdapter.notifyDataSetChanged();
        return view;
    }

    private void UpdateData() {
        itemList.clear();
        jsonUrl = "http://api.themoviedb.org/3/movie/popular?api_key=20cc796f209efabc74f71dbc1ea14bd5";
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String setting = prefs.getString(getString(R.string.orderBy_Key), getString(R.string.mostPopular));
        if (setting.equals("Most Popular")) {
            type = 0;
            jsonUrl = "http://api.themoviedb.org/3/movie/popular?api_key=20cc796f209efabc74f71dbc1ea14bd5";
        } else if (setting.equals("Highest Rated")) {
            type = 1;
            jsonUrl = "http://api.themoviedb.org/3/movie/top_rated?api_key=20cc796f209efabc74f71dbc1ea14bd5";
        } else {
            type = 3;
        }
        requestQueue = Volley.newRequestQueue(getContext());

        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, jsonUrl, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    myRealm.beginTransaction();
                    JSONArray arr = response.getJSONArray("results");
                    //the response JSON Object
                    String originalTitle;
                    String imageViewUrl;
                    String overview;
                    String voteAverage;
                    String releaseDate;
                    String getMovieId;
                    for (int i = 0; i < arr.length(); i++) {
                        JSONObject element = arr.getJSONObject(i);
                        imageViewUrl = element.getString("poster_path");
                        overview = element.getString("overview");
                        originalTitle = element.getString("original_title");
                        voteAverage = element.getString("vote_average");
                        releaseDate = element.getString("release_date");
                        getMovieId = element.getString("id");
                        ItemModel itemModel = new ItemModel(originalTitle, imageViewUrl, overview,
                                voteAverage, releaseDate, getMovieId);
                        itemModel.setType(type);
                        if (myRealm.where(ItemModel.class).equalTo("movieId", itemModel.getMovieId()).equalTo("favourite", true).findFirst() != null) {
                            itemModel.setFavourite(true);
                        }
                        myRealm.copyToRealmOrUpdate(itemModel);
                    }
                    myRealm.commitTransaction();
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
        requestQueue.add(jsonObjectRequest);
        if (type == 1) {
            results = myRealm.where(ItemModel.class).equalTo("type", 1).findAll();
            for (int i = 0; i < results.size(); i++) {
                ItemModel itemModel = results.get(i);
                itemList.add(itemModel);
            }
        } else if (type == 0) {
            //   myAdapter.clearData();
            results = myRealm.where(ItemModel.class).equalTo("type", 0).findAll();
            for (int i = 0; i < results.size(); i++) {
                ItemModel itemModel = results.get(i);
                itemList.add(itemModel);
            }
        } else if (type == 3) {
            results = myRealm.where(ItemModel.class).equalTo("favourite", true).findAll();
            for (int i = 0; i < results.size(); i++) {
                ItemModel itemModel = results.get(i);
                itemList.add(itemModel);
            }
            if (results.size() < 1) {
                Toast.makeText(getContext(), "There is no favourite yet", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        myAdapter.notifyDataSetChanged();
    }

    @Override
    public void onStart() {
        super.onStart();
        UpdateData();
        myAdapter.notifyDataSetChanged();


    }
}
