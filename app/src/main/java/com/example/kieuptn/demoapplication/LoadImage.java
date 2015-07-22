package com.example.kieuptn.demoapplication;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


/**
 * Created by KieuPTN on 7/10/2015.
 */
public class LoadImage extends AsyncTask<String, Integer, Bitmap> {
    Bitmap mBitmap;
    ImageView imageView;
    ProgressBar progressBar;

    public LoadImage(ImageView _imageView, ProgressBar _progressBar) {
        imageView = _imageView;
        progressBar = _progressBar;
    }

    @Override
    protected Bitmap doInBackground(String... params) {
        String urlString = params[0];
        // get path to sdCard
        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + "/DemoApplication");
        if (!myDir.exists()) {
            myDir.mkdirs();
        }

        // remove special character from String
        String imageName = urlString.replaceAll("[\\-+?^:;/. ]", "");

        // create imagePath and check path exist
        File file = new File(myDir, imageName + ".png");
        if (!file.exists()) {
            saveImage(urlString, file);
        } else {
            loadImage(file);
        }
        return mBitmap;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        super.onPostExecute(bitmap);
        progressBar.setVisibility(View.INVISIBLE);
        imageView.setImageBitmap(mBitmap);
        //imageView.setScaleType(ImageView.ScaleType.FIT_XY);
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
    }

    private Bitmap downloadBitmap (String urlString) {
        URL url;
        Bitmap bitmap = null;
        try {
            url = new URL(urlString);
            HttpURLConnection httpURLConnection;
            try {
                httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("GET");
                httpURLConnection.connect();
                InputStream inputStream = httpURLConnection.getInputStream();
                bitmap = BitmapFactory.decodeStream(inputStream);

            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    private void saveImage(String urlString, File file) {
        mBitmap = downloadBitmap(urlString);
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            mBitmap.compress(Bitmap.CompressFormat.JPEG, 90, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private  void loadImage(File file) {
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            mBitmap = BitmapFactory.decodeStream(fileInputStream);
            fileInputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
