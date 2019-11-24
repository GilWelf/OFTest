package com.welf.oftest.core.rest.exception;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.welf.oftest.R;

import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import io.reactivex.ObservableTransformer;

public class ExceptionUtils {

    /**
     * ObservableTransformer used to display an error dialog if an exception is thrown during an api call
     * @param context context
     * @return {@link ObservableTransformer}
     */
    public static <T> ObservableTransformer<T, T> displayErrorDialog(@NonNull Context context) {
        return tObservable -> tObservable
                .doOnError(throwable -> {
                    if (throwable instanceof CustomException) {
                        new AlertDialog.Builder(context)
                                .setTitle(R.string.common_error)
                                .setMessage(throwable.getMessage())
                                .setPositiveButton(android.R.string.ok, null)
                                .create().show();
                    } else if (throwable instanceof SocketTimeoutException) {
                        new AlertDialog.Builder(context)
                                .setTitle(R.string.timeout_exception_title)
                                .setMessage(R.string.timeout_exception_message)
                                .setPositiveButton(android.R.string.ok, null)
                                .create().show();
                    } else if (throwable instanceof UnknownHostException && !isNetworkAvailable(context)) {
                        new AlertDialog.Builder(context)
                                .setTitle(R.string.common_error)
                                .setMessage(R.string.unknown_host_exception_message)
                                .setPositiveButton(android.R.string.ok, null)
                                .create().show();
                    } else if (throwable instanceof Exception){
                        new AlertDialog.Builder(context)
                                .setTitle(R.string.common_error)
                                .setMessage(throwable.getMessage())
                                .setPositiveButton(android.R.string.ok, null)
                                .create().show();
                    }
                });
    }

    /**
     * Check if the network is available
     * @param context context
     * @return true if network is available and false if not
     */
    private static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

}
