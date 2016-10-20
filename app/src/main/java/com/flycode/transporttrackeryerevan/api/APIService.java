package com.flycode.transporttrackeryerevan.api;

import com.flycode.transporttrackeryerevan.response.BusesListResponse;
import com.google.android.gms.maps.model.LatLng;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created on 10/18/16 __ Schumakher .
 */

public interface APIService {
    @GET("/api/points")
    Call<BusesListResponse>getBuses();
}
