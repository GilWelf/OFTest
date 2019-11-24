package com.welf.oftest.ui;

import androidx.appcompat.app.AppCompatActivity;

import com.welf.oftest.R;
import com.welf.oftest.databinding.DetailsActivityBinding;
import com.welf.oftest.model.Venue;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.BindingObject;
import org.androidannotations.annotations.DataBound;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;

@DataBound
@EActivity(value = R.layout.details_activity)
public class DetailsActivity extends AppCompatActivity {

    @BindingObject
    DetailsActivityBinding binding;

    @Extra
    Venue venue;

    @AfterViews
    void afterViews(){
        binding.setCurrentVenue(venue);
    }

}
