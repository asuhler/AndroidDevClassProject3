package com.example.austin.playingwithprojectthree;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
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
    protected ArrayList<animal> doInBackground(String... params){
        ArrayList<animal> pets = new ArrayList<animal>();
        String fullJSON = null;
        URL address = null;
        int status = 0;
        try{
            address = new URL(params[0]);
        }catch (MalformedURLException e){
            e.printStackTrace();
        }
        try {
            BufferedReader buffer = null;
            HttpURLConnection conn = (HttpURLConnection) address.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept-Charset", "UTF-8");
            conn.setConnectTimeout(R.integer.Timeout);
            conn.setReadTimeout(R.integer.Timeout);

            try {
                conn.connect();
                if((conn.getResponseCode()/100)!= 2){
                    Log.e("LOG", Integer.toString(conn.getResponseCode()) + " Failed to connect sucessfully");
                    //return null;
                    }
                buffer = new BufferedReader(new InputStreamReader(conn.getInputStream()),R.integer.Buffer_Read_Size );

                StringBuffer sbuffer = new StringBuffer();
                while(buffer.readLine()!=null){
                    sbuffer.append(buffer.readLine());
                }
                fullJSON = sbuffer.toString();


            } catch(Exception e){
                Log.e("LOG", e.toString() + " Failed inside the connect to server and read buffer");
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
            JSONObject JSON = new JSONObject(fullJSON);
            JSONArray STUFF = JSON.getJSONArray("pets");

            for(int i=0; i<STUFF.length(); i++){
                animal temp = new animal("","");
                JSONObject name = STUFF.getJSONObject(i);
                temp.name = name.getString("name");
                JSONObject file = STUFF.getJSONObject(i);
                temp.file = file.getString("file");
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
