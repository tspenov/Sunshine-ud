package com.example.ceci.sunshine;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
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
import com.example.ceci.sunshine.FetchWeatherTask;


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
    public void onStart(){
        super.onStart();
        updateWeather();
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
            //Toast.makeText(getActivity(), "Settings selected", Toast.LENGTH_SHORT)
            //       .show();

            Intent i = new Intent(getActivity(), SettingsActivity.class);
            startActivity(i);

        }else if(id == R.id.action_refresh) {

            updateWeather();

            Toast.makeText(getActivity(), "Refresh selected", Toast.LENGTH_SHORT)
                    .show();

        }else if(id == R.id.action_view_map) {
            showMap();
        }

        return super.onOptionsItemSelected(item);
    }


    public void showMap() {

        SharedPreferences settings
                = PreferenceManager.getDefaultSharedPreferences(getActivity());

        //location ny city name
        String city = settings.getString("city", "London");
        Uri geoLocation = Uri.parse("geo:0,0?q=" + city);


        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(geoLocation);
        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    private void updateWeather() {
        SharedPreferences settings
                = PreferenceManager.getDefaultSharedPreferences(getActivity());

        //location ny city name
        String city = settings.getString("city", "London");
        FetchWeatherTask fw = new FetchWeatherTask(getActivity(), forecastAdapter);


        //units: metric or imperial
        //Resources res = getResources();
        //String[] unitsArr = res.getStringArray(R.array.units);

        String units = settings.getString("units", "metric");
        String location = settings.getString(getString(R.string.pref_location_key),
                getString(R.string.pref_location_default));
        fw.execute(location);
        //String units = unitsArr[unitsIdx];

    }


}
