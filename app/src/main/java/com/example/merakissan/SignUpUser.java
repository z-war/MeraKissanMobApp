package com.example.merakissan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.internal.$Gson$Preconditions;

import java.util.HashMap;
import java.util.Map;

public class SignUpUser extends AppCompatActivity {
    private EditText first_name, last_name, email, password;
    private Button signup;
    private TextView tologinact;
    FirebaseAuth muath;
    FirebaseFirestore db;
    Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_user);
        init();
    }

    private void init() {
        try {
            first_name = findViewById(R.id.firstNameET);
            last_name = findViewById(R.id.lastNameET);
            email = findViewById(R.id.emailET);
            password = findViewById(R.id.passwordET);
            signup = findViewById(R.id.signupBTN);
            muath = FirebaseAuth.getInstance();
            db = FirebaseFirestore.getInstance();
            tologinact = findViewById(R.id.tologinactivity);
            dialog = new Dialog(this);
            dialog.setContentView(R.layout.please_wait_dialog_box);
            dialog.setCancelable(false);
            signup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.show();
                    setSignup();
                }
            });
            String text = "<font color='black'>Already a Member </font><font color='red'>Login Here!</font>";
            tologinact.setText(Html.fromHtml(text),TextView.BufferType.SPANNABLE);
            tologinact.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                    startActivity(new Intent(getBaseContext() , MainActivity.class));
                }
            });
        } catch (Exception e) {
            Toast.makeText(this, "Error Initializing" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void setSignup() {
        try {
            muath.signOut();
            if (!email.getText().toString().isEmpty() && !password.getText().toString().isEmpty() && !first_name.getText().toString().isEmpty() && !last_name.getText().toString().isEmpty()) {
                muath.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            Map<String, Object> mydatamap = new HashMap<>();
                            mydatamap.put("first_name", first_name.getText().toString());
                            mydatamap.put("last_name", last_name.getText().toString());
                            mydatamap.put("password", password.getText().toString());
                            mydatamap.put("phone","");
                            mydatamap.put("imageuri","");
                            db.collection("Users").document(email.getText().toString()).set(mydatamap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(SignUpUser.this, "User SignedUp Successfully", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(getBaseContext(),Home_Screen.class));
                                    dialog.dismiss();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    dialog.dismiss();
                                    Toast.makeText(SignUpUser.this, "Error From Firebase" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });

                        } else {
                            dialog.dismiss();
                            Toast.makeText(SignUpUser.this, "" + task.getException(), Toast.LENGTH_SHORT).show();

                        }
                    }
                });

            } else if (email.getText().toString().isEmpty()) {
                dialog.dismiss();
                Toast.makeText(this, "Please Enter Email Address Before Signing Up", Toast.LENGTH_SHORT).show();
                email.requestFocus();
            } else if (password.getText().toString().isEmpty()) {
                dialog.dismiss();
                Toast.makeText(this, "Please Enter Password", Toast.LENGTH_SHORT).show();
                password.requestFocus();

            } else if (first_name.getText().toString().isEmpty()) {
                dialog.dismiss();
                Toast.makeText(this, "Please Enter First Name", Toast.LENGTH_SHORT).show();
                first_name.requestFocus();
            } else if (last_name.getText().toString().isEmpty()) {
                dialog.dismiss();
                Toast.makeText(this, "Please Enter Last Name", Toast.LENGTH_SHORT).show();
                last_name.requestFocus();
            }

        } catch (Exception e) {
            dialog.dismiss();
            Toast.makeText(this, "Error Sign up " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

}
