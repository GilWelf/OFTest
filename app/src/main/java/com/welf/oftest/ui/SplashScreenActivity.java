package com.welf.oftest.ui;

import androidx.appcompat.app.AppCompatActivity;

import org.androidannotations.annotations.EActivity;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;

@EActivity
public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onResume() {
        super.onResume();

        //We wait 1.5 seconds before starting the MainActivity.
        Observable.timer(1500, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnTerminate(this::finish)
                .subscribe(aLong -> {
                    MainActivity_.intent(this).start();
                });

    }
}
