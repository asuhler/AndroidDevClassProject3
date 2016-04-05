package com.example.austin.playingwithprojectthree;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.RelativeLayout;
import android.widget.ImageView;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import android.graphics.Bitmap;



public class MainActivity extends AppCompatActivity{
    public static final String[] NO_CONNECTION = {"No_Data"};
    RelativeLayout rl;
    ImageView background;
    SharedPreferences myPreference;
    SharedPreferences.OnSharedPreferenceChangeListener listener;
    Toolbar toolbar;
    Spinner spinner;
    ArrayList<animal> pets;
    Bitmap background_image;
    String[] Pet_Names;
    private ArrayAdapter adapter;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rl = (RelativeLayout)findViewById(R.id.RelativeLayout1);
        background = (ImageView)findViewById(R.id.BackgroundView);
        myPreference = PreferenceManager.getDefaultSharedPreferences(this);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        spinner = (Spinner)findViewById(R.id.spinner);
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);









        setSupportActionBar(toolbar);

        //Lets remove the title
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        //populate spinner


        listener = new SharedPreferences.OnSharedPreferenceChangeListener() {

            @Override
            public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
                //Toast.makeText(MainActivity.this, "Key=" + key, Toast.LENGTH_SHORT).show();
                if (key.equals("PREF_SERVER")) {
                    String myString = myPreference.getString("PREF_SERVER", "");
                    //Toast.makeText(MainActivity.this, "From Listener PREF_SERVER=" + myString, Toast.LENGTH_SHORT).show();
                    boolean hi = checkConnection();
                    if(hi==false){
                        setupSimpleSpinner(NO_CONNECTION);
                        background.setImageResource(R.color.toolbarTransparent);
                    }else {
                        setupSimpleSpinner(Pet_Names);
                        Bitmap hello = null;
                        String url = (myPreference.getString("PREF_SERVER", "") + "p0.png");
                        Log.e("LOG", url);
                        hello = downloadBMP(url);

                    }
                }

            }
        };
        // register the listener
        myPreference.registerOnSharedPreferenceChangeListener(listener);

        boolean hi =checkConnection();
        if(hi==false){
            setupSimpleSpinner(NO_CONNECTION);
            background.setImageResource(R.color.toolbarTransparent);
        }else{
            setupSimpleSpinner(Pet_Names);
            Bitmap hello = null;
            String url = (myPreference.getString("PREF_SERVER", "") + "p0.png");
            Log.e("LOG", url);
            hello = downloadBMP(url);

        }


    }

    public ArrayList<animal> getAnimalList(){
        ArrayList<animal> pets = new ArrayList<animal>();
        String address = myPreference.getString("PREF_SERVER", "") + "pets.json";

        DownloadJson hi = new DownloadJson();
        try{
            pets = hi.execute(address).get();

        }catch (ExecutionException e){
            Log.e("LOG" , "YOU FAILED with an execution exception in getAnimalList");
            e.printStackTrace();
        }catch (InterruptedException e){
            Log.e("LOG", "YOU FAILED with an interruption exception in getAnimalList");
            e.printStackTrace();
        }


        return pets;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case R.id.action_settings:
                Intent myIntent = new Intent(this, PrefActivity.class);
                startActivity(myIntent);
                return true;
        }

        //all else fails let super handle it
        return super.onOptionsItemSelected(item);
    }



    private void setupSimpleSpinner(String[] input) {
        //create a data adapter to fill above spinner with choices
        //R.array.numbers is arraylist in strings.xml
        //R.layout.spinner_item_simple is just a textview
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public static final int SELECTED_ITEM = 0;

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int pos, long rowid) {
                boolean hi = checkConnection();
                String url = myPreference.getString("PREF_SERVER", "") + "p" + pos +".png";
                Log.e("LOG", url);
                if (hi == true) {
                    downloadBMP(url);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });


        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,input);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }



    //checks for JSON file at initial URL set in preferences, returns array of names for spinner
    private boolean checkConnection(){
        int return_code = 0;

        if(!isConnectedToNetwork()){
            Toast.makeText(MainActivity.this, R.string.No_Network, Toast.LENGTH_SHORT).show();
            background.setImageResource(R.drawable.cat_404);
            return false;
        }else{
            //Toast.makeText(MainActivity.this, R.string.NETWORK, Toast.LENGTH_SHORT).show();
            String address = myPreference.getString("PREF_SERVER", "");
            Toast.makeText(MainActivity.this, address, Toast.LENGTH_SHORT).show();
            Log.e("LOG", "PREF_SERVER: " + address);
            CheckConnection BLARG = new CheckConnection();

            try{
                Log.e("LOG", address);
                return_code = BLARG.execute(address).get();
            }catch (InterruptedException e){
                Log.e("LOG", "Interrupted");
                e.printStackTrace();
            }catch (ExecutionException e){
                Log.e("LOG", address);
                Log.e("LOG", "failed to execute");
                e.printStackTrace();
            }
        }
        if(return_code==-1){
            Log.e("LOG", Integer.toString(return_code)+ " Failed to connect to the server, general failure");

        }else if(return_code != 200){
            Log.e("LOG", Integer.toString(return_code) + " Failed to connect to the server, failed to connect");
            Toast.makeText(MainActivity.this, "Failed to connect to page" + myPreference.getString("PREF_SERVER", "") + " returned code " + return_code, Toast.LENGTH_SHORT).show();
            return false;
        }else{
            //Toast.makeText(MainActivity.this, "IT WORKEDDDDD", Toast.LENGTH_SHORT).show();
            //Log.e("LOG", "IT WORKEDDDDDDD");

            pets = getAnimalList();
            if(pets.size()==0){
                //Log.e("LOG",pets.toString()+ " No pets in the pet array list");
                return false;
            }else{
                Pet_Names = new String[pets.size()];
                for(int i=0; i<pets.size(); i++){
                    Pet_Names[i]=(pets.get(i).name);
                }
                //Toast.makeText(MainActivity.this, Pet_Names.toString(), Toast.LENGTH_SHORT).show();



                return true;
            }

        }

        return false;

    }

    //download image from server based on object and file name
    private Bitmap downloadBMP(String filename){
        Bitmap output = null;

        downloadImage image = new downloadImage();
        try{
            output = image.execute(filename).get();
            background.setImageBitmap(output);
        }catch (Exception e){
            Log.e("LOG", e.toString());
            e.printStackTrace();
        }

        return output;
    }

    //check if phone is connected to network

    private boolean isConnectedToNetwork(){
        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo net = manager.getActiveNetworkInfo();
        if(net!=null){
            if(net.isConnectedOrConnecting()){
                return true;
            }
        }

        return false;
    }






}
