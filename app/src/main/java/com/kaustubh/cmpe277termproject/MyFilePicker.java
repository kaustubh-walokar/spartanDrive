package com.kaustubh.cmpe277termproject;

import android.app.Fragment;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.kaustubh.cmpe277termproject.aws.utils.AWS;
import com.kaustubh.cmpe277termproject.aws.utils.Constants;
import com.madhura.cmpe277termproject.LocalFilePicker;
import com.parse.ParseInstallation;

public class MyFilePicker extends AppCompatActivity {
    String emailId;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.action_info) {
            final AWS.InfoSummary[] info = new AWS.InfoSummary[1];
            new AsyncTask<Void, Void, Void>() {

                @Override
                protected Void doInBackground(Void... params) {

                    info[0] = AWS.getFilesSummary(emailId, getApplication());

                    return null;
                }

                @Override
                protected void onPostExecute(Void aVoid) {

                    Intent i = new Intent(MyFilePicker.this, ReportInfo.class);
                    i.putExtra("fileSize", info[0].sizeOfFiles);
                    i.putExtra("numFiles", info[0].numberOfFiles);
                    startActivity(i);
                    super.onPostExecute(aVoid);
                }
            }.execute();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_file_picker);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // try {

        ParseInstallation parseInstallation = ParseInstallation.getCurrentInstallation();

        //TODO update this to actual username


        Bundle extras = getIntent().getExtras();
        String st = extras.getString("type");

        emailId = extras.getString(Constants.USERNAME);

        //register user with email ID
        parseInstallation.put("user_name", emailId);
        parseInstallation.saveInBackground();
        Log.d("PARSE", emailId + " registered");
        Bundle args = new Bundle();
        args.putString(Constants.USERNAME, emailId);


        if (st != null)
            if (st.equals("upload")) {
                LocalFilePicker frag = new LocalFilePicker();
                frag.setArguments(args);

                getFragmentManager().beginTransaction()
                        .replace(R.id.frame, frag)
                        .commit();

            } else {

                FileListFragment frag = new FileListFragment();
                frag.setArguments(args);

                getFragmentManager().beginTransaction()
                        .replace(R.id.frame, frag)
                        .commit();
            }
        else {

            Fragment frag = new MenuFragment();
            frag.setArguments(args);

            getFragmentManager().beginTransaction()
                    .replace(R.id.frame, frag)
                    .commit();

        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
        //getActionBar().setDisplayHomeAsUpEnabled(true);

//        }catch (Exception e)
//        {
//            e.printStackTrace();
//        }
    }

}

