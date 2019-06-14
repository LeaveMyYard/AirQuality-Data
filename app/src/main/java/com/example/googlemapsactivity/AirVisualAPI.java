package com.example.googlemapsactivity;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

interface API_Interface {
    @GET("countries")
    Call<API_AllCountriesRequest> getAllCountries(@Query("key") String key);

    @GET("states")
    Call<API_StatesRequest> getAllStates(@Query("country") String country, @Query("key") String key);

    @GET("cities")
    Call<API_CitiesRequest> getAllCities(@Query("state") String state, @Query("country")String country, @Query("key") String key);

    @GET("city")
    Call<API_DataRequest> getCityData(@Query("city") String city, @Query("state") String state, @Query("country")String country, @Query("key") String key);
}

class API_AllCountriesRequest {
    String status;
    List<Country> data;

    class Country {
        String country;
    }
}

class API_StatesRequest{
    String status;
    List<State> data;

    class State {
        String state;
    }
}

class API_CitiesRequest{
    String status;
    List<City> data;

    class City {
        String city;
    }
}

class API_DataRequest{
    public String status;
    public Data data;

    class Data {
        public String city;
        public String state;
        public String country;

        public Location location;
        public Current current;

        class Location{
            public String type;
            public List<String> coordinates;
        }

        class Current{
            public Weather weather;
            public Pollution pollution;

            class Weather{
                public String ts;
                public String tp;
                public String pr;
                public String hu;
                public String ws;
                public String wd;
                public String ic;
            }

            class Pollution{
                public String ts;
                public String aqius;
                public String mainus;
                public String aqicn;
                public String maincn;
            }
        }
    }
}