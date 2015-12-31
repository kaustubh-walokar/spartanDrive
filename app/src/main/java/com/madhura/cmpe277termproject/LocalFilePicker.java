package com.madhura.cmpe277termproject;

import android.app.ListFragment;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.kaustubh.cmpe277termproject.R;
import com.kaustubh.cmpe277termproject.aws.utils.Constants;
import com.kaustubh.cmpe277termproject.aws.utils.Utils;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link LocalFilePicker.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link LocalFilePicker#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LocalFilePicker extends ListFragment {
    //Variables for FilePicker
    public final static String EXTRA_FILE_PATH = "file_path";
    public final static String EXTRA_SHOW_HIDDEN_FILES = "show_hidden_files";
    public final static String EXTRA_ACCEPTED_FILE_EXTENSIONS = "accepted_file_extensions";
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private final static String DEFAULT_INITIAL_DIRECTORY = "/sdcard";
    protected File mDirectory;
    protected ArrayList<File> mFiles;
    protected FilePickerListAdapter mAdapter;
    protected boolean mShowHiddenFiles = false;
    protected String[] acceptedFileExtensions;
    AmazonS3Client s3client;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private OnFragmentInteractionListener mListener;
    String username= null;
    public LocalFilePicker() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FileListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LocalFilePicker newInstance(String param1, String param2) {
        LocalFilePicker fragment = new LocalFilePicker();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }


    public void setUsername(String username) {
        this.username = username;
    }
   // @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }


        String emailID = getArguments().getString(Constants.USERNAME);
        if (emailID != null) {
            setUsername(emailID);
           // updatePrefix();
        } else {
            try {
                throw new Exception("set email in arguments");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        //s3client = Utils.getS3Client(this.getContext());
        //AWS.getFilesForUser("",this.getActivity());
      //  new MyTask(this.getActivity().getApplicationContext()).execute("madhurab31@gmail.com/");

        // Set initial directory
        mDirectory = new File(DEFAULT_INITIAL_DIRECTORY);


        // Initialize the ArrayList
        mFiles = new ArrayList<File>();

        // Set the ListAdapter
        mAdapter = new FilePickerListAdapter(getActivity(), mFiles);
        setListAdapter(mAdapter);

        // Initialize the extensions array to allow any file extensions
        //  acceptedFileExtensions = new String[]{".doc", ".docx", ".csv", ".pdf"};
        acceptedFileExtensions = new String[]{};
        //For Back Button
        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) getActivity().findViewById(R.id.toolbar);
        //getActivity().setActionBar(toolbar);
        //setSupportAc
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setHasOptionsMenu(true);

    }//end of OnCreate()


    //For Back Button Activation
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case android.R.id.home:
                if (mDirectory.getParentFile() != null) {
                    mDirectory = mDirectory.getParentFile();
                    refreshFilesList();
                } else Toast.makeText(getActivity(), "At top ", Toast.LENGTH_SHORT).show();
                return true;
        }
        Toast.makeText(getActivity(), "here ", Toast.LENGTH_SHORT).show();
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onResume() {
        refreshFilesList();
        super.onResume();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_file_list, container, false);

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
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

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        final File newFile = (File) l.getItemAtPosition(position);
        Log.d("File: ", newFile.toString());
        if (newFile.isFile()) {
            // Set result
            Intent extra = new Intent();
            extra.putExtra(EXTRA_FILE_PATH, newFile.getAbsolutePath());
            getActivity().setResult(getActivity().RESULT_OK, extra);

            // Finish the activity
            Toast.makeText(getActivity(), "File uploaded succesfully!!", Toast.LENGTH_SHORT).show();

//            new UploadTaskFromAws().execute();

            new AsyncTask<Void,Void,Void>(){
                TransferObserver observer;
                @Override
                protected Void doInBackground(Void... params) {
                    s3client=Utils.getS3Client(getActivity());
                    TransferUtility transferUtility = new TransferUtility(s3client, getActivity());


                    observer = transferUtility.upload(
                            Constants.BUCKET_NAME,
                            username+"/Uploaded/"+newFile.getName(),
                            newFile
                    );
                    return null;
                }

                @Override
                protected void onPostExecute(Void aVoid) {

                    super.onPostExecute(aVoid);
                }
            }.execute();

            getActivity().finish();
        } else {
            Log.d("Directory ", mDirectory.toString());
            mDirectory = newFile;
            // Update the files list
            Log.d("Directory ", mDirectory.toString());
            refreshFilesList();
        }

        super.onListItemClick(l, v, position, id);
    }

    /**
     * Updates the list view to the current directory
     */
    protected void refreshFilesList() {
        // Clear the files ArrayList
        mFiles.clear();

        // Set the extension file filter
        ExtensionFilenameFilter filter = new ExtensionFilenameFilter(acceptedFileExtensions);

        // Get the files in the directory
        // File[] files = mDirectory.listFiles();
        File[] files = mDirectory.listFiles(filter);
        if (files != null && files.length > 0) {
            for (File f : files) {
                if (f.isHidden() && !mShowHiddenFiles) {
                    // Don't add the file
                    continue;
                }

                // Add the file the ArrayAdapter
                mFiles.add(f);
            }

            Collections.sort(mFiles, new FileComparator());
        }
        mAdapter.setData(mFiles);
        mAdapter.notifyDataSetChanged();
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
//    public interface OnFragmentInteractionListener {
//        // TODO: Update argument type and name
//        void onFragmentInteraction(Uri uri);
//    }

    //Adding ExtensionFileNameFilter
    private class ExtensionFilenameFilter implements FilenameFilter {
        private String[] mExtensions;

        public ExtensionFilenameFilter(String[] extensions) {
            super();
            mExtensions = extensions;
        }

        @Override
        public boolean accept(File dir, String filename) {
            if (new File(dir, filename).isDirectory()) {
                // Accept all directory names
                return true;
            }
            if (mExtensions != null && mExtensions.length > 0) {
                for (int i = 0; i < mExtensions.length; i++) {
                    if (filename.endsWith(mExtensions[i])) {
                        // The filename ends with the extension
                        return true;
                    }
                }
                // The filename did not match any of the extensions
                return false;
            }
            // No extensions has been set. Accept all file extensions.
            return true;
        }
    }

    //Adding FileComparator
    private class FileComparator implements Comparator<File> {
        @Override
        public int compare(File f1, File f2) {
            if (f1 == f2) {
                return 0;
            }
            if (f1.isDirectory() && f2.isFile()) {
                // Show directories above files
                return -1;
            }
            if (f1.isFile() && f2.isDirectory()) {
                // Show files below directories
                return 1;
            }
            // Sort the directories alphabetically
            return f1.getName().compareToIgnoreCase(f2.getName());
        }
    }

    private class MyTask extends AsyncTask<String, Void, Void> {

        private Context context;

        public MyTask(Context context) {
            this.context = context;
        }

        @Override
        protected Void doInBackground(String... params) {

//            AWS.getFilesForUser("kaustubh.walokar@gmail.com/", context);
            return null;
        }
    }

    private class UploadTaskFromAws extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String file_suffix = params[0];
            File file = new File(Environment.getExternalStorageDirectory().toString() + "/" + file_suffix);


            AmazonS3 s3 = Utils.getS3Client(getActivity());
            TransferUtility transferUtility = new TransferUtility(s3, getActivity());

            final TransferObserver observer = transferUtility.upload(
                    Constants.BUCKET_NAME,     /* The bucket to download from */
                    file_suffix,    /* The key for the object to download */
                    file        /* The file to download the object to */
            );

            observer.getState().equals(TransferState.COMPLETED);

            return observer.getAbsoluteFilePath();


            //return null;
        }

    }




    /*
         Code For FilePicker
     */
    private class FilePickerListAdapter extends ArrayAdapter<File> {

        private List<File> mObjects;

        public FilePickerListAdapter(Context context, List<File> objects) {
            super(context, R.layout.file_picker_list_item, android.R.id.text1, objects);
            mObjects = objects;
        }

        public void setData(List<File> objects) {
//            this.mObjects=objects;
            //   this.mObjects.clear();
            this.mObjects.addAll(objects);
            // notifyDataSetChanged();

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

            File object = mObjects.get(position);

            ImageView imageView = (ImageView) row.findViewById(R.id.file_picker_image);
            TextView textView = (TextView) row.findViewById(R.id.file_name);
            // Set single line
            textView.setSingleLine(true);

            textView.setText(object.getName());
            if (object.isFile()) {
                //text file
                Log.d("File:::: ", object.getName());
                //txt file
                if(object.getName().contains(".txt")){
                    imageView.setImageResource(R.mipmap.text_image);
                }
                //pdf file
                else if(object.getName().contains(".pdf")){
                    imageView.setImageResource(R.mipmap.pdf_image);
                }
                //doc file
                else if(object.getName().contains(".doc")){
                    imageView.setImageResource(R.mipmap.doc_image);
                }
                //xls file
                else if(object.getName().contains(".xls")){
                    imageView.setImageResource(R.mipmap.xls_image);
                }
                //ppt file
                else if(object.getName().contains(".ppt")){
                    imageView.setImageResource(R.mipmap.ppt_image);
                }
                // Show the file icon
                else
                    imageView.setImageResource(R.drawable.file);
            } else {
                // Show the folder icon
                imageView.setImageResource(R.drawable.folder);
            }
            return row;
        }

    }//end of FilePickerListAdapter()


}
