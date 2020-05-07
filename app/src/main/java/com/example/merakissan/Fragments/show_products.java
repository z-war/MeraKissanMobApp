package com.example.merakissan.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.merakissan.Adapters.ShowProductAdapter;
import com.example.merakissan.Models.Product;
import com.example.merakissan.R;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class show_products extends Fragment {
    private RecyclerView recyclerView;
    private ShowProductAdapter showProductAdapter;
    private FirebaseFirestore db;
    private View frag;
    private Fragment confirmOrder;
    public show_products() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        frag = inflater.inflate(R.layout.fragment_show_products, container, false);
        init();
        return frag;
    }

    private void init() {
        try {
            db = FirebaseFirestore.getInstance();
            recyclerView = frag.findViewById(R.id.show_productsRV);
            confirmOrder = new Confirm_order();
            Fill_RecyclerView();


        } catch (Exception e) {
            Toast.makeText(getActivity(), "Error " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void Fill_RecyclerView()
    {
        try {

            Query query = db.collection("Products");
            FirestoreRecyclerOptions<Product> options;
            options= new FirestoreRecyclerOptions.Builder<Product>().setQuery(query,Product.class).build();
            showProductAdapter = new ShowProductAdapter(options);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            recyclerView.setAdapter(showProductAdapter);
            showProductAdapter.setOnItemClickListner(new ShowProductAdapter.OnItemClickListner() {
                @Override
                public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                    Product product = documentSnapshot.toObject(Product.class);
                    String id = documentSnapshot.getId();
                    Bundle product_data = new Bundle();
                    product_data.putString("Email" , product.getCreatedBy());
                    product_data.putString("Price" , Integer.toString(product.getProductPrice()));
                    product_data.putString("ImageUri" , product.getImageUri());
                    product_data.putString("Title",product.getProductTitle());
                    product_data.putString("Description",product.getProductDescription());
                    product_data.putString("ProductId" , id);
                    confirmOrder.setArguments(product_data);

                    getFragmentManager().beginTransaction().replace(R.id.fragment_holder,confirmOrder).commit();
                }
            });
        }catch (Exception e)
        {
            Toast.makeText(getActivity(), "error loading items in recycler view"+e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        showProductAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        showProductAdapter.stopListening();
    }
}
