package com.kosbaship.ecommerce.Sellers;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.kosbaship.ecommerce.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

// (10 - B - 6)
// creation of this Activity
public class SellerAddNewProductActivity extends AppCompatActivity {

    // create them when you want them
    private String CategoryName, Description, Price, Pname, saveCurrentDate, saveCurrentTime;

    //(11 - B - 1)
    // declare the views
    private Button AddNewProductButton;
    private ImageView InputProductImage;
    private EditText InputProductName, InputProductDescription, InputProductPrice;

    //(11 - D - 2 - b)
    // this is the request code
    private static final int GalleryPick = 1;

    //(11 - D - 3 - b)
    // this is the image uri
    private Uri ImageUri;

    //(11 - D - 4 - b - two - 2)
    //(11 - D - 4 - b - two - 14)
    // create a product random key
    // create the download image url
    private String productRandomKey, downloadImageUrl;

    //(11 - D - 4 - b - two - 4)
    // we will use this storage reference to create a folder
    // to add all our product images inside it
    private StorageReference ProductImagesRef;

    //(11 - D - 4 - b - two - 9)
    // initialize the dialog bar
    private ProgressDialog loadingBar;


    //(11 - D - 4 - b - two - 17 - b) declare Products reference
    // (26 - A - 4) declare Sellers reference
    private DatabaseReference ProductsRef, SellersRef;

    // (26 - A - 3)
    // this for seller information
    private String currentSellerName,
            currentSellerAddress,
            currentSellerPhone,
            currentSellerEmail,
            currentSellerID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_add_new_product);

        // (10 - B - 7)
        // (11) Go to activity_seller_add_new_product
        // Receive the name of the category that send to us from
        // the admin category
        CategoryName = getIntent().getExtras().get("category").toString();

        //(11 - D - 4 - b - two - 5)
        // create the storage with the name Product Images
        ProductImagesRef = FirebaseStorage.getInstance().getReference().child("ProductImages");

        //(11 - D - 4 - b - two - 17 - c)
        // create the Products node (table)
        ProductsRef = FirebaseDatabase.getInstance().getReference().child("Products");

        // (26 - A - 5)
        // get reference to the sellers node
        SellersRef = FirebaseDatabase.getInstance().getReference().child("Sellers");

        //(11 - B - 2)
        //(11 - C) Go to Build.gradle
        // get reference to the views
        AddNewProductButton = findViewById(R.id.add_new_product);
        InputProductImage = findViewById(R.id.select_product_image);
        InputProductName = findViewById(R.id.product_name);
        InputProductDescription = findViewById(R.id.product_description);
        InputProductPrice = findViewById(R.id.product_price);
        //(11 - D - 4 - b - two - 10)
        // Create the dialog bar
        loadingBar = new ProgressDialog(this);


        //                                  (11 - D)
        // (12) Go to the browser and click storage >Get Started >Get it
        //      click rules change       allow read, write: if request.auth != null;
        //                      to            allow read, write: if request.auth == null;
        //      click publish
        //  (13) there is a steps the Go nav_header_home.xml
        //          1 - Delete HomeActivity.java
        //          2 - Delete activity_home.xml
        //          3 - Create Navigation Drawer Activity (With the last Deleted Activity HOME)
        // allow the user to select product image
        InputProductImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //(11 - D - 1)
                // this where the user select the product image
                OpenGallery();
            }
        });

        //(11 - D - 4)
        AddNewProductButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                //(11 - D - 4 - a)
                ValidateProductData();
            }
        });


        // (26 - A - 6)
        // here we should get the Seller who is online (Current Seller)
        SellersRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()){
                            //retrieve the following info
                            currentSellerName = dataSnapshot.child("name").getValue().toString();
                            currentSellerAddress = dataSnapshot.child("address").getValue().toString();
                            currentSellerPhone = dataSnapshot.child("phone").getValue().toString();
                            currentSellerEmail = dataSnapshot.child("email").getValue().toString();
                            currentSellerID = dataSnapshot.child("sid").getValue().toString();


                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

    }

    //(11 - D - 2)
    // this method will create an intent to select (Only) a picture from the gallery
    // and attach the result to a request code
    private void OpenGallery()
    {
        //(11 - D - 2 - a)
        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        //start this activity and you will get a result
        startActivityForResult(galleryIntent, GalleryPick);
    }

    //(11 - D - 3)
    // here we will get the result of the requested intent and this
    // result will be Image URI
    // we will store it first in the firebase storage and then into
    // the firebase database
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        //(11 - D - 3 - a)
        if (requestCode==GalleryPick  &&  resultCode==RESULT_OK  &&  data!=null)
        {
            //(11 - D - 3 - c)
            ImageUri = data.getData();
            InputProductImage.setImageURI(ImageUri);
        }
    }

    //(11 - D - 4 - b)
    // make sure that the admin fill the product data fields
    private void ValidateProductData()
    {
        // get the data from the admin
        Description = InputProductDescription.getText().toString();
        Price = InputProductPrice.getText().toString();
        Pname = InputProductName.getText().toString();

        // if the data empty display a toast message for hi,
        if (ImageUri == null)
        {
            Toast.makeText(this, "Product image is mandatory...", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(Description))
        {
            Toast.makeText(this, "Please write product description...", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(Price))
        {
            Toast.makeText(this, "Please write product Price...", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(Pname))
        {
            Toast.makeText(this, "Please write product name...", Toast.LENGTH_SHORT).show();
        }
        else
        {
            //(11 - D - 4 - b - one)
            StoreProductInformation();
        }
    }

    //(11 - D - 4 - b - two)
    private void StoreProductInformation()
    {
        //(11 - D - 4 - b - two - 17 - e)
        // (12) Go to the browser and click storage >Get Started >Get it
        //      click rules change       allow read, write: if request.auth != null;
        //                      to            allow read, write: if request.auth == null;
        // click publish
        loadingBar.setTitle("Add New Product");
        loadingBar.setMessage("Dear Admin, please wait while we are adding the new product.");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();

        //(11 - D - 4 - b - two - 0)
        // create calender to get the date and time from it
        Calendar calendar = Calendar.getInstance();

        //(11 - D - 4 - b - two - 0)
        // this to get the current date
        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        // store the current date into a string variable
        saveCurrentDate = currentDate.format(calendar.getTime());

        //(11 - D - 4 - b - two - 0)
        // this to get the current time
        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        // store the current time into a string variable
        saveCurrentTime = currentTime.format(calendar.getTime());

        //(11 - D - 4 - b - two - 1)
        // we need this product random key because there will be a thousand
        // of different products so for each product data we need a random key
        // tho refresh the product data
        // we can use firebase push message to create a unique random key but we will use the method of combining the current date
        // with the current time
        productRandomKey = saveCurrentDate + saveCurrentTime;

        //(11 - D - 4 - b - two - 3)
        // store image uri or product image inside the firebase storage
        // after that we will be able to store the link of that image
        // in the firebse database to display it to the users
        // for that Create a storage reference (we will create a folder)
        //(11 - D - 4 - b - two - 6) complete this line
        //   ImageUri.getLastPathSegment()
        //      we pass the image uri that contain the images with it's name by calling the method
        //      get last segment and add the random product key to the name and at the end the extension
        //   filePath => this is the link for the product image
        final StorageReference filePath = ProductImagesRef.child(ImageUri.getLastPathSegment() + productRandomKey + ".jpg");

        //(11 - D - 4 - b - two - 7)
        // this will finally upload the image to the firebase storage
        final UploadTask uploadTask = filePath.putFile(ImageUri);

        //(11 - D - 4 - b - two - 8)
        // this in case if any failure occurs
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e)
            {
                //(11 - D - 4 - b - two - 11)
                String message = e.toString();
                Toast.makeText(SellerAddNewProductActivity.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                loadingBar.dismiss();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
            {
                //(11 - D - 4 - b - two - 12)
                Toast.makeText(SellerAddNewProductActivity.this, "Product Image uploaded Successfully...", Toast.LENGTH_SHORT).show();

                //(11 - D - 4 - b - two - 13)
                // get the link of this image to store it into firebase database
                Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception
                    {
                        if (!task.isSuccessful())
                        {
                            throw task.getException();
                        }
                        //(11 - D - 4 - b - two - 15)
                        // these two lines will download the image url to save it into the data base
                        // this will only get the uri not the link
                        downloadImageUrl = filePath.getDownloadUrl().toString();
                        return filePath.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task)
                    {
                        if (task.isSuccessful())
                        {
                            // this will get the url (link) for the image
                            downloadImageUrl = task.getResult().toString();

                            Toast.makeText(SellerAddNewProductActivity.this, "got the Product image Url Successfully...", Toast.LENGTH_SHORT).show();
                            //(11 - D - 4 - b - two - 16)
                            // save the product into the database
                            SaveProductInfoToDatabase();
                        }
                    }
                });
            }
        });
    }


    //(11 - D - 4 - b - two - 17)
    // create this method to save the product into the database
    private void SaveProductInfoToDatabase()
    {
        //(11 - D - 4 - b - two - 17 - a)
        //create a hash jap to th whole product
        HashMap<String, Object> productMap = new HashMap<>();
        productMap.put("pid", productRandomKey);
        productMap.put("date", saveCurrentDate);
        productMap.put("time", saveCurrentTime);
        productMap.put("description", Description);
        // the images is stored inside the data storage with this url step (11 - D - 4 - b - two - 15)
        productMap.put("image", downloadImageUrl);
        // the category comes when the first activity is opened
        productMap.put("category", CategoryName);
        productMap.put("price", Price);
        productMap.put("pname", Pname);
        // (26 - A - 2)
        // Create Seller Identification so we can recognize them when the user purchase from them
        // (26 - A - 7)
        // (26 - B) Go to activity_seller_categoryy.xml
        // pass the values we get here
        productMap.put("sellerName", currentSellerName);
        productMap.put("sellerAddress", currentSellerAddress);
        productMap.put("sellerPhone", currentSellerPhone);
        productMap.put("sellerEmail", currentSellerEmail);
        productMap.put("sID", currentSellerID);
        // (26 - A - 1)
        // add this field to get the confirmation from the administration
        // for the product so that it will appears for the Buyer on home activity
        //  Approved ====> the admin accept and it ready for sell
        //  Not Approved ====> admin didi not accept yed
        productMap.put("productstate", "Not Approved");

        //(11 - D - 4 - b - two - 17 - d)
        // ProductsRef.child(productRandomKey):
        //      give a unique random key for each product in the db
        //      because the should not be replaceable for each other
        //updateChildren(productMap):
        //      attach the rest of the values (as columns) for this parent
        ProductsRef.child(productRandomKey).updateChildren(productMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task)
                    {
                        if (task.isSuccessful())
                        {
                            // (26 - E)
                            // (26 - F) Goto HomeActivity.java
                            // if the product add send the user to SellerHomeActivity
                            // if every thing done back to AdminCategoryActivity
                            Intent intent = new Intent(SellerAddNewProductActivity.this, SellerHomeActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);

                            loadingBar.dismiss();
                            Toast.makeText(SellerAddNewProductActivity.this, "Product is added successfully..", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            loadingBar.dismiss();
                            String message = task.getException().toString();
                            Toast.makeText(SellerAddNewProductActivity.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
