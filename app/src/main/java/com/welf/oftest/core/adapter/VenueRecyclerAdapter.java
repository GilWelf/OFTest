package com.welf.oftest.core.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.welf.oftest.R;
import com.welf.oftest.model.Venue;

import java.util.ArrayList;
import java.util.List;

public class VenueRecyclerAdapter extends RecyclerView.Adapter<VenueRecyclerAdapter.VenueViewHolder> {

    private final List<Venue> data;
    private final LayoutInflater inflater;
    private OnVenueSelectedListener onVenueSelectedListener;

    public VenueRecyclerAdapter(ArrayList<Venue> data, Context context) {
        if (data == null) {
            this.data = new ArrayList<>();
        } else {
            this.data = new ArrayList<>(data);
        }
        this.inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public VenueViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new VenueViewHolder(inflater.inflate(R.layout.venue_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull VenueViewHolder holder, int position) {
        holder.onBind(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public interface OnVenueSelectedListener{
        void onVenueSelected(Venue venue);
    }

    public void setOnVenueSelectedListener(OnVenueSelectedListener onVenueSelectedListener) {
        this.onVenueSelectedListener = onVenueSelectedListener;
    }

    public void replaceAll(List<Venue> venues){
        this.data.clear();
        this.data.addAll(venues);
        notifyDataSetChanged();
    }

    class VenueViewHolder extends RecyclerView.ViewHolder{

        private final TextView nameTextView;

        VenueViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
        }

        void onBind(Venue venue){

            itemView.setOnClickListener(view -> {
                if (onVenueSelectedListener != null){
                    onVenueSelectedListener.onVenueSelected(venue);
                }
            });

            nameTextView.setText(venue.getName());

        }
    }

}
