package com.example.kieuptn.demoapplication;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by KieuPTN on 7/10/2015.
 */
public class ParsingJSON extends AsyncTask {

    private static ParsingJSON parsingJSON;
    JSONObject mJSONObject = null;
    private static ArrayList<ObjectItem> arrayList;
    jsonInterface mJSONInterface;

    public ParsingJSON(jsonInterface ac){
        this.mJSONInterface = ac;
    }

    @Override
    protected Object doInBackground(Object[] objects) {
        arrayList = new ArrayList<ObjectItem>();
        readJSONObject();
        return null;
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
        mJSONInterface.callBack(arrayList);
    }

    // ReadJSON
    protected void getJSONFromURL (URL url) {
        try {
            HttpsURLConnection httpsURLConnection = (HttpsURLConnection)url.openConnection();
            httpsURLConnection.setRequestMethod("GET");
            httpsURLConnection.connect();

            // get JSON data from server
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpsURLConnection.getInputStream()));
            String temp;
            StringBuilder jsonString = new StringBuilder();
            while ((temp = bufferedReader.readLine()) != null) {
                jsonString.append(temp);
            }
            try {
                mJSONObject = new JSONObject(jsonString.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void readJSONObject() {
        URL url = null;
        try {
            url = new URL("https://itunes.apple.com/us/rss/topsongs/limit=100/json");
            getJSONFromURL(url);
            try {
                JSONArray entryJSONArray = mJSONObject.getJSONObject("feed").getJSONArray("entry");
                for (int i = 0; i < entryJSONArray.length(); i++){
                    JSONObject entryJSONObject = entryJSONArray.getJSONObject(i);

                    JSONObject idSJONObject = entryJSONObject.getJSONObject("id").getJSONObject("attributes");
                    JSONObject nameJSONObject = entryJSONObject.getJSONObject("im:name");
                    JSONArray imageJSONArray = entryJSONObject.getJSONArray("im:image");
                    JSONObject artistJSONObject = entryJSONObject.getJSONObject("im:artist");
                    JSONArray linkJSONArray = entryJSONObject.getJSONArray("link");
                    JSONObject linkJSONObject = linkJSONArray.getJSONObject(1).getJSONObject("attributes");
                    JSONObject typeJSONObject = entryJSONObject.getJSONObject("im:contentType").getJSONObject("attributes");

                    // create and add value objectItem
                    ObjectItem objectItem = new ObjectItem();
                    objectItem.setId(idSJONObject.getInt("im:id"));
                    objectItem.setImageURLString(imageJSONArray.getJSONObject(2).getString("label"));
                    objectItem.setSong(nameJSONObject.getString("label"));
                    objectItem.setArtist(artistJSONObject.getString("label"));
                    objectItem.setLinkString(linkJSONObject.getString("href"));
                    objectItem.setType(typeJSONObject.getString("label"));
                    arrayList.add(objectItem);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    // create interface
    public static interface jsonInterface {
        void callBack (ArrayList<ObjectItem> arr);
    }

}
