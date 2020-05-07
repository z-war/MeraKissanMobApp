package com.example.merakissan.Fragments;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.merakissan.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class Confirm_order extends Fragment {

    private TextView nameTV, phone_numberTV, titleTV, priceTV, descriptionTV;
    private ImageView product_imageView;
    private Button confirm_btn;
    private FirebaseFirestore db;
    private StorageReference storageReference;
    private FirebaseAuth muath;
    private FirebaseUser curuser;
    private View frag;
    private String seller_email;
    private Dialog dialog;
    public Confirm_order() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        frag = inflater.inflate(R.layout.fragment_confirm_order, container, false);
        initialize();
        return frag;
    }

    private void initialize() {
        try {
            nameTV = frag.findViewById(R.id.user_emailTV);
            titleTV = frag.findViewById(R.id.title_holderTV);
            priceTV = frag.findViewById(R.id.price_holderTV);
            descriptionTV = frag.findViewById(R.id.description_holderTV);
            product_imageView = frag.findViewById(R.id.image_holderIV);
            db = FirebaseFirestore.getInstance();
            storageReference = FirebaseStorage.getInstance().getReference();
            muath = FirebaseAuth.getInstance();
            curuser = muath.getCurrentUser();
            phone_numberTV = frag.findViewById(R.id.phone_number_holderTV);
            confirm_btn = frag.findViewById(R.id.confirm_button);
            dialog = new Dialog(getContext());
            dialog.setContentView(R.layout.please_wait_dialog_box);
            dialog.setCancelable(false);
            loadfrombundletoxml();
            confirm_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.show();
                    confirmOrder();
                }
            });


        } catch (Exception e) {
            Toast.makeText(getContext(), "Error Init" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }


    private void loadfrombundletoxml() {
        try {
            seller_email = getArguments().getString("Email");
            db.collection("Users").document(seller_email).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    String name = documentSnapshot.getString("first_name");
                    name += " " + documentSnapshot.getString("last_name");
                    if (!documentSnapshot.getString("phone").isEmpty()) {
                        phone_numberTV.setText(documentSnapshot.getString("phone"));
                    } else {
                        phone_numberTV.setText("No Phone Number");
                    }
                    nameTV.setText(name);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getContext(), "Error getting document" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
            titleTV.setText(getArguments().getString("Title"));
            descriptionTV.setText(getArguments().getString("Description"));
            priceTV.setText(getArguments().getString("Price"));

            Glide.with(getContext()).load(getArguments().getString("ImageUri")).into(product_imageView);


        } catch (Exception e) {
            Toast.makeText(getContext(), "Error Loading From Bundle To Xml" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void confirmOrder() {
        try {
            if (curuser.getEmail().equals(seller_email)) {
                Toast.makeText(getContext(), "Please Select Another product you can not buy what you are selling", Toast.LENGTH_SHORT).show();


            } else {
                  Map<String, Object> order_details = new HashMap<>();
                order_details.put("OrderBy", curuser.getEmail());
                order_details.put("OrderFrom", seller_email);
                order_details.put("CreatedDate", new Date());
                order_details.put("ProductId", getArguments().getString("ProductId"));
                order_details.put("OrderStatus", "Unconfirmed");
                db.collection("Orders").add(order_details).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        dialog.dismiss();
                        Toast.makeText(getContext(), "Order Confirmed Please wait while the seller Confrims", Toast.LENGTH_SHORT).show();
                    }
                }).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        dialog.dismiss();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        dialog.dismiss();
                        Toast.makeText(getContext(), "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        } catch (Exception e) {
            dialog.dismiss();
            Toast.makeText(getContext(), "" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }
}
