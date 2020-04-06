package com.kosbaship.ecommerce.Admin;

import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.kosbaship.ecommerce.Interface.ItemClickListner;
import com.kosbaship.ecommerce.Model.Products;
import com.kosbaship.ecommerce.R;
import com.kosbaship.ecommerce.ViewHolder.ProductViewHolder;
import com.squareup.picasso.Picasso;

public class AdminCheckNewProductActivity extends AppCompatActivity {

    // (27 - B - 5)
    // add the recyclerview
    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    // (27 - C - 1)
    //Declare db reference to get all un approved products
    DatabaseReference unVerifiedProductsRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_check_new_product);

        // (27 - C - 2)
        // get reference to the Products node
        unVerifiedProductsRef = FirebaseDatabase.getInstance().getReference().child("Products");

        // (27 - B - 6)
        //access it by the id
        recyclerView =  findViewById(R.id.admin_product_checklist);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
    }

    @Override
    protected void onStart() {
        super.onStart();
        // (27 - C - 3)
        // create the firebase query to retrieve all un verified product
        FirebaseRecyclerOptions<Products> options =
                new FirebaseRecyclerOptions.Builder<Products>()
                .setQuery(unVerifiedProductsRef.orderByChild("productstate").equalTo("Not Approved"), Products.class)
                .build();

        // (27 - C - 4)
        //                      we will use the same layout Product Item Layout
        // the view holder allow us to access our product item layout
        // pass the query to it
        FirebaseRecyclerAdapter<Products, ProductViewHolder> adapter =
                new FirebaseRecyclerAdapter<Products, ProductViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull ProductViewHolder holder, int position, @NonNull final Products model) {
                        // (27 - C - 4 - Two)
                        // model is an instance of the Class Products
                        holder.txtProductName.setText(model.getPname());
                        holder.txtProductDescription.setText(model.getDescription());
                        holder.txtProductPrice.setText("Price = " + model.getPrice() + "$");
                        // these two steps to check u are able to use picasso
                        // (13 - D - 5 - d - Two - 2)
                        // (13 - D - 5 - d - Two - 1) go to Build.gradle
                        // we use Picasso Library to display pics
                        // or in other word to retrieve pics from db and display it
                        Picasso.get().load(model.getImage()).into(holder.imageView);

                        // (27 - C - 4 - Three)
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

                                AlertDialog.Builder builder = new AlertDialog.Builder(AdminCheckNewProductActivity.this);
                                builder.setTitle("Do you want to Approve this Product, Are you Sure?");
                                // get the yes or no
                                builder.setItems(options, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        // if the admin clicks Yes we have to change Product state to approved
                                        if (which == 0) // zero here refer to "Yes btn" which is the first option
                                        {
                                            //(27 - C - 5)
                                            ChangeProductState(productID);
                                        }
                                        else // otherwise he choose "No"
                                        {

                                        }
                                    }
                                });
                                //(27 - C - 6)
                                builder.show();
                            }
                        });


                    }

                    @NonNull
                    @Override
                    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                        // (27 - C - 4 - One)
                        // access our product layout item here
                        // this the shape that the product details presented to the user
                        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.product_items_layout, viewGroup, false);
                        ProductViewHolder holder = new ProductViewHolder(view);
                        return holder;
                    }
                };

        //(27 - C - 7)
        //(28) Goto activity_home_seller.xml
        // start listening to the adaptor
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    //(27 - C - 5)
    // deal wit the db to change product state
    private void ChangeProductState(String productID) {
        // get reference directly to the clicked product with it's id
        unVerifiedProductsRef
                .child(productID)
                .child("productstate")
                .setValue("Approved")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            onStart();
                            Toast.makeText(AdminCheckNewProductActivity.this,
                                    "That item has been approved, and " +
                                            "it's now for available sale" +
                                            "from the seller.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
