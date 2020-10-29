package com.example.addhotelandlocation;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class FinalPayment extends AppCompatActivity implements View.OnClickListener {
    EditText chekin,checkout,transactionid;
    TextView price,roomsize;
    Button plus,minus,pay;
    ImageView amole,cbe,roompicture;
    DatePickerDialog datePickerDialog;
    DatePicker datePicker;
    DatabaseReference reference;
    FirebaseDatabase database;
    int hotelroomsize,currentprice;
    Calendar calendar1,calendar2;
    String selectedhotelname,name,phone,checkin,checkoutroom,trans,numberofroom,tp;
    String Key,roomtypename,date1,date2;
Date checkindate,checkoutdate;
    String checkTrasanction=null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finalpayment);
        checkout=findViewById(R.id.checkout);
        chekin=findViewById(R.id.checkin);
        roomsize=findViewById(R.id.numberofrooms);
        transactionid=findViewById(R.id.transactionid);
        plus=findViewById(R.id.plus);
        minus=findViewById(R.id.minus);
        pay=findViewById(R.id.Pay);
        amole=findViewById(R.id.amolepayment);
        cbe=findViewById(R.id.cbepayment);
        roompicture=findViewById(R.id.imageofroom);
        price=findViewById(R.id.totalsum);
        price.invalidate();
        database=FirebaseDatabase.getInstance();
        chekin.setInputType(InputType.TYPE_NULL);
        checkout.setInputType(InputType.TYPE_NULL);
        chekin.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
             Calendar calendar=Calendar.getInstance();
             int year=calendar.get(Calendar.YEAR);
             int month=calendar.get(Calendar.MONTH);
             int day=calendar.get(Calendar.DAY_OF_MONTH);
             if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                 datePickerDialog=new DatePickerDialog(FinalPayment.this    , new DatePickerDialog.OnDateSetListener() {
                     @SuppressLint("DefaultLocale")
                     @Override
                     public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                         chekin.setText(String.format("%d/%d/%d", dayOfMonth, month, year));
                         date1=chekin.getText().toString();
                         price.invalidate();
                         SimpleDateFormat simpleDateFormat=new SimpleDateFormat("dd/MM/yyyy");
                         try {
                             checkindate=simpleDateFormat.parse(chekin.getText().toString());
                             calendar1=new GregorianCalendar();
                             calendar1.setTime(checkindate);
                         } catch (ParseException e) {
                             e.printStackTrace();
                         }

                     }
                 },year,month,day);
                  long now = System.currentTimeMillis() - 1000;
                 datePickerDialog.getDatePicker().setMinDate(now);
                  datePickerDialog.getDatePicker().setMaxDate(now+(1000*60*60*24*7));
                 datePickerDialog.show();

             }
         }
     });

        checkout.setOnClickListener(new View.OnClickListener() {
            Calendar calendar=Calendar.getInstance();
            int year=calendar.get(Calendar.YEAR);
            int month=calendar.get(Calendar.MONTH);
            int day=calendar.get(Calendar.DAY_OF_MONTH);
            @Override
            public void onClick(View v) {
                datePickerDialog=new DatePickerDialog(FinalPayment.this, new DatePickerDialog.OnDateSetListener() {
                    @SuppressLint("DefaultLocale")
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        checkout.setText(String.format("%d/%d/%d", dayOfMonth, month, year));
                        date2=checkout.getText().toString();
                        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("dd/MM/yyyy");
                        try {
                            checkoutdate=simpleDateFormat.parse(checkout.getText().toString());
                            calendar2=new GregorianCalendar();
                            calendar2.setTime(checkoutdate);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        int valueend=getdaybetween(calendar1.getTime(),calendar2.getTime());
                        int piceof= Integer.parseInt(getIntent().getStringExtra("roomprice"));
                        int finalprice=valueend*piceof;
                        price.setText(String.valueOf(finalprice)+"ETB");
                    }
                },year,month,day);
                long now = System.currentTimeMillis() +24*60*60*1000;
                datePickerDialog.getDatePicker().setMinDate(now);
                datePickerDialog.getDatePicker().setMaxDate(now+(1000*60*60*24*7));
                datePickerDialog.show();
            }
        });



        pay.setOnClickListener(this);
        roomtypename=getIntent().getStringExtra("roomtype");
            final String hotelname=getIntent().getStringExtra("hotelname");
            reference=database.getReference().child("Hotels").child("HotelDetail");
            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Query query=reference.orderByChild("hotelname").equalTo(hotelname);
                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                               Key =dataSnapshot.getKey();
                                DatabaseReference reference2=reference.child(Key).child("RoomTpe").child(roomtypename);
                                reference2.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @SuppressLint("DefaultLocale")
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        final RoomDetail roomDetail=snapshot.getValue(RoomDetail.class);
                                        Picasso.get().load(roomDetail.getRoomimage()).fit().into(roompicture);
                                        final int roomsizehotel=Integer.parseInt(roomDetail.getRoomsize());
                                        hotelroomsize=Integer.parseInt(roomsize.getText().toString());
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                if (currentprice!=0) {
                                                    currentprice = getdaybetween(calendar1.getTime(), calendar2.getTime());
                                                    int piceorroo = roomDetail.getRoomprice();
                                                    int priceperday = piceorroo * currentprice;
                                                    price.setText(String.valueOf(priceperday) + "ETB");
                                                }

                                            }
                                        });
                                        plus.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                               if (hotelroomsize!=roomsizehotel){
                                                   hotelroomsize++;
                                                   int valueof=getdaybetween(calendar1.getTime(),calendar2.getTime());
                                                   Log.d("VALUE",  String.valueOf(valueof));
                                                   roomsize.setText(String.valueOf(hotelroomsize));
                                                   int priceperroom=roomDetail.getRoomprice();
                                                   int priceroom=Integer.parseInt(roomsize.getText().toString());
                                                   int finalprice=priceperroom * priceroom*valueof;
                                                   price.setText(String.valueOf(finalprice)+"ETB");
                                               }else{
                                                   Toast.makeText(FinalPayment.this, "MAX ROOMS", Toast.LENGTH_SHORT).show();
                                               }
                                            }
                                        });

                                        minus.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                if (hotelroomsize!=1){
                                                    hotelroomsize--;
                                                    long priceof=getdaybetween(calendar1.getTime(),calendar2.getTime());
                                                    roomsize.setText(String.valueOf(hotelroomsize));
                                                    int priceperroom=roomDetail.getRoomprice();
                                                    int priceroom=Integer.parseInt(roomsize.getText().toString());
                                                    int finalprice=priceperroom * priceroom*(int)priceof;
                                                    price.setText(String.valueOf(finalprice)+"ETB");
                                                }else{
                                                    Toast.makeText(FinalPayment.this, "Min Rooms", Toast.LENGTH_SHORT).show();
                                                }

                                            }
                                        });


                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });


    }

    @Override
    public void onClick(View v) {
        final String Trasaction=transactionid.getText().toString();
        checkin=chekin.getText().toString();
        checkoutroom=checkout.getText().toString();
          numberofroom=roomsize.getText().toString();
        if (Trasaction.isEmpty()) {
            transactionid.setError("PLease add txn id");
            transactionid.setFocusable(true);
            return;
        }
        if (checkin.isEmpty()) {
            chekin.setError("Selecet Checkin Date");
            chekin.setFocusable(true);
            return;
        }
        if (checkoutroom.isEmpty()) {
            checkout.setError("Selecet Check OutDate");
            checkout.setFocusable(true);
            return;
        }

        final DatabaseReference databaseReference=database.getReference().child("transactionid");
       Query query=databaseReference.orderByChild("transaction").equalTo(Trasaction);
       query.addListenerForSingleValueEvent(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot snapshot) {
               if (snapshot.exists()){
                   Toast.makeText(FinalPayment.this, "File Exsist", Toast.LENGTH_SHORT).show();
               }else{
                   selectedhotelname=getIntent().getStringExtra("hotelname");
                   roomtypename=getIntent().getStringExtra("roomtype");
                   final DatabaseReference databaseReference1=reference.child(Key).child("RoomTpe").child(roomtypename);
                   databaseReference1.addListenerForSingleValueEvent(new ValueEventListener() {
                       @Override
                       public void onDataChange(@NonNull DataSnapshot snapshot) {
                           RoomDetail roomDetail=snapshot.getValue(RoomDetail.class);
                           int currentroomsize=Integer.parseInt(roomDetail.getRoomsize());
                           int selectedroomsize=Integer.parseInt(roomsize.getText().toString());
                           int remainingroom=currentroomsize-selectedroomsize;
                           String total=String.valueOf(remainingroom);
                           databaseReference1.child("roomsize").setValue(total);
                       }

                       @Override
                       public void onCancelled(@NonNull DatabaseError error) {

                       }
                   });
                   FirebaseAuth auth=FirebaseAuth.getInstance();
                   String UID=auth.getCurrentUser().getUid();
                   DocumentReference documentReference=FirebaseFirestore.getInstance().collection("USERS").document(UID);
                   documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                       @Override
                       public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                      if (task.isSuccessful()){
                             DocumentSnapshot documentSnapshot=task.getResult();
                             name=documentSnapshot.get("FirstName").toString()+" "+documentSnapshot.get("LastName").toString();
                             phone=documentSnapshot.get("Phone").toString();
                             checkin=chekin.getText().toString();
                             checkoutroom=checkout.getText().toString();
                             tp=price.getText().toString();
                             numberofroom=roomsize.getText().toString();
                          ResarvationDetail resarvationDetail=new ResarvationDetail(name,checkin,checkoutroom,Trasaction,numberofroom,tp,phone,roomtypename,selectedhotelname);
                          DatabaseReference databaseReference2=reference.child(Key).child("Reserved");
                          databaseReference2.child(name).push().setValue(resarvationDetail);
                          DatabaseReference myresarvation=database.getReference().child("Hotels").child("Myreservation");
                          FirebaseAuth auth=FirebaseAuth.getInstance();
                          String Uid=auth.getCurrentUser().getUid();
                          myresarvation.child(Uid).push().setValue(resarvationDetail);
                          DatabaseReference transaction_id=database.getReference().child("transactionid");
                              TransactionDetail detail1=new TransactionDetail(Trasaction);
                              transaction_id.push().setValue(detail1);
                          startActivity(new Intent(getApplicationContext(),HomeActivity.class));
                          finish();
                          }
                       }
                   });

               }
           }

           @Override
           public void onCancelled(@NonNull DatabaseError error) {

           }
       });

/*
 databaseReference.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(FinalPayment.this, "removed", Toast.LENGTH_SHORT).show();
            }
        });

 */

    }
    public int getdaybetween(Date d1, Date d2){
        return (int)( (d2.getTime() - d1.getTime()) / (1000 * 60 * 60 * 24));
    }
}
