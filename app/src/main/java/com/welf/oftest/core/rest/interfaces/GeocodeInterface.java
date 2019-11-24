package com.welf.oftest.core.rest.interfaces;

import com.welf.oftest.core.rest.dto.geocode.GeocodeDTO;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * LocationIQ Interface API
 */
public interface GeocodeInterface {

    @GET("search.php")
    Observable<List<GeocodeDTO>> getPositionForAddress(@Query("key") String apiKey, @Query("q") String queryAddress, @Query("format") String format);

}
