package com.example.eva.newsapplication;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.widget.Toast;

import java.io.IOException;
import java.net.UnknownHostException;

import ru.mail.weather.lib.News;
import ru.mail.weather.lib.NewsLoader;
import ru.mail.weather.lib.Storage;
import ru.mail.weather.lib.Topics;


public class NewsService extends IntentService {

    static final String UPDATE_ACTION = "com.example.eva.update_news";

    public NewsService() {
        super("NewsService");
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        Context context = getApplicationContext();
        if (intent != null) {
            String topic = Storage.getInstance(context).loadCurrentTopic();
            try {
                NewsLoader newsLoader = new NewsLoader();
                //Storage.getInstance(context).saveIsUpdateInBg(true);
                News loadedNews = newsLoader.loadNews(topic);
                Storage.getInstance(context).saveNews(loadedNews);
                //Storage.getInstance(context).saveIsUpdateInBg(false);

                Intent intentToBR = new Intent(UPDATE_ACTION);
                sendBroadcast(intentToBR);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
