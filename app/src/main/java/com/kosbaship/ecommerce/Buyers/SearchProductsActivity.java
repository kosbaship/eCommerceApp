package com.kosbaship.ecommerce.Buyers;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.kosbaship.ecommerce.Model.Products;
import com.kosbaship.ecommerce.R;
import com.kosbaship.ecommerce.ViewHolder.ProductViewHolder;
import com.squareup.picasso.Picasso;

public class SearchProductsActivity extends AppCompatActivity {
    //(20 - B)
    //(20 - B - 1)
    // declare this variables
    private Button SearchBtn;
    private EditText inputText;
    private RecyclerView searchList;
    //(20 - C - 2)
    // create this variable to store the user search phrase premenently
    private String SearchInput;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_products);

        //(20 - B - 2)
        //(20 - B - 3) go to activity_home_drawer.xml to set the button which will open this activity
        // get reference to this views on the screen
        inputText = findViewById(R.id.search_product_name);
        SearchBtn = findViewById(R.id.search_btn);
        searchList = findViewById(R.id.search_list);
        searchList.setLayoutManager(new LinearLayoutManager(SearchProductsActivity.this));




        //(20 - C)
        //(21) Go To activity_seller_category,xml
        //(20 - C - 1)
        // set on click li.. to the Search button
        SearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                //(20 - C - 3)
                // get the data from the user (the search word)
                SearchInput = inputText.getText().toString();
                // call on start method which we will programe inside it the firebase recycler adaptor
                onStart();
            }
        });
    }

    //(20 - C - 4)
    //
    @Override
    protected void onStart() {
        super.onStart();

        //(20 - C - 4 - a)
        // define the database reference which refer to products node
        // here it's like showing all the products to the user and he will search about specefic one
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Products");

        //(20 - C - 4 - b)
        // create this recycler option to pass it to the view holder
        // use (setQuery) helper methods do perform the search
        // reference.orderByChild("pname")  ===> in which attribute of the
        // product i want to match the search input with
        //
        // startAt(SearchInput) ===> this the variable that save the user word or phrase
        FirebaseRecyclerOptions<Products> options =
                new FirebaseRecyclerOptions.Builder<Products>()
                        .setQuery(reference.orderByChild("pname").startAt(SearchInput).endAt(SearchInput + "\uf8ff"), Products.class)
                        .build();

        //(20 - C - 4 - c)
        // add the recycler adaptor the search list
        // hint : we will render the products like we view it on the home page
        //
        //                  here we will pass our layout
        //                  just like home page rendering
        FirebaseRecyclerAdapter<Products, ProductViewHolder> adapter =
                new FirebaseRecyclerAdapter<Products, ProductViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull ProductViewHolder holder, int position, @NonNull final Products model)
                    {
                        //(20 - C - 4 - c - 2)
                        // match the product details from the db with the
                        // views on the screen
                        holder.txtProductName.setText(model.getPname());
                        holder.txtProductDescription.setText(model.getDescription());
                        holder.txtProductPrice.setText("Price = " + model.getPrice() + "$");
                        Picasso.get().load(model.getImage()).into(holder.imageView);

                        // create an event listener which will render the clicked product details in
                        // the product activity
                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view)
                            {
                                Intent intent = new Intent(SearchProductsActivity.this, ProductDetailsActivity.class);
                                intent.putExtra("pid", model.getPid());
                                startActivity(intent);
                            }
                        });
                    }

                    @NonNull
                    @Override
                    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
                    {
                        //(20 - C - 4 - c - 1)
                        // access our product layout item here
                        // this the shape that the product details presented with to the user
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_items_layout, parent, false);
                        ProductViewHolder holder = new ProductViewHolder(view);
                        return holder;
                    }
                };

        //(20 - C - 4 - d)
        // perform the adaptor to the recycler view
        searchList.setAdapter(adapter);
        adapter.startListening();
    }
}
