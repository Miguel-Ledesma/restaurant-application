package main.restaurant_app;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.TextView;
import android.os.Bundle;


public class restaurantGUI extends AppCompatActivity {
    private CursorAdapter adapter;
    private restaurantDBHelper dbHelper;
    private SQLiteDatabase db;
    private TextView mDisplay;
    private restaurantPasswordHelper mHelper = new restaurantPasswordHelper();

    private final int[] buttonIDs = {
            R.id.zero_button, R.id.one_button, R.id.two_button,
            R.id.three_button, R.id.four_button, R.id.five_button,
            R.id.six_button, R.id.seven_button, R.id.eight_button,
            R.id.nine_button, R.id.enter_button, R.id.backspace_button
    };

    @Override
    protected void onStart() {
        super.onStart();
        dbHelper = new restaurantDBHelper(this);
        db = dbHelper.getWritableDatabase();
        new GetRestaTasks().execute((Void[]) null);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDisplay = findViewById(R.id.display);
        // CREATE DIALOG WHEN ID IS INCORRECT
        final AlertDialog wrongID = new AlertDialog.Builder(this)
                .setTitle("Wrong ID")
                .setMessage("This ID does not exist!").create();
        wrongID.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                new CountDownTimer(2000, 1000) {

                    @Override
                    public void onTick(long millisUntilFinished){}

                    @Override
                    public void onFinish() {
                        if(wrongID.isShowing()) {
                            wrongID.dismiss();
                        }
                    }
                }.start();
            }
        });

        for (int id: buttonIDs) {
            if (id == R.id.enter_button) {
                findViewById(id).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (mDisplay.getText().equals("")) {
                            wrongID.show();
                            return;
                        }
                        String query = "SELECT * FROM " +
                                restaurantContract.Employee.TABLE_NAME + " WHERE _id = '" +
                                mDisplay.getText() + "'";
                        Cursor cursor = db.rawQuery(query, null);
                        if (cursor.getCount() > 0) {
                            //Log.i("IM HERE!", "HERE OVER HERE" + cursor.getCount());
                            cursor.moveToFirst();  //CURSOR POINTS TO ENTRIES, NOT COLUMNS
                            //CHECK FOR ADMIN
                            if (cursor.getInt(4) == 1) {
                                Intent adminAct = new Intent(restaurantGUI.this,
                                        AdminAct.class);
                                startActivity(adminAct);
                            }
                            //CHECK FOR MANAGER
                            else if (cursor.getInt(3) == 1) {
                                Intent manageAct = new Intent(restaurantGUI.this,
                                        ManagAct.class);
                                startActivity(manageAct);
                            }
                            //CHECK FOR FOH OR BOH
                            else {
                                if (cursor.getString(2) == "FOH") {
                                    Intent fohAct = new Intent(restaurantGUI.this,
                                            FOHAct.class);
                                    startActivity(fohAct);
                                }
                                else if (cursor.getString(2) == "BOH") {
                                    Intent bohAct = new Intent(restaurantGUI.this,
                                            BOHAct.class);
                                    startActivity(bohAct);
                                }
                            }

                        }
                        else {
                            //Log.i("TEXT Length", "" + mDisplay.getText().length());
                            int lengthDisplay = mDisplay.getText().length();
                            for(int i = 0; i < lengthDisplay; i++) {
                                mDisplay.setText(mHelper.addKey("backspace"));
                            }
                            wrongID.show();
                        }
                    }
                });
            }
            else {
                findViewById(id).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mDisplay.setText(mHelper.addKey(((Button) view).getText().toString()));
                    }
                });
            }
        }
    }
    private class GetRestaTasks extends AsyncTask<Void, Void, Cursor> {
        @Override
        protected Cursor doInBackground(Void... params) {
            String[] columns = {restaurantContract.Employee.COLUMN_NAME_ID};
            SQLiteDatabase db = dbHelper.getReadableDatabase();

            return db.query(restaurantContract.Employee.TABLE_NAME,
                    columns,
                    null,
                    null,
                    null,
                    null,
                    null);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.restaurantcollection_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.addSamples:
                addSamples();
                new GetRestaTasks().execute((Void[]) null);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onStop() {
        adapter.changeCursor(null);
        db.close();
        super.onStop();
    }

    private void addSamples() {
        new AddSampleUserTask().execute((Void[])null);
    }

    private class AddSampleUserTask extends AsyncTask<Void, Object, Object> {
        @Override
        protected Object doInBackground(Void... objects) {
            ContentValues values = new ContentValues();
            values.put(restaurantContract.Employee.COLUMN_NAME_ID, "0000");
            values.put(restaurantContract.Employee.COLUMN_NAME_PERSON, "Miguel Ledesma");
            values.put(restaurantContract.Employee.COLUMN_NAME_FOHBOH, "BOH");
            values.put(restaurantContract.Employee.COLUMN_NAME_MANAGER, 1);
            values.put(restaurantContract.Employee.COLUMN_NAME_ADMIN, 1);
            values.put(restaurantContract.Employee.COLUMN_NAME_PAY, 7.25);
            db.insert(restaurantContract.Employee.TABLE_NAME, null, values);
            return null;
        }
    }
}
