package com.kosbaship.ecommerce;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kosbaship.ecommerce.Model.Products;
import com.kosbaship.ecommerce.Prevalent.Prevalent;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;


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
    // (17 - E - 5)
    // create the variable state = "Normal"
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

        // (15 - C - 5)
        addToCartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                // (17 - E - 8)
                // create thiss validation to tell the user about his current state
                if (state.equals("Order Placed") || state.equals("Order Shipped"))
                {
                    Toast.makeText(ProductDetailsActivity.this, "you can add purchase more products, once your order is shipped or confirmed.", Toast.LENGTH_LONG).show();
                }
                else {
                    // (15 - C - 5 - a)
                    addingToCartList();
                }
            }
        });

    }


    // (17 - E - 7)
    // call the validation method
    // inside on start
    @Override
    protected void onStart()
    {
        super.onStart();

        CheckOrderState();
    }


    // (15 - C - 5 - b)
    // (15 - D) Go and create the CartActivity.java
    private void addingToCartList() {
        // to store current date and time to save it with the product
        String saveCurrentTime, saveCurrentDate;

        Calendar calForDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate = currentDate.format(calForDate.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        // get current time
        saveCurrentTime = currentTime.format(calForDate.getTime());

        // we will store the cart list inside the firebase db
        // and also we create a specific node for that
        final DatabaseReference cartListRef = FirebaseDatabase.getInstance().getReference().child("Cart List");

        // we use hash map to store in the database
        final HashMap<String, Object> cartMap = new HashMap<>();
        cartMap.put("pid", productID);
        cartMap.put("pname", productName.getText().toString());
        cartMap.put("price", productPrice.getText().toString());
        // this is the current date and time to the db with the product
        cartMap.put("date", saveCurrentDate);
        cartMap.put("time", saveCurrentTime);
        cartMap.put("quantity", numberButton.getNumber());
        cartMap.put("discount", "");

        // save the current product inside the currrent user with it's phone inside Userview node inside cartlist node
        cartListRef.child("User View").child(Prevalent.currentOnlineUser.getPhone())
                .child("Products").child(productID)
                .updateChildren(cartMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task)
                    {
                        if (task.isSuccessful())
                        {
                            // we do this so the admin can specify the user and the product he want to
                            // purchase
                            // by that we store the cart list two times one for the user view and another for the
                            // admin
                            cartListRef.child("Admin View").child(Prevalent.currentOnlineUser.getPhone())
                                    .child("Products").child(productID)
                                    .updateChildren(cartMap)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task)
                                        {
                                            if (task.isSuccessful())
                                            {
                                                Toast.makeText(ProductDetailsActivity.this, "Added to Cart List.", Toast.LENGTH_SHORT).show();

                                                Intent intent = new Intent(ProductDetailsActivity.this, HomeActivity.class);
                                                startActivity(intent);
                                            }
                                        }
                                    });
                        }
                    }
                });
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
    // (17 - E - 6)
    // add this method here like the one we create inside the CartActivity.java
    private void CheckOrderState()
    {
        DatabaseReference ordersRef;
        ordersRef = FirebaseDatabase.getInstance().getReference().child("Orders").child(Prevalent.currentOnlineUser.getPhone());

        ordersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                // becaouse we only allow one order so passed on sipping state
                // prevint the user from adding any other item to the cart if he already made an order
                if (dataSnapshot.exists())
                {
                    String shippingState = dataSnapshot.child("state").getValue().toString();

                    if (shippingState.equals("shipped"))
                    {
                        state = "Order Shipped";
                    }
                    else if(shippingState.equals("not shipped"))
                    {
                        state = "Order Placed";
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
