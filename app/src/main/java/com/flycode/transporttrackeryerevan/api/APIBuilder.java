package com.flycode.transporttrackeryerevan.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created on 10/18/16 __ Schumakher .
 */

public class APIBuilder {
    private static final String BASE_URL = "http://192.168.0.111:9000/";
    //private static final String BASE_URL = "http://37.252.84.241:3000/";
    private static Retrofit retrofit = null;

    public static Retrofit getClient() {
        if(retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
