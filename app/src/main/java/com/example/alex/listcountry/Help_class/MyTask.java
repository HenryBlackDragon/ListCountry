package com.example.alex.listcountry.Help_class;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MyTask extends AsyncTask<Void, Void, String> {
    private static final String COUNTRY_URL = "https://restcountries.eu/rest/v2/";
    private String request = "";
    public MyTask(){ }
    public MyTask(String request){
        this.request = request;
    }

    protected String doInBackground(Void... urls) {
        try {
            URL url = new URL(COUNTRY_URL+request);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            try {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader
                        (urlConnection.getInputStream()));
                StringBuilder stringBuilder = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line).append("\n");
                }
                bufferedReader.close();
                return stringBuilder.toString();
            } finally {
                urlConnection.disconnect();
            }
        } catch (Exception e) {
            Log.e("ERROR", e.getMessage(), e);
            return null;
        }
    }

}
