package com.example.ceci.sunshine;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by ceci on 11/17/15.
 */
public class WeatherAPI {

    private String LOG_TAG = WeatherAPI.class.getSimpleName();

    public void setLogTag(String LT) {
        this.LOG_TAG = LT;
        return;
    }

    public String fetchData(String fetchUrl){

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String weatherJson = null;

        try {
            URL url = new URL(fetchUrl);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();

            if (inputStream == null) {

                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while( (line = reader.readLine()) != null) {

                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                return null;

            }
            weatherJson = buffer.toString();



        } catch (IOException e) {

            Log.e(this.LOG_TAG, "Error ", e);
            return null;

        } finally {

            if (urlConnection != null) {
                urlConnection.disconnect();
            }

            if (reader != null) {
                try {
                    reader.close();
                }catch(final IOException e){
                    Log.e(this.LOG_TAG, "Error closing stream ", e);
                }

            }
        }


        return weatherJson;
    }

}
