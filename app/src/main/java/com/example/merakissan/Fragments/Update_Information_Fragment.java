package com.example.merakissan.Fragments;

import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.merakissan.R;
import com.google.android.gms.tasks.Continuation;
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
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

import static android.app.Activity.RESULT_OK;


public class Update_Information_Fragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private View frag;
    private final static int req_code = 12;
    private StorageReference mStorageRef;
    private FirebaseFirestore db;
    private EditText first_name, last_name, password, email, phone;
    private ImageView profile_pic;
    private Button updateBTN;
    private FirebaseAuth mauth;
    private FirebaseUser curuser;
    private Boolean isimageselected = false;
    private Uri objecturi;
    private Dialog dialog;

    public Update_Information_Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        frag = inflater.inflate(R.layout.fragment_update__information_, container, false);
        init();
        return frag;
    }

    private void init() {
        try {
            first_name = frag.findViewById(R.id.firstNameET);
            last_name = frag.findViewById(R.id.lastNameET);
            email = frag.findViewById(R.id.emailET);
            password = frag.findViewById(R.id.passwordET);
            phone = frag.findViewById(R.id.phoneET);
            profile_pic = frag.findViewById(R.id.IV);
            updateBTN = frag.findViewById(R.id.update_infoBTN);
            mStorageRef = FirebaseStorage.getInstance().getReference("Images");
            db = FirebaseFirestore.getInstance();
            mauth = FirebaseAuth.getInstance();
            curuser = mauth.getCurrentUser();
            dialog = new Dialog(getContext());
            dialog.setContentView(R.layout.please_wait_dialog_box);
            dialog.setCancelable(false);
            email.setText(curuser.getEmail());
            db.collection("Users").document(curuser.getEmail()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot doc = task.getResult();
                        first_name.setText(doc.getString("first_name"));
                        last_name.setText(doc.getString("last_name"));
                        password.setText(doc.getString("password"));
                        if (!doc.getString("phone").isEmpty()) {
                            phone.setText(doc.getString("phone"));
                        }
                        if (!doc.getString("imageuri").isEmpty()) {
                            String url = doc.getString("imageuri");
                            Toast.makeText(getContext(), "Image found" + url, Toast.LENGTH_LONG).show();
                            Glide.with(getActivity().getBaseContext()).load(url).into(profile_pic);

                        } else {
                            Toast.makeText(getContext(), "No Image Found", Toast.LENGTH_SHORT).show();
                        }


                    }

                }
            }).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    Toast.makeText(getContext(), "User Details ", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getContext(), "Eror" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

            profile_pic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectiamge();
                }
            });

            updateBTN.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.show();
                    update_user_details();
                }
            });
        } catch (Exception e) {
            Toast.makeText(getContext(), "Error Init " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void selectiamge() {
        try {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(intent, req_code);
        } catch (Exception e) {
            Toast.makeText(getContext(), "Error Selecting Image" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == req_code && resultCode == RESULT_OK && data != null) {
            objecturi = data.getData();
            if (objecturi != null) {
                try {
                    Bitmap objectBitmap = MediaStore.Images.Media.getBitmap(getActivity().getApplicationContext().getContentResolver(), objecturi);
                    profile_pic.setImageBitmap(objectBitmap);
                    isimageselected = true;
                } catch (Exception e) {
                    Toast.makeText(getContext(), "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getContext(), "No Data in URi Object ", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getContext(), "No Image Selected", Toast.LENGTH_SHORT).show();
        }
    }


    private void update_user_details() {
        try {
            final Map<String, Object> datamap = new HashMap<>();
            datamap.put("first_name", first_name.getText().toString());
            datamap.put("last_name", last_name.getText().toString());
            datamap.put("phone", phone.getText().toString());
            if (isimageselected) {
                String imagename = curuser.getEmail() + " " + getextention(objecturi);
                final StorageReference ref = mStorageRef.child(imagename);
                UploadTask task = ref.putFile(objecturi);
                task.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()) {
                            dialog.dismiss();
                            Toast.makeText(getContext(), "Uploading Image Not success", Toast.LENGTH_LONG).show();
                            throw task.getException();

                        }
                        return ref.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {

                            datamap.put("imageuri", task.getResult().toString());
                            datamap.put("password", password.getText().toString());

                            db.collection("Users").document(curuser.getEmail()).update(datamap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    dialog.dismiss();
                                    Toast.makeText(getContext(), "User Details Uploaded Succesfully", Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    dialog.dismiss();
                                    Toast.makeText(getContext(), "Error Uploading Detials" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        dialog.dismiss();
                        Toast.makeText(getContext(), "" + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });


            } else {




                db.collection("Users").document(curuser.getEmail()).update(datamap).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        dialog.dismiss();
                        Toast.makeText(getContext(), "User Details Uploaded Succesfully With Out New Profile Pic", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        dialog.dismiss();
                        Toast.makeText(getContext(), "Error Uploading Detials" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }

        } catch (Exception e) {
            dialog.dismiss();
            Toast.makeText(getContext(), "" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private String getextention(Uri objecturi) {
        try {
            ContentResolver contentResolver = getActivity().getApplicationContext().getContentResolver();
            MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
            String ex = mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(objecturi));
            return ex;
        } catch (Exception e) {
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            return null;

        }

    }
}
