package com.example.addhotelandlocation;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputType;
import android.view.View;
import android.view.animation.Animation;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;

public class RegistrationPage extends AppCompatActivity {
    EditText firstname,lastname,email,birthdate;
    FirebaseFirestore firebaseFirestore;
    DocumentReference documentReference;
    FirebaseAuth auth;
    ImageView imageview;
    Button register;
    Uri imageuri;
    DatePickerDialog datePickerDialog;
    Animation top,bottom;
    FirebaseStorage firebaseStorage;
    StorageReference storageReference;
    public static final int PICK_IMAGE=99;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        firstname=findViewById(R.id.firstname);
        lastname=findViewById(R.id.lastname);
        email=findViewById(R.id.email);
        birthdate=findViewById(R.id.date);
        register=findViewById(R.id.Register);
        imageview=findViewById(R.id.profileadd);
        firebaseFirestore=FirebaseFirestore.getInstance();
        auth=FirebaseAuth.getInstance();
        firebaseStorage=FirebaseStorage.getInstance();
        birthdate.setInputType(InputType.TYPE_NULL);
        imageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent,PICK_IMAGE);
            }
        });
        birthdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar=Calendar.getInstance();
                calendar.add(Calendar.YEAR,-19);
                int year=calendar.get(Calendar.YEAR);
                int month=calendar.get(Calendar.MONTH);
                int day=calendar.get(Calendar.DAY_OF_MONTH);
                datePickerDialog=new DatePickerDialog(RegistrationPage.this, android.R.style.Theme_Holo_Light_Dialog, new DatePickerDialog.OnDateSetListener() {
                    @SuppressLint("DefaultLocale")
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        birthdate.setText(String.format("%d/%d/%d", i2, i1, i));

                    }
                },year,month,day);
                datePickerDialog.getDatePicker().setMaxDate(calendar.getTimeInMillis());
                datePickerDialog.show();
            }

        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (imageuri!=null){
                    adduserwithProifle();
                }else{
                    adduserwithoutprofile();
                }
            }
        });
    }

    public void adduserwithProifle(){
        final String fname,lname,mail,bday;
        fname=firstname.getText().toString();
        lname=lastname.getText().toString();
        mail=email.getText().toString();
        bday=birthdate.getText().toString();
        if (fname.isEmpty()||fname.length()<3){
            firstname.setError("Name is Empty");
            firstname.setFocusable(true);
            return;
        }
        if (lname.isEmpty()||lname.length()<3){
            lastname.setError("Last Name is Empty");
            lastname.setFocusable(true);
            return;
        }
        if (mail.isEmpty()){
            email.setError("Email is Empty");
            email.setFocusable(true);
            return;
        }
        if (bday.isEmpty()){
            birthdate.setError("Add your birthDate");
            birthdate.setFocusable(true);
        }
        storageReference=firebaseStorage.getReference().child("UserProfile").child("IMAGEOFUSER"+System.currentTimeMillis()+"."+getFileExtenstion(imageuri));
        storageReference.putFile(imageuri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> uriTask=taskSnapshot.getMetadata().getReference().getDownloadUrl();
            uriTask.addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                 String image=uri.toString();
                    String phone="+"+getIntent().getStringExtra("Phone");
                    Map<String,Object> map=new HashMap<>();
                    map.put("FirstName",fname);
                    map.put("LastName",lname);
                    map.put("Email",mail);
                    map.put("BirthDate",bday);
                    map.put("Imageuri",image);
                    map.put("Phone",phone);
                    String UID=auth.getCurrentUser().getUid();
                    documentReference=firebaseFirestore.collection("USERS").document(UID);
                    documentReference.set(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                         if (task.isSuccessful()){
                             startActivity(new Intent(getApplicationContext(),HomeActivity.class));
                             finish();
                         }  else {
                             Toast.makeText(RegistrationPage.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                         }
                        }
                    });
                }
            });
            }
        });
    }
    public void adduserwithoutprofile(){
        final String fname,lname,mail,bday;
        fname=firstname.getText().toString();
        lname=lastname.getText().toString();
        mail=email.getText().toString();
        bday=birthdate.getText().toString();
        if (fname.isEmpty()||fname.length()<3){
            firstname.setError("Name is Empty");
            firstname.setFocusable(true);
            return;
        }
        if (lname.isEmpty()||lname.length()<3){
            lastname.setError("Last Name is Empty");
            lastname.setFocusable(true);
            return;
        }
        if (mail.isEmpty()){
            email.setError("Email is Empty");
            email.setFocusable(true);
            return;
        }
        if (bday.isEmpty()){
            birthdate.setError("Add your birthDate");
            birthdate.setFocusable(true);
        }
        String phone="+"+getIntent().getStringExtra("Phone");
        Map<String,Object> map=new HashMap<>();
        map.put("FirstName",fname);
        map.put("LastName",lname);
        map.put("Email",mail);
        map.put("BirthDate",bday);
        map.put("Phone",phone);
        String UID=auth.getCurrentUser().getUid();
        documentReference=firebaseFirestore.collection("USERS").document(UID);
        documentReference.set(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    startActivity(new Intent(getApplicationContext(),HomeActivity.class));
                    finish();
                }  else {
                    Toast.makeText(RegistrationPage.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
     public String getFileExtenstion(Uri uri){
         ContentResolver cr=getContentResolver();
         MimeTypeMap mimeTypeMap=MimeTypeMap.getSingleton();
         return mimeTypeMap.getExtensionFromMimeType(cr.getType(uri));
     }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==PICK_IMAGE&&data!=null&&data.getData()!=null){
            imageuri=data.getData();
            try {
                Bitmap bitmap= MediaStore.Images.Media.getBitmap(getContentResolver(),imageuri);
                imageview.setImageBitmap(bitmap);
                Picasso.get().load(imageuri).transform(new CropCircleTransformation()).fit().into(imageview);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}
