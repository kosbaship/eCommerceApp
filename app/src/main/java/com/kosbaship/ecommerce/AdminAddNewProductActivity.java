package com.kosbaship.ecommerce;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

// (10 - B - 6)
// creation of this Activity
public class AdminAddNewProductActivity extends AppCompatActivity {

    private String CategoryName, Description, Price, Pname, saveCurrentDate, saveCurrentTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_new_product);

        // Receive the name of the category that send to us from
        // the admin category
        CategoryName = getIntent().getExtras().get("category").toString();
        Toast.makeText(this, CategoryName, Toast.LENGTH_LONG).show();


    }
}
