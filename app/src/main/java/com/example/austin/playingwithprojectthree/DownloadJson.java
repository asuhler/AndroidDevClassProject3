package com.example.austin.playingwithprojectthree;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.Buffer;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * Created by Austin on 3/25/2016.
 */

public class DownloadJson extends AsyncTask<String, Void, ArrayList<animal>>{
    public static final int BUFFER_READSIZE = 8096;
    public static final int TIMEOUT = 1000;
    protected ArrayList<animal> doInBackground(String... params){
        ArrayList<animal> pets = new ArrayList<animal>();
        String fullJSON = "";
        URL address = null;
        int status = 0;
        Log.e("LOG", "Made it inside the friggen download");
        try{
            address = new URL(params[0]);
        }catch (MalformedURLException e){
            Log.e("LOG", "Failed due to malformed URL exception: " + params[0]);
            e.printStackTrace();
        }

        try {
            BufferedReader buffer = null;
            HttpURLConnection conn = (HttpURLConnection) address.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept-Charset", "UTF-8");
            conn.setConnectTimeout(TIMEOUT);
            conn.setReadTimeout(TIMEOUT);
            Log.e("LOG", "Made it past connection setup");

            try {
                conn.connect();
                if((conn.getResponseCode()/100)!= 2){
                    Log.e("LOG", Integer.toString(conn.getResponseCode()) + " Failed to connect sucessfully");
                    conn.disconnect();
                    return null;
                    }

                buffer = new BufferedReader(new InputStreamReader(conn.getInputStream()), BUFFER_READSIZE);
                String data;
                StringBuffer sbuffer = new StringBuffer();
                while((data=buffer.readLine())!=null){
                    Log.e("LOG", "made it inside the while loop");
                    sbuffer.append(data);
                }
                Log.e("LOG", "made it past the while loop");
                fullJSON = sbuffer.toString();


            } catch(Exception e){
                Log.e("LOG", " Failed inside the connect to server and read buffer");
                e.printStackTrace();

            }finally {
                buffer.close();
                conn.disconnect();
                Log.e("LOG", "Exited the finally");
            }

        }catch( IOException e){
            Log.e("LOG", e.toString() + " YOU FAILED");
            e.printStackTrace();
        }

        try{
            Log.e("LOG", fullJSON);
            JSONObject JSON = new JSONObject(fullJSON);
            Log.e("LOG", "made it there");
            JSONArray STUFF = JSON.getJSONArray("pets");
            if (STUFF == null) {
                Log.e("LOG", "tried to dereference null jsonArray");

            }

            for(int i=0; i<STUFF.length(); i++){
                animal temp = new animal("","");

                JSONObject pet = STUFF.getJSONObject(i);
                Log.e("LOG", pet.toString());
                temp.name = pet.getString("name");
                temp.file = pet.getString("file");
                pets.add(temp);
            }

        }catch (JSONException e){
            Log.e("LOG", e.toString() + " YOU FAILED");
            e.printStackTrace();
        }

        return pets;
    }

    @Override
    protected void onPostExecute(ArrayList<animal> result) {

    }

}
