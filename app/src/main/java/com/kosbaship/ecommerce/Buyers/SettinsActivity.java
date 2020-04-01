package com.kosbaship.ecommerce.Buyers;

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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.kosbaship.ecommerce.Prevalent.Prevalent;
import com.kosbaship.ecommerce.R;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

//                              (14 - B)
//(14 - ) go to
// modify the activity so tat the user can edit and update his information
public class SettinsActivity extends AppCompatActivity {

    //(14 - B - 1)
    // declare the views on the screen
    private CircleImageView profileImageView;
    private EditText fullNameEditText, userPhoneEditText, addressEditText;
    private TextView profileChangeTextBtn,  closeTextBtn, saveTextButton;
    //(22 - F - 1)
    // declare this var
    private Button securityQuestionBtn;

    //(14 - B - 4 - a)
    //(14 - B - 5) go to HomeActivity.java
    // declare this variables to use it when we allowing the user to do changes
    private Uri imageUri;
    // this to store the image url after we upload the profile pic to the firebase storage
    private String myUrl = "";
    //(14 - B - 4 - e - 2 - Two - 3)
    private StorageTask uploadTask;
    private StorageReference storageProfilePrictureRef;
    private String checker = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settins);

        //(14 - B - 4 - e - 2 - Two - 3)
        // specify the firebase file reference
        // this will be the database folder that contains the images
        storageProfilePrictureRef = FirebaseStorage
                .getInstance()
                .getReference()
                .child("ProfilePictures");


        //(14 - B - 2)
        // get reference to the views on the screen
        profileImageView = findViewById(R.id.settings_profile_image);
        fullNameEditText = findViewById(R.id.settings_full_name);
        userPhoneEditText = findViewById(R.id.settings_phone_number);
        addressEditText = findViewById(R.id.settings_address);
        profileChangeTextBtn = findViewById(R.id.profile_image_change_btn);
        closeTextBtn = findViewById(R.id.close_settings_btn);
        saveTextButton = findViewById(R.id.update_account_settings_btn);
        //(22 - F - 2)
        // get reference to the views on the screen
        securityQuestionBtn = findViewById(R.id.security_questions_btn);

        //(14 - B - 2)
        //call this method to display the user information that comes from the db
        userInfoDisplay(profileImageView, fullNameEditText, userPhoneEditText, addressEditText);

        //(14 - B - 4 - b)
        // close the activity
        closeTextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                finish();
            }
        });

        //(22 - F - 3)
        //(22 - G) Go to ResetPasswordActivity.java
        // set this btn to open the reset password activity
        securityQuestionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(SettinsActivity.this, ResetPasswordActivity.class);
                // we send this data to defrantiate between the user who come from the settings
                // or the one who comes from login activity because every one will see different
                // questions
                intent.putExtra("check", "settings");
                startActivity(intent);
            }
        });


        //(14 - B - 4 - e)
        // perform storing the user information into the firebase database
        saveTextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                if (checker.equals("clicked"))
                {
                    //(14 - B - 4 - e - 1)
                    // in this method we will allow the user to update
                    // all the information in the settings activity
                    userInfoSaved();
                }
                else
                {
                    // this case when the user change only the name or phone
                    // and did not change the Photo so we will execute this
                    // method
                    // and tis why we user the checker
                    //          (14 - B - 4 - e - 3)
                    //(14 - B - 4 - e - 3 - One)
                    updateOnlyUserInfo();
                }
            }
        });
        //(14 - B - 4 - c)
        // send the user to the gallery and get the aprpriate image
        profileChangeTextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                //(14 - B - 4 - c - one)
                // i use this to check that the user what to make changes
                // when he clicked on the
                checker = "clicked";


                //(14 - B - 4 - c - Two)
                //(14 - B - 4 - c - Three) go to AndroidManifest.xml
                //(14 - B - 4 - c - Five) Two is th Five step
                // we will user this cropping feature by using this library
                // and also it will get us the picture result from the activity
                // so we need to receive that result in the next step
                CropImage.activity(imageUri)
                        .setAspectRatio(1, 1)
                        .start(SettinsActivity.this);
            }
        });
    }
    //(14 - B - 4 - e - 3 - Two)
    private void updateOnlyUserInfo() {
        // get db ref
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users");

        // store the values
        HashMap<String, Object> userMap = new HashMap<>();
        userMap. put("name", fullNameEditText.getText().toString());
        userMap. put("address", addressEditText.getText().toString());
        userMap. put("phoneOrder", userPhoneEditText.getText().toString());
        // linke the hashmapp to the node
        ref.child(Prevalent.currentOnlineUser.getPhone()).updateChildren(userMap);

        // open the activity
        startActivity(new Intent(SettinsActivity.this, HomeActivity.class));
        Toast.makeText(SettinsActivity.this, "Profile Info update successfully.", Toast.LENGTH_SHORT).show();
        finish();
    }



    //(14 - B - 4 - d)
    // change the image in the settings activity with the one we received from the
    // CropImage feature
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        // we will receive the request code from the crop image
        if (requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE  &&  resultCode==RESULT_OK  &&  data!=null)
        {
            // get the result it self (the image profile)
            // the result is image uri
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            // save the image uri that come from the gallery to this variable
            // because we will upload it to the database from this globle variable
            imageUri = result.getUri();

            //render the image as the user profile image
            profileImageView.setImageURI(imageUri);
        }
        else {
            // otherwise the image not selected or there is any errorr
            Toast.makeText(this, "Error, Try Again.", Toast.LENGTH_SHORT).show();

            startActivity(new Intent(SettinsActivity.this, SettinsActivity.class));
            finish();
        }
    }


    //(14 - B - 4 - e - 2)
    // check if the Edit Fields is empty request the focus again
    // and uplaod the image
    private void userInfoSaved()
    {
        if (TextUtils.isEmpty(fullNameEditText.getText().toString()))
        {
            Toast.makeText(this, "Name is mandatory.", Toast.LENGTH_SHORT).show();
            fullNameEditText.requestFocus();
        }
        else if (TextUtils.isEmpty(addressEditText.getText().toString()))
        {
            Toast.makeText(this, "Name is address.", Toast.LENGTH_SHORT).show();
            addressEditText.requestFocus();
        }
        else if (TextUtils.isEmpty(userPhoneEditText.getText().toString()))
        {
            Toast.makeText(this, "Name is mandatory.", Toast.LENGTH_SHORT).show();
            userPhoneEditText.requestFocus();

        }
        // this checker will be assigned as checked if the user was click
        // on change profile btn
        else if(checker.equals("clicked"))
        {
            //(14 - B - 4 - e - 2 - one)
            uploadImage();
        }
    }

    //(14 - B - 4 - e - 2 - Two)
    private void uploadImage() {

        //(14 - B - 4 - e - 2 - Two - 1)
        //create a progress dialog to be diplayed while the image uploaded
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Update Profile");
        progressDialog.setMessage("Please wait, while we are updating your account information");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();


        //(14 - B - 4 - e - 2 - Two - 2)
        // confairm that the image uri not null
        if (imageUri != null) {
            // this phone number is the image name
            final StorageReference fileRef = storageProfilePrictureRef
                    .child(Prevalent.currentOnlineUser.getPhone() + ".jpg");


            //(14 - B - 4 - e - 2 - Two - 4)
            // save our file to the sotrage
            uploadTask = fileRef.putFile(imageUri);

            //(14 - B - 4 - e - 2 - Two - 5)
            // get the result of this upload task
            uploadTask.continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull Task task) throws Exception
                {
                    // if the task fail
                    if (!task.isSuccessful())
                    {
                        throw task.getException();
                    }
                    // get the download url to store it
                    // into the firebase database
                    return fileRef.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            // if the task succeded
                            if (task.isSuccessful()) {

                                // store the DownloadedUrl that comes when the task
                                // not fail and store it inside this variable
                                Uri downloadUrl = task.getResult();
                                // convert it to string
                                myUrl = downloadUrl.toString();


                                DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users");

                                HashMap<String, Object> userMap = new HashMap<>();
                                userMap. put("name", fullNameEditText.getText().toString());
                                userMap. put("address", addressEditText.getText().toString());
                                userMap. put("phoneOrder", userPhoneEditText.getText().toString());
                                // this is the string url for the image here we store it inside the db
                                userMap. put("image", myUrl);
                                // store it inside user node in the database
                                ref.child(Prevalent.currentOnlineUser.getPhone()).updateChildren(userMap);

                                // close the pro ...
                                progressDialog.dismiss();

                                startActivity(new Intent(SettinsActivity.this, HomeActivity.class));
                                Toast.makeText(SettinsActivity.this, "Profile Info update successfully.", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                            else
                            {
                                progressDialog.dismiss();
                                Toast.makeText(SettinsActivity.this, "Error.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
        else
        {
            Toast.makeText(this, "image is not selected.", Toast.LENGTH_SHORT).show();
        }
    }

    //(14 - B - 3)
    //call this method to display
    private void userInfoDisplay(final CircleImageView profileImageView, final EditText fullNameEditText, final EditText userPhoneEditText, final EditText addressEditText)
    {
        //(14 - B - 3 - a)
        // create a database reference to specific user who logged in
        // we user here the phone number as a parent node id
        DatabaseReference UsersRef = FirebaseDatabase.getInstance().getReference().child("Users").child(Prevalent.currentOnlineUser.getPhone());

        UsersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //(14 - B - 3 - b)
                //(14 - B - 3 - c) go to the Users.java and com back
                // if this user exist in the data base
                if (dataSnapshot.exists())
                {
                    // we will search for his image first
                    if (dataSnapshot.child("image").exists())
                    {
                        // fetch the user data
                        String image = dataSnapshot.child("image").getValue().toString();
                        String name = dataSnapshot.child("name").getValue().toString();
                        String phone = dataSnapshot.child("phone").getValue().toString();
                        String address = dataSnapshot.child("address").getValue().toString();

                        // display the user data into the settings activity
                        Picasso.get().load(image).into(profileImageView);
                        fullNameEditText.setText(name);
                        userPhoneEditText.setText(phone);
                        addressEditText.setText(address);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
