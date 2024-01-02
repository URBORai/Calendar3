package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.EventDay;
import com.applandeo.materialcalendarview.listeners.OnDayClickListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class MainActivity extends AppCompatActivity {
    EditText editText;
    List<EventDay> events;//標記列表
    Button button;
    ListView listView;
    CalendarView calendarView;
    String date;//日期
    String key;//firebase子節點名稱
    String userid;
    Calendar calendar;
    long dateInMillis;//取得經過時間(getTimeInMillis())讓calender知道哪天要標記

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        userid = bundle.getString("userid");
        setContentView(R.layout.activity_main);
        setevent();
        calendarView=findViewById(R.id.calendarview);
        button=findViewById(R.id.button);
        listView=findViewById(R.id.listv);
        editText=findViewById(R.id.editTextText3);
        calendarView.setOnDayClickListener(new OnDayClickListener() {
            @Override
            public void onDayClick(EventDay eventDay) {
                calendar = eventDay.getCalendar();
                dateInMillis = calendar.getTimeInMillis();
                int year = eventDay.getCalendar().get(Calendar.YEAR);
                int month = eventDay.getCalendar().get(Calendar.MONTH)+1;
                int day = eventDay.getCalendar().get(Calendar.DAY_OF_MONTH);
                date=year+"/"+month+"/"+day;
                setlistview(date);
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editText.getText().toString().equals(""))
                {
                    Bundle bundle=new Bundle();
                    Intent intent=new Intent();
                    bundle.putString("userid",userid);
                    bundle.putString("DATE",date);
                    bundle.putLong("event",dateInMillis);
                    intent.putExtras(bundle);
                    intent.setClass(MainActivity.this, second.class);
                    startActivity(intent);
                }
                else
                {
                    Date date1 = new Date();
                    SimpleDateFormat timeformat = new SimpleDateFormat("HH:mm");
                    String time=timeformat.format(date1);
                    FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
                    DatabaseReference databaseReference=firebaseDatabase.getReference(userid);
                    String msg=editText.getText().toString();
                    String messagekey=databaseReference.push().getKey();
                    fire fire=new fire(dateInMillis,time+" "+msg,messagekey,msg,date,time,false);
                    databaseReference.child(messagekey).setValue(fire);
                    editText.setText("");
                }
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                String SELECT = String.valueOf(((TextView)view).getText());
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("確認刪除")
                        .setMessage("確認刪除此項事件紀錄")
                        .setPositiveButton("確定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
                                DatabaseReference databaseReference=firebaseDatabase.getReference(userid);
                                Query query = databaseReference.orderByChild("date").equalTo(date);
                                query.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        for (DataSnapshot postSnapshot : snapshot.getChildren())
                                        {
                                            fire fi=postSnapshot.getValue(fire.class);
                                            if(SELECT.equals( fi.getTimeAndMessage()))
                                            {
                                            key = fi.getKey();
                                            delete(key);
                                            }
                                        }
                                    }
                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                            }
                        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        }).show();
                return true;
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String select = ((TextView)view).getText().toString();
                DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference(userid);
                Query query = databaseReference.orderByChild("date").equalTo(date);
                query.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot postSnapshot : snapshot.getChildren())
                        {
                            fire fi=postSnapshot.getValue(fire.class);
                            if(select.equals(fi.getTimeAndMessage()))
                            {
                            Intent intent=new Intent();
                            Bundle bundle = new Bundle();
                            bundle.putString("userid",userid);
                            bundle.putString("date",fi.getDate());
                            bundle.putString("time",fi.getTime());
                            bundle.putString("key",fi.getKey());
                            bundle.putLong("event",fi.getEvent());
                            bundle.putString("message", fi.getMessage());
                            bundle.putString("timeAndMessage", fi.getTimeAndMessage());
                            bundle.putBoolean("check",fi.isCheck());
                            intent.putExtras(bundle);
                            intent.setClass(MainActivity.this, update.class);
                            startActivity(intent);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
    }
    private void setlistview(String date)
    {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference(userid);
        Query query = databaseReference.orderByChild("date").equalTo(date);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<String> messages =new ArrayList<>();
                for(DataSnapshot messageSnaoshot : snapshot.getChildren())
                {
                    fire messagefire = messageSnaoshot.getValue(fire.class);
                    String time = messagefire.getTime();
                    String message = messagefire.getMessage();
                    String MessageWithTime =time+" "+message;
                    messages.add(MessageWithTime);
                }
                ArrayAdapter<String> adapter=new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1,messages);
                listView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void delete(String key)
    {
        // 1. 從 events 列表中找到要刪除的事件
        for (EventDay eventDay : events) {
            if (eventDay.getCalendar().getTimeInMillis() == dateInMillis) {
                events.remove(eventDay);
                break;  // 找到後即可退出迴圈
            }
        }

        // 2. 從 Firebase 中刪除該事件
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child(userid).child(key);
        databaseReference.removeValue();

        // 3. 重新設置 calendarView 的 events 列表
        calendarView.setEvents(events);
    }
    private void setevent()
    {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(userid);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                events = new ArrayList<>();
                for(DataSnapshot messageSnaoshot : snapshot.getChildren())
                {
                    fire eventfire = messageSnaoshot.getValue(fire.class);
                    long event = eventfire.getEvent();
                    Calendar retrievedCalendar = Calendar.getInstance();
                    retrievedCalendar.setTimeInMillis(event);
                    events.add(new EventDay(retrievedCalendar, R.drawable.baseline_adjust_24));
                    calendarView.setEvents(events);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}