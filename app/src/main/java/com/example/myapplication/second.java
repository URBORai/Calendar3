package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class second extends AppCompatActivity {
    EditText editText,edt;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    TimePicker timePicker;
    CheckBox checkBox;
    Button button;
    String time,userid;
    boolean check;//檢測是否要通知
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nextpage);
        firebaseDatabase=FirebaseDatabase.getInstance();
        editText=findViewById(R.id.editTextText);
        edt=findViewById(R.id.editTextText2);
        timePicker=findViewById(R.id.tp);
        timePicker.setIs24HourView(true);
        checkBox=findViewById(R.id.checkBox);
        button=findViewById(R.id.sure);
        Intent intent=this.getIntent();
        Bundle bundle=intent.getExtras();
        userid = bundle.getString("userid");
        long dateInMillis = bundle.getLong("event");
        String date=bundle.getString("DATE");
        edt.setText(date);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {//設定是否要通知
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(checkBox.isChecked())
                {
                    check=true;
                }
                else
                {
                    check=false;
                }
            }
        });
        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
            }
        });
        button.setOnClickListener(new View.OnClickListener() {//取得時間、事件並傳入資料庫
            @Override
            public void onClick(View v) {
                String message=editText.getText().toString();
                databaseReference=firebaseDatabase.getReference(userid);
                String messagekey = databaseReference.push().getKey();
                time=timePicker.getHour()+":"+timePicker.getMinute();
                fire me=new fire(dateInMillis,time+" "+message,messagekey,message,date,time,check);
                databaseReference.child(messagekey).setValue(me);
                finish();
            }
        });
    }
}
