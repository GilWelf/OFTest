package com.welf.oftest.core.rest;

import android.content.Context;
import android.util.Pair;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.welf.oftest.BuildConfig;
import com.welf.oftest.R;
import com.welf.oftest.core.rest.exception.CustomException;
import com.welf.oftest.core.rest.interfaces.FoursquareInterface;
import com.welf.oftest.core.rest.interfaces.GeocodeInterface;
import com.welf.oftest.model.Venue;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableTransformer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import retrofit2.HttpException;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * IOManager is the singleton that manages interfaces and API calls
 */
@EBean(scope = EBean.Scope.Singleton)
public class IOManager {

    private static final String FOURSQUARE_CODE = "code";
    private static final String FOURSQUARE_GROUPS = "groups";
    private static final String FOURSQUARE_ITEMS = "items";
    private static final String FOURSQUARE_VENUE = "venue";
    private static final String FOURSQUARE_LOCATION = "location";
    private static final String FOURSQUARE_NAME = "name";
    private static final String FOURSQUARE_CATEGORIES = "categories";
    private static final String FOURSQUARE_ADDRESS = "formattedAddress";

    @RootContext
    Context context;

    private GeocodeInterface geocodeInterface;
    private FoursquareInterface foursquareInterface;

    @AfterInject
    void afterInject(){

        //when IOManager is initialized we prepare all interfaces to be used
        geocodeInterface = prepareInterface(GeocodeInterface.class, BuildConfig.locationIQ_baseURL);
        foursquareInterface = prepareInterface(FoursquareInterface.class, BuildConfig.foursquare_baseURL);

    }

    // This method is used to initialise API interfaces
    private <E> E prepareInterface(Class<E> restInterfaceClass, String baseUrl){
        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(getStandardHttpClient())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(restInterfaceClass);
    }

    // Create a new OkHttpClient
    private OkHttpClient getStandardHttpClient(){
        return new OkHttpClient.Builder()
                .connectTimeout(20, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .writeTimeout(20, TimeUnit.SECONDS)
                .build();

    }

    //This ObsTransformer check if the response is successfull. If not throw an error.
    private <T> ObservableTransformer<T, T> responseTransformer(){
        return observer -> observer.flatMap(response -> {
            if (((Response) response).isSuccessful() && ((Response) response).body() != null){
                return observer;
            }else{
                return Observable.error(new HttpException((Response<?>) response));
            }
        });
    }

    //This method retrieve latitude and longitude of the address provided by the user
    public Observable<Pair<Double, Double>> getPosition(String address){
        return geocodeInterface.getPositionForAddress(BuildConfig.locationIQ_geocode_api, address, "json")
                .flatMap(geocodeDTOS -> {

                    //If multiple addresses are retrieved, we throw an Exception asking to user to specify this address
                    if (geocodeDTOS.size() > 1){
                        return Observable.error(new CustomException(context.getString(R.string.precision_exception_message)));
                    } else{
                        return Observable.just(geocodeDTOS.get(0));
                    }

                })
                .flatMap(geocodeDTO -> {
                    //We convert GeocodeDTO to Pair<Double, Double>
                    return Observable.just( new Pair<>( Double.parseDouble(geocodeDTO.getLat()), Double.parseDouble(geocodeDTO.getLon()) ) );
                });
    }

    public Observable<Venue> getVenues(String latlon){
        return foursquareInterface.getVenues(BuildConfig.foursquare_client_id, BuildConfig.foursquare_client_secret, "20190425", latlon, "restaurants", "10")
                .compose(responseTransformer())
                .flatMap(resultDTOResponse -> {
                    //We check if the response code is a success code. If if it is, we provide the venues list else we throw a custom error
                    JsonObject metaJSON = resultDTOResponse.body().getMeta();
                    JsonObject responseJSON = resultDTOResponse.body().getResponse();
                    int metaCode = metaJSON.get(FOURSQUARE_CODE).getAsInt();
                    if (metaCode >= 200 && metaCode < 300){
                        return Observable.fromIterable(responseJSON.get(FOURSQUARE_GROUPS).getAsJsonArray().get(0).getAsJsonObject().get(FOURSQUARE_ITEMS).getAsJsonArray());
                    }else{
                        return Observable.error(new CustomException(context.getString(R.string.server_error)));
                    }

                })
                .flatMap(jsonElement -> {

                    //We provide and format the data

                    //name
                    String name = jsonElement.getAsJsonObject().get(FOURSQUARE_VENUE).getAsJsonObject().get(FOURSQUARE_NAME).getAsString();

                    //addresss
                    String formattedAddress;
                    StringBuilder sb = new StringBuilder();
                    JsonArray formattedAddressJsonArray = jsonElement.getAsJsonObject().get(FOURSQUARE_VENUE).getAsJsonObject().get(FOURSQUARE_LOCATION).getAsJsonObject().get(FOURSQUARE_ADDRESS).getAsJsonArray();
                    for (int i = 0; i < formattedAddressJsonArray.size(); i++) {
                        sb.append(formattedAddressJsonArray.get(i).getAsString());
                    }
                    formattedAddress = sb.toString();

                    //categories
                    String category;
                    sb = new StringBuilder();
                    JsonArray categoriesJsonArray = jsonElement.getAsJsonObject().get(FOURSQUARE_VENUE).getAsJsonObject().get(FOURSQUARE_CATEGORIES).getAsJsonArray();
                    for (int i = 0; i < categoriesJsonArray.size(); i++) {
                        sb.append(categoriesJsonArray.get(i).getAsJsonObject().get(FOURSQUARE_NAME).getAsString());
                        sb.append(" ");
                    }
                    category = sb.toString();

                    return Observable.just(new Venue(name, formattedAddress, category));

                });
    }


}
