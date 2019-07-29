package com.kosbaship.ecommerce;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ConfirmFinalOrderActivity extends AppCompatActivity {

    // (17 - B - 1) declare variables to the views on the screen
    // (17 - C) Go to CartActivity.java
    private EditText nameEditText, phoneEditText, addressEditText, cityEditText;
    private Button confirmOrderBtn;

    // (17 - C - 4) declare the totalAmount varible so u can user it
    // to recieve the coming text intent that holds the over all price
    // calculated inside the cart activity
    private String totalAmount = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_final_order);

        // (17 - C - 5) recieve the coming text intent that holds the over all price
        //    // calculated inside the cart activity
        totalAmount = getIntent().getStringExtra("Total Price");
        // display a toast massage with the total price of the order
        Toast.makeText(this, "Total Price =  $ " + totalAmount, Toast.LENGTH_SHORT).show();


        // (17 - B - 2) get reference to the views on the screen
        confirmOrderBtn = (Button) findViewById(R.id.confirm_final_order_btn);
        nameEditText = (EditText) findViewById(R.id.shippment_name);
        phoneEditText = (EditText) findViewById(R.id.shippment_phone_number);
        addressEditText = (EditText) findViewById(R.id.shippment_address);
        cityEditText = (EditText) findViewById(R.id.shippment_city);



    }
}
