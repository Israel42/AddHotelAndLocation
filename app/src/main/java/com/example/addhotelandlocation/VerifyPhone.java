package com.example.addhotelandlocation;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.FirebaseUserMetadata;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.concurrent.TimeUnit;

import cn.pedant.SweetAlert.SweetAlertDialog;
import in.aabhasjindal.otptextview.OtpTextView;

public class VerifyPhone extends AppCompatActivity {
    private static final String TAG = "VALUES";
    TextView textView1,textView2;
    OtpTextView otpTextView;
    Button verify;
    ImageView imageView;
    String VerificationId;
    String phonenum;
    Animation topanim,bottom;
    FirebaseAuth auth;
    FirebaseFirestore firebaseFirestore;
    DocumentReference documentReference;
    SweetAlertDialog sweetAlertDialog;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verfiyphone);
        textView1=findViewById(R.id.textViewverify1);
        textView2=findViewById(R.id.textViewverify2);
        imageView=findViewById(R.id.imageViewverify);
        otpTextView=findViewById(R.id.otpTextView);
        verify=findViewById(R.id.verfiy);
        auth=FirebaseAuth.getInstance();
        firebaseFirestore=FirebaseFirestore.getInstance();
        topanim= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.top_anim);
        bottom=AnimationUtils.loadAnimation(getApplicationContext(),R.anim.bottom_anim);
        textView1.setAnimation(topanim);
        textView2.setAnimation(topanim);
        imageView.setAnimation(topanim);
        verify.setAnimation(bottom);
        otpTextView.setAnimation(bottom);
        phonenum=getIntent().getStringExtra("PHONE");
        sendPhoneAutintication(phonenum);
        sweetAlertDialog=new SweetAlertDialog(VerifyPhone.this);
        sweetAlertDialog.setContentText("Waiting For Code")
                .setTitle("HighTechQuestion");
        sweetAlertDialog.changeAlertType(SweetAlertDialog.PROGRESS_TYPE);
        sweetAlertDialog.show();
  verify.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
          String code=otpTextView.getOTP();
          if (code.isEmpty()){
              otpTextView.showError();
          }
          VerifyCode(code);
      }
  });
    }

    private void sendPhoneAutintication(String phonenum) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+"+phonenum,
                120,
                TimeUnit.SECONDS,
                TaskExecutors.MAIN_THREAD,
                mCallBack

        );
    }
    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallBack=new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            VerificationId=s;
        }

        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
            String code=phoneAuthCredential.getSmsCode();
            if (code!=null){
                otpTextView.setOTP(code);
                VerifyCode(code);
            }

        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {

        }
    };

    private void VerifyCode(String code) {
        PhoneAuthCredential credential=PhoneAuthProvider.getCredential(VerificationId,code);
        SignInWithCredential(credential);
    }

    private void SignInWithCredential(PhoneAuthCredential credential) {
        auth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    final FirebaseUser user = task.getResult().getUser();
                    String uid = user.getUid();
                    final FirebaseFirestore db = FirebaseFirestore.getInstance();
                    final DocumentReference docRef = db.collection("USERS").document(uid);
                    docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(final DocumentSnapshot documentSnapshot) {
                            if (documentSnapshot.exists()) {
                                Toast.makeText(VerifyPhone.this, "There is Value", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(VerifyPhone.this,HomeActivity.class));
                                finish();
                             } else {
                                //redirect to sign up page
                                Toast.makeText(VerifyPhone.this, "No Value", Toast.LENGTH_SHORT).show();
                                Intent intent=new Intent(VerifyPhone.this,RegistrationPage.class);
                                intent.putExtra("Phone",phonenum);
                                startActivity(intent);
                                finish();
                            }
                        }
                    });
                }
            }
        });
    }
}
