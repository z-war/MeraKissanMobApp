package com.example.merakissan.Fragments;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.merakissan.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static android.app.Activity.RESULT_OK;


public class sell_fragment extends Fragment {


    private static final int req_code = 1;
    private FirebaseAuth authuser;
    private FirebaseUser curuser;
    private FirebaseFirestore db;
    private StorageReference storageReference;
    private View frag;
    private Uri objecturi;
    private Spinner dropdown, Dropdown_cat;
    private String[] items = {"LiveStock", "Equipment"};
    private String[] items_cattle = {"Cow", "Buffalo", "Sheep", "Goat", "Poultry", "Camel"};
    private String[] items_equipment = {"Tractor", "Cultivator", "Plower", "harrows", "Sprayers", "Seed Drills", "Scythe", "Sickle", "Harvester"};
    private ArrayAdapter<String> adapter, adapter_for_cateory;
    private String product_type, product_catogary;
    private EditText titleET, descriptionET, priceET;
    private ImageView product_imageIV;
    private Button btn_upload;
    private boolean isimageselected = false;
    private Dialog dialog;
    public sell_fragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        frag = inflater.inflate(R.layout.fragment_sell_fragment, container, false);
        init();
        return frag;
    }

    private void init() {
        try {
            db = FirebaseFirestore.getInstance();
            storageReference = FirebaseStorage.getInstance().getReference("Product_Images");
            authuser = FirebaseAuth.getInstance();
            curuser = authuser.getCurrentUser();
            Dropdown_cat = frag.findViewById(R.id.product_category);
            adapter_for_cateory = new ArrayAdapter<>(getActivity().getBaseContext(), android.R.layout.simple_spinner_dropdown_item, items_cattle);
            Dropdown_cat.setAdapter(adapter_for_cateory);
            dropdown = frag.findViewById(R.id.product_type);
            adapter = new ArrayAdapter<>(getActivity().getBaseContext(), android.R.layout.simple_spinner_dropdown_item, items);
            dropdown.setAdapter(adapter);
            product_catogary = "Cow";
            product_type = "Livestock";
            titleET = frag.findViewById(R.id.prod_titleET);
            descriptionET = frag.findViewById(R.id.prod_descriptionET);
            priceET = frag.findViewById(R.id.prod_pricET);
            product_imageIV = frag.findViewById(R.id.product_imageIV);
            btn_upload = frag.findViewById(R.id.add_productBTN);
            dialog = new Dialog(getContext());
            dialog.setContentView(R.layout.please_wait_dialog_box);
            dialog.setCancelable(false);
            product_imageIV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    select_image_from_gallery();
                }
            });
            btn_upload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.show();
                    add_product();

                }
            });
            dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    switch (position) {
                        case 0:
                            product_type = "LiveStock";
                            adapter_for_cateory = new ArrayAdapter<>(getActivity().getBaseContext(), android.R.layout.simple_spinner_dropdown_item, items_cattle);
                            Dropdown_cat.setAdapter(adapter_for_cateory);
                            break;
                        case 1:
                            product_type = "Equipment";
                            adapter_for_cateory = new ArrayAdapter<>(getActivity().getBaseContext(), android.R.layout.simple_spinner_dropdown_item, items_equipment);
                            Dropdown_cat.setAdapter(adapter_for_cateory);

                            break;

                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            Dropdown_cat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if(product_type=="LiveStock")
                    {
                        product_catogary = items_cattle[position];

                    }else if(product_type=="Equipment")
                    {
                        product_catogary = items_equipment[position];
                    }

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        } catch (Exception e) {
            Toast.makeText(getContext(), "Error " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }

    private void select_image_from_gallery() {
        try {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(intent, req_code);
        } catch (Exception e) {
            Toast.makeText(getContext(), "select_image_from_gallery error " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == req_code && resultCode == RESULT_OK && data != null) {
                objecturi = data.getData();
                Bitmap objbitmap = MediaStore.Images.Media.getBitmap(getActivity().getApplicationContext().getContentResolver(), objecturi);
                product_imageIV.setImageBitmap(objbitmap);
                isimageselected = true;
                btn_upload.requestFocus();
            } else {
                Toast.makeText(getContext(), "No Image Selected", Toast.LENGTH_SHORT).show();
            }

        } catch (Exception e) {

        }
    }

    private void add_product() {
        try {
            if (!titleET.getText().toString().isEmpty() && !descriptionET.getText().toString().isEmpty() && !priceET.getText().toString().isEmpty() && isimageselected) {
                int price = Integer.parseInt(priceET.getText().toString());
                if (price > 0) {
                    final Map<String, Object> datamap = new HashMap<>();
                    datamap.put("ProductType", product_type);
                    datamap.put("ProductCategory", product_catogary);
                    datamap.put("ProductTitle", titleET.getText().toString());
                    datamap.put("ProductDescription", descriptionET.getText().toString());
                    datamap.put("ProductPrice", price);
                    datamap.put("CreatedBy", curuser.getEmail());
                    SimpleDateFormat sfd = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                    datamap.put("CreatedAt", sfd.format(new Date()).toString() );
                    if (isimageselected) {
                        String imagename = curuser.getEmail() + "-" + new Date();
                        final StorageReference reference = storageReference.child(imagename);
                        UploadTask task = reference.putFile(objecturi);
                        task.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                            @Override
                            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                                if (!task.isSuccessful()) {
                                    Toast.makeText(getContext(), "Uploading Image Not success", Toast.LENGTH_LONG).show();
                                    throw task.getException();

                                }

                                    return reference.getDownloadUrl();
                            }
                        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@NonNull Task<Uri> task) {
                                if (task.isSuccessful()) {
                                    datamap.put("ImageUri", task.getResult().toString());

                                    db.collection("Products").add(datamap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentReference> task) {
                                            dialog.dismiss();
                                            Toast.makeText(getContext(), "Product Added Succesfully", Toast.LENGTH_SHORT).show();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            dialog.dismiss();
                                            Toast.makeText(getContext(), "Error " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            }
                        });
                    }


                } else {
                    dialog.dismiss();
                    Toast.makeText(getContext(), "Please Enter Postive Value For Price", Toast.LENGTH_SHORT).show();
                    priceET.requestFocus();
                }
            } else if (titleET.getText().toString().isEmpty()) {
                Toast.makeText(getContext(), "Please Enter Title", Toast.LENGTH_SHORT).show();
                titleET.requestFocus();
                dialog.dismiss();
            } else if (descriptionET.getText().toString().isEmpty()) {
                Toast.makeText(getContext(), "Pelase Enter Product Description", Toast.LENGTH_SHORT).show();
                descriptionET.requestFocus();
                dialog.dismiss();
            } else if (priceET.getText().toString().isEmpty()) {
                dialog.dismiss();
                Toast.makeText(getContext(), "Please Enter Price", Toast.LENGTH_SHORT).show();
                priceET.requestFocus();
            } else if (!isimageselected) {
                dialog.dismiss();
                Toast.makeText(getContext(), "Please Enter Image", Toast.LENGTH_SHORT).show();
                product_imageIV.requestFocus();
            }
        } catch (Exception e) {
            dialog.dismiss();
            Toast.makeText(getContext(), "add_product" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

}
