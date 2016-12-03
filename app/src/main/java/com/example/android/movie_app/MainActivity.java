package com.example.android.movie_app;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import io.realm.Realm;

public class MainActivity extends AppCompatActivity {
    //boolean mIsTwoPane=false;
    Context context;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        context = getBaseContext();
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.item_container, new PostersFragment());
        fragmentTransaction.commit();
        if(isTablet()) {
            Realm realm = Realm.getInstance(getBaseContext());
            int type;
            if(sharedPreferences.getString("OrderBy","Most Popular").equals("Most Popular")) {
                type = 0;
            } else {
                type = 1;
            }
            ItemModel itemModel = realm.where(ItemModel.class).equalTo("type",type).findFirst();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            DetailActivityFragment detailFragment=new DetailActivityFragment();
            Bundle args = new Bundle();
            detailFragment.setArguments(args);
            transaction.add(R.id.detailConainer, detailFragment);
            transaction.commit();
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
    public boolean isTablet() {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }
    public void transact(String id) {
        FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
        DetailActivityFragment detailActivityFragment=new DetailActivityFragment();
        Bundle bundle=new Bundle();
        bundle.putString("id",id);
        detailActivityFragment.setArguments(bundle);
        fragmentTransaction.replace(R.id.detailConainer, detailActivityFragment);
        fragmentTransaction.commit();

    }
}
