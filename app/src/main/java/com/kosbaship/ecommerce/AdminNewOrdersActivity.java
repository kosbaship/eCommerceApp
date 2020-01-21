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

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.kosbaship.ecommerce.Model.AdminOrders;

//(18)
//(18 - A) go  to activity_admin_category.xml
// create this activity and then go to activity_admin_category.xml
// to create th btn which will open this activity
public class AdminNewOrdersActivity extends AppCompatActivity {

    //(18 - C)
    //(18 - C - 1)
    // here we declare the reference
    private RecyclerView ordersList;
    private DatabaseReference ordersRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_new_orders);


        //(18 - C - 2 - a)
        // create the reference for the orders node
        // which I will need to get the orders from the db
        // and display it to the admin
        ordersRef = FirebaseDatabase.getInstance().getReference().child("Orders");


        //(18 - C - 2 - b)
        // create the reference for the orders recycler view and display it on the screen
        ordersList = findViewById(R.id.orders_list);
        ordersList.setLayoutManager(new LinearLayoutManager(this));


    }


    //(18 - C - 3)
    // here  we will receive and display the orders
    // using Firebase recycler adaptor to regulate the items
    @Override
    protected void onStart() {
        super.onStart();

        //(18 - C - 3 - a) go and create model class for the orders node
        //(18 - C - 3 - b)
        //(18 - C - 3 - b - 1) use firebase recycler to pass it to the static class holder in the next step
        // so we will need
        // 1 - model class as bridge to the database (AdminOrders)
        // 2 - list of data so we have order node in the firebase db (ordersRef)
        FirebaseRecyclerOptions<AdminOrders> options =
                new FirebaseRecyclerOptions.Builder<AdminOrders>()
                        .setQuery(ordersRef, AdminOrders.class)
                        .build();

        //(18 - C - 3 - b - 2) use firebase recycler adapter
        // which will need 1 - model class  (AdminOrders)
        //                 2 - static class
        //                     which we will create here inside
        //                     this activity to link the recycler
        //                      with the views inside the app
        //                      and this will let us to change the text inside
        //                      the app to the user.
        //                     (AdminOrdersViewHolder) and pass to it the firebase recycler option
        FirebaseRecyclerAdapter<AdminOrders, AdminOrdersViewHolder> adapter =
                new FirebaseRecyclerAdapter<AdminOrders, AdminOrdersViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull AdminOrdersViewHolder holder, final int position, @NonNull final AdminOrders model) {
                        // (19) go and Create AdminUserProductsActivity.java start with the xml file
                        //(18 - C - 3 - b - 6)
                        // display the values (comes from db) in the fields (in the app  to the user)
                        holder.userName.setText("Name: " + model.getName());
                        holder.userPhoneNumber.setText("Phone: " + model.getPhone());
                        holder.userTotalPrice.setText("Total Amount =  $" + model.getTotalAmount());
                        holder.userDateTime.setText("Order at: " + model.getDate() + "  " + model.getTime());
                        holder.userShippingAddress.setText("Shipping Address: " + model.getAddress() + ", " + model.getCity());



                        //(19 - B - 2)
                        // this button will open an activity and present the whole order of the user to the admin
                        // and we will send the phone number (the user "buyer" id) node which had a node inside it
                        // with all  the product the user parcheced
                        holder.ShowOrdersBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view)
                            {

                                //(19 - B - 8)
                                // when the admin click on any order in the order list
                                // it should get the phone number on the user of this order
                                // we get the position of this order and then we will get the key
                                String uID = getRef(position).getKey();


                                //(19 - B - 3)
                                //(19 - B - 4) go to AdminUserProductsActivity.java
                                // create an intent when the Admin clicks on the order it will open an activity
                                // to show the whole order for this user
                                Intent intent = new Intent(AdminNewOrdersActivity.this, AdminUserProductsActivity.class);
                                // this is the phone of the user in the list item
                                // send it with the intent to let the next activity use this phone
                                // to retrieve the data from the database
                                intent.putExtra("uid", uID);
                                startActivity(intent);
                            }
                        });

//                        holder.itemView.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View view)
//                            {
//                                CharSequence options[] = new CharSequence[]
//                                        {
//                                                "Yes",
//                                                "No"
//                                        };
//
//                                AlertDialog.Builder builder = new AlertDialog.Builder(AdminNewOrdersActivity.this);
//                                builder.setTitle("Have you shipped this order products ?");
//
//                                builder.setItems(options, new DialogInterface.OnClickListener() {
//                                    @Override
//                                    public void onClick(DialogInterface dialogInterface, int i)
//                                    {
//                                        if (i == 0)
//                                        {
//                                            String uID = getRef(position).getKey();
//
//                                            RemoverOrder(uID);
//                                        }
//                                        else
//                                        {
//                                            finish();
//                                        }
//                                    }
//                                });
//                                builder.show();
//                            }
//                        });
                    }

                    @NonNull
                    @Override
                    public AdminOrdersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
                    {
                        //(18 - C - 3 - b - 5)
                        // link and inflate the orders_layout.xml item with this data
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.orders_layout, parent, false);
                        return new AdminOrdersViewHolder(view);
                    }
                };
        //(18 - C - 3 - b - 4)
        // these a static lines
        ordersList.setAdapter(adapter);
        adapter.startListening();


    }
    //(18 - C - 3 - b - 3)
    // create the static class to be the recycler view holder
    // and this holder will access the (list item layout) (orders_layout.xml)
    public static class AdminOrdersViewHolder extends RecyclerView.ViewHolder
    {

        public TextView userName, userPhoneNumber, userTotalPrice, userDateTime, userShippingAddress;
        public Button ShowOrdersBtn;


        public AdminOrdersViewHolder(View itemView)
        {
            super(itemView);


            userName = itemView.findViewById(R.id.order_user_name);
            userPhoneNumber = itemView.findViewById(R.id.order_phone_number);
            userTotalPrice = itemView.findViewById(R.id.order_total_price);
            userDateTime = itemView.findViewById(R.id.order_date_time);
            userShippingAddress = itemView.findViewById(R.id.order_address_city);
            ShowOrdersBtn = itemView.findViewById(R.id.show_all_products_btn);
        }
    }


//    private void RemoverOrder(String uID)
//    {
//        ordersRef.child(uID).removeValue();
//    }

}
