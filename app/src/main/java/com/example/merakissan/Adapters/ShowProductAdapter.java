package com.example.merakissan.Adapters;

import android.app.Application;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.merakissan.Models.Product;
import com.example.merakissan.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ShowProductAdapter extends FirestoreRecyclerAdapter<Product, ShowProductAdapter.AppViewHolder> {

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public ShowProductAdapter(@NonNull FirestoreRecyclerOptions<Product> options) {

        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull AppViewHolder appViewHolder, int i, @NonNull Product product) {
        appViewHolder.description_holderTV.setText(product.getProductDescription());
        appViewHolder.price_holderTV.setText(Integer.toString(product.getProductPrice()) );
        appViewHolder.title_holderTV.setText(product.getProductTitle());
        Glide.with(appViewHolder.image_holderIV.getContext()).load(product.getImageUri()).into(appViewHolder.image_holderIV);
        appViewHolder.email_older_tv.setText(product.getCreatedBy());
    }

    @NonNull
    @Override
    public AppViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View row = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_row, parent, false);
        AppViewHolder object = new AppViewHolder(row);
        return object;
    }

    class AppViewHolder extends RecyclerView.ViewHolder {

        TextView title_holderTV, price_holderTV, description_holderTV , email_older_tv;
        ImageView image_holderIV;

        public AppViewHolder(@NonNull View itemView) {
            super(itemView);
            title_holderTV = itemView.findViewById(R.id.title_holderTV);
            price_holderTV = itemView.findViewById(R.id.price_holderTV);
            email_older_tv = itemView.findViewById(R.id.user_emailTV);
            description_holderTV = itemView.findViewById(R.id.description_holderTV);
            image_holderIV = itemView.findViewById(R.id.image_holderIV);
        }
    }
}
