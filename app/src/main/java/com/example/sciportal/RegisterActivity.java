package com.example.sciportal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RegisterActivity extends AppCompatActivity {

    TextView tvLogin;
    EditText edtFName, edtLName, edtEmail, edtRegNo, edtPasswd, edtConfPasswd;
    Button btnRegister;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    NewUser newUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        edtFName = findViewById(R.id.edt_fname);
        edtLName = findViewById(R.id.edt_lname);
        edtEmail = findViewById(R.id.edt_email);
        edtRegNo = findViewById(R.id.edt_regno);
        edtPasswd = findViewById(R.id.edt_pass);
        edtConfPasswd = findViewById(R.id.edt_conf_pass);
        btnRegister = findViewById(R.id.btn_register);
        tvLogin = findViewById(R.id.tv_login);

        // Write a message to the database
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("UserInfo");
        newUser = new NewUser();

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String fName = edtFName.getText().toString();
                String lName = edtLName.getText().toString();
                String email = edtEmail.getText().toString();
                String regNo = edtRegNo.getText().toString();
                String pass = edtPasswd.getText().toString();
                String confPass = edtConfPasswd.getText().toString();
                if (fName.isEmpty() || lName.isEmpty() || email.isEmpty()
                || regNo.isEmpty() || pass.isEmpty() || confPass.isEmpty()){
                    Toast.makeText(getApplicationContext(),
                            "All field must be filled", Toast.LENGTH_LONG).show();
                } else {
                    if (pass.equals(confPass)){
                        addNewUser(fName, lName, email, regNo, pass);
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    }else {
                        Toast.makeText(getApplicationContext(),
                                "Password fields don't match", Toast.LENGTH_LONG).show();
                    }

                }
            }
        });

        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent login = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(login);
            }
        });
    }

    void addNewUser(String fName, String lName, String email, String regNo, String passwd){
        newUser.setfName(fName);
        newUser.setlName(lName);
        newUser.setEmail(email);
        newUser.setRegNo(regNo);
        newUser.setPasswd(passwd);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                databaseReference.setValue(newUser);
                Toast.makeText(getApplicationContext(), "User registered successfully",
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), "Error! User registration failed",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
}