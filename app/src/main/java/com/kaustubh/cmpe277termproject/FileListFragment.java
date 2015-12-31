package com.kaustubh.cmpe277termproject;

import android.app.Dialog;
import android.app.ListFragment;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.kaustubh.cmpe277termproject.aws.utils.AWS;
import com.kaustubh.cmpe277termproject.aws.utils.Constants;
import com.kaustubh.cmpe277termproject.aws.utils.Utils;
import com.parse.ParseInstallation;
import com.parse.ParsePush;
import com.parse.ParseQuery;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.concurrent.ExecutionException;


public class FileListFragment extends ListFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static String aws_key;

    private static Stack<String> pathStack = new Stack();
    private static String aws_file_name;
    FilePickerListAdapter adapter;
    ArrayList<FilePickerRow> files;
    String username = "";//"kaustubh.walokar@gmail.com";
    String suffix = "/";
    String prefix = username + suffix;
    AmazonS3Client s3client;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private OnFragmentInteractionListener mListener;
    public FileListFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static FileListFragment newInstance(String param1, String param2) {
        FileListFragment fragment = new FileListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        String emailID = getArguments().getString("emailID");
        if (emailID != null) {
            setUsername(emailID);
            updatePrefix();
        } else {
            try {
                throw new Exception("set email in arguments");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        files = new ArrayList<>();
        getFilesFromS3();

//        //ToDO place at correct location
//        try {
//            Notifications.notify(emailID,"kaustubh.walokar@gmail.com");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        adapter = new FilePickerListAdapter(getActivity(), files);
        setListAdapter(adapter);

//        AWS.uploadFile(getActivity(), "", "kaustubh/");

        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) getActivity().findViewById(R.id.toolbar);
        //getActivity().setActionBar(toolbar);
        //setSupportAc
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setHasOptionsMenu(true);
        // adapter.notifyDataSetChanged();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case android.R.id.home:
                if (!pathStack.empty()) {
                    suffix = pathStack.pop();
                    updatePrefix();
                    refreshList();
                } else Toast.makeText(getActivity(), "At top ", Toast.LENGTH_SHORT).show();
                return true;
        }
        // Toast.makeText(getActivity(), "here ", Toast.LENGTH_SHORT).show();
        return super.onOptionsItemSelected(item);
    }

    private void getFilesFromS3() {
        try {
            files.clear();
            Log.d("PREFIX when fet", prefix);

            files = (ArrayList<FilePickerRow>) new MyTask(this.getActivity()
                    .getApplicationContext())
                    .execute(prefix)
                    .get();

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

    }

    private void updatePrefix() {
        prefix = username + suffix;
    }

    public void refreshList() {
        updatePrefix();
        adapter.clear();
        getFilesFromS3();
        adapter.setData(files);
        //adapter.notifyDataSetChanged();

    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {

        if (((TextView) v.findViewById(R.id.type)).getText().equals("folder")) {

            pathStack.push(suffix);

            suffix = suffix + ((TextView) v.findViewById(R.id.file_name)).getText();
            Log.d("SUFFIX", suffix);
            updatePrefix();
            Log.d("PREFIX", prefix);
            refreshList();
            //adapter.notifyDataSetChanged();

        } else {
            // Toast.makeText(getActivity(), "This is a File", Toast.LENGTH_SHORT).show();
        }
        super.onListItemClick(l, v, position, id);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_file_list, container, false);
        ((ListView) view.findViewById(R.id.listView)).setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                //  Toast.makeText(getActivity(), "Hello", position).show();// view delete share
                aws_key = ((TextView) view.findViewById(R.id.aws_key)).getText().toString();
                aws_file_name = ((TextView) view.findViewById(R.id.file_name)).getText().toString();
                registerForContextMenu(view);
                return false;
            }
        });

        return view;

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        aws_key = ((TextView) v.findViewById(R.id.aws_key)).getText().toString();
        Log.d("S3:", aws_key);

        super.onCreateContextMenu(menu, v, menuInfo);
        menu.setHeaderTitle("Select");
        menu.add(0, v.getId(), 0, "View");// group id,item id, order, title
        menu.add(0, v.getId(), 0, "Delete");
        menu.add(0, v.getId(), 0, "Share");
        menu.add(0, v.getId(), 0, "Email");
    }


    @Override
    public boolean onContextItemSelected(MenuItem item) {

        if (item.getTitle() == "View") {

            // Toast.makeText(getActivity(), "View", Toast.LENGTH_SHORT).show();
            String file = null;
            try {
                Log.d("DOWNLOAD", aws_key + " being downloaded");

                file = new DownloadTaskFromAws().execute(aws_key).get();
                // Toast.makeText(getActivity(),file + "downloaded", Toast.LENGTH_SHORT).show();
                Log.d("DOWNLOAD", file);

            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            //Uri uriSavedImage=Uri.fromFile(file);

            Intent intent = new Intent(android.content.Intent.ACTION_VIEW);
            //File file = new File("NOTICE");
            //DOWNLOAD USING AWS
            //SAVE TO LOCAL LOCATION
            //OPEN

            File f = new File(file);
            String extension = android.webkit.MimeTypeMap.getFileExtensionFromUrl(Uri.fromFile(f).toString());
            String mimetype = android.webkit.MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
            if (extension.equalsIgnoreCase("") || mimetype == null) {
                // if there is no extension or there is no definite mimetype, still try to open the file
                intent.setDataAndType(Uri.fromFile(f), "text/*");
            } else {
                intent.setDataAndType(Uri.fromFile(f), mimetype);
            }
            // custom message for the intent
            startActivity(Intent.createChooser(intent, "Choose an Application:"));

        }
        // return super.onContextItemSelected(item);
        if (item.getTitle() == "Open") {
            Toast.makeText(getActivity(), "Open", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(android.content.Intent.ACTION_VIEW);
            File file = new File("NOTICE");
            //DOWNLOAD USING AWS
            //SAVE TO LOCAL LOCATION
            //OPEN
            String extension = android.webkit.MimeTypeMap.getFileExtensionFromUrl(Uri.fromFile(file).toString());
            String mimetype = android.webkit.MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
            if (extension.equalsIgnoreCase("") || mimetype == null) {
                // if there is no extension or there is no definite mimetype, still try to open the file
                intent.setDataAndType(Uri.fromFile(file), "text/*");
            } else {
                intent.setDataAndType(Uri.fromFile(file), mimetype);
            }
            // custom message for the intent
            startActivity(Intent.createChooser(intent, "Choose an Application:"));
        } else if (item.getTitle() == "Delete") {

            new AsyncTask<Void, Void, Void>() {

                @Override
                protected Void doInBackground(Void... params) {
                    AWS.deleteKey(aws_key, getActivity());
                    return null;
                }
            }.execute();

            Toast.makeText(getActivity(), "File Deleted Refresh to see changes", Toast.LENGTH_SHORT).show();

        } else if (item.getTitle() == "Email") {
            Toast.makeText(getActivity(), "Email", Toast.LENGTH_SHORT).show();
            //
            String file = null;
            try {
                Log.d("DOWNLOAD", aws_key + " being downloaded");


                file = new DownloadTaskFromAws().execute(aws_key).get();
                Toast.makeText(getActivity(), file + "downloaded", Toast.LENGTH_SHORT).show();
                Log.d("DOWNLOAD", file);

            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }


            Intent emailIntent = new Intent(Intent.ACTION_SEND);
            emailIntent.setData(Uri.parse("mailto:"));
            //String[] to = {"arpit.khare@sjsu.edu"};
            //emailIntent.putExtra(Intent.EXTRA_EMAIL, to);

            //intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File("/path/to/file")));
            //TODO add to email paraameter
            emailIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File(file)));//"/PICTURES/test.jpg"
            emailIntent.setType("text/plain");

            // emailIntent.putExtra(Intent.EXTRA_SUBJECT, "New : Email with attachment");
            // emailIntent.putExtra(Intent.EXTRA_TEXT, "Hi, this email is being send from my android application which I have created. - Arjun Shukla");
            emailIntent.setType("message/rfc822");// MIME Type email specification

            Intent addIntent = new Intent();//whatever you want
            Intent[] intentArray = {addIntent};

            Intent chooser = Intent.createChooser(emailIntent, "Send Email");
            chooser.putExtra(Intent.EXTRA_INITIAL_INTENTS, intentArray);
            startActivity(chooser);

            //
        } else if (item.getTitle() == "Share") {

            final String[] shareWithUser = {null};
            final Dialog dialog = new Dialog(getActivity());
            dialog.setContentView(R.layout.custom_dialog);
            dialog.setTitle("      Share With      ");

            Button button = (Button) dialog.findViewById(R.id.done);


            final AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {

                @Override
                protected Void doInBackground(Void... params) {

                    AWS.shareFileWithUserNamed(shareWithUser[0], aws_key, aws_file_name, getActivity());
                    return null;
                }

                @Override
                protected void onPostExecute(Void aVoid) {

                    super.onPostExecute(aVoid);
                }
            };

            button.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                    EditText edit = (EditText) dialog.findViewById(R.id.username);
                    String text = edit.getText().toString();
                    shareWithUser[0] = text;
                    dialog.dismiss();
                    task.execute();
                    ParseQuery pushQuery = ParseInstallation.getQuery();

                    //TODO update this to the user notif is to be sent to
                    String fromUserId = username;
                    String toUserId = shareWithUser[0];

                    pushQuery.whereEqualTo("user_name", toUserId);

                    // Send push notification to query
                    ParsePush push = new ParsePush();
                    push.setQuery(pushQuery); // Set our Installation query
                    push.setMessage(fromUserId + " just shared a file with you!");
                    push.sendInBackground();

                    Log.d("PARSE NOTIFIED", fromUserId + "--->" + toUserId);

                }
            });


            dialog.show();


        } else {
            return false;
        }
        return true;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private class DownloadTaskFromAws extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String file_suffix = params[0];
            File file = new File(Environment.getExternalStorageDirectory().toString() + "/" + file_suffix);


            AmazonS3 s3 = Utils.getS3Client(getActivity());
            TransferUtility transferUtility = new TransferUtility(s3, getActivity());

            final TransferObserver observer = transferUtility.download(
                    Constants.BUCKET_NAME,     /* The bucket to download from */
                    file_suffix,    /* The key for the object to download */
                    file        /* The file to download the object to */
            );

            return observer.getAbsoluteFilePath();


            //return null;
        }

    }



    private class MyTask extends AsyncTask<String, Void, List> {

        private Context context;

        public MyTask(Context context) {
            this.context = context;
        }

        @Override
        protected List doInBackground(String... params) {

            //AWS.getFilesForUser(params[0], context);
//            List<S3ObjectSummary> summ = AWS.getFileObjectsByPrefix("kaustubh.walokar@gmail.com", context);

            String user = params[0];

//            if (user.endsWith("/"))
//                user=user.replace("/","");

            List<S3ObjectSummary> summ = AWS.getFileObjectsByUser(user, context);
            for (S3ObjectSummary s : summ) {
                Log.d("S3", s.getKey());
            }


            Log.d("USER", user);
            ArrayList<FilePickerRow> rowsOfFiles = null;//= new ArrayList<>();
            rowsOfFiles = FilePickerRow.getRowsFromS3ObjectSummaries(user, summ, rowsOfFiles);

            List<String> folders = AWS.getFolderObjectsByPrefix(user, context);
            ArrayList<FilePickerRow> rowsOfFolders = FilePickerRow.getRowsFromFolderNames(user, folders, null);


            ArrayList<FilePickerRow> values = new ArrayList<>();

            values.addAll(rowsOfFolders);
            values.addAll(rowsOfFiles);


            Log.d("RET", values.toString());

            return values;
        }

        @Override
        protected void onPostExecute(List list) {
            Log.d("Notified", prefix);
            //adapter.notifyDataSetChanged();
            // getActivity().runOnUiThread(run);
            super.onPostExecute(list);
        }
    }

    private class FilePickerListAdapter extends ArrayAdapter<FilePickerRow> {

        private ArrayList<FilePickerRow> files;

        public FilePickerListAdapter(Context context, ArrayList<FilePickerRow> objects) {
            super(context, R.layout.file_picker_list_item, android.R.id.text1, objects);
            this.files = objects;
        }

        public void setData(ArrayList<FilePickerRow> files) {
            this.files.clear();
            this.files.addAll(files);
            notifyDataSetChanged();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View row = null;

            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                row = inflater.inflate(R.layout.file_picker_list_item, parent, false);
            } else {
                row = convertView;
            }

            FilePickerRow object = files.get(position);

            ImageView imageView = (ImageView) row.findViewById(R.id.file_picker_image);
            TextView textView = (TextView) row.findViewById(R.id.file_name);
            // Set single line
            textView.setSingleLine(true);
            textView.setText(object.getName());

            ((TextView) row.findViewById(R.id.file_desc)).setText(object.description);
            ((TextView) row.findViewById(R.id.aws_key)).setText(object.getS3Key());
            if (object.isFile()) {
                // Show the file icon
                //txt file
                if (object.getName().contains(".txt")) {
                    imageView.setImageResource(R.mipmap.text_image);
                }
                //pdf file
                else if (object.getName().contains(".pdf")) {
                    imageView.setImageResource(R.mipmap.pdf_image);
                }
                //doc file
                else if (object.getName().contains(".doc")) {
                    imageView.setImageResource(R.mipmap.doc_image);
                }
                //xls file
                else if (object.getName().contains(".xls")) {
                    imageView.setImageResource(R.mipmap.xls_image);
                }
                //ppt file
                else if (object.getName().contains(".ppt")) {
                    imageView.setImageResource(R.mipmap.ppt_image);
                } else
                    imageView.setImageResource(R.drawable.file);
                ((TextView) row.findViewById(R.id.type)).setText("file");

                // if(object.getType()== )
            } else {
                // Show the folder icon
                imageView.setImageResource(R.drawable.folder);
                ((TextView) row.findViewById(R.id.type)).setText("folder");
            }

            return row;
        }

    }


}
