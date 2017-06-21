package com.example.alex.listcountry;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity implements View.OnClickListener, SearchView.OnQueryTextListener {

    private ImageView image_Africa, image_Americas, image_Asia, image_Europe, image_Oceania;
    private TextView text_coonect, text_africa, text_americas, text_asia, text_europe, text_oceania;
    private Intent intent;
    private String region;
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        text_coonect = (TextView) findViewById(R.id.text_connect);
        text_africa = (TextView) findViewById(R.id.text_africa);
        text_americas = (TextView) findViewById(R.id.text_americas);
        text_asia = (TextView) findViewById(R.id.text_asia);
        text_europe = (TextView) findViewById(R.id.text_europe);
        text_oceania = (TextView) findViewById(R.id.text_oceania);

        image_Africa = (ImageView) findViewById(R.id.image_africa);
        image_Americas = (ImageView) findViewById(R.id.image_americas);
        image_Asia = (ImageView) findViewById(R.id.image_asia);
        image_Europe = (ImageView) findViewById(R.id.image_europe);
        image_Oceania = (ImageView) findViewById(R.id.image_oceania);

        TabHost tabHost = (TabHost) findViewById(R.id.tab_host);
        tabHost.setup();

        TabHost.TabSpec tabSpec = tabHost.newTabSpec("tag1");
        tabSpec.setContent(R.id.tab_africa);
        tabSpec.setIndicator("",image_Africa.getDrawable());
        tabHost.addTab(tabSpec);

        tabSpec = tabHost.newTabSpec("tag2");
        tabSpec.setContent(R.id.tab_americas);
        tabSpec.setIndicator("",image_Americas.getDrawable());
        tabHost.addTab(tabSpec);

        tabSpec = tabHost.newTabSpec("tag3");
        tabSpec.setContent(R.id.tab_asia);
        tabSpec.setIndicator("",image_Asia.getDrawable());
        tabHost.addTab(tabSpec);

        tabSpec = tabHost.newTabSpec("tag2");
        tabSpec.setContent(R.id.tab_europe);
        tabSpec.setIndicator("",image_Europe.getDrawable());
        tabHost.addTab(tabSpec);

        tabSpec = tabHost.newTabSpec("tag3");
        tabSpec.setContent(R.id.tab_oceania);
        tabSpec.setIndicator("",image_Oceania.getDrawable());
        tabHost.addTab(tabSpec);

        tabHost.setCurrentTab(0);

        image_Africa.setOnClickListener(this);
        image_Americas.setOnClickListener(this);
        image_Asia.setOnClickListener(this);
        image_Europe.setOnClickListener(this);
        image_Oceania.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        text_coonect.setVisibility(View.VISIBLE);
        intent = new Intent(this, CountryActivity.class);
        switch (v.getId()) {
            case R.id.image_africa: {
                image_Africa.setVisibility(View.INVISIBLE);
                text_africa.setVisibility(View.INVISIBLE);
                region = "Africa";
                intent.putExtra("region", region);
                startActivity(intent);
                break;
            }
            case R.id.image_americas: {
                image_Americas.setVisibility(View.INVISIBLE);
                text_americas.setVisibility(View.INVISIBLE);
                region = "Americas";
                intent.putExtra("region", region);
                startActivity(intent);
                break;
            }
            case R.id.image_asia: {
                image_Asia.setVisibility(View.INVISIBLE);
                text_asia.setVisibility(View.INVISIBLE);
                region = "Asia";
                intent = new Intent(this, CountryActivity.class);
                intent.putExtra("region", region);
                startActivity(intent);
                break;
            }
            case R.id.image_europe: {
                image_Europe.setVisibility(View.INVISIBLE);
                text_europe.setVisibility(View.INVISIBLE);
                region = "Europe";
                intent.putExtra("region", region);
                startActivity(intent);
                break;
            }
            case R.id.image_oceania: {
                image_Oceania.setVisibility(View.INVISIBLE);
                text_oceania.setVisibility(View.INVISIBLE);
                region = "Oceania";
                intent.putExtra("region", region);
                startActivity(intent);
                break;
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem search = menu.findItem(R.id.app_bar_search);
        searchView = (SearchView) MenuItemCompat.getActionView(search);
        searchView.setOnQueryTextListener(this);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        text_coonect.setVisibility(View.VISIBLE);
        int id = item.getItemId();
        switch (id) {
            case R.id.country_map: {
                intent = new Intent(this, GoogleMapsActivity.class);
                startActivity(intent);
                image_Africa.setVisibility(View.INVISIBLE);
                text_africa.setVisibility(View.INVISIBLE);

                image_Americas.setVisibility(View.INVISIBLE);
                text_americas.setVisibility(View.INVISIBLE);

                image_Americas.setVisibility(View.INVISIBLE);
                text_americas.setVisibility(View.INVISIBLE);

                image_Asia.setVisibility(View.INVISIBLE);
                text_asia.setVisibility(View.INVISIBLE);

                image_Europe.setVisibility(View.INVISIBLE);
                text_europe.setVisibility(View.INVISIBLE);

                image_Oceania.setVisibility(View.INVISIBLE);
                text_oceania.setVisibility(View.INVISIBLE);
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        image_Africa.setVisibility(View.VISIBLE);
        image_Americas.setVisibility(View.VISIBLE);
        image_Asia.setVisibility(View.VISIBLE);
        image_Europe.setVisibility(View.VISIBLE);
        image_Oceania.setVisibility(View.VISIBLE);

        text_africa.setVisibility(View.VISIBLE);
        text_americas.setVisibility(View.VISIBLE);
        text_asia.setVisibility(View.VISIBLE);
        text_europe.setVisibility(View.VISIBLE);
        text_oceania.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        Log.d("myLog", query);
        intent = new Intent(this, CountryActivity.class);
        intent.putExtra("search", query);
        startActivity(intent);
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }
}
