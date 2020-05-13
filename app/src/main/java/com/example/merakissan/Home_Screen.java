package com.example.merakissan;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.merakissan.Fragments.Update_Information_Fragment;
import com.example.merakissan.Fragments.sell_fragment;
import com.example.merakissan.Fragments.show_orders_for_this_user;
import com.example.merakissan.Fragments.show_products;
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
    private ImageView profile_pic , cattleIV , tractorIV ,FertilizersIV;
    private TextView user_email , cattleTV , tractorTV;
    private TextView user_name;
    private FirebaseAuth muath;
    private FirebaseUser curUser;
    private FirebaseFirestore db;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private Toolbar toolbar;
    private Fragment update_info;
    private Fragment sell_frag;
    private Fragment show_products,show_orders;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home__screen);

        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void init() {
        try {
            update_info = new Update_Information_Fragment();
            navigationView = findViewById(R.id.design_navigation_view);
            drawerLayout = findViewById(R.id.drawer_layout);
            header_view = navigationView.getHeaderView(0);
            toolbar = findViewById(R.id.toolbar);
            user_email = header_view.findViewById(R.id.user_emailTV_header);
            user_name = header_view.findViewById(R.id.user_nameTV_header);
            profile_pic = header_view.findViewById(R.id.user_profile_picIV_header);
            toolbar.setTitle(R.string.app_name);
            show_orders = new show_orders_for_this_user();
            sell_frag = new sell_fragment();
            show_products = new show_products();
            muath = FirebaseAuth.getInstance();
            db = FirebaseFirestore.getInstance();
            cattleIV = findViewById(R.id.livestockIV);
            cattleIV.setClipToOutline(true);
            cattleIV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle data = new Bundle();
                    data.putString("ProductType" , "LiveStock");
                    show_products.setArguments(data);
                    changefrag(show_products);
                }
            });
            tractorIV = findViewById(R.id.equipmentIV);
            tractorIV.setClipToOutline(true);
            tractorIV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle data = new Bundle();
                    data.putString("ProductType" , "Equipment");
                    show_products.setArguments(data);
                    changefrag(show_products);
                }
            });
            FertilizersIV = findViewById(R.id.fertilizersIV);
            FertilizersIV.setClipToOutline(true);
            navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    if (item.getItemId() == R.id.log_out) {
                        closedrawer();
                        muath.signOut();
                        finish();
                        startActivity(new Intent(getBaseContext(), MainActivity.class));
                    } else if (item.getItemId() == R.id.show_products) {
                        closedrawer();
                        Bundle data = new Bundle();
                        data.putString("ProductType" , "any");
                        show_products.setArguments(data);
                        changefrag(show_products);
                        changefrag(show_products);
                    } else if (item.getItemId() == R.id.update_user_details) {
                        closedrawer();

                        changefrag(update_info);

                        return true;
                    }else if(item.getItemId() == R.id.home_screen)
                    {
                        closedrawer();

                        startActivity(new Intent(getBaseContext(),Home_Screen.class));
                        finish();
                        return true;
                    }
                    else if(item.getItemId() == R.id.sell_screen)
                    {
                        closedrawer();

                        changefrag(sell_frag);
                        return true;
                    }
                    else if(item.getItemId() == R.id.user_orders)
                    {
                        closedrawer();

                        changefrag(show_orders);
                        return true;
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

    private void setcurrentuserdetails() {
        try {
            curUser = muath.getCurrentUser();
            user_email.setText(curUser.getEmail());
            DocumentReference ref = db.collection("Users").document(curUser.getEmail());
            ref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {

                        DocumentSnapshot doc = task.getResult();
                        String name = doc.getString("first_name");
                        name += " ";
                        name += doc.getString("last_name");
                        user_name.setText(name);
                        if (!doc.getString("imageuri").isEmpty()) {
                            Glide.with(getApplicationContext()).load(doc.getString("imageuri")).into(profile_pic);
                        }
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
                    Toast.makeText(Home_Screen.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            Toast.makeText(this, "Setting User Details " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void setuphamburgericon() {
        try {
            actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, (R.string.open), (R.string.close));
            actionBarDrawerToggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.colorPrimaryDark));
            actionBarDrawerToggle.syncState();
        } catch (Exception e) {
            Toast.makeText(this, "Error Setting Up Hamburger ICon" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void closedrawer() {
        try {
            drawerLayout.closeDrawer(GravityCompat.START);
        } catch (Exception e) {
            Toast.makeText(this, "Error Closing Drawer" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void changefrag(Fragment obj) {
        try {
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_holder, obj).commit();
        } catch (Exception e) {
            Toast.makeText(this, "Error Changing fragment" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}
