//package com.madhura.cmpe277termproject;
//
//import android.annotation.TargetApi;
//import android.app.ListFragment;
//import android.content.Context;
//import android.net.Uri;
//import android.os.AsyncTask;
//import android.os.Build;
//import android.os.Bundle;
//import android.support.v4.app.Fragment;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ArrayAdapter;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import com.amazonaws.services.s3.AmazonS3Client;
//import com.kaustubh.cmpe277termproject.R;
//import com.kaustubh.cmpe277termproject.aws.utils.AWS;
//
//import java.io.File;
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.List;
//
//
///**
// * A simple {@link Fragment} subclass.
// * Activities that contain this fragment must implement the
// * {@link FileListFragment.OnFragmentInteractionListener} interface
// * to handle interaction events.
// * Use the {@link FileListFragment#newInstance} factory method to
// * create an instance of this fragment.
// */
//public class FileListFragment extends ListFragment {
//    // TODO: Rename parameter arguments, choose names that match
//    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//    private static final String ARG_PARAM1 = "param1";
//    private static final String ARG_PARAM2 = "param2";
//
//    // TODO: Rename and change types of parameters
//    private String mParam1;
//    private String mParam2;
//
//    //Variables for FilePicker
//    public final static String EXTRA_FILE_PATH = "file_path";
//    public final static String EXTRA_SHOW_HIDDEN_FILES = "show_hidden_files";
//    public final static String EXTRA_ACCEPTED_FILE_EXTENSIONS = "accepted_file_extensions";
//    private final static String DEFAULT_INITIAL_DIRECTORY = "/";
//    protected File mDirectory;
//    protected ArrayList<File> mFiles;
//    protected FilePickerListAdapter mAdapter;
//    protected boolean mShowHiddenFiles = false;
//    protected String[] acceptedFileExtensions;
//
//
//
//    private OnFragmentInteractionListener mListener;
//
//    public FileListFragment() {
//        // Required empty public constructor
//    }
//
//    /**
//     * Use this factory method to create a new instance of
//     * this fragment using the provided parameters.
//     *
//     * @param param1 Parameter 1.
//     * @param param2 Parameter 2.
//     * @return A new instance of fragment FileListFragment.
//     */
//    // TODO: Rename and change types and number of parameters
//    public static FileListFragment newInstance(String param1, String param2) {
//        FileListFragment fragment = new FileListFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
//        return fragment;
//    }
//
//    AmazonS3Client s3client;
//    //@TargetApi(Build.VERSION_CODES.M)
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
//        //s3client = Utils.getS3Client(this.getContext());
//        //AWS.getFilesForUser("",this.getActivity());
//        new MyTask(this.getActivity().getApplicationContext()).execute("kaustubh.walokar@gmail.com/");
//
//        // Set initial directory
//        mDirectory = new File(DEFAULT_INITIAL_DIRECTORY);
//
//        // Initialize the ArrayList
//        mFiles = new ArrayList<File>();
//
//        // Set the ListAdapter
//        mAdapter = new FilePickerListAdapter(getActivity(), mFiles);
//        setListAdapter(mAdapter);
//
//    }//end of OnCreate()
//
//    @Override
//    public void onResume() {
//     //   refreshFilesList();
//        super.onResume();
//    }
//
//    /**
//     * Updates the list view to the current directory
//     */
////    protected void refreshFilesList() {
////        // Clear the files ArrayList
////        mFiles.clear();
////
////        // Set the extension file filter
//////        ExtensionFilenameFilter filter = new ExtensionFilenameFilter(acceptedFileExtensions);
////
////        // Get the files in the directory
////        File[] files = mDirectory.listFiles();
////                //mDirectory.listFiles(filter);
////        if(files != null && files.length > 0) {
////            for(File f : files) {
////                if(f.isHidden() && !mShowHiddenFiles) {
////                    // Don't add the file
////                    continue;
////                }
////
////                // Add the file the ArrayAdapter
////                mFiles.add(f);
////            }
////
////            //Collections.sort(mFiles, new FileComparator());
////        }
////        mAdapter.notifyDataSetChanged();
////    }
//
//
//
//    private class MyTask extends AsyncTask<String ,Void,Void>{
//
//        private Context context;
//
//        public MyTask(Context context) {
//            this.context = context;
//        }
//
//        @Override
//        protected Void doInBackground(String... params) {
//
////            AWS.getFilesForUser("kaustubh.walokar@gmail.com/", context);
//            return null;
//        }
//    }
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//
//        return inflater.inflate(R.layout.fragment_file_list, container, false);
//
//    }
//
//    // TODO: Rename method, update argument and hook method into UI event
//    public void onButtonPressed(Uri uri) {
//        if (mListener != null) {
//            mListener.onFragmentInteraction(uri);
//        }
//    }
//
//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
//    }
//
//    @Override
//    public void onDetach() {
//        super.onDetach();
//        mListener = null;
//    }
//
//    /**
//     * This interface must be implemented by activities that contain this
//     * fragment to allow an interaction in this fragment to be communicated
//     * to the activity and potentially other fragments contained in that
//     * activity.
//     * <p/>
//     * See the Android Training lesson <a href=
//     * "http://developer.android.com/training/basics/fragments/communicating.html"
//     * >Communicating with Other Fragments</a> for more information.
//     */
//    public interface OnFragmentInteractionListener {
//        // TODO: Update argument type and name
//        void onFragmentInteraction(Uri uri);
//    }
//
//    /*
//         Code For FilePicker
//     */
//    private class FilePickerListAdapter extends ArrayAdapter<File> {
//
//        private List<File> mObjects;
//
//        public FilePickerListAdapter(Context context, List<File> objects) {
//            super(context, R.layout.file_picker_list_item, android.R.id.text1, objects);
//            mObjects = objects;
//        }
//
//        @Override
//        public View getView(int position, View convertView, ViewGroup parent) {
//
//            View row = null;
//
//            if(convertView == null) {
//                LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//                row = inflater.inflate(R.layout.file_picker_list_item, parent, false);
//            } else {
//                row = convertView;
//            }
//
//            File object = mObjects.get(position);
//
//            ImageView imageView = (ImageView)row.findViewById(R.id.file_picker_image);
//            TextView textView = (TextView)row.findViewById(R.id.file_name);
//            // Set single line
//            textView.setSingleLine(true);
//
//            textView.setText(object.getName());
//            if(object.isFile()) {
//                // Show the file icon
//                imageView.setImageResource(R.drawable.file);
//            } else {
//                // Show the folder icon
//                imageView.setImageResource(R.drawable.folder);
//            }
//            return row;
//        }
//
//    }//end of FilePickerListAdapter()
//
//
//
//
//
//}
