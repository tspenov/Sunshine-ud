package com.example.ceci.sunshine;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.support.v4.app.Fragment;


import org.json.JSONException;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Created by ceci on 12/8/15.
 */
public class ForecastFragment extends Fragment {

    private ArrayAdapter<String> forecastAdapter;

    public ForecastFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        Log.e("Fragment", "onCreate 1");
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        String[] data = {
                "Today - Sunny - 24",
                "Tuesday - Cloudy - 18",
                "Wednesday - Sunny - 30",
                "Today - Sunny - 24",
                "Tuesday - Cloudy - 18",
                "Wednesday - Sunny - 30",
                "Today - Sunny - 24",
                "Tuesday - Cloudy - 18",
                "Wednesday - Sunny - 30",
                "Today - Sunny - 24",
                "Tuesday - Cloudy - 18",
                "Wednesday - Sunny - 30"
        };

        final List<String> forecastData = new ArrayList<>(
                Arrays.asList(data)
        );


        forecastAdapter = new ArrayAdapter<String>(
                getActivity(),
                R.layout.list_item_forecast,
                R.id.list_item_forecast_textview,
                forecastData
        );


        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        ListView lv = (ListView) rootView.findViewById(R.id.listview_forecast);
        lv.setAdapter(forecastAdapter);


        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                Intent i = new Intent(getActivity(), DetailActivity.class);
                i.putExtra("EXTRA_TEXT", forecastAdapter.getItem(position));
                startActivity(i);

                Toast.makeText(getActivity(),
                        forecastAdapter.getItem(position), Toast.LENGTH_LONG)
                        .show();
            }
        });


        Log.e("Test message", "Alabala error");
        Log.d("TEst message", "alabala debug");

        //String baseUrl = "http://api.openweathermap.org/data/2.5/forecast/daily?q=Sofia&mode=json&units=metric&cnt=7";
        //String apiKey = "&APPID=" + BuildConfig.OPEN_WEATHER_MAP_API_KEY;

        //WeatherAPI wa = new WeatherAPI();
        //wa.fetchData(baseUrl);

        return rootView;
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        Log.e("Fragment", "onCreateOptionsMenu");
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.forecastfragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Toast.makeText(getActivity(), "Settings selected", Toast.LENGTH_SHORT)
                    .show();
        }else if(id == R.id.action_refresh) {

            FetchWeatherTask fw = new FetchWeatherTask();
            fw.execute("Sofia");

            Toast.makeText(getActivity(), "Refresh selected", Toast.LENGTH_SHORT)
                    .show();

        }

        return super.onOptionsItemSelected(item);
    }


    public class FetchWeatherTask extends AsyncTask<String, Void, String[]> {

        private final String LOG_TAG = FetchWeatherTask.class.getSimpleName();

        @Override
        protected String[] doInBackground(String... params) {

            if (params.length == 0) {
                return null;
            }

            String format = "json";
            String units = "metric";
            int numDays = 7;

            final String FORECAST_BASE_URL = "http://api.openweathermap.org/data/2.5/forecast/daily?";
            final String QUERY_PARAM = "q";
            final String FORMAT_PARAM = "mode";
            final String UNITS_PARAM = "units";
            final String DAYS_PARAM = "cnt";
            final String APPID_PARAM = "APPID";



            Uri builtUri = Uri.parse(FORECAST_BASE_URL).buildUpon()
                        .appendQueryParameter(QUERY_PARAM, params[0])
                        .appendQueryParameter(FORMAT_PARAM, format)
                        .appendQueryParameter(UNITS_PARAM, units)
                        .appendQueryParameter(DAYS_PARAM, Integer.toString(numDays))
                        .appendQueryParameter(APPID_PARAM, BuildConfig.OPEN_WEATHER_MAP_API_KEY)
                        .build();

            Log.d(LOG_TAG, "Built URI " + builtUri.toString());




            WeatherAPI wa = new WeatherAPI();
            String jsonData = wa.fetchData(builtUri.toString());

            ParseWeatherData parser = new ParseWeatherData();
            String[] result  = new String[0];
            try {
                result = parser.getWeatherDataFromJson(jsonData, numDays);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return result;
        }

        @Override
        protected void onPostExecute(String[] strings) {
            if (strings != null) {
                forecastAdapter.clear();

                for (String str : strings) {
                    forecastAdapter.add(str);
                }

            }
        }


    }
}
