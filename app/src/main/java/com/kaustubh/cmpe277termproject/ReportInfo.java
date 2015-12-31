package com.kaustubh.cmpe277termproject;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

public class ReportInfo extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_info);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        long size = getIntent().getExtras().getLong("fileSize");
        int num = getIntent().getExtras().getInt("numFiles");

        TextView numFiles = (TextView) findViewById(R.id.numFiles);
        numFiles.setText("Number Of Files : "+ num);

        TextView filesize = (TextView) findViewById(R.id.file_size);
        filesize.setText("Size Of Files : "+ size/1024 + "MB");

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
    }

}
