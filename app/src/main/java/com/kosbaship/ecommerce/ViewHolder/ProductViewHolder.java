package com.kosbaship.ecommerce.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.kosbaship.ecommerce.Interface.ItemClickListner;
import com.kosbaship.ecommerce.R;

//                                  (13 - D - 2)
// (13 - D - 3) go and create the interface ItemClickListner.java
// create this view holder to hold the views to the recycler view
// I used to do it as an inner class
//
// this will access our product item layout
////      (13 - D - 2 - a) extends and implements
public class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    //      (13 - D - 2 - d)
    public TextView txtProductName, txtProductDescription, txtProductPrice;
    public ImageView imageView;
    //                          (13 - D - 4)
    // (13 - D - 5) go back to HomeActivity.java
    // (13 - D - 4 - a)
    // initialize this listener to use it when the user
    // click on a post it will move the user to the DetailsActivity.java
    // where the user can see the details of any product
    public ItemClickListner listner;

    //      (13 - D - 2 - b)
    // generate the constructor
    public ProductViewHolder(View itemView)
    {
        super(itemView);

        //      (13 - D - 2 - e)
        imageView = itemView.findViewById(R.id.product_image);
        txtProductName = itemView.findViewById(R.id.product_name);
        txtProductDescription = itemView.findViewById(R.id.product_description);
        txtProductPrice = itemView.findViewById(R.id.product_price);
    }

    // (13 - D - 4 - b)
    public void setItemClickListner(ItemClickListner listner)
    {
        this.listner = listner;
    }

    //      (13 - D - 2 - c)
    // we also need this method
    @Override
    public void onClick(View view)
    {
        // (13 - D - 4 - c)
        listner.onClick(view, getAdapterPosition(), false);
    }
}