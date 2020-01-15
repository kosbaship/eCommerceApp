package com.kosbaship.ecommerce.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.kosbaship.ecommerce.Interface.ItemClickListner;
import com.kosbaship.ecommerce.R;

//                              (16 - B - 5)
// (16 - B - 6) Go Back to CartActivity.java
// create this view holder to hold the views to the recycler view
// I used to do it as an inner class
// this will access our product cart item layout
// // (16 - B - 5 - a)
// extends and implements
public class CartViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    // (16 - B - 5 - d)
    // declare this variables
    public TextView txtProductName, txtProductPrice, txtProductQuantity;
    private ItemClickListner itemClickListner;


    // (16 - B - 5 - b)
    // generate the constructor
    public CartViewHolder(View itemView)
    {
        super(itemView);
        // (16 - B - 5 - e)
        // get reference to the views
        txtProductName = itemView.findViewById(R.id.cart_product_name);
        txtProductPrice = itemView.findViewById(R.id.cart_product_price);
        txtProductQuantity = itemView.findViewById(R.id.cart_product_quantity);
    }

    // (16 - B - 5 - c)
    // we also need this method
    @Override
    public void onClick(View view) {
        // pass the view above and the rest
        itemClickListner.onClick(view, getAdapterPosition(), false);
    }

    public void setItemClickListner(ItemClickListner itemClickListner)
    {
        this.itemClickListner = itemClickListner;
    }

}
