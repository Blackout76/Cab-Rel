package com.example.blackout.gne.Network;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by blackout on 15/10/2015.
 */
public class HTTP extends AsyncTask<String, Integer, String> {
    public AsyncResponse delegate=null;

    @Override
    protected void onProgressUpdate(Integer... values){
        super.onProgressUpdate(values);
        // Mise à jour de la ProgressBar
        Log.e("HTTP", "Progress :" + values[0]);
    }

    @Override
    protected String doInBackground(String... urlString) {

        URL url = null;
        HttpURLConnection conn = null;
        try {
            url = new URL(urlString[0]);//"http://172.30.0.184:80/clientConnect"
            conn =(HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            conn.connect();
            InputStream input = conn.getInputStream();
            return readStream(input);
        } catch (IOException e) {
            Log.e("HTTP", e.getMessage());
            return  null;
        }
    }

    @Override
    protected void onPostExecute(String result) {
        delegate.httpResponse(result);
    }

    private String readStream(InputStream in) {
        BufferedReader reader = null;
        StringBuffer response = new StringBuffer();
        try {
            reader = new BufferedReader(new InputStreamReader(in));
            String line = "";
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            Log.e("HTTP", "response:" + response.toString());
            return response.toString();
        } catch (IOException e) {
            Log.e("HTTP", e.getMessage());
            return null;
        }

    }
}