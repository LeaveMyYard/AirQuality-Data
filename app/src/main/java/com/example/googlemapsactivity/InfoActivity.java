package com.example.googlemapsactivity;

import android.graphics.Color;
import android.media.Image;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.w3c.dom.Text;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InfoActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private API_DataRequest cityData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Call<API_DataRequest> call = SelectActivity.api_Interface.getCityData(SelectActivity.city, SelectActivity.state, SelectActivity.country, "TPDGBd4NSTTNxREsb");
        final InfoActivity infoActivity = this;
        call.enqueue(new Callback<API_DataRequest>() {
            @Override
            public void onResponse(Call<API_DataRequest> call, Response<API_DataRequest> response) {
                infoActivity.cityData = response.body();
                TextView textName = findViewById(R.id.CityName);
                String name = infoActivity.cityData.data.city + ", " + infoActivity.cityData.data.state + ", " + infoActivity.cityData.data.country;
                textName.setText(name);

                TextView airDataNum = findViewById(R.id.textAirStatusNum);
                airDataNum.setText(infoActivity.cityData.data.current.pollution.aqius);

                TextView textAirStatus = findViewById(R.id.textAirStatusString);
                textAirStatus.setText("PM2.5 | " + infoActivity.cityData.data.current.pollution.aqicn + " µg/m³");

                infoActivity.refreshMap(Float.valueOf(infoActivity.cityData.data.location.coordinates.get(0)),
                                        Float.valueOf(infoActivity.cityData.data.location.coordinates.get(1)),
                                        name);

                ImageView imagePerson = findViewById(R.id.imagePerson);
                LinearLayout coloredLayout = findViewById(R.id.ColoredLayout);
                TextView typeView = findViewById(R.id.textAirStatus);
                String type = infoActivity.cityData.data.current.pollution.mainus;
                int pol = Integer.valueOf(infoActivity.cityData.data.current.pollution.aqius);
                if (pol < 50){
                    imagePerson.setImageResource(R.drawable.ic_face_1_green);
                    coloredLayout.setBackgroundColor(Color.GREEN);
                    typeView.setText("Healthy");
                }
                else if(pol < 100) {
                    imagePerson.setImageResource(R.drawable.ic_face_2_yellow);
                    coloredLayout.setBackgroundColor(Color.YELLOW);
                    typeView.setText("Moderate");
                }
                else if(pol < 150) {
                    imagePerson.setImageResource(R.drawable.ic_face_3_orange);
                    coloredLayout.setBackgroundColor(Color.rgb(255, 150,00));
                    typeView.setText("Poorly");
                }
                else if(pol < 200) {
                    imagePerson.setImageResource(R.drawable.ic_face_4_red);
                    coloredLayout.setBackgroundColor(Color.RED);
                    typeView.setText("Unhealthy");
                }
                else {
                    imagePerson.setImageResource(R.drawable.ic_face_5_purple);
                    coloredLayout.setBackgroundColor(Color.MAGENTA);
                    typeView.setText("Very Unhealthy");
                }

                TextView wind = findViewById(R.id.windString);
                TextView humidity = findViewById(R.id.humidityString);
                TextView pressure = findViewById(R.id.pressureString);
                TextView temperature = findViewById(R.id.tempString);

                wind.setText(infoActivity.cityData.data.current.weather.ws + " km/h");
                humidity.setText(infoActivity.cityData.data.current.weather.hu + "%");
                pressure.setText(infoActivity.cityData.data.current.weather.pr + " mb");
                temperature.setText(infoActivity.cityData.data.current.weather.tp + "°");

                ImageView image1 = findViewById(R.id.imageView4);
                ImageView image2 = findViewById(R.id.imageView5);
                ImageView image3 = findViewById(R.id.imageView6);

                image1.setImageResource(R.drawable.ic_wind_05_s_72_px_2);
                image2.setImageResource(R.drawable.ic_humidity_72_px_2);
                image3.setImageResource(R.drawable.ic_pressure_72_px_2);

                TextView weather = findViewById(R.id.weatherText);
                ImageView imageView = findViewById(R.id.weatherImage);
                String weatherData = infoActivity.cityData.data.current.weather.ic;
                if(weatherData.equals("01d")){
                    weather.setText("clear sky (day)");
                    imageView.setImageResource(R.drawable.i01d);
                }
                else if(weatherData.equals("01n")){
                    weather.setText("clear sky (night)");
                    imageView.setImageResource(R.drawable.i01n);
                }
                else if(weatherData.equals("02d")) {
                    weather.setText("few clouds (day)");
                    imageView.setImageResource(R.drawable.i02d);
                }
                else if(weatherData.equals("02n")) {
                    weather.setText("few clouds (night)");
                    imageView.setImageResource(R.drawable.i02n);
                }
                else if(weatherData.equals("03d")) {
                    weather.setText("scattered clouds");
                    imageView.setImageResource(R.drawable.i03d);
                }
                else if(weatherData.equals("04d")) {
                    weather.setText("broken clouds");
                    imageView.setImageResource(R.drawable.i04d);
                }
                else if(weatherData.equals("09d")) {
                    weather.setText("shower rain");
                    imageView.setImageResource(R.drawable.i09d);
                }
                else if(weatherData.equals("10d")) {
                    weather.setText("rain (day time)");
                    imageView.setImageResource(R.drawable.i10d);
                }
                else if(weatherData.equals("10n")) {
                    weather.setText("rain (night time)");
                    imageView.setImageResource(R.drawable.i10n);
                }
                else if(weatherData.equals("11d")) {
                    weather.setText("thunderstorm");
                    imageView.setImageResource(R.drawable.i11d);
                }
                else if(weatherData.equals("13d")) {
                    weather.setText("snow");
                    imageView.setImageResource(R.drawable.i13d);
                }
                else {
                    weather.setText("mist");
                    imageView.setImageResource(R.drawable.i50d);
                }
            }

            @Override
            public void onFailure(Call<API_DataRequest> call, Throwable t) {
                TextView textView = findViewById(R.id.CityName);
                textView.setText(t.getMessage());
            }
        });
    }

    public void refreshMap(float lng, float lat, String name) {
        LatLng marker = new LatLng(lat, lng);
        mMap.addMarker(new MarkerOptions().position(marker).title(name));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(marker));
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
    }

}
