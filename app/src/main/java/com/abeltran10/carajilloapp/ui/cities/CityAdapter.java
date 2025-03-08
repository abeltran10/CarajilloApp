package com.abeltran10.carajilloapp.ui.cities;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.abeltran10.carajilloapp.R;
import com.abeltran10.carajilloapp.data.model.Bar;
import com.abeltran10.carajilloapp.data.model.City;
import com.abeltran10.carajilloapp.data.repo.BarRepository;
import com.abeltran10.carajilloapp.data.repo.CitiesRepository;
import com.abeltran10.carajilloapp.ui.main.MainAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CityAdapter extends FirestoreRecyclerAdapter<City, CityAdapter.ViewHolder> {
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    private BarRepository barRepository = BarRepository.getInstance();

    private CityAdapter.OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(City city);
    }

    public CityAdapter(@NonNull FirestoreRecyclerOptions<City> options, CityAdapter.OnItemClickListener listener) {
        super(options);
        this.listener = listener;
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

    @SuppressLint("SetTextI18n")
    @Override
    protected void onBindViewHolder(@NonNull CityAdapter.ViewHolder viewHolder, int position, @NonNull City city) {
        viewHolder.getCityName().setText(city.getName());

        executorService.execute(() -> {
            try {
                long totalBars =  barRepository.totalBars(city.getId());
                viewHolder.getTotalBars().setText(totalBars + " " + "bars");
            } catch (IOException e) {
                viewHolder.getTotalBars().setText(e.getMessage());
            }
        });

        viewHolder.itemView.setOnClickListener(view -> listener.onItemClick(city));
    }


}
