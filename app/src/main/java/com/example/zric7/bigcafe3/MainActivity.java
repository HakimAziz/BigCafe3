package com.example.zric7.bigcafe3;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import butterknife.OnClick;


public class MainActivity extends AppCompatActivity {

//   =======> Saat halaman jalan... (onCreate)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
//    ===========================

    //    Jika klik Btn_login
    public void login(View view) {
        startActivity(new Intent(this, beMenuActivity.class));
    }

}
