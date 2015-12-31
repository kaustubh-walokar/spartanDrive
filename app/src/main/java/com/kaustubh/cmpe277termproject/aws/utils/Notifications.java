package com.kaustubh.cmpe277termproject.aws.utils;

import android.util.Log;

import com.parse.ParseInstallation;
import com.parse.ParsePush;
import com.parse.ParseQuery;

/**
 * Created by kaustubh on 12/3/15.
 */
public class Notifications {

    public static void notify(String fromUserId, String toUserId) throws Exception{
        ParseQuery pushQuery = ParseInstallation.getQuery();

        //TODO update this to the user notif is to be sent to
        pushQuery.whereEqualTo("user_name",toUserId);

        // Send push notification to query
        ParsePush push = new ParsePush();
        push.setQuery(pushQuery); // Set our Installation query
        push.setMessage(fromUserId + " just shared a file with you!");
        push.sendInBackground();
        Log.d("PARSE NOTIFIED",fromUserId +"--->"+toUserId);

    }
}
