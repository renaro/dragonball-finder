package com.jnj.hackthon;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by renarosantos on 09/12/16.
 */

public interface Service {

    // Retrofit REST Definitions. Based on Project API Documentation.
    @GET("/api/control/{character}/{move}/{angle}")
    Call<ControlResponse> control(@Path("character") String character, @Path("move") String move, @Path("angle") String angle);

    // Retrofit REST Definitions. Based on Project API Documentation.
    @GET("/api/radar/{character}")
    Call<RadarResponse> radar(@Path("character") String character);


}
