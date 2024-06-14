package com.example.intelli_chat_cc;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.intelli_chat_cc.Utils.AndroidUtils;
import com.example.intelli_chat_cc.databinding.ActivityVerifyOtpBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class VerifyOTP extends AppCompatActivity {
    private Long timeOutLimit = 60L;
    private String phoneNumber;
    ActivityVerifyOtpBinding binding;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    PhoneAuthProvider.ForceResendingToken resendingToken;
    private String verificationCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityVerifyOtpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Fetch the information first of last Intent
        phoneNumber = getIntent().getStringExtra("phoneNumber");

        sendOTP(phoneNumber, false);
        binding.verifyPhoneNum.setOnClickListener(
                v->{
                    String sentOTP = binding.enterOTP.getText().toString().trim();
                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationCode, sentOTP);
                    signInWithPhoneAuthCredentials(credential);
                }
        );

    }

    // Send OTP Function
    private void sendOTP(String phoneNumber, boolean resend){
        PhoneAuthOptions.Builder options = PhoneAuthOptions.newBuilder(mAuth)
                .setPhoneNumber(phoneNumber)
                .setTimeout(timeOutLimit, TimeUnit.SECONDS)
                .setActivity(VerifyOTP.this)
                .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                        Log.d("VERIFY-OTP", "onVerificationCompleted: "+ phoneAuthCredential);
                        signInWithPhoneAuthCredentials(phoneAuthCredential);
                    }

                    @Override
                    public void onVerificationFailed(@NonNull FirebaseException e) {
                        AndroidUtils.ToastMessage(VerifyOTP.this, "OTP-Verification Failed");
                    }

                    @Override
                    public void onCodeSent(@NonNull String verificationId,
                                           @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                        super.onCodeSent(verificationId, forceResendingToken);
                        Log.d("VERIFY-OTP", "onCodeSent: "+verificationId);

                        verificationCode = verificationId;
                        resendingToken = forceResendingToken;
                        AndroidUtils.ToastMessage(VerifyOTP.this, "Code Send Successfully");
                    }
                });


        if(resend) PhoneAuthProvider.verifyPhoneNumber(options.setForceResendingToken(resendingToken).build());
        else PhoneAuthProvider.verifyPhoneNumber(options.build());
    }

    private void signInWithPhoneAuthCredentials(PhoneAuthCredential credential){
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        Log.d("ON-Complete", "signInWithCredential: Success");

                        // Move the Intent to Main
                        Intent intent = new Intent(VerifyOTP.this, UserProfileActivity.class);
                        intent.putExtra("phoneNumber",phoneNumber);
                        startActivity(intent);
                        finish();
                    }
                    else{
                        AndroidUtils.ToastMessage(VerifyOTP.this, "OTP Verification Failed");
                    }
                })
                .addOnFailureListener(e -> {
                    AndroidUtils.ToastMessage(VerifyOTP.this, "SignIn Failed\nTryAgain!");
                });
    }
}