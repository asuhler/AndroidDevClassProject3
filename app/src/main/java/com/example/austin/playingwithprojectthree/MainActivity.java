package com.example.austin.playingwithprojectthree;

import android.graphics.Color;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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

public class MainActivity extends AppCompatActivity {
    RelativeLayout rl;
    ImageView background;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        rl = (RelativeLayout)findViewById(R.id.RelativeLayout1);
        background = (ImageView)findViewById(R.id.BackgroundView);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Lets remove the title
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        //populate spinner
        setupSimpleSpinner();


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
            case R.id.TestBackground:
                changeBackground();
                return true;
            case R.id.about:
                doHelp();
                return true;
        }

        //all else fails let super handle it
        return super.onOptionsItemSelected(item);
    }


    Spinner spinner;
    private void setupSimpleSpinner() {
        //create a data adapter to fill above spinner with choices
        //R.array.numbers is arraylist in strings.xml
        //R.layout.spinner_item_simple is just a textview
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.numbers, R.layout.spinner_item_simple);

        //get a reference to the spinner
        spinner = (Spinner)findViewById(R.id.spinner);

        //bind the spinner to the datasource managed by adapter
        spinner.setAdapter(adapter);
        //respond when spinner clicked
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public static final int SELECTED_ITEM = 0;

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int pos, long rowid) {
                if (arg0.getChildAt(SELECTED_ITEM) != null) {
                    ((TextView) arg0.getChildAt(SELECTED_ITEM)).setTextColor(Color.WHITE);
                    Toast.makeText(MainActivity.this, (String) arg0.getItemAtPosition(pos), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
    }

    private void changeBackground(){
        background.setImageResource(R.drawable.falken);


    }


    private void doHelp() {
        // Create out AlterDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("This is where the help screen goes");
        //create an anonymous class that is listening for button click
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            /**
             * This method will be invoked when a button in the dialog is clicked.
             * Note the @override
             * Note also that I have to scope the context in the toast below, thats because anonymous classes have a
             * reference to the class they were declared in accessed via Outerclassname.this
             *
             * @param dialog The dialog that received the click.
             * @param which  The button that was clicked (e.g.
             *               {@link DialogInterface#BUTTON1}) or the position
             */
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(MainActivity.this, "clicked OK in Help", Toast.LENGTH_SHORT).show();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }


}
