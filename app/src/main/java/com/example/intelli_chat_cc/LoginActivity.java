package com.example.intelli_chat_cc;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.intelli_chat_cc.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {

    ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.countryCodePicker.registerCarrierNumberEditText(binding.enterMobileNumber);
        // Check is mobile is correct or not


        // Mobile number is correct, pass the information to next intent
        binding.sendOtpBtn.setOnClickListener(
                v-> {
                    if(!binding.countryCodePicker.isValidFullNumber()){
                        binding.enterMobileNumber.setError("Check mobile number");
                        return;
                    }

                    Intent intent = new Intent(LoginActivity.this, VerifyOTP.class);
                    intent.putExtra("phoneNumber", binding.countryCodePicker.getFullNumberWithPlus());
                    startActivity(intent);
                    finish();
                }
        );
    }
}