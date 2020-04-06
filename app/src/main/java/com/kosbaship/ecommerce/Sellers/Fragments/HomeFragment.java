package com.kosbaship.ecommerce.Sellers.Fragments;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.kosbaship.ecommerce.Model.Products;
import com.kosbaship.ecommerce.R;
import com.kosbaship.ecommerce.ViewHolder.SellerViewHolder;
import com.squareup.picasso.Picasso;

public class HomeFragment extends Fragment {


    //(28 - B - 1)
    //declare
    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    DatabaseReference unVerifiedProductsRef;
    View parentHolder;


    //(24 - E - 1) Override this method
    //(24 - F) Go to SellerHomeActivity.java
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // (24 - E -2)
        // change the return to this
        //(28 - B - 2) use the parentHolder var
        parentHolder = inflater.inflate(R.layout.fragment_home, container, false);

        //(28 - B - 4)
        // get reference to the Products node
        unVerifiedProductsRef = FirebaseDatabase.getInstance().getReference().child("Products");
        //access it by the id
        recyclerView =  parentHolder.findViewById(R.id.seller_home_recycler);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        //(28 - B - 3)
        // always make the return after modifications
        return parentHolder;
    }

    //(28 - B - 5)
    // when the user login retrieve all un verified products to the seller
    // and we also allow the seller to delete any unwanted product
    // if the item not available any more for example
    @Override
    public void onStart() {
        super.onStart();

        //(28 - B - 6)
        //(28 - C) go an create seller_item_layout.xml
        // copy the same code in AdminCheckNewProductActivity.java
        //             but i will add another node to get access to all current seller
        //             product so we will get his sid and compare it with his id
        // create the firebase query to retrieve all un verified product
        FirebaseRecyclerOptions<Products> options =
                new FirebaseRecyclerOptions.Builder<Products>()
                        .setQuery(
                                unVerifiedProductsRef
                                        .orderByChild("sID")
                                        .equalTo(FirebaseAuth.getInstance().getCurrentUser().getUid()),
                                Products.class)
                        .build();

        //(28 - C - 6) replace all ProductViewHolder with SellerViewHolder
        //                      we will use the same layout Product Item Layout
        // the view holder allow us to access our product item layout
        // pass the query to it
        FirebaseRecyclerAdapter<Products, SellerViewHolder> adapter =
                new FirebaseRecyclerAdapter<Products, SellerViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull SellerViewHolder holder, int position, @NonNull final Products model) {
                        // model is an instance of the Class Products
                        holder.txtProductName.setText(model.getPname());
                        holder.txtProductDescription.setText(model.getDescription());
                        //(28 - C - 8) get to  Products.java
                        //(28 - C - 9)
                        holder.txtProductPrice.setText("Price = " + model.getPrice() + "$");
                        holder.txtProductStatus.setText("State : " + model.getProductstate());
                        // these two steps to check u are able to use picasso
                        // (13 - D - 5 - d - Two - 2)
                        // (13 - D - 5 - d - Two - 1) go to Build.gradle
                        // we use Picasso Library to display pics
                        // or in other word to retrieve pics from db and display it
                        Picasso.get().load(model.getImage()).into(holder.imageView);


                        // this dialog to allow the admin to approve the product to be
                        // displayed as one for sale
                        holder.imageView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                // get the product id
                                final String productID = model.getPid();

                                // create a dialog box
                                CharSequence options[] = new CharSequence[]{
                                        // here we will pass the options (btns) of the dialog box
                                        "Yes",
                                        "No"
                                };

                                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                                builder.setTitle("Do you want to Delete this Product, Are you Sure?");
                                // get the yes or no
                                builder.setItems(options, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        // if the admin clicks Yes we have to change Product state to approved
                                        if (which == 0) // zero here refer to "Yes btn" which is the first option
                                        {
                                            //(28 - C - 7)
                                            RemoveProduct(productID);
                                        }
                                        else // otherwise he choose "No"
                                        {

                                        }
                                    }
                                });

                                builder.show();
                            }
                        });


                    }

                    @NonNull
                    @Override
                    public SellerViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

                        //(28 - C - 2) change to inflate(R.layout.seller_item_layout
                        //(28 - C - 3) go and Create SellerViewHolder.java
                        // access our product layout item here
                        // this the shape that the product details presented to the user
                        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.seller_item_layout, viewGroup, false);
                        SellerViewHolder holder = new SellerViewHolder(view);
                        return holder;
                    }
                };

        // start listening to the adaptor
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    //(28 - C - 7)
    private void RemoveProduct(String productID) {
        // this refer to the Product node
        // so the order with this position remove it for me plz
        unVerifiedProductsRef.child(productID).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(getContext(), "The Product has been deleted Successfully.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
