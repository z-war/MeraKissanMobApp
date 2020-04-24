package com.example.merakissan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.internal.$Gson$Preconditions;

public class MainActivity extends AppCompatActivity {
   private EditText email , password;
   private Button login , tosignup;
   FirebaseFirestore db;
   FirebaseAuth mAuth;
   Dialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init()
    {
        try {
            dialog = new Dialog(this);
            dialog.setContentView(R.layout.please_wait_dialog_box);
            dialog.setCancelable(false);
            mAuth =FirebaseAuth.getInstance();
            db =FirebaseFirestore.getInstance();
            email =findViewById(R.id.emailET);
            password =findViewById(R.id.passwordET);
            login = findViewById(R.id.loginBTN);
            tosignup = findViewById(R.id.tosignupactivityBTN);
            tosignup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(getBaseContext(),SignUpUser.class));
                }
            });
            login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.show();
                    login();
                }
            });
        }catch (Exception e)
        {
            Toast.makeText(this, "Error initializing "+e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void login()
    {
        try {
                if (!email.getText().toString().isEmpty()&& !password.getText().toString().isEmpty())
                {
                    mAuth.signOut();
                    mAuth.signInWithEmailAndPassword(email.getText().toString(),password.getText().toString()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {

                            dialog.dismiss();
                            Toast.makeText(MainActivity.this, "Logged In", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getBaseContext(),Home_Screen.class));
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            dialog.dismiss();
                            Toast.makeText(MainActivity.this, "Error Logging in "+e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                }else if(email.getText().toString().isEmpty())
                {
                    dialog.dismiss();
                    Toast.makeText(this, "Please enter email address", Toast.LENGTH_SHORT).show();
                }
                else if(password.getText().toString().isEmpty())
                {
                    dialog.dismiss();
                    Toast.makeText(this, "Please Enter Password", Toast.LENGTH_SHORT).show();
                }
        }catch (Exception e)
        {
            dialog.dismiss();
            Toast.makeText(this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }


}
