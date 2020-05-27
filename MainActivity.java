package com.example.highandlow;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    //HighAndLowへ
    public void toHighAndLow(View v){
        Intent intent = new Intent(MainActivity.this,HighLowActivity.class);
        startActivity(intent);
    }
}
