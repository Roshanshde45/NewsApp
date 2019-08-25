package com.example.recyclerviewpractice;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.common.base.MoreObjects;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class CreateAccountActivity extends AppCompatActivity {

    private EditText usernameText;
    private EditText emailText;
    public ImageButton backButton;
    private EditText passwordText;
    private Button CreateAccount;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference collectionReference = db.collection("Users");
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        firebaseAuth = FirebaseAuth.getInstance();
        usernameText = findViewById(R.id.usernameAccount);
        passwordText = findViewById(R.id.passwordAccount);
        emailText = findViewById(R.id.emailAccount);
        CreateAccount = findViewById(R.id.createAccountButton);

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                currentUser = firebaseAuth.getCurrentUser();
                if(currentUser !=null ){
                    //we have user
                }else{
                    //No user
                }
            }
        };

        CreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!TextUtils.isEmpty(emailText.getText().toString()) && !TextUtils.isEmpty(passwordText.getText().toString()) && !TextUtils.isEmpty(usernameText.getText().toString())){
                    String email = emailText.getText().toString().trim();
                    String password = passwordText.getText().toString().trim();
                    String username = usernameText.getText().toString().trim();

                    createEmailAccount(email,password,username);

                }else {
                    Toast.makeText(getApplicationContext(),"Empty Fields are not Allowed",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void  createEmailAccount(String email, String password, final String username){
        if(!TextUtils.isEmpty(email)&& (!TextUtils.isEmpty(password)&& (!TextUtils.isEmpty(username)))){
            Toast.makeText(getApplicationContext(),"Creating Account",Toast.LENGTH_SHORT).show();

            firebaseAuth.createUserWithEmailAndPassword(email ,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if(task.isSuccessful()){
                        currentUser = firebaseAuth.getCurrentUser();
                        assert currentUser != null;
                        final String CurrentUserId = currentUser.getUid();

                        Map<String,String> userObj = new HashMap<>();
                        userObj.put("userID",CurrentUserId);
                        userObj.put("Username",username);

                        collectionReference.add(userObj).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Toast.makeText(getApplicationContext(),"Calling collection reference",Toast.LENGTH_SHORT).show();

                                documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {


                                        if(Objects.requireNonNull(task.getResult()).exists()){
                                            String name = task.getResult().getString("username");

                                            Intent intent = new Intent(CreateAccountActivity.this,DashboardActivity.class);
                                            intent.putExtra("username",name);
                                            intent.putExtra("userId", CurrentUserId);
                                            startActivity(intent);


                                        }
                                    }
                                });

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d("addOnFailure", "onFailure: Gotcha");
                                Toast.makeText(getApplicationContext(),"Failed",Toast.LENGTH_SHORT).show();



                            }
                        });
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getApplicationContext(),"Failed",Toast.LENGTH_SHORT).show();
                }
            });

        }else {
                Toast.makeText(getApplicationContext(),"Failed in else",Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void onStart() {
        super.onStart();

        currentUser = firebaseAuth.getCurrentUser();
        firebaseAuth.addAuthStateListener(authStateListener);
    }
    public void moveback(View view) {

        Intent intent = new Intent(CreateAccountActivity.this,LoginActivity.class);
        startActivity(intent);
    }

}
