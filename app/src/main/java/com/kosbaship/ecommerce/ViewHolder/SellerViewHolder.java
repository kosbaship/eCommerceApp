package com.kosbaship.ecommerce.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.kosbaship.ecommerce.Interface.ItemClickListner;
import com.kosbaship.ecommerce.R;



                    //              //(28 - C - 3)
                    // this code is coped from ProductViewHolder     <=====
// this will access our product item layout
//extends and implements
public class SellerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    //(28 - C - 4)
    public TextView txtProductName, txtProductDescription, txtProductPrice, txtProductStatus;
    public ImageView imageView;


    // initialize this listener to use it when the user
    // click on a post it will move the user to the DetailsActivity.java
    // where the user can see the details of any product
    public ItemClickListner listner;


    // generate the constructor
    public SellerViewHolder(View itemView)
    {
        super(itemView);

        imageView = itemView.findViewById(R.id.product_seller_image);
        txtProductName = itemView.findViewById(R.id.product_seller_name);
        txtProductDescription = itemView.findViewById(R.id.product_seller_description);
        txtProductPrice = itemView.findViewById(R.id.product_seller_price);
        //(28 - C - 5)
        //(28 - C - 6) Go back to HomeFragment.java
        txtProductStatus = itemView.findViewById(R.id.product_seller_state);
    }


    public void setItemClickListner(ItemClickListner listner)
    {
        this.listner = listner;
    }


    // we also need this method
    @Override
    public void onClick(View view)
    {

        listner.onClick(view, getAdapterPosition(), false);
    }
}