package com.abeltran10.carajilloapp.ui.main;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.abeltran10.carajilloapp.R;
import com.abeltran10.carajilloapp.data.model.Bar;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class MainAdapter extends FirestoreRecyclerAdapter<Bar, MainAdapter.ViewHolder> {
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(String name, String location, Double rating);
    }

    public MainAdapter(@NonNull FirestoreRecyclerOptions<Bar> options, OnItemClickListener listener) {
        super(options);
        this.listener = listener;
    }


    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder)
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView restaurantName;

        private final TextView restaurantLocation;

        private final RatingBar rating;

        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View

            restaurantName = (TextView) view.findViewById(R.id.restaurant_name);
            restaurantLocation = (TextView) view.findViewById(R.id.restaurant_location);
            rating = (RatingBar) view.findViewById(R.id.ratingBar);
        }

        public TextView getRestaurantName() {
            return restaurantName;
        }

        public TextView getRestaurantLocation() {
            return restaurantLocation;
        }

        public RatingBar getRating() {
            return rating;
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.list_row, viewGroup, false);

        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onBindViewHolder(@NonNull ViewHolder viewHolder, int position, @NonNull Bar bar) {
        viewHolder.getRestaurantName().setText(bar.getName().toUpperCase());
        String location = bar.getCity().toUpperCase() + " - " + bar.getAddress().toUpperCase() + " (" + bar.getPostalCode() + ")";
        viewHolder.getRestaurantLocation().setText(location);
        viewHolder.getRating().setRating(bar.getRating().floatValue());

        viewHolder.itemView.setOnClickListener(view -> listener.onItemClick(bar.getName(), location, bar.getRating()));
    }

    @NonNull
    @Override
    public Bar getItem(int position) {
        return super.getItem(position);
    }

    @Override
    public int getItemCount() {
        return super.getItemCount();
    }
}
