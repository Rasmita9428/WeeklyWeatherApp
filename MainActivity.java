package com.androdocs.weatherapp;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.androdocs.httprequest.HttpRequest;
import com.androdocs.weatherapp.Comman_file.CommonKeys;
import com.androdocs.weatherapp.Comman_file.Common_Methods;
import com.androdocs.weatherapp.Comman_file.GPSTracker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    String str_currentlatitude = "";
    String str_currentlongitude = "", City_ID="";
    String API = "02e24deaa9fa3286feaeead84040b350";
    TextView addressTxt, updated_atTxt, statusTxt, tempTxt, temp_minTxt, temp_maxTxt, sunriseTxt,
            sunsetTxt, day1date,day1sunrise,day1sunset,day2date,day2sunrise,day2sunset,day3date,day3sunrise,day3sunset,day4date,day4sunrise,day4sunset,day5date,day5sunrise,day5sunset;
    GPSTracker gpsTracker;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkPermission();
        addressTxt = findViewById(R.id.address);
        updated_atTxt = findViewById(R.id.updated_at);
        statusTxt = findViewById(R.id.status);
        tempTxt = findViewById(R.id.temp);

        temp_minTxt = findViewById(R.id.temp_min);
        temp_maxTxt = findViewById(R.id.temp_max);

        sunriseTxt = findViewById(R.id.sunrise);
        sunsetTxt = findViewById(R.id.sunset);

        day1date = findViewById(R.id.day1date);
        day1sunrise = findViewById(R.id.day1sunrise);
        day1sunset = findViewById(R.id.day1sunset);

        day2date = findViewById(R.id.day2date);
        day2sunrise = findViewById(R.id.day2sunrise);
        day2sunset = findViewById(R.id.day2sunset);

        day3date = findViewById(R.id.day3date);
        day3sunrise = findViewById(R.id.day3sunrise);
        day3sunset = findViewById(R.id.day3sunset);

        day4date = findViewById(R.id.day4date);
        day4sunrise = findViewById(R.id.day4sunrise);
        day4sunset = findViewById(R.id.day4sunset);

        day5date = findViewById(R.id.day5date);
        day5sunrise = findViewById(R.id.day5sunrise);
        day5sunset = findViewById(R.id.day5sunset);

        new weatherTask().execute();
        new weekweatherTask().execute();
    }

    class weatherTask extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            /* Showing the ProgressBar, Making the main design GONE */
            findViewById(R.id.loader).setVisibility(View.VISIBLE);
            findViewById(R.id.mainContainer).setVisibility(View.GONE);
            findViewById(R.id.errorText).setVisibility(View.GONE);
        }

        protected String doInBackground(String... args) {
            String response = HttpRequest.excuteGet("https://api.openweathermap.org/data/2.5/weather?lat="+str_currentlatitude+"&lon="+str_currentlongitude + "&units=metric&appid=" + API);
            return response;
        }

        @Override
        protected void onPostExecute(String result) {


            try {
                JSONObject jsonObj = new JSONObject(result);
                Log.e("daydata", String.valueOf(jsonObj));
                JSONObject main = jsonObj.getJSONObject("main");
                JSONObject sys = jsonObj.getJSONObject("sys");
                JSONObject wind = jsonObj.getJSONObject("wind");
                JSONObject weather = jsonObj.getJSONArray("weather").getJSONObject(0);
                City_ID=weather.getString("id");
                Log.e("City_id",City_ID);
                Long updatedAt = jsonObj.getLong("dt");
                String updatedAtText = "Updated at: " + new SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.ENGLISH).format(new Date(updatedAt * 1000));
                String temp = main.getString("temp") + "°C";
//                String tempMin = "Min Temp: " + main.getString("temp_min") + "°C";
//                String tempMax = "Max Temp: " + main.getString("temp_max") + "°C";

                Long sunrise = sys.getLong("sunrise");
                Long sunset = sys.getLong("sunset");
                String weatherDescription = weather.getString("description");

                String address = jsonObj.getString("name") + ", " + sys.getString("country");


                /* Populating extracted data into our views */
                addressTxt.setText(address);
                updated_atTxt.setText(updatedAtText);
                statusTxt.setText(weatherDescription.toUpperCase());
                tempTxt.setText(temp);
//                temp_minTxt.setText(tempMin);
//                temp_maxTxt.setText(tempMax);
                sunriseTxt.setText(new SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(new Date(sunrise * 1000)));
                sunsetTxt.setText(new SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(new Date(sunset * 1000)));


                /* Views populated, Hiding the loader, Showing the main design */
                findViewById(R.id.loader).setVisibility(View.GONE);
                findViewById(R.id.mainContainer).setVisibility(View.VISIBLE);


            } catch (JSONException e) {
                findViewById(R.id.loader).setVisibility(View.GONE);
                findViewById(R.id.errorText).setVisibility(View.VISIBLE);
            }

        }
    }



    class weekweatherTask extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            /* Showing the ProgressBar, Making the main design GONE */
            findViewById(R.id.loader).setVisibility(View.VISIBLE);
            findViewById(R.id.mainContainer).setVisibility(View.GONE);
            findViewById(R.id.errorText).setVisibility(View.GONE);
        }

        protected String doInBackground(String... args) {
            String response = HttpRequest.excuteGet("https://api.openweathermap.org/data/2.5/forecast?lat="+str_currentlatitude+"&lon="+str_currentlongitude + "&units=metric&appid=" + API);
            return response;

        }

        @Override
        protected void onPostExecute(String result) {


            try {
                JSONObject object=new JSONObject(result);
                JSONArray array=object.getJSONArray("list");
                for(int i=0;i<array.length();i++){
                   JSONObject mainobj= new JSONObject(array.getString(i));
                    JSONObject main = mainobj.getJSONObject("main");

                   if(i==6){
                       Date date1=new Date(System.currentTimeMillis() + (1000 * 60 * 60 * 24));
                       SimpleDateFormat sdf=new SimpleDateFormat("dd,MMM");
                       String date=sdf.format(date1);
                       day1date.setText(date);
                       String tempMin = main.getString("temp_min") + "°C";
                       day1sunrise.setText(tempMin);
                   }
                    if(i==8){
                        String tempMax =  main.getString("temp_max") + "°C";
                        day1sunset.setText(tempMax);
                   }
                    if(i==12){
                        Date date1=new Date(System.currentTimeMillis() + (1000 * 60 * 60 * 48));
                        SimpleDateFormat sdf=new SimpleDateFormat("dd,MMM");
                        String date=sdf.format(date1);
                        day2date.setText(date);
                        String tempMin = main.getString("temp_min") + "°C";
                        day2sunrise.setText(tempMin);
                    }
                    if(i==14){

                        String tempMax = main.getString("temp_max") + "°C";
                        day2sunset.setText(tempMax);
                    }
                    if(i==18){
                        Date date1=new Date(System.currentTimeMillis() + (1000 * 60 * 60 * 72));
                        SimpleDateFormat sdf=new SimpleDateFormat("dd,MMM");
                        String date=sdf.format(date1);
                        day3date.setText(date);

                        String tempMin = main.getString("temp_min") + "°C";
                        day3sunrise.setText(tempMin);
                    }
                    if(i==20){

                        String tempMax = main.getString("temp_max") + "°C";
                        day3sunset.setText(tempMax);
                    }
                    if(i==24){
                        Date date1=new Date(System.currentTimeMillis() + (1000 * 60 * 60 * 96));
                        SimpleDateFormat sdf=new SimpleDateFormat("dd,MMM");
                        String date=sdf.format(date1);
                        day4date.setText(date);
                        String tempMin = main.getString("temp_min") + "°C";
                        day4sunrise.setText(tempMin);
                    }
                    if(i==26){
                        String tempMax = main.getString("temp_max") + "°C";
                        day4sunset.setText(tempMax);
                    }
                    if(i==28){
                        Date date1=new Date(System.currentTimeMillis() + (1000 * 60 * 60 * 96));
                        SimpleDateFormat sdf=new SimpleDateFormat("dd,MMM");
                        String date=sdf.format(date1);
                        day5date.setText(date);
                        String tempMin = main.getString("temp_min") + "°C";
                        day5sunrise.setText(tempMin);
                    }
                    if(i==30){
                        String tempMax = main.getString("temp_max") + "°C";
                        day5sunset.setText(tempMax);
                    }
                }


                /* Views populated, Hiding the loader, Showing the main design */
                findViewById(R.id.loader).setVisibility(View.GONE);
                findViewById(R.id.mainContainer).setVisibility(View.VISIBLE);


            } catch (JSONException e) {
                findViewById(R.id.loader).setVisibility(View.GONE);
                findViewById(R.id.errorText).setVisibility(View.VISIBLE);
            }

        }
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(
                this,
                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
                CommonKeys.PERMISSION_CODE
        );
    }

    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
                LocationManager.NETWORK_PROVIDER
        );
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CommonKeys.PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                new weatherTask().execute();
                new weekweatherTask().execute();
            }
        }
    }
    public void checkPermission() {
        gpsTracker = new GPSTracker(MainActivity.this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            String[] permissions = new String[]{Manifest.permission.ACCESS_COARSE_LOCATION};
            if (Common_Methods.isPermissionNotGranted(MainActivity.this, permissions)) {
                requestPermissions();
                new weatherTask().execute();
                new weekweatherTask().execute();
                return;
            } else {
                str_currentlatitude = String.valueOf(gpsTracker.getLatitude());
                str_currentlongitude = String.valueOf(gpsTracker.getLongitude());
                 Toast.makeText(MainActivity.this, str_currentlatitude + str_currentlongitude, Toast.LENGTH_SHORT).show();
            }
        } else
            str_currentlatitude = String.valueOf(gpsTracker.getLatitude());
           str_currentlongitude = String.valueOf(gpsTracker.getLongitude());
        Log.e("Latlong",str_currentlatitude + str_currentlongitude);
    }
}
