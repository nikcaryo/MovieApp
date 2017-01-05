package caryotacos.experimental;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private String date = "";
    private String theater = "http://www.fandango.com/century20downtownredwoodcityandxd_aaubf/theaterpage"; //default theater set to Redwood
    private String[] theaterList = {
            "Redwood City", "Shoreline", "AMC Cupertino"
    };
    private String theaterToast = "Redwood City Theater loaded"; //default toast for theater
    private int duration = Toast.LENGTH_LONG; //how long the toast pops up for
    private int mYear, mDay, mMonth; //stores what date is selected by the user...
    private int year, month, day; //as opposed to the actual date
    private Calendar calendar;
    private RecyclerView mRecyclerView; //recycler view holds the Cards that show the movies
    private RecyclerView.Adapter mAdapter; //adapter to decide what goes in each card
    private RecyclerView.LayoutManager mLayoutManager;@
            Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); //set layout to the main xml layout file
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar); //finds the toolbar and sets the button up
        setSupportActionBar(toolbar);

        calendar = Calendar.getInstance(); //gets current date and stores it
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH) + 1;
        day = calendar.get(Calendar.DAY_OF_MONTH);
        date = "?date=" + month + "/" + day + "/" + year;

        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view); //initialize the recycler view
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        new URLParser().execute(theater); //executes the Async task with the default url

        final Context context = getApplicationContext(); //shows the toast
        Toast toast = Toast.makeText(context, theaterToast, duration);
        toast.show();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab); //sets the fab button up to change theaters
        assert fab != null;
        fab.setOnClickListener(new View.OnClickListener() { //set button up to listen for a click
            @Override
        public void onClick(View view) { //when clicked...

            new AlertDialog.Builder(view.getContext()) //make an alert dialog with the following settings
                    .setTitle("Change Theater")
                    .setItems(theaterList, new DialogInterface.OnClickListener() { //build a list within the alert dialog
                        public void onClick(DialogInterface dialog, int which) { //get which one is clicked, and load the url and show the toast
                            if (which == 0)
                                theater = "http://www.fandango.com/century20downtownredwoodcityandxd_aaubf/theaterpage";
                            if (which == 1)
                                theater = "http://www.fandango.com/centurycinemas16_aacfx/theaterpage";
                            if (which == 2)
                                theater = "http://www.fandango.com/amccupertinosquare16_aaujf/theaterpage";

                            theaterToast = theaterList[which] + " Theater loaded \nDate set to : " + date.substring(6);
                            new URLParser().execute(theater + date);
                            Toast toast = Toast.makeText(context, theaterToast, duration);
                            toast.show();
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {}
                    })
                    .show();

        }
        });
    }
    //Async task... dedicated to non ui tasks (e.g. getting html page and parsing it)
    //called with new URLParser().execute(String[] url)
    //

    class URLParser extends AsyncTask < String, Void, ArrayList < Movie >> { //<input a string, dont return anything to update progress, pass an arraylist to the post execute
        protected void onPreExecute() { //when the task is called, hide the current theater Cards, show the loading circle
            findViewById(R.id.loadingPanel).setVisibility(View.VISIBLE);
            findViewById(R.id.my_recycler_view).setVisibility(View.GONE);


        }

        protected ArrayList < Movie > doInBackground(String...strings) {
            MovieList movies = new MovieList(strings[0]); //get the first url, and return the arraylist with its movies
            return movies.getMovies(); //"returning" actually just passes it to the postExecute method
        }

        protected void onPostExecute(ArrayList < Movie > movies) { //show the Cards, add the Cards, hide the circle
            findViewById(R.id.loadingPanel).setVisibility(View.GONE);
            findViewById(R.id.my_recycler_view).setVisibility(View.VISIBLE);
            mAdapter = new MyAdapter(movies);
            mRecyclerView.setAdapter(mAdapter);

        }
    }



    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_scrolling, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) { //when the option menu is pressed,
                                                            // call method showDialog
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            showDialog(999);
        }
        return super.onOptionsItemSelected(item);
    }

    protected Dialog onCreateDialog(int id) { //this method sets up the calander
        if (id == 999) {
            DatePickerDialog dialog = new DatePickerDialog(this, myDateListener, mYear, mMonth, mDay);
            dialog.getDatePicker().setMinDate(calendar.getTimeInMillis()); //start date = today
            //android dates work by declaring the number of milliseconds since January 1, 1970 (UNIX Time)
            dialog.getDatePicker().setMaxDate(calendar.getTimeInMillis() + 432000000); //end date is 5 days from now
            dialog.setTitle("");
            return dialog;
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener myDateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int year, int month, int day) { //this is what happends when a user picks a date

            mYear = year;
            mMonth = month + 1;
            mDay = day;

            date = "?date=" + mMonth + "/" + mDay + "/" + mYear;
            theater += date;
            theaterToast = "Date set to : " + date.substring(6); //show a toast that says what day you chose
            Toast toast = Toast.makeText(getApplicationContext(), theaterToast, duration);
            toast.show();
            new URLParser().execute(theater); //reload the Cards

        }
    };
}