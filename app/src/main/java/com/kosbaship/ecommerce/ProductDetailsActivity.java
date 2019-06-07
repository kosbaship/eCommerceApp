package com.kosbaship.ecommerce;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kosbaship.ecommerce.Model.Products;
import com.squareup.picasso.Picasso;


//                              (15 - B)
// (15 - C) Go to HomeActivity.java

public class ProductDetailsActivity extends AppCompatActivity {

    //(15 - B - 1)
    // declare the views on the screen
    private Button addToCartButton;
    private ImageView productImage;
    private ElegantNumberButton numberButton;
    private TextView productPrice, productDescription, productName;
    // (15 - C - 2 - a)
    // declare the variable productID to
    // receive the product id that com with the intent
    private String productID = "", state = "Normal";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        // (15 - C - 2 - b)
        // receive the product id that com with the intent
        productID = getIntent().getStringExtra("pid");

        //(15 - B - 2)
        // get reference to the views on the screen
        addToCartButton = findViewById(R.id.pd_add_to_cart_button);
        numberButton = findViewById(R.id.number_btn);
        productImage = findViewById(R.id.product_image_details);
        productName = findViewById(R.id.product_name_details);
        productDescription = findViewById(R.id.product_description_details);
        productPrice = findViewById(R.id.product_price_details);

        // (15 - C - 3)
        getProductDetails(productID);

    }


    // (15 - C - 4)
    private void getProductDetails(String productID) {
        // create database reference for products node
        DatabaseReference productsRef = FirebaseDatabase.getInstance().getReference().child("Products");

        //searching for specific product
        productsRef.child(productID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // if this specific product exist
                if (dataSnapshot.exists()) {
                    // get it's value
                    Products products = dataSnapshot.getValue(Products.class);

                    // get these values and also render then in the acticity views
                    productName.setText(products.getPname());
                    productPrice.setText(products.getPrice());
                    productDescription.setText(products.getDescription());
                    // use picasso to get and load image in the image view
                    Picasso.get().load(products.getImage()).into(productImage);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
