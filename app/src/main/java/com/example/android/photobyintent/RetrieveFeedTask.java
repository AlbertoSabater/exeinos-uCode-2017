package com.example.android.photobyintent;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.Result;

/**
 * Created by DSalva on 12/03/2017.
 */

class RetrieveFeedTask extends AsyncTask<String, Void, Bitmap> {

    private Exception exception;
    public ResultActivity activity;

    public RetrieveFeedTask(ResultActivity a)
    {
        this.activity = a;
    }
    protected Bitmap doInBackground(String... dirs) {
        Bitmap bmp = null;
        try {
            URL url = null;
            try {
                url = new URL(dirs[0]);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            try {
                bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
            return bmp;
        } catch (Exception e) {
            this.exception = e;

            return bmp;
        }
    }

    protected void onPostExecute(Bitmap feed) {
        activity.putImage(feed);
    }
}