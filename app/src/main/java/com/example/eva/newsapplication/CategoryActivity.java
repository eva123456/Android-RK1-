package com.example.eva.newsapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import ru.mail.weather.lib.Storage;
import ru.mail.weather.lib.Topics;

public class CategoryActivity extends AppCompatActivity implements View.OnClickListener{

    Button btnAuto, btnIt, btnHealth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        btnAuto = (Button) findViewById(R.id.btnAuto);
        btnAuto.setOnClickListener(this);

        btnIt = (Button) findViewById(R.id.btnIt);
        btnIt.setOnClickListener(this);

        btnHealth = (Button) findViewById(R.id.btnHealth);
        btnHealth.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        String currentTopic = ((Button) v).getText().toString();
        Storage.getInstance(this).saveCurrentTopic(currentTopic);
        finish();
    }
}
