package com.abeltran10.carajilloapp.ui.main;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.abeltran10.carajilloapp.R;
import com.abeltran10.carajilloapp.data.model.Bar;
import com.abeltran10.carajilloapp.data.model.City;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class MainAdapter extends FirestoreRecyclerAdapter<Bar, MainAdapter.ViewHolder> {
    private OnItemClickListener onItemClickListener;

    private OnBtnShareListener onBtnShareListener;

    private City city;

    public interface OnItemClickListener {
        void onItemClick(Bar bar, City city);
    }

    public interface OnBtnShareListener {
        void onItemClick(Bar bar, City city);
    }

    public MainAdapter(@NonNull FirestoreRecyclerOptions<Bar> options, City city) {
        super(options);
        this.city = city;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setOnBtnShareListener(OnBtnShareListener onBtnShareListener) {
        this.onBtnShareListener = onBtnShareListener;
    }


    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder)
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView restaurantName;

        private final TextView restaurantLocation;

        private final RatingBar rating;

        private final ImageView btnShare;

        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View

            restaurantName = (TextView) view.findViewById(R.id.restaurant_name);
            restaurantLocation = (TextView) view.findViewById(R.id.restaurant_location);
            rating = (RatingBar) view.findViewById(R.id.ratingBar);
            btnShare = (ImageView) view.findViewById(R.id.btnCompartir);

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

        public ImageView getBtnShare() {
            return btnShare;
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
        String location = city.getName() + " - " + bar.getAddress() + " (" + bar.getPostalCode() + ")";
        viewHolder.getRestaurantLocation().setText(location);
        viewHolder.getRating().setRating(bar.getRating());

        viewHolder.getBtnShare().setOnClickListener(view -> onBtnShareListener.onItemClick(bar, city));

        viewHolder.itemView.setOnClickListener(view -> onItemClickListener.onItemClick(bar, city));
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
