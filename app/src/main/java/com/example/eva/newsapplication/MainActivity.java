package com.example.eva.newsapplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.menu.MenuAdapter;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import ru.mail.weather.lib.News;
import ru.mail.weather.lib.NewsLoader;
import ru.mail.weather.lib.Scheduler;
import ru.mail.weather.lib.Storage;
import ru.mail.weather.lib.Topics;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    Button btnCategory, btnUpdate, btnDoNotUpdate;
    TextView tvNewsInfo;
    BroadcastReceiver bcReceiver;
    //String currentTopic = Topics.AUTO;
    static final String UPDATE_ACTION = "com.example.eva.update_news";
    static final int REQUEST_TOPIC = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnCategory = (Button) findViewById(R.id.btnCategory);
        btnUpdate = (Button) findViewById(R.id.btnUpdate);
        btnDoNotUpdate = (Button) findViewById(R.id.btnDoNotUpdate);

        btnCategory.setOnClickListener(this);
        btnUpdate.setOnClickListener(this);
        btnDoNotUpdate.setOnClickListener(this);

        tvNewsInfo = (TextView) findViewById(R.id.tvNewsInfo);

        bcReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                showNews(MainActivity.this);
            }
        };
        registerReceiver(bcReceiver, new IntentFilter(UPDATE_ACTION));

    }

    @Override
    public void onStart(){
        super.onStart();
        Intent intent = new Intent(UPDATE_ACTION);
        startService(intent);
        showNews(this);
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        unregisterReceiver(bcReceiver);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if(intent == null)
            return;

        String currentTopic = intent.getStringExtra("topic");
        Storage.getInstance(this).saveCurrentTopic(currentTopic);
        Intent intentNews = new Intent(UPDATE_ACTION);
        startService(intentNews);
      //  showNews(this);

    }

    private void showNews(Context context){
        News news = Storage.getInstance(context).getLastSavedNews();
        String currentTopic = Storage.getInstance(context).loadCurrentTopic();

        String date = new SimpleDateFormat("MM/dd/yyyy").format(new Date(news.getDate()));
        String newsInfo = news.getTitle() + "\n" + news.getBody() + "\n" + date;
        tvNewsInfo.setText(newsInfo);
        btnCategory.setText(currentTopic);
    }

    @Override
    public void onClick(View v) {
        Intent intentToService = new Intent(this, NewsService.class);
        switch (v.getId()){
            case R.id.btnCategory:
                Intent intentToActivity = new Intent(this, CategoryActivity.class);
                startActivityForResult(intentToActivity, REQUEST_TOPIC);
                break;
            case R.id.btnUpdate:
                Scheduler.getInstance().schedule(this, intentToService, 10000);
                break;
            case R.id.btnDoNotUpdate:
                Scheduler.getInstance().unschedule(this, intentToService);
                break;
        }
    }
}
