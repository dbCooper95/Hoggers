package com.example.bhutanidhruv16.explist;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.bhutanidhruv16.explist.Adapters.NavDrawerAdapter;
import com.example.bhutanidhruv16.explist.Fragments.FragmentAddOrder;
import com.example.bhutanidhruv16.explist.Fragments.FragmentDailySale;
import com.example.bhutanidhruv16.explist.Fragments.FragmentLoginDetails;
import com.example.bhutanidhruv16.explist.Fragments.FragmentMenuMaker;
import com.example.bhutanidhruv16.explist.Fragments.FragmentProbableOrders;
import com.example.bhutanidhruv16.explist.db.CurrentOrder;
import com.example.bhutanidhruv16.explist.db.Login;
import com.example.bhutanidhruv16.explist.db.Order;
import com.example.bhutanidhruv16.explist.db.OrderSugar;

import java.util.ArrayList;

public class MainActivity extends ActionBarActivity {

    //First We Declare Titles And Icons For Our Navigation Drawer List View
    //This Icons And Titles Are holded in an Array as you can see

    String TITLES[] = {"Current Orders", "Probable Orders", "Edit Menu", "Daily Sale", "Login Details", "Logout"};
    int ICONS[] = {R.drawable.orderlist, R.drawable.usermenufilled, R.drawable.editmenu, R.drawable.sale, R.drawable.logindetails, R.drawable.signout};

    //Similarly we Create a String Resource for the name and email in the navdrawer_header view
    //And we also create a int resource for profile picture in the navdrawer_header view

    String NAME = "Dhruv Bhutani";
    String EMAIL = "ADMIN";
    int PROFILE = R.drawable.home;

    private Toolbar toolbar;                              // Declaring the Toolbar Object

    RecyclerView mRecyclerView;                           // Declaring RecyclerView
    RecyclerView.Adapter mAdapter;                        // Declaring Adapter For Recycler View
    RecyclerView.LayoutManager mLayoutManager;            // Declaring Layout Manager as a linear layout manager
    DrawerLayout Drawer;                                  // Declaring DrawerLayout

    ActionBarDrawerToggle mDrawerToggle;                  // Declaring Action Bar Drawer Toggle

    RelativeLayout main_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    /* Assinging the toolbar object ot the view
    and setting the the Action bar to our toolbar
     */

        SharedPreferences pref = getApplication().getSharedPreferences("MyPref", 0);
        SharedPreferences.Editor editor = pref.edit();

        toolbar = (Toolbar) findViewById(R.id.tool_bar_main);
        setSupportActionBar(toolbar);

        main_layout = (RelativeLayout) findViewById(R.id.main_rellay);
        mRecyclerView = (RecyclerView) findViewById(R.id.RecyclerView); // Assigning the RecyclerView Object to the xml View

        mRecyclerView.setHasFixedSize(true);                            // Letting the system know that the list objects are of fixed size

        ArrayList<Login> loginrecords = (ArrayList<Login>) Login.listAll(Login.class);
        for (int i = 0; i < loginrecords.size(); i++) {
            if (loginrecords.get(i).loginid == pref.getLong("loggedin", 0)) {
                EMAIL = loginrecords.get(i).username;
            }
        }

        mAdapter = new NavDrawerAdapter(TITLES, ICONS, NAME, EMAIL, PROFILE);       // Creating the Adapter of MyAdapter class(which we are going to see in a bit)
        // And passing the titles,icons,navdrawer_header view name, navdrawer_header view email,
        // and navdrawer_header view profile picture

        mRecyclerView.setAdapter(mAdapter);                              // Setting the adapter to RecyclerView

        mLayoutManager = new LinearLayoutManager(this);                 // Creating a layout Manager

        mRecyclerView.setLayoutManager(mLayoutManager);                 // Setting the layout Manager


        Drawer = (DrawerLayout) findViewById(R.id.DrawerLayout);        // Drawer object Assigned to the view
        mDrawerToggle = new ActionBarDrawerToggle(this, Drawer, toolbar, R.string.openDrawer, R.string.closeDrawer) {

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                // code here will execute once the drawer is opened( As I dont want anything happened whe drawer is
                // open I am not going to put anything here)
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                // Code here will execute once drawer is closed
            }


        }; // Drawer Toggle Object Made
        Drawer.setDrawerListener(mDrawerToggle); // Drawer Listener set to the Drawer toggle
        mDrawerToggle.syncState();               // Finally we set the drawer toggle sync State


        final GestureDetector mGestureDetector = new GestureDetector(MainActivity.this, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }
        });

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_rellay, new FragmentAddOrder()).commit();

        mRecyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(RecyclerView recyclerView, MotionEvent motionEvent) {
                View child = recyclerView.findChildViewUnder(motionEvent.getX(), motionEvent.getY());
                if (child != null && mGestureDetector.onTouchEvent(motionEvent)) {
                    Drawer.closeDrawers();
//                    Toast.makeText(MainActivity.this, "The Item Clicked is: " + recyclerView.getChildPosition(child), Toast.LENGTH_SHORT).show();
                    switch (recyclerView.getChildPosition(child)) {
                        case 1:
                            FragmentAddOrder addOrder = (FragmentAddOrder) (getSupportFragmentManager().findFragmentByTag("order"));
                            if (addOrder == null)
                                getSupportFragmentManager().beginTransaction()
                                        .replace(R.id.main_rellay, new FragmentAddOrder()).commit();
                            else {
                                getSupportFragmentManager().beginTransaction().remove(addOrder);
                                getSupportFragmentManager().beginTransaction().add(R.id.main_rellay, new FragmentAddOrder(), "order");
                            }
                            break;
                        case 2:
//                            getSupportFragmentManager()
//                                    .beginTransaction()
//                                    .replace(R.id.main_rellay, new FragmentProbableOrders()).commit();
                            // work in progress
                            break;
                        case 3:
                            if (EMAIL.equals("admin")) {
                                getSupportFragmentManager()
                                        .beginTransaction()
                                        .replace(R.id.main_rellay, new FragmentMenuMaker()).commit();
                            } else {
                                Toast.makeText(MainActivity.this, "Donot have access", Toast.LENGTH_SHORT).show();
                            }
                            break;
                        case 4:
                            if (EMAIL.equals("admin")) {
                                getSupportFragmentManager()
                                        .beginTransaction()
                                        .replace(R.id.main_rellay, new FragmentDailySale()).commit();
                            } else {
                                Toast.makeText(MainActivity.this, "Donot have access", Toast.LENGTH_SHORT).show();
                            }
                            break;
                        case 5:
                            if (EMAIL.equals("admin")) {
                                getSupportFragmentManager()
                                        .beginTransaction()
                                        .replace(R.id.main_rellay, new FragmentLoginDetails()).commit();
                            } else {
                                Toast.makeText(MainActivity.this, "Donot have access", Toast.LENGTH_SHORT).show();
                            }
                            break;
                        case 6:
                            SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0);
                            final SharedPreferences.Editor editor = pref.edit();
                            editor.putLong("loggedin", 0);
                            editor.commit();
                            Intent in = new Intent(MainActivity.this, LoginPage.class);
                            startActivity(in);
                            break;
                    }
                    return true;
                }
                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView recyclerView, MotionEvent motionEvent) {

            }
        });
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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_restart) {
            SharedPreferences pref = getApplication().getSharedPreferences("MyPref", 0);
            SharedPreferences.Editor editor = pref.edit();

            ArrayList<OrderSugar> orders = (ArrayList<OrderSugar>) OrderSugar.listAll(OrderSugar.class);
            if (orders.size() > 0) {
                Toast.makeText(getApplicationContext(), "Complete pending orders", Toast.LENGTH_SHORT).show();
            } else {
                editor.putLong("orderid", 1).apply();
                Toast.makeText(getApplicationContext(), "Resetting Id", Toast.LENGTH_SHORT).show();

            }
            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        ActivityCompat.finishAffinity(this);
    }

}
