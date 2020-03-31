package com.kosbaship.ecommerce;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kosbaship.ecommerce.Model.Users;
import com.kosbaship.ecommerce.Prevalent.Prevalent;

import java.util.HashMap;

public class ResetPasswordActivity extends AppCompatActivity {
    //(22 - G - 1)
    // create this string text to check from where this activity was opened
    private String check = "";
    //(22 - G - 4 - a)
    private TextView PageTitle, titlteQuestions;
    private EditText phoneNumber, question1, question2;
    private Button verifyButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        //(22 - G - 2)
        // receive the intent with the name that inside settings actiity and login activity
        // which was sent with the intent
        check = getIntent().getStringExtra("check");

        //(22 - G - 4 - b)
        // get reference to the page title text
        PageTitle = findViewById(R.id.page_title);
        phoneNumber = findViewById(R.id.find_phone_number);
        question1 = findViewById(R.id.question_1);
        question2 = findViewById(R.id.question_2);
        titlteQuestions = findViewById(R.id.title_questions);
        verifyButton = findViewById(R.id.verify_btn);


    }

    //(22 - G - 3)
    // watch from where this
    @Override
    protected void onStart() {
        super.onStart();
        // make the phone number by default is Gone
        // and make it visible when u want
        phoneNumber.setVisibility(View.GONE);

        if (check.equals("settings")) {
            //(22 - G - 5 - a)
            // if the user comes from settings make
            // these changes
            PageTitle.setText("Set Question");
            titlteQuestions.setText("Please, Answer The following Security Questions?");
            verifyButton.setText("Confirm");

            //(22 - G - 5 - d)
            // retrieve the last answers from the db if it exist
            // when first lunched the activity
            displayPreviouseAnsers();

            // when he click confirm
            verifyButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //(22 - G - 5 - b)
                    // apply this helper method code
                    setAnswers();


                }
            });
        } else if (check.equals("login")) {
            phoneNumber.setVisibility(View.VISIBLE);
            //(22 - H - 1)
            // when the user comes from login activity that means her
            // forget his pass and need to reset it again
            verifyButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //(22 - H - 2)
                    // call this helper method
                    verifyUser();
                }
            });

        }
    }
    //(22 - H - 3)
    // verify the phone number and the answers
    private void verifyUser() {
        // get them from the screen
        final String phone = phoneNumber.getText().toString();
        final String answer1 = question1.getText().toString().toLowerCase();
        final String answer2 = question2.getText().toString().toLowerCase();

        // check if the fields are empty
        if (!phone.equals("") && !answer1.equals("") && !answer2.equals("")) {
            // create a reference to the current online user phone from the db
            // who is now online and who can set the questions
            final DatabaseReference ref = FirebaseDatabase // this will be the (dataSnapshot)
                    .getInstance()
                    .getReference()
                    .child("Users")
                    .child(phone);

            // we need to check if the user is exist alongside with security question
            // check if the data exist by getting it and compare it with the current data
            // on the screen
            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    // if the Prevalent.currentOnlineUser.getPhone() exists
                    if (dataSnapshot.exists()) {
                        // this is the shipping phone in the db
                        String mPhone = dataSnapshot.child("phone").getValue().toString();

                        // check if this phone number has a child (Security Questions)
                        // then retrieve these answers and compere them with what user typed
                        if (dataSnapshot.hasChild("Security Questions")) {

                            // get the answers  from the db
                            String ans1 = dataSnapshot.child("Security Questions").child("answer1").getValue().toString();
                            String ans2 = dataSnapshot.child("Security Questions").child("answer2").getValue().toString();
                            // compare the answers with the user answers
                            if (!ans1.equals(answer1)) {
                                Toast.makeText(ResetPasswordActivity.this,
                                        "Your First Answer is Incorrect",
                                        Toast.LENGTH_SHORT).show();
                            } else if (!ans2.equals(answer2)) {
                                Toast.makeText(ResetPasswordActivity.this,
                                        "Your Second Answer is Incorrect",
                                        Toast.LENGTH_SHORT).show();
                            } else { // if both correct we allow the user to change his pass
                                // create an alert dialog
                                AlertDialog.Builder builder = new AlertDialog.Builder(ResetPasswordActivity.this);
                                builder.setTitle("New Password");
                                // create edit text in this dialog box
                                final EditText newPassword = new EditText(ResetPasswordActivity.this);
                                newPassword.setHint("Write Password Here...");
                                builder.setView(newPassword);
                                // these will be two button change btn and cancel btn
                                builder.setPositiveButton("Change", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        // if it not null we will going to change the pass
                                        if (!newPassword.getText().toString().equals("")) {
                                            // store new password inside the db
                                            ref.child("password")
                                                    .setValue(newPassword.getText().toString())
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            // here the task successful so we will tell the user
                                                            if (task.isSuccessful()) {
                                                                Toast.makeText(ResetPasswordActivity.this,
                                                                        "Your Password Has changed Successfully",
                                                                        Toast.LENGTH_SHORT).show();
                                                                // send the user to the Login Activity
                                                                Intent intent = new Intent(ResetPasswordActivity.this, LoginActivity.class);
                                                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                                startActivity(intent);
                                                            }
                                                        }
                                                    });
                                        }
                                    }
                                });
                                // create cancel btn
                                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                });

                                // display the dialog
                                builder.show();
                            }
                        } else {
                            Toast.makeText(ResetPasswordActivity.this,
                                    "You Hve NOT set the Security Question",
                                    Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(ResetPasswordActivity.this,
                                "This is a Wrong Phone Number",
                                Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        } else {
            Toast.makeText(ResetPasswordActivity.this,
                    "Please, Complete The Form",
                    Toast.LENGTH_SHORT).show();
        }
    }

    //(22 - G - 5 - e)
    private void displayPreviouseAnsers() {
        // create a reference to the current online user phone from the db
        // who is now online and who can set the questions
        DatabaseReference ref = FirebaseDatabase
                .getInstance()
                .getReference()
                .child("Users")
                .child(Prevalent.currentOnlineUser.getPhone())
                .child("Security Questions");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // get the strings from the database
                    String ans1 = dataSnapshot.child("answer1").getValue().toString();
                    String ans2 = dataSnapshot.child("answer2").getValue().toString();

                    //display it on the screen fro the user
                    question1.setText(ans1);
                    question2.setText(ans2);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    //(22 - G - 5 - c)
    private void setAnswers() {
        //get the answers from the user
        String answer1 = question1.getText().toString().toLowerCase();
        String answer2 = question2.getText().toString().toLowerCase();

        if (question1.equals("") && question2.equals("")) {
            Toast.makeText(ResetPasswordActivity.this,
                    "Please Answer Both Questions",
                    Toast.LENGTH_LONG).show();
        } else {
            // create a reference to the current online user phone from the db
            // who is now online and who can set the questions
            DatabaseReference ref = FirebaseDatabase
                    .getInstance()
                    .getReference()
                    .child("Users")
                    .child(Prevalent.currentOnlineUser.getPhone());

            // this will be the form of the node
            HashMap<String, Object> userdataMap = new HashMap<>();
            //store the data that comes from the user in the
            // Security Questions node
            userdataMap.put("answer1", answer1);
            userdataMap.put("answer2", answer2);

            // create another child for the questions
            ref.child("Security Questions").updateChildren(userdataMap)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            // if adding the questions are Successful
                            // do the following
                            if (task.isSuccessful()) {
                                Toast.makeText(ResetPasswordActivity.this,
                                        "Congratulations, u Created a Security Questions",
                                        Toast.LENGTH_SHORT).show();

                                // send the user to the home Activity
                                Intent intent = new Intent(ResetPasswordActivity.this, HomeActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                            }
                        }
                    });

        }
    }
}
