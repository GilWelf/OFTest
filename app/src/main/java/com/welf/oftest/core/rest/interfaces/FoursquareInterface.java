package com.welf.oftest.core.rest.interfaces;

import com.welf.oftest.core.rest.dto.foursquare.ResultDTO;

import io.reactivex.Observable;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Foursquare Interface API
 */
public interface FoursquareInterface {

    @GET("venues/explore")
    Observable<Response<ResultDTO>> getVenues(
            @Query("client_id") String client_id,
            @Query("client_secret") String client_secret,
            @Query("v") String v,
            @Query("ll") String ll,
            @Query("query") String query,
            @Query("limit") String limit
    );

}
