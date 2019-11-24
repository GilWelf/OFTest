package com.welf.oftest.ui;

import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.welf.oftest.R;
import com.welf.oftest.core.adapter.VenueRecyclerAdapter;
import com.welf.oftest.core.rest.IOManager;
import com.welf.oftest.core.rest.exception.ExceptionUtils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


@EActivity(R.layout.list_activity)
public class ListActivity extends AppCompatActivity {

    @Bean
    IOManager ioManager;

    @Extra
    double lat;
    @Extra
    double lon;

    @ViewById
    RecyclerView VenueRecyclerView;
    @ViewById
    LinearLayout loaderLayout;

    private VenueRecyclerAdapter adapter;

    @AfterViews
    void afterViews(){

        VenueRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        adapter = new VenueRecyclerAdapter(new ArrayList<>(), this);
        adapter.setOnVenueSelectedListener(venue -> {
            DetailsActivity_.intent(this).venue(venue).start();
        });
        VenueRecyclerView.setAdapter(adapter);


        loadVenues();
    }

    //request venues from api and load it in adapter
    void loadVenues(){

        String latLon = lat + "," + lon;

        ioManager.getVenues(latLon)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(ExceptionUtils.displayErrorDialog(this))
                .doOnSubscribe(disposable -> {
                    loaderLayout.setVisibility(View.VISIBLE);
                    loaderLayout.bringToFront();
                })
                .doOnTerminate(() -> {
                    loaderLayout.setVisibility(View.GONE);
                })
                .toList()
                .subscribe(venues -> adapter.replaceAll(venues), Throwable::printStackTrace);

    }




}
