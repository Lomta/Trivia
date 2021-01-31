package com.example.fridge;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "TAG something";
    private FirebaseAuth mAuth;
    boolean switched = false;
    Switch producerButto;
    String isProducer = "false";
    String username ="";
    boolean newAcc = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Users");


        EditText emailEditText = this.findViewById(R.id.editTextTextEmailAddress);
        EditText passwordEditText = this.findViewById(R.id.editTextTextPassword);
        Button signInButton = this.findViewById(R.id.signInButton);
        TextView registerText = this.findViewById(R.id.registerText);
        EditText usernameEditText = this.findViewById(R.id.userNameText);
        usernameEditText.setVisibility(View.INVISIBLE);
        Button signUpButton = this.findViewById(R.id.signUpButton);
        signUpButton.setVisibility(View.INVISIBLE);
        producerButto = this.findViewById(R.id.producerButton);
        producerButto.setVisibility(View.INVISIBLE);
        newAcc = false;




        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                signIn(email, password);
            }
        });

        registerText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signInButton.setVisibility(View.INVISIBLE);
                usernameEditText.setVisibility(View.VISIBLE);
                signUpButton.setVisibility(View.VISIBLE);
                registerText.setVisibility(View.INVISIBLE);
                producerButto.setVisibility(View.VISIBLE);
            }
        });

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                username = usernameEditText.getText().toString();

                if (producerButto.isChecked()) switched = true;
                if (!email.equals("") && !(password.equals(""))) {
                    createAccount(email, password);
                }
            }
        });

    }

    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null)
           updateUI(currentUser);
    }

    private void updateUI(FirebaseUser currentUser) {
        if (currentUser != null) {
            String id = String.valueOf(currentUser.getEmail().hashCode());
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            String path = "Users/" + id;
            DatabaseReference myRef = database.getReference(path);
            myRef = myRef.child("IsProducer");
            myRef.addValueEventListener(new ValueEventListener() {
               @Override
               public void onDataChange(@NonNull DataSnapshot snapshot) {
                   Intent intent;
                   System.out.println(snapshot);
                   isProducer = snapshot.getValue().toString();
                   if (isProducer.equals("false")) {
                       intent = new Intent(MainActivity.this, MenuActivity.class);
                   }
                   else {
                       intent = new Intent(MainActivity.this, ProducersActivity.class);
                   }

                   startActivity(intent);
               }

               @Override
               public void onCancelled(@NonNull DatabaseError error) {

               }
           });
        }
    }

    private void createAccount(String email, String password) {

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            addData(user);
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        // ...
                    }
                });
    }

    private void signIn(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                            Toast.makeText(MainActivity.this, "signed in succ",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });
    }

private void addData(FirebaseUser user) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
    String id = String.valueOf(user.getEmail().hashCode());
    DatabaseReference ref = database.getReference("Users");
    ref.child(id).child("UserName").setValue(username);
    if (switched)
        ref.child(id).child("IsProducer").setValue("true");
    else
        ref.child(id).child("IsProducer").setValue("false");
    newAcc = true;
}


}
