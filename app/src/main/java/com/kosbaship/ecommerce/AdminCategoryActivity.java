package com.kosbaship.ecommerce;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
// (9 - E - 2)
// (10) GO to activity_admin_category.xml
// creation of this activity

// (10 - B - 4)
// implements View.OnClickListener
// then
// Override the needed Methods
public class AdminCategoryActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_category);
        //(18 - A - 2 - a)
        // get reference to the logout btn and check order state
        findViewById(R.id.admin_logout_btn).setOnClickListener(this);
        findViewById(R.id.check_orders_btn).setOnClickListener(this);



        // (10 - B - 1, 2, 3)
        // declare the products views on the screen
        // get reference to the views on the screen
        // set click listeners for the events on the images (Directly)
        findViewById(R.id.t_shirts).setOnClickListener(this);
        findViewById(R.id.sports_t_shirts).setOnClickListener(this);
        findViewById(R.id.female_dresses).setOnClickListener(this);
        findViewById(R.id.sweathers).setOnClickListener(this);

        findViewById(R.id.glasses).setOnClickListener(this);
        findViewById(R.id.hats_caps).setOnClickListener(this);
        findViewById(R.id.purses_bags_wallets).setOnClickListener(this);
        findViewById(R.id.shoes).setOnClickListener(this);

        findViewById(R.id.headphones_handfree).setOnClickListener(this);
        findViewById(R.id.laptop_pc).setOnClickListener(this);
        findViewById(R.id.watches).setOnClickListener(this);
        findViewById(R.id.mobilephones).setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        Intent intent;
        /*
         *                          // (10 - B - 5)
         *    // (10 - B - 6) Go and Create  AdminAddNewProductActivity.java
         * Create switch case the receive every button ID and perform the right action
         * and              (send the category info with the intent)
         *
         * */
        switch (v.getId()) {
            case R.id.t_shirts:
                intent = new Intent(AdminCategoryActivity.this, AdminAddNewProductActivity.class);
                // allow the admin to add product to each specific category
                // send the category info with the intent
                intent.putExtra("category", "tShirts");
                startActivity(intent);
                break;
            case R.id.sports_t_shirts:
                intent = new Intent(AdminCategoryActivity.this, AdminAddNewProductActivity.class);
                intent.putExtra("category", "Sports tShirts");
                startActivity(intent);
                break;
            case R.id.female_dresses:
                intent = new Intent(AdminCategoryActivity.this, AdminAddNewProductActivity.class);
                intent.putExtra("category", "Female Dresses");
                startActivity(intent);
                break;
            case R.id.sweathers:
                intent = new Intent(AdminCategoryActivity.this, AdminAddNewProductActivity.class);
                intent.putExtra("category", "Sweathers");
                startActivity(intent);
                break;
            case R.id.glasses:
                intent = new Intent(AdminCategoryActivity.this, AdminAddNewProductActivity.class);
                intent.putExtra("category", "Glasses");
                startActivity(intent);
                break;
            case R.id.hats_caps:
                intent = new Intent(AdminCategoryActivity.this, AdminAddNewProductActivity.class);
                intent.putExtra("category", "Hats Caps");
                startActivity(intent);
                break;
            case R.id.purses_bags_wallets:
                intent = new Intent(AdminCategoryActivity.this, AdminAddNewProductActivity.class);
                intent.putExtra("category", "Wallets Bags Purses");
                startActivity(intent);
                break;
            case R.id.shoes:
                intent = new Intent(AdminCategoryActivity.this, AdminAddNewProductActivity.class);
                intent.putExtra("category", "Shoes");
                startActivity(intent);
                break;
            case R.id.headphones_handfree:
                intent = new Intent(AdminCategoryActivity.this, AdminAddNewProductActivity.class);
                intent.putExtra("category",  "HeadPhones HandFree");
                startActivity(intent);
                break;
            case R.id.laptop_pc:
                intent = new Intent(AdminCategoryActivity.this, AdminAddNewProductActivity.class);
                intent.putExtra("category", "Laptops");
                startActivity(intent);
                break;
            case R.id.watches:
                intent = new Intent(AdminCategoryActivity.this, AdminAddNewProductActivity.class);
                intent.putExtra("category",  "Watches");
                startActivity(intent);
                break;
            case R.id.mobilephones:
                intent = new Intent(AdminCategoryActivity.this, AdminAddNewProductActivity.class);
                intent.putExtra("category", "Mobile Phones");
                startActivity(intent);
                break;

            //(18 - A - 2 - b) create the functions for the two btns (check order & Logout)
            case  R.id.check_orders_btn:
                intent = new Intent(AdminCategoryActivity.this, AdminNewOrdersActivity.class);
                startActivity(intent);
                break;
            case  R.id.admin_logout_btn:
                intent = new Intent(AdminCategoryActivity.this, MainActivity.class);
                // this flag to clear the activity from the previous task
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
                break;

        }
    }
}
