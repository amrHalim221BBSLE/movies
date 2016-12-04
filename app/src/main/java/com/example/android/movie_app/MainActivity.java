package com.example.android.movie_app;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity implements PosterListener{
    boolean mIsTwoPane=false;
    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        context = getBaseContext();
        PostersFragment mMainFragment = new PostersFragment();
        //Set The Activity to be a listener to the Fragment
        mMainFragment.setSelectedMovie(this);
        getSupportFragmentManager().beginTransaction().add(R.id.item_container, mMainFragment, "").commit();
        //Check if two pane
        if (null != findViewById(R.id.detailConainer)) {
            mIsTwoPane = true;
        }
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(this,SettingsActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    public void setSelectedMovie(String id) {
        // Case One Pane
        //Start Details Activity
        if (!mIsTwoPane) {
            Intent i = new Intent(this, DetailActivity.class);
            i.putExtra("id", id);
            startActivity(i);
        } else {
            //Case Two-PAne
            DetailActivityFragment mDetailsFragment= new DetailActivityFragment();
            Bundle extras= new Bundle();
            extras.putString("id",id);
            mDetailsFragment.setArguments(extras);
            getSupportFragmentManager().beginTransaction().replace(R.id.detailConainer,mDetailsFragment,"").commit();
        }
    }
}
