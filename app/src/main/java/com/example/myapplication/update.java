package com.example.myapplication;
//更新事件資料
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class update extends AppCompatActivity {
    EditText editText,edt;
    TimePicker timePicker;
    CheckBox checkBox;
    Button button;
    String TIME,userid;
    boolean CHECK;

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.nextpage);
        Intent intent = this.getIntent();
        Bundle bundle =intent.getExtras();
        userid = bundle.getString("userid");
        String date = bundle.getString("date");
        String time = bundle.getString("time");
        long event = bundle.getLong("event");
        String key = bundle.getString("key");
        String message =bundle.getString("message");
        String timeAndMessage = bundle.getString("timeAndMessage");
        Boolean check = bundle.getBoolean("check");
        editText=findViewById(R.id.editTextText);
        edt=findViewById(R.id.editTextText2);
        timePicker=findViewById(R.id.tp);
        timePicker.setIs24HourView(true);
        checkBox=findViewById(R.id.checkBox);
        button=findViewById(R.id.sure);
        editText.setText(message);
        edt.setText(date);
        if(check == true)
        {
            checkBox.setChecked(true);
        }
        else
        {
            checkBox.setChecked(false);
        }
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(checkBox.isChecked())
                {
                    CHECK=true;
                }
                else
                {
                    CHECK=false;
                }
            }
        });
        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {

            }
        });
        button.setText("修改");
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference(userid);
                String MSG=editText.getText().toString();
                String DATE=edt.getText().toString();
                TIME=timePicker.getHour()+":"+timePicker.getMinute();
                fire fi=new fire(event,TIME+" "+MSG,key,MSG,DATE,TIME,CHECK);
                databaseReference.child(key).setValue(fi);
                finish();
            }
        });
    }

}
