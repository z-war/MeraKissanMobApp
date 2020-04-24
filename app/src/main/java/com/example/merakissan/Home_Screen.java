package com.example.merakissan;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class Home_Screen extends AppCompatActivity {
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private View header_view;
    private ImageView profile_pic;
    private TextView user_email;
    private TextView user_name;
    private FirebaseAuth muath;
    private FirebaseUser curUser;
    private FirebaseFirestore db;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home__screen);
init();
    }

    private void init()
    {
        try {
            navigationView = findViewById(R.id.design_navigation_view);
            drawerLayout = findViewById(R.id.drawer_layout);
            header_view = navigationView.getHeaderView(0);
            toolbar = findViewById(R.id.toolbar);
            user_email = header_view.findViewById(R.id.user_emailTV_header);
            user_name =header_view.findViewById(R.id.user_nameTV_header);
            muath = FirebaseAuth.getInstance();
            db =FirebaseFirestore.getInstance();

            navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    if(item.getItemId() == R.id.log_out)
                    {
                        closedrawer();
                        muath.signOut();
                        startActivity(new Intent(getBaseContext(),MainActivity.class));
                    }else
                        if(item.getItemId() == R.id.show_orders)
                        {
                            closedrawer();
                        }
                    return false;
                }
            });
            setuphamburgericon();
            setcurrentuserdetails();
        } catch (Exception e) {
            Toast.makeText(this, "" + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void setcurrentuserdetails()
    {
        try {
            curUser = muath.getCurrentUser();
            user_email.setText(curUser.getEmail());
            DocumentReference ref =  db.collection("Users").document(curUser.getEmail());
            ref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if(task.isSuccessful())
                            {
                                DocumentSnapshot  doc = task.getResult();
                                String name = doc.getString("first_name");
                                name+=" ";
                                name+= doc.getString("last_name");
                                user_name.setText(name);
                            }
                }
            }).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    Toast.makeText(Home_Screen.this, "Welcome", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(Home_Screen.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }catch (Exception e)
        {
            Toast.makeText(this, "Setting User Details "+e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
    private void setuphamburgericon()
    {
        try {
            actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,(R.string.open),(R.string.close));
            actionBarDrawerToggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.colorPrimaryDark));
            actionBarDrawerToggle.syncState();
        }catch (Exception e)
        {
            Toast.makeText(this, "Error Setting Up Hamburger ICon"+e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
    private void closedrawer()
    {
        try {
            drawerLayout.closeDrawer(GravityCompat.START);
        }catch (Exception e)
        {
            Toast.makeText(this, "Error Closing Drawer"+e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}