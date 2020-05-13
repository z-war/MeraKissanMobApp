package com.example.merakissan.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.merakissan.Models.Product;
import com.example.merakissan.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.security.PrivilegedAction;


public class complete_orders_or_delete_orders extends Fragment {

    private View frag;
    private TextView buyername , buyerphone , producttitle, productprice , orderdate;
    private Button cancel , confirm;
    private FirebaseFirestore db;


    public complete_orders_or_delete_orders() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        frag = inflater.inflate(R.layout.fragment_complete_orders_or_delete_orders, container, false);
        init();
        return frag;
    }

    private void init()
    {
        try {
            buyername = frag.findViewById(R.id.buyerNameTV);
            buyerphone = frag.findViewById(R.id.buyerPhoneTV);
            productprice = frag.findViewById(R.id.ProductPriceTV);
            producttitle = frag.findViewById(R.id.productTitleTV);
            orderdate = frag.findViewById(R.id.OrderDateTV);
            db = FirebaseFirestore.getInstance();
            frombundletoxml();


        }catch (Exception e)
        {
            Toast.makeText(getContext(), "Error Init"+e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void frombundletoxml()
    {
        try {
            String buyeremail = getArguments().getString("BuyerEmail");
            final String productID = getArguments().getString("ProductId");
            orderdate.setText("Order Date: "+getArguments().getString("OrderDate"));
            db.collection("Users").document(buyeremail).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    String name = documentSnapshot.getString("first_name");
                    name +=" "+ documentSnapshot.getString("last_name");
                    String phone;
                    if (!documentSnapshot.getString("phone").isEmpty()) {
                        phone =  documentSnapshot.getString("phone");
                    } else {
                        phone = "No Phone Number";
                    }
                    buyername.setText("Name: "+name);
                    buyerphone.setText("Phone: "+phone);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getContext(), "error getting data from firebase"+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

            db.collection("Products").document(productID).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    Product product = documentSnapshot.toObject(Product.class);
                    productprice.setText("Price: "+Integer.toString(product.getProductPrice()) );
                    producttitle.setText("Title: "+product.getProductTitle());
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getContext(), "Error"+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        }catch (Exception e)
        {

        }
    }
}
