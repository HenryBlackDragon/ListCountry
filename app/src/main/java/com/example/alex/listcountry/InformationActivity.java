package com.example.alex.listcountry;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.alex.listcountry.Help_class.Json;
import com.example.alex.listcountry.Help_class.MyTask;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class InformationActivity extends AppCompatActivity  implements SearchView.OnQueryTextListener {

    private TextView textView;
    private String info, result, fromMap;
    private String FILENAME, fullInfo = "";
    private Button btn_search;
    private SearchView searchView;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);
        Intent intent = getIntent();
        info = intent.getStringExtra("info");
        fromMap = intent.getStringExtra("fromMap");
        textView = (TextView) findViewById(R.id.text_country);
        textView.setMovementMethod(new ScrollingMovementMethod());
        btn_search = (Button) findViewById(R.id.btn_retry);
        try {
            String[] nameCountry = null;

            if (fromMap == null) {
                nameCountry = info.split("\\[");
                FILENAME = nameCountry[0];
            } else {
                FILENAME = fromMap;
            }
            boolean connection = new TestTask().execute().get();
            if (!openFile(FILENAME) && !connection) {
                textView.setText("Please turn on Wi-Fi or Mobile data,\nthen try again.");
                btn_search.setVisibility(View.VISIBLE);
                btn_search.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        recreate();
                    }
                });
            } else if (!openFile(FILENAME) && connection) {
                String name;
                if (fromMap == null) {
                    name = nameCountry[0].replace(" ", "%20");
                } else {
                    name = fromMap.replace(" ", "%20");
                }
                MyTask myTask = new MyTask("name/" + name + "?fullText=true");
                myTask.execute();
                try {
                    result = myTask.get();
                    writeFile(myTask.get());
                    Json json = new Json(result);
                    ArrayList<String> zzz = json.mapInfo();
                    for (String pair : zzz) {
                        fullInfo += pair + ";\n";
                    }
                    textView.setText(fullInfo);
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
            } else {
                openFile(FILENAME);
                //  textView.setText(result);
                Json json = new Json(result);
                ArrayList<String> zzz = json.mapInfo();
                for (String pair : zzz) {
                    fullInfo += pair + ";\n";
                }
                textView.setText(fullInfo);
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    private void writeFile(String resultAPI) {
        try {
            BufferedWriter bfw = new BufferedWriter(new OutputStreamWriter(openFileOutput(FILENAME, MODE_PRIVATE)));
            bfw.write(resultAPI);
            bfw.close();
            Log.d("myLog", "File write!");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean openFile(String fileName) {
        try {
            InputStream inputStream = openFileInput(fileName);

            if (inputStream != null) {
                InputStreamReader isr = new InputStreamReader(inputStream);
                BufferedReader reader = new BufferedReader(isr);
                String line;
                StringBuilder builder = new StringBuilder();

                while ((line = reader.readLine()) != null) {
                    builder.append(line + "\n");
                }
                result = builder.toString();
                inputStream.close();
            }
            return true;
        } catch (Throwable t) {
            return false;
        }
    }


    private class TestTask extends AsyncTask<Void, Void, Boolean> {

        protected Boolean doInBackground(Void... urls) {
            ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            if (activeNetwork != null && activeNetwork.isConnected()) {
                try {
                    URL url = new URL("http://www.google.com/");
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestProperty("User-Agent", "test");
                    httpURLConnection.setRequestProperty("Connection", "close");
                    httpURLConnection.setConnectTimeout(1000);
                    httpURLConnection.connect();
                    if (httpURLConnection.getResponseCode() == 200) {
                        return true;
                    }
                    return false;
                } catch (IOException e) {
                    Log.d("myLog", "Error!", e);
                    return false;
                }
            }
            return false;
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
        int id = item.getItemId();
        switch (id) {
            case R.id.country_map: {
                intent = new Intent(this, GoogleMapsActivity.class);
                startActivity(intent);
                textView.setText(R.string.str_connect);
                btn_search.setVisibility(View.GONE);
                break;
            }
        }
        return super.onOptionsItemSelected(item);
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
