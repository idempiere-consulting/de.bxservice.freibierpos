package de.bxservice.bxpos.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.Gravity;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import de.bxservice.bxpos.R;
import de.bxservice.bxpos.logic.AssetsPropertyReader;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, GuestNumberDialogFragment.GuestNumberDialogListener {

    public final static String EXTRA_NUMBER_OF_GUESTS = "de.bxservice.bxpos.GUESTS";
    public final static String EXTRA_ASSIGNED_TABLE   = "de.bxservice.bxpos.TABLE";

    private int numberOfGuests = 0;
    private String selectedTable = "";
    private AssetsPropertyReader assetsPropertyReader;
    private Properties properties;

    List<String> list;
    GridView grid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        list=new ArrayList<String>();
        grid=(GridView) findViewById(R.id.tableView);

        list.add("Table 1");
        list.add("Table 2");
        list.add("Table 3");
        list.add("Table 4");
        list.add("Table 5");
        list.add("Table 6");
        list.add("Table 7");
        list.add("Table 8");

        grid.setGravity(Gravity.CENTER_HORIZONTAL);

        ArrayAdapter<String> adp = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line,list);
        grid.setAdapter(adp);

        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {


                /*Toast.makeText(getBaseContext(), list.get(arg2),
                        Toast.LENGTH_SHORT).show();*/
                /*Toast.makeText(getBaseContext(), "Hola",
                        Toast.LENGTH_SHORT).show();*/
                setSelectedTable(list.get(arg2));
                showGuestNumberDialog();
            }
        });

        FloatingActionButton newOrderButton = (FloatingActionButton) findViewById(R.id.new_order_button);
        newOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), CreateOrderActivityOption2.class);
                intent.putExtra(EXTRA_NUMBER_OF_GUESTS, getNumberOfGuests());
                intent.putExtra(EXTRA_ASSIGNED_TABLE, getSelectedTable());

               /* Toast.makeText(getBaseContext(), Integer.toString(getNumberOfGuests())+" "+getSelectedTable(),
                        Toast.LENGTH_SHORT).show();*/


                startActivity(intent);
                //createOrder(view);
                /*Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        ProductCategoryWebServiceAdapter a = new ProductCategoryWebServiceAdapter();

                    }
                });
                thread.start();*/
               /* Snackbar.make(view, a.getTemp(), Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/


            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        assetsPropertyReader = new AssetsPropertyReader(this);
        properties = assetsPropertyReader.getProperties("bxpos.properties");

        Toast.makeText(getBaseContext(),properties.getProperty("username"),
                Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.open_orders) {

            Intent intent = new Intent(this, ViewOpenOrdersActivity.class);
            startActivity(intent);


        } else if (id == R.id.nav_reservation) {

            Intent intent = new Intent(this, ManageReservationActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_report) {

            Intent intent = new Intent(this, EditOrderActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_settings) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void showGuestNumberDialog() {
        // Create an instance of the dialog fragment and show it
        GuestNumberDialogFragment guestDialog = new GuestNumberDialogFragment();
        guestDialog.show(getFragmentManager(), "NumberOfGuestDialogFragment");
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public void setNumberOfGuests(int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public String getSelectedTable() {
        return selectedTable;
    }

    public void setSelectedTable(String selectedTable) {
        this.selectedTable = selectedTable;
    }

    // The dialog fragment receives a reference to this Activity through the
    // Fragment.onAttach() callback, which it uses to call the following methods
    @Override
    public void onDialogPositiveClick(GuestNumberDialogFragment dialog) {

        // User touched the dialog's positive button
        int guests = dialog.getNumberOfGuests();
        setNumberOfGuests(guests);
        createOrder(dialog.getView());

    }

    public void createOrder(View view){

        Intent intent = new Intent(this, CreateOrderActivity.class);
        intent.putExtra(EXTRA_NUMBER_OF_GUESTS, getNumberOfGuests());
        intent.putExtra(EXTRA_ASSIGNED_TABLE, getSelectedTable());

        Toast.makeText(getBaseContext(), Integer.toString(getNumberOfGuests())+" "+getSelectedTable(),
                Toast.LENGTH_SHORT).show();

        startActivity(intent);
    }
}