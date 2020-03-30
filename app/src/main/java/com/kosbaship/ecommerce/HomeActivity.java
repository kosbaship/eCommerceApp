package com.kosbaship.ecommerce;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.kosbaship.ecommerce.Model.Products;
import com.kosbaship.ecommerce.Prevalent.Prevalent;
import com.kosbaship.ecommerce.ViewHolder.ProductViewHolder;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import io.paperdb.Paper;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    //                      (13 - D - 5 - c - One)
    //set the database Reference
    private DatabaseReference ProductsRef;
    // (13 - D - 5 - e - 1)
    // initialize the recyclerView
    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    //(21 - C)
    //(21 - C - 1)
    // create this var to
    // receive the data that comes with the admin previliege
    private String type = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        //(21 - C - 2)
        // receive the data from the intent
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        // we make this small validation because the user intent comes empty
        if (bundle != null) {
            type = getIntent().getExtras().get("Admin").toString();
        }



        //                      (13 - D - 5 - c - Two)
        // initialize this db reference
        ProductsRef = FirebaseDatabase.getInstance().getReference().child("Products");


        // (13 - B - 2 - b)
        // enable the logout button so we need to user the paper library
        Paper.init(this);


        Toolbar toolbar = findViewById(R.id.toolbar);
        //                      (13 - B)
        // (13 - B - 1)
        // st a title to the toolbar
        toolbar.setTitle("Home");
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // (16 - B - 7)
                // (16 - B - 8) Go back to the CartActivity.java
                //(21 - C - 10)
                //(21 - C - 11) Go To activity_admin_maintain_products
                // do not allow the admin to open the cart
                if (!type.equals("Admin")) {
                    Intent intent = new Intent(HomeActivity.this, CartActivity.class);
                    startActivity(intent);
                } else {
                    Snackbar.make(view, "You Not allowed to do that", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            }
        });



        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        navigationView.setNavigationItemSelectedListener(this);

        //                                  (13 - C)
        // (13 - C - 1)
        // (13 - C - 2) go to LoginActivity.java
        // initialize the header view to display the user name after he logged in
        // and also the user image
        View headerView = navigationView.getHeaderView(0);
        TextView userNameTextView = headerView.findViewById(R.id.user_profile_name);
        CircleImageView profileImageView = headerView.findViewById(R.id.user_profile_image);

        // we add this validation becaouse we create the admin only with the name and password
        // inside the db without any other details like pic and name
        if (!type.equals("Admin")) {
            // (13 - C - 3)
            // (13 - D) Go and Create product_items_layout.xml
            // set the current user name for him
            //                  uninstall ur app and run to test it
            userNameTextView.setText(Prevalent.currentOnlineUser.getName());

            //                      (14 - B - 5)
            //(15) go and create ProductDelailsActivity.java
            // get the user image from the db and
            // display it in the drawer home activity
            // get the image from the current online user
            // display this while the image loading
            // dislpay this image here
            Picasso.get().load(Prevalent.currentOnlineUser.getImage()).placeholder(R.drawable.profile).into(profileImageView);
        }
        // (13 - D - 5 - e - 2)
        // get reference to it
        recyclerView = findViewById(R.id.recycler_menu);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

    }

    //                          (13 - D - 5)
    // (14) Go and create SettingsActivity.java
    // by using Firebase Recycler Adapter we will retrieve out Products
    @Override
    protected void onStart() {
        super.onStart();
        //                      (13 - D - 5 - c)
        // (13 - D - 5 - c - Three)
        // (13 - D - 5 - a)   go to Build.gradle
        // (13 - D - 5 - b)   go to Products.java
        // query all the products from the product node in the db
        FirebaseRecyclerOptions<Products> options =
                new FirebaseRecyclerOptions.Builder<Products>()
                        .setQuery(ProductsRef, Products.class)
                        .build();
        // (13 - D - 5 - d)
        // add firebase recycler adapter (and pass to it the FirebaseRecyclerOptions obj that contain our query)
        // this need the model to save the info inside it locally
        // and the ViewHolder that will tell which data we are
        // intrested in to display for the user
        FirebaseRecyclerAdapter<Products, ProductViewHolder> adapter =
                new FirebaseRecyclerAdapter<Products, ProductViewHolder>(options) {
                    // (13 - D - 5 - d - Two)
                    // display the data on the text views
                    @Override
                    protected void onBindViewHolder(@NonNull ProductViewHolder holder, int position, @NonNull final Products model)
                    {
                        // model is an instance of the Class Products
                        holder.txtProductName.setText(model.getPname());
                        holder.txtProductDescription.setText(model.getDescription());
                        holder.txtProductPrice.setText("Price = " + model.getPrice() + "$");
                        // (13 - D - 5 - d - Two - 2)
                        // (13 - D - 5 - d - Two - 1) go to Build.gradle
                        // we use Picasso Library to display pics
                        // or in other word to retrieve pics from db and display it
                        Picasso.get().load(model.getImage()).into(holder.imageView);

                        //                              (15 - C)
                        // (15 - D) Go and create the CartActivity.java
                        // (15 - C - 1) set up the onclick listener
                        // (15 - C - 2) goto the ProductDetailsActivity.java
                        // create an event listener which will render the clicked product details in
                        // the product activity
                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                //(21 - C - 3)
                                //(21 - C - 4) Go to activity_admin_maintain_products.xml
                                // here specefy who click on the product the
                                // admin or the user and direct each one to his
                                // right activty
                                if (type.equals("Admin"))
                                {
                                    Intent intent = new Intent(HomeActivity.this, AdminMaintainProductsActivity.class);
                                    // pid is product id stored into the db get it with the model class
                                    // send the product ID == I think we use the Id if we want to delete it
                                    intent.putExtra("pid", model.getPid());
                                    startActivity(intent);
                                }
                                else
                                {
                                    Intent intent = new Intent(HomeActivity.this, ProductDetailsActivity.class);
                                    // pid is product id stored into the db get it with the model class
                                    // send the product ID == I think we use the Id if we want to delete it
                                    intent.putExtra("pid", model.getPid());
                                    startActivity(intent);
                                }
                            }
                        });
                    }
                    // (13 - D - 5 - d - One)
                    // access our product layout item here
                    @NonNull
                    @Override
                    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
                    {
                        // access our product layout item here
                        // this the shape that the product details presented with to the user
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_items_layout, parent, false);
                        ProductViewHolder holder = new ProductViewHolder(view);
                        return holder;
                    }
                };

        // (13 - D - 5 - e - 3)
        // set the adapter to the recycler view
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        Intent intent;
        // (13 - B - 2)
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        // (13 - B - 2 - a)
        // modify switch statement
        switch (id){
            case R.id.nav_cart:
                // Handle the Cart action
                // (16 - B - 7)
                // (16 - B - 8) Go back to the CartActivity.java
                //(21 - C - 10)
                // do not allow the admin to open the cart
                if (!type.equals("Admin")) {
                     intent = new Intent(HomeActivity.this, CartActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(HomeActivity.this, "You Not allowed to do that", Toast.LENGTH_SHORT)
                            .show();
                }
                break;
            //(20 - B - 4)
            //(20 - C) Go to SearchProductsActivity.java
            // create the intent for the SearchProductsActivity.java
            case R.id.nav_search:
                //(21 - C - 10)
                // do not allow the admin to open this
                if (!type.equals("Admin")) {
                    intent = new Intent(HomeActivity.this, SearchProductsActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(HomeActivity.this, "You Not allowed to do that", Toast.LENGTH_SHORT)
                            .show();
                }
                break;
            case R.id.nav_categories:
                //(21 - C - 10)
                // do not allow the admin to open this
                if (type.equals("Admin")) {
                    Toast.makeText(HomeActivity.this, "You Not allowed to do that", Toast.LENGTH_SHORT)
                            .show();
                }
                break;
            case R.id.nav_settings:
                //(14 - A - 2)
                //(14 - B) Go to SettinsActivity.java
                // open settings activity
                //(21 - C - 10)
                // do not allow the admin to open this
                if (!type.equals("Admin")) {
                    intent = new Intent(HomeActivity.this, SettinsActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(HomeActivity.this, "You Not allowed to do that", Toast.LENGTH_SHORT)
                            .show();
                }
                break;
            case R.id.nav_logout:
                // (13 - B - 2 - c)
                //enable the logout button and then send the user to the main activity
                Paper.book().destroy();
                intent = new Intent(HomeActivity.this, MainActivity.class);
                // to close all the existing activities we need to do some flags in this Intent
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                break;
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
