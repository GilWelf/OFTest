package com.welf.oftest.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.google.android.material.textfield.TextInputEditText;
import com.welf.oftest.BuildConfig;
import com.welf.oftest.R;
import com.welf.oftest.core.rest.IOManager;
import com.welf.oftest.core.rest.exception.ExceptionUtils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import io.reactivex.android.schedulers.AndroidSchedulers;

@EActivity(value = R.layout.activity_main)
public class MainActivity extends AppCompatActivity {

    @Bean
    IOManager ioManager;

    @ViewById
    TextInputEditText editTextAddress;
    @ViewById
    Button button;
    @ViewById
    LinearLayout loaderLayout;

    @AfterViews
    void afterViews(){
        loaderLayout.setOnClickListener(view -> {});

        if (BuildConfig.DEBUG){
            editTextAddress.setText("5455 Ave de Gaspé Montréal");
        }
    }

    @Click(R.id.button)
    void onButtonClicked(){

        // Call to geocode api.
        // If the request succeed we start ListActivity with lat and lon values as extra
        // If not, we display an error dialog
        // We also display a Progressbar during the request to inform the user
        ioManager.getPosition(editTextAddress.getText().toString())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(ExceptionUtils.displayErrorDialog(this))
                .doOnSubscribe(disposable -> {
                    button.setClickable(false);
                    loaderLayout.setVisibility(View.VISIBLE);
                    loaderLayout.bringToFront();
                })
                .doOnTerminate(() -> {
                    button.setClickable(true);
                    loaderLayout.setVisibility(View.GONE);
                })
                .subscribe(doubleDoublePair -> {

                    ListActivity_.intent(this)
                            .lat(doubleDoublePair.first)
                            .lon(doubleDoublePair.second)
                            .start();

                }, Throwable::printStackTrace);
    }

}
