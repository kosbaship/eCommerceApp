package com.kosbaship.ecommerce;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;
import android.widget.EditText;

public class SearchProductsActivity extends AppCompatActivity {
    //(20 - B)
    //(20 - B - 1)
    // declare this variables
    private Button SearchBtn;
    private EditText inputText;
    private RecyclerView searchList;
    private String SearchInput;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_products);

        //(20 - B - 2)
        //(20 - B - 3) go to activity_home_drawer.xml to set the button wich will open this activity
        // get reference to this views on the screen
        inputText = findViewById(R.id.search_product_name);
        SearchBtn = findViewById(R.id.search_btn);
        searchList = findViewById(R.id.search_list);
        searchList.setLayoutManager(new LinearLayoutManager(SearchProductsActivity.this));

    }
}
