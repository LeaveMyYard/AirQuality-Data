package com.example.googlemapsactivity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class SelectActivity extends AppCompatActivity {

    static public String country, state, city;
    static public API_Interface api_Interface = null;
    static public API_DataRequest cityData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select);
        final SelectActivity selectActivity = this;
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://api.airvisual.com/v2/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        api_Interface = retrofit.create(API_Interface.class);
        final API_Interface apiInterface = api_Interface;
        Call<API_AllCountriesRequest> call = apiInterface.getAllCountries("TPDGBd4NSTTNxREsb");
        call.enqueue(new Callback<API_AllCountriesRequest>() {
            @Override
            public void onResponse(Call<API_AllCountriesRequest> call, Response<API_AllCountriesRequest> response) {
                API_AllCountriesRequest data = response.body();
                selectActivity.onAllCountriesLoaded(data);
            }

            @Override
            public void onFailure(Call<API_AllCountriesRequest> call, Throwable t) {
                selectActivity.loadingError(t);
            }
        });

        Spinner spinner1 = findViewById(R.id.Country);
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                TextView textView = findViewById(R.id.StateText);
                textView.setVisibility(View.VISIBLE);

                selectActivity.deleteErrorMessage();

                final Spinner spinner = findViewById(R.id.State);
                spinner.setVisibility(View.VISIBLE);

                Spinner spinnerCountry = findViewById(R.id.Country);
                String countryString = spinnerCountry.getSelectedItem().toString();

                Call<API_StatesRequest> call = apiInterface.getAllStates(countryString, "TPDGBd4NSTTNxREsb");
                call.enqueue(new Callback<API_StatesRequest>() {
                    @Override
                    public void onResponse(Call<API_StatesRequest> call, Response<API_StatesRequest> response) {
                        API_StatesRequest allStates = response.body();
                        ArrayList<String> arraySpinner = new ArrayList<String>();

                        if(allStates == null){
                            selectActivity.loadingError("API Error, this country has no states!");
                            return;
                        }

                        for (API_StatesRequest.State c : allStates.data){
                            arraySpinner.add(c.state);
                        }

                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(selectActivity,
                                android.R.layout.simple_spinner_dropdown_item, arraySpinner);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinner.setAdapter(adapter);
                    }

                    @Override
                    public void onFailure(Call<API_StatesRequest> call, Throwable t) {
                        selectActivity.loadingError(t);
                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                TextView textView = findViewById(R.id.StateText);
                textView.setVisibility(View.INVISIBLE);
                Spinner spinner = findViewById(R.id.State);
                spinner.setVisibility(View.INVISIBLE);
            }

        });

        Spinner spinner2 = findViewById(R.id.State);
        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                TextView textView = findViewById(R.id.CityText);
                textView.setVisibility(View.VISIBLE);

                selectActivity.deleteErrorMessage();

                final Spinner spinner = findViewById(R.id.City);
                spinner.setVisibility(View.VISIBLE);

                Spinner spinnerCountry = findViewById(R.id.Country);
                String countryString = spinnerCountry.getSelectedItem().toString();

                Spinner spinnerState = findViewById(R.id.State);
                String stateString = spinnerState.getSelectedItem().toString();

                Call<API_CitiesRequest> call = apiInterface.getAllCities(stateString, countryString, "TPDGBd4NSTTNxREsb");
                call.enqueue(new Callback<API_CitiesRequest>() {
                    @Override
                    public void onResponse(Call<API_CitiesRequest> call, Response<API_CitiesRequest> response) {
                        API_CitiesRequest allStates = response.body();
                        ArrayList<String> arraySpinner = new ArrayList<String>();

                        if(allStates == null){
                            selectActivity.loadingError("API Error, this state has no cities!");
                            return;
                        }

                        for (API_CitiesRequest.City c : allStates.data){
                            arraySpinner.add(c.city);
                        }

                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(selectActivity,
                                android.R.layout.simple_spinner_dropdown_item, arraySpinner);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinner.setAdapter(adapter);
                    }

                    @Override
                    public void onFailure(Call<API_CitiesRequest> call, Throwable t) {
                        selectActivity.loadingError(t);
                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                TextView textView = findViewById(R.id.CityText);
                textView.setVisibility(View.INVISIBLE);
                Spinner spinner = findViewById(R.id.City);
                spinner.setVisibility(View.INVISIBLE);
            }

        });

        Spinner spinner3 = findViewById(R.id.City);
        spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                Button button = findViewById(R.id.GoButton);
                button.setVisibility(View.VISIBLE);
                selectActivity.deleteErrorMessage();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                Button button = findViewById(R.id.GoButton);
                button.setVisibility(View.VISIBLE);
            }
        });
    }

    private void onAllCountriesLoaded(API_AllCountriesRequest allCountries){

        Spinner spinner = findViewById(R.id.Country);
        ArrayList<String> arraySpinner = new ArrayList<String>();

        for (API_AllCountriesRequest.Country c : allCountries.data){
            arraySpinner.add(c.country);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item, arraySpinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

    }

    private void loadingError(Throwable t){
        TextView textView = findViewById(R.id.ErrorText);
        textView.setVisibility(View.VISIBLE);
        textView.setText("Error: " + t.getMessage());
    }

    private void deleteErrorMessage() {
        TextView textView = findViewById(R.id.ErrorText);
        textView.setVisibility(View.INVISIBLE);
    }

    private void loadingError(String t){
        TextView textView = findViewById(R.id.ErrorText);
        textView.setVisibility(View.VISIBLE);
        textView.setText("Error: " + t);
    }

    public void onButtonClick(View view){
        Spinner spinnerCountry = findViewById(R.id.Country);
        Spinner spinnerState = findViewById(R.id.State);
        Spinner spinnerCity = findViewById(R.id.City);
        country = spinnerCountry.getSelectedItem().toString();
        state = spinnerState.getSelectedItem().toString();
        city = spinnerCity.getSelectedItem().toString();

        Intent intent = new Intent(this, InfoActivity.class);
        startActivity(intent);
    }
}
