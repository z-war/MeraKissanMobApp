package com.example.merakissan.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.merakissan.Adapters.ShowOrdersAdapter;
import com.example.merakissan.Models.Orders;
import com.example.merakissan.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;


public class show_orders_for_this_user extends Fragment {
    View frag;
    RecyclerView RV;
    FirebaseFirestore db;
    FirebaseAuth muath;
    FirebaseUser curuser;
    Fragment complete_orders;
    ShowOrdersAdapter showOrdersAdapter;

    public show_orders_for_this_user() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        frag = inflater.inflate(R.layout.fragment_show_orders_for_this_user, container, false);
        init();
        return frag;
    }
        private void init()
        {
            try {
                RV = frag.findViewById(R.id.RV);
                db = FirebaseFirestore.getInstance();
                muath = FirebaseAuth.getInstance();
                curuser = muath.getCurrentUser();
                complete_orders = new complete_orders_or_delete_orders();
                FillRecyclerView();
            }catch (Exception e)
            {
                Toast.makeText(getContext(), "Error "+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }

        private void FillRecyclerView()
        {
            try {
                Query query = db.collection("Orders").whereEqualTo("OrderFrom" ,curuser.getEmail() ).whereEqualTo("OrderStatus","Unconfirmed");
                FirestoreRecyclerOptions<Orders> options ;
                options = new FirestoreRecyclerOptions.Builder<Orders>().setQuery(query,Orders.class).build();
                showOrdersAdapter = new ShowOrdersAdapter(options);
                RV.setLayoutManager(new LinearLayoutManager(getActivity()));
                RV.setAdapter(showOrdersAdapter);
                showOrdersAdapter.setOnItemClickListner(new ShowOrdersAdapter.OnItemClickListner() {
                    @Override
                    public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                        Orders orders = documentSnapshot.toObject(Orders.class);
                        Bundle data  = new Bundle();
                        data.putString("BuyerEmail" , orders.getOrderBy());
                        data.putString("ProductId" , orders.getProductId());
                        data.putString("OrderDate" , orders.getCreatedDate());
                        complete_orders.setArguments(data);
                        getFragmentManager().beginTransaction().replace(R.id.fragment_holder,complete_orders).commit();

                    }
                });
            }catch (Exception e)
            {
                Toast.makeText(getContext(), "Error "+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }

    @Override
    public void onStart() {
        super.onStart();
    showOrdersAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        showOrdersAdapter.stopListening();
    }
}
