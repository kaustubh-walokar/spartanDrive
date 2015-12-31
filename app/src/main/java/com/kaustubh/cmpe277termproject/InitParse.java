package com.kaustubh.cmpe277termproject;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.parse.Parse;
import com.parse.ParsePush;

/**
 * Created by kaustubh on 12/12/15.
 */
public class InitParse extends Application{
    @Override
    public void onCreate() {

        super.onCreate();
        Parse.initialize(this, "key", "key");
        ParsePush.subscribeInBackground("notifications");


    }
}
