package com.example.alex.listcountry;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.example.alex.listcountry.Help_class.Json;
import com.example.alex.listcountry.Help_class.MyTask;
import com.example.alex.listcountry.Help_class.SomeAdapter;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutionException;

public class CountryActivity extends ListActivity {

    private Intent intent;
    private String result, region, search;
    private final String FILENAME = "names_capitals_region";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        intent = getIntent();
        region = intent.getStringExtra("region");
        search = intent.getStringExtra("search");
        try {
            String[] names, namesCapitals;
            boolean connection = new TestTask().execute().get();
            if (!openFile(FILENAME) && !connection) {
                startActivity(new Intent(this, MainActivity.class));
                Toast.makeText(getApplicationContext(),
                        "Please turn on Wi-Fi or Mobile data ", Toast.LENGTH_LONG).show();
            } else if (!openFile(FILENAME) && connection) {
                MyTask myTask = new MyTask("?fields=name;capital;region");
                myTask.execute();
                try {
                    result = myTask.get();
                    writeFile(myTask.get());
                    Json json = new Json(result);
                    if (search == null) {
                        names = json.getNames(region);
                        namesCapitals = json.getNamesCapitals(region);
                    } else {
                        names = json.searchNames(search);
                        namesCapitals = json.searchCountry(search);
                    }
                    SomeAdapter someAdapter = new SomeAdapter(this, namesCapitals, names);
                    setListAdapter(someAdapter);
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
            } else {
                openFile(FILENAME);
                Json json = new Json(result);
                if (search == null) {
                    names = json.getNames(region);
                    namesCapitals = json.getNamesCapitals(region);
                } else {
                    names = json.searchNames(search);
                    namesCapitals = json.searchCountry(search);
                }
                SomeAdapter someAdapter = new SomeAdapter(this, namesCapitals, names);
                setListAdapter(someAdapter);
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        intent = new Intent(this, InformationActivity.class);
        intent.putExtra("info", l.getItemAtPosition(position).toString());
        startActivity(intent);
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
}