package com.kosbaship.ecommerce.Sellers;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.kosbaship.ecommerce.Buyers.MainActivity;
import com.kosbaship.ecommerce.R;
import com.kosbaship.ecommerce.Sellers.Fragments.AddFragment;
import com.kosbaship.ecommerce.Sellers.Fragments.HomeFragment;
import com.kosbaship.ecommerce.Sellers.Fragments.LogoutFragment;

//(24 - F - 0)
// go to AndroidManifest.xml and add this activity to it
public class SellerHomeActivity extends AppCompatActivity {

    //                ----- caution -----
    //          choose the activity_home_seller
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_seller);

        //(24 - F - 1) get reference
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        //(24 - F - 3)
        // make the home fragment selected by default
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container,// pass the container of our fragment
                        new HomeFragment()).commit();


        //(24 - F - 2)
        // react to clicks on the items
        bottomNav.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                   @Override
                   public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                       // find out what selected via switch statment
                       Fragment selectedFragment = null;

                       switch (item.getItemId()){
                           case R.id.nav_home:
                               selectedFragment = new HomeFragment();
                               break;
                           case R.id.nav_add:
                               selectedFragment = new AddFragment();
                               break;
                           case R.id.nav_logout:
                               selectedFragment = new LogoutFragment();
                               //(24 - G - 1)
                               //(24 - G - 2) goto SellerRegistrationActivity.java
                               // when the user click on the logout btn
                               // log hi, out
                               final FirebaseAuth mAuth;
                               mAuth = FirebaseAuth.getInstance();
                               mAuth.signOut();

                               // send the Seller to MainActivity  activity
                               Intent intent = new Intent(SellerHomeActivity.this, MainActivity.class);
                               intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                               startActivity(intent);
                               finish();
                               break;

                       }
                       // now the time to show them
                       getSupportFragmentManager()
                               .beginTransaction()
                               .replace(R.id.fragment_container,// pass the container of our fragment
                                       selectedFragment).commit();
                       return true; // this mean that we want to select the return item

                   }
               }
        );
    }


}
