package com.kosbaship.ecommerce.Admin;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.kosbaship.ecommerce.Model.Cart;
import com.kosbaship.ecommerce.R;
import com.kosbaship.ecommerce.ViewHolder.CartViewHolder;

public class AdminUserProductsActivity extends AppCompatActivity {

    //(19 - B - 1)
    //(19 - B - 2) GO to AdminNewOrderActivity.java
    // this the variables we need in order to make the recycler view working
    private RecyclerView productsList;
    RecyclerView.LayoutManager layoutManager;
    private DatabaseReference cartListRef;

    //(19 - B - 4) receive the current phone number node
    // this variable to receive the user id which I will use it
    // I will receive it with intent
    // to retrieve the data inside it from the database
    // Cart list >> Admin View >> Phone Number (this var) >> Products >> the product name itself
    private String userID = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_user_products);

        //(19 - B - 4) receive the current phone number node
        // this variable to receive the user id which I will use it
        // I will receive it with intent
        // to retrieve the data inside it from the database
        // Cart list >> Admin View >> Phone Number (this var) >> Products >> the product name itself
        userID = getIntent().getStringExtra("uid");

        //(19 - B - 7)
        //(19 - B - 8) Go to AdminNewOrdersActivity
        // initialize our recycler view
        productsList = findViewById(R.id.products_list);
        productsList.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        productsList.setLayoutManager(layoutManager);


        //(19 - B - 5)
        // we know all the orders stored inside the cart list node passing through some nodes
        // until saved under specefic use node who parchased the products
        cartListRef = FirebaseDatabase.getInstance().getReference()
                .child("Cart List").child("Admin View").child(userID).child("Products");

    }


    //(19 - B - 6)
    // override this method to get the recycler view work
    @Override
    protected void onStart(){
        super.onStart();

        //(19 - B - 6 - a)
        // use firebase recycler option
        // we will pass the Cart model because it contain the data for the product
        // in the way that I want to present it for the admin
        // the product details will presented to the admin in the same way
        // was presented to the user in the cart list before he confirm the final order
        FirebaseRecyclerOptions<Cart> options =
                new FirebaseRecyclerOptions.Builder<Cart>()
                        .setQuery(cartListRef, Cart.class)
                        .build();

        //(19 - B - 6 - b)
        // in simple we get the data from the model and present it on the screen with the holder
        // The data fetched from the server and stored in the model class.
        // 1 - first parameter is the model
        // 2 - second is the CartViewHolder and this the reason
        // I choose to create it as a static class instead of an inner class
        // this class know the main information that should be present in the screen to the user
        // we use it to set the data on the screen
        FirebaseRecyclerAdapter<Cart, CartViewHolder> adapter = new FirebaseRecyclerAdapter<Cart, CartViewHolder>(options) {// pass fire base recyclerview (options)
            @Override
            protected void onBindViewHolder(@NonNull CartViewHolder holder, int position, @NonNull Cart model) {
                //(19 - B - 6 - b - 2)
                // get the Quantity from the cart model and displayed inside the activity
                holder.txtProductQuantity.setText("Quantity = " + model.getQuantity());
                // the same with the price
                holder.txtProductPrice.setText("Price " + model.getPrice() + "$");
                // the same with the name
                holder.txtProductName.setText(model.getPname());
            }

            @NonNull
            @Override
            public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                //(19 - B - 6 - b - 1)
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_items_layout, parent, false);
                CartViewHolder holder = new CartViewHolder(view);
                return holder;
            }
        };
        //(19 - B - 6 - b - 3)
        //finally set the adaptor
        productsList.setAdapter(adapter);
        adapter.startListening();
    }


}
