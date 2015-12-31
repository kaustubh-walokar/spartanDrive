package com.kaustubh.cmpe277termproject;


import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.kaustubh.cmpe277termproject.aws.utils.Constants;


/**
 * A simple {@link Fragment} subclass.
 */
public class MenuFragment extends Fragment {


    public MenuFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      //
      // Toast.makeText(getActivity(),getArguments().getString(Constants.USERNAME), Toast.LENGTH_SHORT).show();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_menu, container, false);
        final Intent i = new Intent(getActivity(),MyFilePicker.class);
        i.putExtra(Constants.USERNAME, getArguments().getString(Constants.USERNAME));
        //i.putExtra(Constants.USERNAME,)


        view.findViewById(R.id.upload_files).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i.putExtra("type","upload");
                startActivity(i);
            }
        });
        view.findViewById(R.id.view_files).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i.putExtra("type","view");
                startActivity(i);
            }
        });

        return view;
    }

}
