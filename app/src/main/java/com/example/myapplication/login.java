package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class login extends AppCompatActivity {
    EditText edtuserid;
    Button btnlogin,btnregister;
    DatabaseReference databaseReference;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        edtuserid=findViewById(R.id.userid);
        btnlogin = findViewById(R.id.login);
        btnregister = findViewById(R.id.register);
        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edtuserid.getText().toString().trim().equals("")){
                    Toast.makeText(login.this,"請輸入userid",Toast.LENGTH_LONG).show();
                    return;
                }
                    databaseReference = FirebaseDatabase.getInstance().getReference("users");
                    Query query = databaseReference.orderByChild("userid").equalTo(edtuserid.getText().toString());
                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists())
                            {
                                Intent intent = new Intent();
                                Bundle bundle = new Bundle();
                                bundle.putString("userid", edtuserid.getText().toString());
                                intent.putExtras(bundle);
                                intent.setClass(login.this, MainActivity.class);
                                startActivity(intent);
                            }
                            else
                            {
                                Toast.makeText(login.this, "userid錯誤", Toast.LENGTH_LONG).show();
                                edtuserid.setText("");
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });
            }
        });
        btnregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edtuserid.getText().toString().trim().equals("")){
                    Toast.makeText(login.this,"請輸入userid",Toast.LENGTH_LONG).show();
                    return;
                }
                    databaseReference = FirebaseDatabase.getInstance().getReference("users");
                    Query query = databaseReference.orderByChild("userid").equalTo(edtuserid.getText().toString());
                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.exists())
                            {
                            Toast.makeText(login.this, "userid重複", Toast.LENGTH_LONG).show();
                            edtuserid.setText("");
                            }else
                            {
                                loginfire loginfire = new loginfire(edtuserid.getText().toString());
                                databaseReference.push().setValue(loginfire);
                                Intent intent = new Intent();
                                Bundle bundle = new Bundle();
                                bundle.putString("userid", edtuserid.getText().toString());
                                intent.putExtras(bundle);
                                intent.setClass(login.this, MainActivity.class);
                                startActivity(intent);
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });
            }
        });
    }
}