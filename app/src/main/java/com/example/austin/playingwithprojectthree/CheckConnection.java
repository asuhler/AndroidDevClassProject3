package com.example.austin.playingwithprojectthree;

import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.net.URL;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.util.logging.Logger;


/**
 * Created by Austin on 3/25/2016.
 */
public class CheckConnection extends AsyncTask<String, Void, Integer>{

    protected Integer doInBackground(String... params){
        URL address = null;
        int status = 0;
        Log.e("LOG", params[0]);
        try{
            address = new URL(params[0]);

        }catch (MalformedURLException e){
            e.printStackTrace();
        }
        try{
            HttpURLConnection conn = (HttpURLConnection) address.openConnection();
            status = conn.getResponseCode();
        } catch (IOException e){
            Log.e("LOG", e.toString() + " YOU FAILED");
            e.printStackTrace();
        }
        return status;
    }

    @Override
    protected void onPostExecute(Integer result) {

    }

}
