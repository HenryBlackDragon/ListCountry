package com.example.alex.listcountry;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Toast;

import com.example.alex.listcountry.Help_class.Json;
import com.example.alex.listcountry.Help_class.MyTask;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

public class GoogleMapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private GoogleMap mMap;
    private HashMap<String, String> namesLatlng;
    private final String FILENAME = "name_latlng";
    private String result, name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);


        try {
            boolean connection = new GoogleMapsActivity.TestTask().execute().get();
            if (!openFile(FILENAME) && !connection) {
                startActivity(new Intent(this, MainActivity.class));
                Toast.makeText(getApplicationContext(),
                        "Please turn on Wi-Fi or Mobile data ", Toast.LENGTH_LONG).show();
            } else if (!openFile(FILENAME) && connection) {
                MyTask myTask = new MyTask("?fields=name;latlng");
                myTask.execute();
                try {
                    result = myTask.get();
                    writeFile(myTask.get());
                    Json json = new Json(result);
                    namesLatlng = json.namesLatlng();
                    SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                            .findFragmentById(R.id.map);
                    mapFragment.getMapAsync(this);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            } else {
                Log.d("myLog", "File open!");
                Json json = new Json(result);
                namesLatlng = json.namesLatlng();
                SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.map);
                mapFragment.getMapAsync(this);
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        for (HashMap.Entry<String, String> pair : namesLatlng.entrySet()
                ) {
            String str = pair.getValue();
            String[] arr = str.split("[,\\]\\[]");
            mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(Double.parseDouble(arr[1]), Double.parseDouble(arr[2])))
                    .icon(BitmapDescriptorFactory.fromAsset(pair.getKey() + ".png")));
            mMap.setOnMarkerClickListener(this);
        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        LatLng latLng = marker.getPosition();
        String lalLng1 = "[" + latLng.latitude + "," + latLng.longitude + "]";
        for (HashMap.Entry<String, String> pair : namesLatlng.entrySet()
                ) {
            if (pair.getValue().equals(lalLng1)) {
                Intent intent = new Intent(this, InformationActivity.class);
                intent.putExtra("fromMap", pair.getKey());
                startActivity(intent);
            }
        }
        return true;
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
