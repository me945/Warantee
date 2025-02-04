package com.example.warantee;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignUp extends AppCompatActivity {

    private EditText emailText, passwordText, confirmPasswordText;
    private Button btnSignUp;
    FirebaseAuth mFirebaseAuth;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        progressDialog = new ProgressDialog(this);

        //Set background transparency to 80%
        View backgroundimage = findViewById(R.id.background);
        Drawable background = backgroundimage.getBackground();
        background.setAlpha(80);

        mFirebaseAuth = FirebaseAuth.getInstance();
        emailText = (EditText)findViewById(R.id.email);
        passwordText = (EditText) findViewById(R.id.password);
        confirmPasswordText = (EditText) findViewById(R.id.confirmPassword);
        btnSignUp = (Button) findViewById(R.id.btnLogIn);

        btnSignUp.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                progressDialog.setMessage("Creating account...");
                progressDialog.show();
                String email = emailText.getText().toString();
                String password = passwordText.getText().toString();
                String confirmPassword = confirmPasswordText.getText().toString();

                if(email.isEmpty()){
                    emailText.setError("Please enter Email");
                    emailText.requestFocus();
                }else if(password.isEmpty()){
                    passwordText.setError("Please enter Password");
                    passwordText.requestFocus();
                }else if(confirmPassword.isEmpty()){
                    confirmPasswordText.setError("Confirm password");
                    confirmPasswordText.requestFocus();
                }else if(!(email.isEmpty() && password.isEmpty() && confirmPassword.isEmpty())){
                    //CHECK IF PASSWORD IS >= 6 characters
                    if(password.equals(confirmPassword)){
                        mFirebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(SignUp.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                progressDialog.dismiss();
                                if(!task.isSuccessful()){
                                    Toast.makeText(SignUp.this, "Signup Unsuccessful", Toast.LENGTH_SHORT);
                                }else{
                                    Toast.makeText(SignUp.this, "Signup Successful", Toast.LENGTH_LONG);
                                    FirebaseAuth.getInstance().signOut();
                                    Intent i = new Intent(getApplicationContext(), Login.class);
                                    startActivity(i);
                                    finish();
                                }
                            }

                        });
                    }else{
                        progressDialog.dismiss();
                        Toast.makeText(SignUp.this, "Password is not the same", Toast.LENGTH_LONG);
                    }
                }else{
                    progressDialog.dismiss();
                    Toast.makeText(SignUp.this, "Error occured", Toast.LENGTH_SHORT);
                }
            }
        });

    }
}
