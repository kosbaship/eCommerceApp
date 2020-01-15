package com.kosbaship.ecommerce.Interface;

import android.view.View;
// (13 - D - 3)
// (13 - D - 4) go ProductViewHolder.java
//create this interface and I will need a view and a position
// when the user click on it we will understand to display our post in the home activity
// whenever the user click onn any post we will use this method to send user on click method to perform
// a certain functionality
public interface ItemClickListner {
    void onClick(View view, int position, boolean isLongClick);
}
