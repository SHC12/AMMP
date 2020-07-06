package com.mobile.pmmp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class AccountActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
    }

    public void toLoginUlang(View view) {
        Toast.makeText(this, "Passwor berhasil di ganti,Silahkan login ulang", Toast.LENGTH_LONG).show();
        startActivity(new Intent(this, MenuLoginActivity.class));

    }
}