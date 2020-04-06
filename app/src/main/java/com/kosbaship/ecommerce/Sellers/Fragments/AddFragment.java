package com.kosbaship.ecommerce.Sellers.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kosbaship.ecommerce.R;
import com.kosbaship.ecommerce.Sellers.SellerAddNewProductActivity;

public class AddFragment extends Fragment implements View.OnClickListener  {


    View parentHolder;

    //(24 - E - 1) Override this method
    //(24 - F) Go to SellerHomeActivity.java
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // (24 - E -2)
        // change the return to this
        parentHolder = inflater.inflate(R.layout.fragment_add, container, false);


        // (10 - B - 1, 2, 3)
        // declare the products views on the screen
        // get reference to the views on the screen
        // set click listeners for the events on the images (Directly)
        parentHolder.findViewById(R.id.t_shirts).setOnClickListener(this);
        parentHolder.findViewById(R.id.sports_t_shirts).setOnClickListener(this);
        parentHolder.findViewById(R.id.female_dresses).setOnClickListener(this);
        parentHolder.findViewById(R.id.sweathers).setOnClickListener(this);

        parentHolder.findViewById(R.id.glasses).setOnClickListener(this);
        parentHolder.findViewById(R.id.hats_caps).setOnClickListener(this);
        parentHolder.findViewById(R.id.purses_bags_wallets).setOnClickListener(this);
        parentHolder.findViewById(R.id.shoes).setOnClickListener(this);

        parentHolder.findViewById(R.id.headphones_handfree).setOnClickListener(this);
        parentHolder.findViewById(R.id.laptop_pc).setOnClickListener(this);
        parentHolder.findViewById(R.id.watches).setOnClickListener(this);
        parentHolder.findViewById(R.id.mobilephones).setOnClickListener(this);

        return parentHolder;
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        /*
         *                          // (10 - B - 5)
         *    // (10 - B - 6) Go and Create  AdminAddNewProductActivity.java
         * Create switch case the receive every button ID and perform the right action
         * and              (send the category info with the intent)
         *
         * */
        switch (v.getId()) {
            case R.id.t_shirts:
                intent = new Intent(getContext(), SellerAddNewProductActivity.class);
                // allow the admin to add product to each specific category
                // send the category info with the intent
                intent.putExtra("category", "tShirts");
                startActivity(intent);
                break;
            case R.id.sports_t_shirts:
                intent = new Intent(getContext(), SellerAddNewProductActivity.class);
                intent.putExtra("category", "Sports tShirts");
                startActivity(intent);
                break;
            case R.id.female_dresses:
                intent = new Intent(getContext(), SellerAddNewProductActivity.class);
                intent.putExtra("category", "Female Dresses");
                startActivity(intent);
                break;
            case R.id.sweathers:
                intent = new Intent(getContext(), SellerAddNewProductActivity.class);
                intent.putExtra("category", "Sweathers");
                startActivity(intent);
                break;
            case R.id.glasses:
                intent = new Intent(getContext(), SellerAddNewProductActivity.class);
                intent.putExtra("category", "Glasses");
                startActivity(intent);
                break;
            case R.id.hats_caps:
                intent = new Intent(getContext(), SellerAddNewProductActivity.class);
                intent.putExtra("category", "Hats Caps");
                startActivity(intent);
                break;
            case R.id.purses_bags_wallets:
                intent = new Intent(getContext(), SellerAddNewProductActivity.class);
                intent.putExtra("category", "Wallets Bags Purses");
                startActivity(intent);
                break;
            case R.id.shoes:
                intent = new Intent(getContext(), SellerAddNewProductActivity.class);
                intent.putExtra("category", "Shoes");
                startActivity(intent);
                break;
            case R.id.headphones_handfree:
                intent = new Intent(getContext(), SellerAddNewProductActivity.class);
                intent.putExtra("category",  "HeadPhones HandFree");
                startActivity(intent);
                break;
            case R.id.laptop_pc:
                intent = new Intent(getContext(), SellerAddNewProductActivity.class);
                intent.putExtra("category", "Laptops");
                startActivity(intent);
                break;
            case R.id.watches:
                intent = new Intent(getContext(), SellerAddNewProductActivity.class);
                intent.putExtra("category",  "Watches");
                startActivity(intent);
                break;
            case R.id.mobilephones:
                intent = new Intent(getContext(), SellerAddNewProductActivity.class);
                intent.putExtra("category", "Mobile Phones");
                startActivity(intent);
                break;
        }
    }
}
