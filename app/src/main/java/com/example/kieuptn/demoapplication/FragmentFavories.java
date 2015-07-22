package com.example.kieuptn.demoapplication;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by KieuPTN on 7/9/2015.
 */
public class FragmentFavories extends Fragment {

    static final String TABLE_NAME = "Favories";
    ArrayList<ObjectItem> arrayList;
    MyArrayAdapter myArrayAdapter;
    ListView listView;
    Database database;

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
        View view = inflater.inflate(R.layout.fragment_favories, container, false);

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
        database.close();
    }

    @Override
    public void onResume() {
        super.onResume();

        database = new Database(getActivity());
        arrayList = new ArrayList<ObjectItem>(database.getDataFromTable(TABLE_NAME, null));

        myArrayAdapter = new MyArrayAdapter(getActivity(), R.layout.custom_list_item, arrayList);

        listView = (ListView)getActivity().findViewById(R.id.listView);
        listView.setAdapter(myArrayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int positionItem, long l) {
                Intent musicPlayerIntent = new Intent(getActivity().getBaseContext(), MusicPlayerActivity.class);
                musicPlayerIntent.putExtra("Object", arrayList.get(positionItem));

                getActivity().startActivity(musicPlayerIntent);
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int positionItem, long l) {
                new AlertDialog.Builder(getActivity()).setTitle("Warning")
                        .setMessage("Do you want to delete favories song?")
                        .setPositiveButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        database.deleteData(TABLE_NAME, arrayList.get(positionItem).getId().toString());
                        arrayList.remove(positionItem);
                        listView.invalidateViews();
                    }
                }).show();
                return false;
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }
}
