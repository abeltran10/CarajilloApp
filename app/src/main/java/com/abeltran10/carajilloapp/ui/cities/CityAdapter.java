package com.abeltran10.carajilloapp.ui.cities;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.abeltran10.carajilloapp.R;
import com.abeltran10.carajilloapp.data.model.City;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.Query;

public class CityAdapter extends FirestoreRecyclerAdapter<City, CityAdapter.ViewHolder> {
    private CityAdapter.OnItemClickListener onItemClickListener;

    private CityAdapter.OnBindTotalBars onBindTotalBars;

    public interface OnItemClickListener {
        void onItemClick(City city);
    }

    public interface OnBindTotalBars {
        void setTotalBars(CityAdapter.ViewHolder viewHolder, City city);
    }

    public CityAdapter(@NonNull FirestoreRecyclerOptions<City> options, CityAdapter.OnBindTotalBars onBindTotalBars) {
        super(options);
        this.onBindTotalBars = onBindTotalBars;
        setHasStableIds(true);
    }

    public CityAdapter(@NonNull FirestoreRecyclerOptions<City> options) {
        super(options);
        setHasStableIds(true);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setOnBindTotalBars(OnBindTotalBars onBindTotalBars) {
        this.onBindTotalBars = onBindTotalBars;
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView cityName;

        private final TextView totalBars;

        public ViewHolder(View view) {
            super(view);

            cityName = (TextView) view.findViewById(R.id.city_name);
            totalBars = (TextView) view.findViewById(R.id.total_bars);
        }

        public TextView getCityName() {
            return cityName;
        }

        public TextView getTotalBars() {
            return totalBars;
        }

    }

    @NonNull
    @Override
    public CityAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.list_cities, viewGroup, false);

        return new CityAdapter.ViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull CityAdapter.ViewHolder viewHolder, int position, @NonNull City city) {
        if (position >= getItemCount())
            return;

        viewHolder.getCityName().setText(city.getName());

        if (onBindTotalBars != null) {
            onBindTotalBars.setTotalBars(viewHolder, city);
        }

        if (onItemClickListener != null) {
            viewHolder.itemView.setOnClickListener(view -> onItemClickListener.onItemClick(city));
        }
    }


}
