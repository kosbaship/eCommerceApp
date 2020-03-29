package com.kosbaship.ecommerce;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.firebase.database.DatabaseReference;

public class AdminMaintainProductsActivity extends AppCompatActivity {


    //(21 - C - 5 - a)
    // declare this views
    private Button applyChangesBtn, deleteBtn;
    private EditText name, price, description;
    private ImageView imageView;

//    private String productID = "";
//    private DatabaseReference productsRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_maintain_products);


        //(21 - C - 5 - b)
        // get reference to this views
        applyChangesBtn = findViewById(R.id.apply_changes_btn);
        name = findViewById(R.id.product_name_maintain);
        price = findViewById(R.id.product_price_maintain);
        description = findViewById(R.id.product_description_maintain);
        imageView = findViewById(R.id.product_image_maintain);
        deleteBtn = findViewById(R.id.delete_product_btn);


    }
}
