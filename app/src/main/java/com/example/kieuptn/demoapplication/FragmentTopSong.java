package com.example.kieuptn.demoapplication;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;

import java.util.ArrayList;

/**
 * Created by KieuPTN on 7/9/2015.
 */
public class FragmentTopSong extends Fragment implements AdapterView.OnItemClickListener, ParsingJSON.jsonInterface {

    ProgressBar progressBar;
    ListView songListView;
    MyArrayAdapter myArrayAdapter = null;
    public static ArrayList<ObjectItem> objectArrayList;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_top_song, container, false);
        progressBar = (ProgressBar)view.findViewById(R.id.listViewProgressBar);
        songListView = (ListView)view.findViewById(R.id.songListView);
        songListView.setItemsCanFocus(false);

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();

        if (myArrayAdapter != null) {
            progressBar.setVisibility(View.INVISIBLE);

            songListView.setAdapter(myArrayAdapter);
            songListView.setOnItemClickListener(this);
        } else {
            // check network stage in device
            ConnectivityManager connMgr = (ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

            if (networkInfo != null && networkInfo.isConnected()) {
                ParsingJSON parsingJSON = new ParsingJSON(this);
                parsingJSON.execute();
            } else {
                new AlertDialog.Builder(getActivity()).setTitle("Network Error")
                        .setMessage("Cannot connect network at this time. Please check your mobile network and try again.")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        }).show();
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void callBack(ArrayList<ObjectItem> arr) {
        progressBar.setVisibility(View.INVISIBLE);
        objectArrayList = arr;
        // create arrayAdapter
        myArrayAdapter = new MyArrayAdapter(getActivity(), R.layout.custom_list_item, objectArrayList);

        songListView.setAdapter(myArrayAdapter);
        songListView.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int positionItem, long l) {
        Intent musicPlayerIntent = new Intent(getActivity().getBaseContext(), MusicPlayerActivity.class);
        musicPlayerIntent.putExtra("Object", objectArrayList.get(positionItem));

        getActivity().startActivity(musicPlayerIntent);
    }

}
