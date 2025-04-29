package com.abeltran10.carajilloapp.ui.location;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.abeltran10.carajilloapp.R;
import com.abeltran10.carajilloapp.data.model.Bar;
import com.abeltran10.carajilloapp.data.model.City;
import com.abeltran10.carajilloapp.ui.main.MainAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class LocationAdapter extends FirestoreRecyclerAdapter<Bar, LocationAdapter.ViewHolder> {

    private City city;

    public LocationAdapter(@NonNull FirestoreRecyclerOptions<Bar> options, City city) {
        super(options);
        this.city = city;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView restaurantName;

        private final TextView restaurantLocation;

        private final ImageButton map;

        public ViewHolder(View view) {
            super(view);

            restaurantName = (TextView) view.findViewById(R.id.restaurant_name);
            restaurantLocation = (TextView) view.findViewById(R.id.restaurant_location);
            map = (ImageButton) view.findViewById(R.id.map);
        }

        public TextView getRestaurantName() {
            return restaurantName;
        }

        public TextView getRestaurantLocation() {
            return restaurantLocation;
        }

        public ImageButton getMap() {
            return map;
        }
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder viewHolder, int position, @NonNull Bar bar) {
        viewHolder.getRestaurantName().setText(bar.getName().toUpperCase());
        String location = city.getName() + " - " + bar.getAddress() + " (" + bar.getPostalCode() + ")";
        viewHolder.getRestaurantLocation().setText(location);
        viewHolder.getMap().setOnClickListener(view -> {
            // URI para Maps
            Uri gmmIntentUri = Uri.parse("geo:0,0" + "?q=Bar "
                    + bar.getName() + ", " + bar.getAddress() + " "
                    + bar.getPostalCode() + ", " + bar.getCity());

            // Intent explícito
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
            mapIntent.setPackage("com.google.android.apps.maps");
            mapIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            Context context = viewHolder.itemView.getContext();
            // Verificamos que Maps esté instalado
            if (mapIntent.resolveActivity(context.getPackageManager()) != null) {
                    context.startActivity(mapIntent);
            } else {
                Toast.makeText(context, "Google Maps no està instal·lat", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.list_location, viewGroup, false);

        return new LocationAdapter.ViewHolder(view);
    }
}
