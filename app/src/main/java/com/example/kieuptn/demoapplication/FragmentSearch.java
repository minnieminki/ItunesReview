package com.example.kieuptn.demoapplication;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by KieuPTN on 7/9/2015.
 */
public class FragmentSearch extends Fragment implements View.OnClickListener, TextWatcher {

    ArrayList<ObjectItem> arrayList;
    ListView resultListView;
    ImageButton searchButton;
    AutoCompleteTextView autoCompleteTextView;

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
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        autoCompleteTextView = (AutoCompleteTextView)view.findViewById(R.id.autoCompleteTextView);
        resultListView = (ListView)view.findViewById(R.id.resultListView);
        searchButton = (ImageButton)view.findViewById(R.id.searchImageButton);
        searchButton.setOnClickListener(this);

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
        arrayList = FragmentTopSong.objectArrayList;
        Map<String, String> map = new HashMap<String, String>();
        ArrayList<String> sourceArrayList = new ArrayList<>();
        for (int i = 0; i < arrayList.size(); i++) {
            if (!map.containsKey(arrayList.get(i).getArtist()) && !map.containsKey(arrayList.get(i).getSong())) {
                map.put(arrayList.get(i).getArtist(), "Artist");
                map.put(arrayList.get(i).getSong(), "Song");
                sourceArrayList.add(arrayList.get(i).getArtist());
                sourceArrayList.add(arrayList.get(i).getSong());
            }
        }
        ArrayAdapter<String> sourceAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, sourceArrayList);
        autoCompleteTextView.setAdapter(sourceAdapter);
        autoCompleteTextView.addTextChangedListener(this);
        autoCompleteTextView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
                if (keyCode == KeyEvent.KEYCODE_ENTER) {
                    autoCompleteTextView.dismissDropDown();
                    InputMethodManager inputMethodManager = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(autoCompleteTextView.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                }
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

    @Override
    public void onClick(View view) {
        if (view == searchButton) {
            final ArrayList<ObjectItem> resultArrayList = new ArrayList<ObjectItem>();

            String key = autoCompleteTextView.getText().toString().toLowerCase();

            if (key.length() == 0) {
                new AlertDialog.Builder(getActivity()).setTitle("Warning").setMessage("Enter key word").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).show();
            } else {
                for (int i = 0; i < arrayList.size(); i++) {
                    ObjectItem objectItem = arrayList.get(i);
                    if ((objectItem.getArtist().toLowerCase().indexOf(key) != -1)
                            || (objectItem.getSong().toLowerCase().indexOf(key) != -1)) {
                        resultArrayList.add(objectItem);
                    }
                }

                MyArrayAdapter myArrayAdapter = new MyArrayAdapter(getActivity(), R.layout.custom_list_item, resultArrayList);
                resultListView.setAdapter(myArrayAdapter);
                resultListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int positionItem, long l) {
                        Intent musicPlayerIntent = new Intent(getActivity().getBaseContext(), MusicPlayerActivity.class);
                        musicPlayerIntent.putExtra("Object", resultArrayList.get(positionItem));

                        getActivity().startActivity(musicPlayerIntent);
                    }
                });
            }
        }
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        autoCompleteTextView.getText();
    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        if (autoCompleteTextView.getText().length() > 0) {
            onClick(searchButton);
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }
}
