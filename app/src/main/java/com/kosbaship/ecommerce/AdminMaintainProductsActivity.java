package com.kosbaship.ecommerce;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class AdminMaintainProductsActivity extends AppCompatActivity {


    //(21 - C - 5 - a)
    // declare this views
    private Button applyChangesBtn, deleteBtn;
    private EditText name, price, description;
    private ImageView imageView;

    //(21 - C - 6 - a)
    // declare this id to receive it from the intent
    // to be all to do actions on this product
    private String productID = "";
    //(21 - C - 7 - a)
    // declare the db reference
    private DatabaseReference productsRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_maintain_products);

        //(21 - C - 6 - b)
        // get the id from the intent
        productID = getIntent().getStringExtra("pid");
        //(21 - C - 7 - b)
        // get reference to the current product inside the Products node in the db
        productsRef = FirebaseDatabase.getInstance().getReference().child("Products").child(productID);

        //(21 - C - 5 - b)
        // get reference to this views
        applyChangesBtn = findViewById(R.id.apply_changes_btn);
        name = findViewById(R.id.product_name_maintain);
        price = findViewById(R.id.product_price_maintain);
        description = findViewById(R.id.product_description_maintain);
        imageView = findViewById(R.id.product_image_maintain);
        deleteBtn = findViewById(R.id.delete_product_btn);

        //(21 - C - 8 - a)
        // create and call
        // this helper method to display a this current product details
        displaySpecificProductInfo();

        //(21 - C - 9)
        //(21 - C - 10) Go to HomeActivity.java
        // apply changes that the admin has do
        applyChangesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {   
                // call this helper method
                applyChanges();
            }
        });

        //(21 - C - 12)
        // add this btn to allow the admin delete specific product from the db
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                //(21 - C - 12 - a)
                //create and call
                deleteThisProduct();
            }
        });

    }
    //(21 - C - 12 - b)
    // apply db remove value methods
    private void deleteThisProduct() {
        //productsRef   ==> refer directly to the current product into the db
        productsRef.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task)
            {
                Intent intent = new Intent(AdminMaintainProductsActivity.this, AdminCategoryActivity.class);
                startActivity(intent);
                finish();

                Toast.makeText(AdminMaintainProductsActivity.this, "The Product Has Removed successfully.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // this is a sperated code for apply changes on the product
    private void applyChanges() {
        // get the data from the admin on the screen
        String pName = name.getText().toString();
        String pPrice = price.getText().toString();
        String pDescription = description.getText().toString();

        // here we check if the admin left the input fields empty
        if (pName.isEmpty())
        {
            Toast.makeText(this, "Write down Product Name.", Toast.LENGTH_SHORT).show();
        }
        else if (pPrice.isEmpty())
        {
            Toast.makeText(this, "Write down Product Price.", Toast.LENGTH_SHORT).show();
        }
        else if (pDescription.isEmpty())
        {
            Toast.makeText(this, "Write down Product Description.", Toast.LENGTH_SHORT).show();
        }
        else  // store changes inside the database
        {
            // create the hashmap that we will pass to the database
            HashMap<String, Object> productMap = new HashMap<>();
            productMap.put("pid", productID);
            productMap.put("description", pDescription);
            productMap.put("price", pPrice);
            productMap.put("pname", pName);

            // apply the update query
            productsRef.updateChildren(productMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task)
                {
                    // tell the admin what happening
                    // and send him to the admin AdminCategoryActivity
                    if (task.isSuccessful())
                    {
                        Toast.makeText(AdminMaintainProductsActivity.this, "Changes applied successfully.", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(AdminMaintainProductsActivity.this, AdminCategoryActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
            });
        }
    }

    //(21 - C - 8 - b)
    // display a this current product details
    private void displaySpecificProductInfo() {

        // we use addValueEventListener
        // To read data at a path and listen for changes
        productsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // if the data u listen to is exist in this path
                // get the values and display them to the user
                if (dataSnapshot.exists()) {

                    String pName = dataSnapshot.child("pname").getValue().toString();
                    String pPrice = dataSnapshot.child("price").getValue().toString();
                    String pDescription = dataSnapshot.child("description").getValue().toString();
                    String pImage = dataSnapshot.child("image").getValue().toString();

                    // display the values to the admin in the edit texts
                    name.setText(pName);
                    price.setText(pPrice);
                    description.setText(pDescription);
                    Picasso.get().load(pImage).into(imageView);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
