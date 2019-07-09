package com.kosbaship.ecommerce;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.kosbaship.ecommerce.Model.Cart;
import com.kosbaship.ecommerce.Prevalent.Prevalent;
import com.kosbaship.ecommerce.ViewHolder.CartViewHolder;

public class CartActivity extends AppCompatActivity {
    // (15 - D - 2 - a)
    // declare the views on the screen
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;

    private Button NextProcessBtn;
    private TextView txtTotalAmount, txtMsg1;

    private int overTotalPrice = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        // (15 - D - 2 - b)
        // (15 - D - 2 - c) go and create the layout to single item on the screen (cart_item_layout)
        // get reference to the views on the screen
        recyclerView = findViewById(R.id.cart_list);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        NextProcessBtn = findViewById(R.id.next_btn);
        txtTotalAmount = findViewById(R.id.total_price);
        txtMsg1 = findViewById(R.id.msg1);
    }
    //                          (16)
    // retreive the cart list items by using the firebase recycler adapter
    @Override
    protected void onStart()
    {
        super.onStart();

//        CheckOrderState();

        // (16 - A)
        // get reference to the database
        final DatabaseReference cartListRef = FirebaseDatabase.getInstance().getReference().child("Cart List");
        // (16 - B - 1)
        // (16 - B - 2) go and create the model Cart.java
        // (16 - B - 3)
        // use the firebase recyclar view (this is the initializiation step)
        FirebaseRecyclerOptions<Cart> options =
                new FirebaseRecyclerOptions.Builder<Cart>()
                        .setQuery(cartListRef.child("User View") // the query will be our reference to the database then the next node is the User View
                                .child(Prevalent.currentOnlineUser.getPhone()) // get the next node which the user id
                                .child("Products"), Cart.class) // then the last node (products) that contain all the items && then pass the model class
                        .build();

        // (16 - B - 4)
        // (16 - B - 5) Go and create the cart view holder of the adapter view
        // this is the execute step (recyclar adapter)
        // (16 - B - 6) complete the RecyclerAdapter
        // (16 - B - 7) Go to HomeActivity.java
        FirebaseRecyclerAdapter<Cart, CartViewHolder> adapter
                = new FirebaseRecyclerAdapter<Cart, CartViewHolder>(options) { // pass fire base recyclerview (options)
            @Override
            protected void onBindViewHolder(@NonNull CartViewHolder holder, int position, @NonNull final Cart model) {
                // (16 - B - 6 - Second)
                // get the Quantity from the cart model and displayed inside the activity
                holder.txtProductQuantity.setText("Quantity = " + model.getQuantity());
                // the same with the price
                holder.txtProductPrice.setText("Price " + model.getPrice() + "$");
                // the same with the name
                holder.txtProductName.setText(model.getPname());

                int oneTyprProductTPrice = ((Integer.valueOf(model.getPrice()))) * Integer.valueOf(model.getQuantity());
                overTotalPrice = overTotalPrice + oneTyprProductTPrice;

                // (16 - C)
                // if the user click on a product inside the cart acitity here we give him the right to
                // edite or remove this product
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view)
                    {
                        // create the options that apears in the dialog box
                        // which will pop up when the user click on the product
                        CharSequence options[] = new CharSequence[]
                                {
                                        "Edit", // this by defualt take index number 0
                                        "Remove" // this by defualt take index number 1
                                };
                        // create the alert dialog to pop up when the user click on the product
                        AlertDialog.Builder builder = new AlertDialog.Builder(CartActivity.this);
                        builder.setTitle("Cart Options:");

                        // this how we create a click listener for every option in the alert dialog
                        // and also we pass the options as argument with the click listener in the builder
                        builder.setItems(options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i)
                            {
                                if (i == 0)  // here we check with the index number
                                {
                                    // sent the user to ProductDetailsActivity to edit the product as he want
                                    Intent intent = new Intent(CartActivity.this, ProductDetailsActivity.class);
                                    // becaouse in the ProductDetailsActivity we received the prodect id when the user open a product from the home activity
                                    // we need also to send the product id when we open the details activity from here
                                    // becaouse the details activity use this id to get it's proparties from the DB
                                    // the name should match the recive name in the activity
                                    intent.putExtra("pid", model.getPid());
                                    startActivity(intent);
                                }
                                if (i == 1) // here we check with the index number
                                {
                                    // do remove the item from the curt list in the user view node
                                    // open the database to see the hirarchky
                                    cartListRef.child("User View")
                                            .child(Prevalent.currentOnlineUser.getPhone())
                                            .child("Products")
                                            .child(model.getPid())
                                            .removeValue()
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task)
                                                {
                                                    if (task.isSuccessful())
                                                    {
                                                        Toast.makeText(CartActivity.this, "Item removed successfully.", Toast.LENGTH_SHORT).show();
                                                        // send the user to the home activity
                                                        Intent intent = new Intent(CartActivity.this, HomeActivity.class);
                                                        startActivity(intent);
                                                    }
                                                }
                                            });
                                }
                            }
                        });
                        // you have to excute this in order to make the aleart dialog to show
                        builder.show();
                    }
                });
            }

            @NonNull
            @Override
            public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                // (16 - B - 6 - First)
                // this where we inflate from
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_items_layout, parent, false);
                CartViewHolder holder = new CartViewHolder(view);
                return holder;
            }
        };
        // (16 - B - 8)
        // set the adaptor to the recycler view
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }


}
