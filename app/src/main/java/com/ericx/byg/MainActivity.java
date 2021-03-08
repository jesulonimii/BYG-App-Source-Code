package com.ericx.byg;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    Toolbar toolbar;
    NavigationView navigationView;
    ActionBarDrawerToggle toggle;
    DrawerLayout drawer;
    Switch darkSwitch;
    int nightMode;
    boolean switchPosition;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    CardView blog, facebook, twitter, instagram;
    WebView feedSite;
    TextView feedText, feedDateText;
    LinearLayout contact;
    String feedError;

    RequestQueue requestQueue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        toolbar = findViewById(R.id.toolbar);
        drawer = findViewById(R.id.drawer);
        navigationView = findViewById(R.id.navView);
        MenuItem menuItem;
        menuItem = navigationView.getMenu().findItem(R.id.switchTheme); // This is the menu item that contains your switch
        darkSwitch = menuItem.getActionView().findViewById(R.id.darkSwitch);
        blog = findViewById(R.id.blogCard);
        feedSite = findViewById(R.id.bygFeed);
        feedText = findViewById(R.id.feedText);
        feedDateText = findViewById(R.id.feedTimeText);
        facebook = findViewById(R.id.facebookCard);
        twitter = findViewById(R.id.twitterCard);
        instagram = findViewById(R.id.instagramCard);
        contact = findViewById(R.id.contactCard);
        feedError = "Could not retrieve feed now \n  -Check your Device connection\n  -It could be an internal error";



        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Beautiful Yet Godly");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);



        toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.open, R.string.close);
        drawer.addDrawerListener(toggle);
        toggle.setDrawerIndicatorEnabled(true);
        toggle.syncState();


        navigationView.setNavigationItemSelectedListener(this);






        //Retrieving saved settings
        sharedPreferences = getSharedPreferences("clite", MODE_PRIVATE);
        nightMode =sharedPreferences.getInt("Theme", 1);
        switchPosition = sharedPreferences.getBoolean("Switch", false);



        //dark Theme
        darkSwitch.setChecked(switchPosition);
        AppCompatDelegate.setDefaultNightMode(nightMode);


        darkSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                checkTheme();
            }
        });



        jsonParse();
        cardsClick();




    }

    private void checkFeed() {
        if (feedText.getText() == feedError){
            jsonParse();
        } else {
            //nothing
        }
    }

    private void cardsClick() {

        blog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Site.class);
                checkFeed();
                startActivity(intent);
            }
        });

        facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try{  Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("fb://page/1167998186686216"));
                    checkFeed();
                    startActivity(intent);}
                catch (Exception e){
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.facebook.com/1167998186686216"));
                    checkFeed();
                    startActivity(intent);
                }

            }
        });

        twitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("twitter://user?screen_name=beau_yet_godly&link_click_id=849749756177782622"));
                    checkFeed();
                    startActivity(intent);
                }
                catch(Exception e){
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/Beau_Yet_Godly"));
                    checkFeed();
                    startActivity(intent);
                }

            }
        });

        instagram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://instagram.com/_u/beautiful_yet_godly"));
                    intent.setPackage("com.instagram.android");
                    checkFeed();
                    startActivity(intent);
                }
                catch (Exception e){
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://instagram.com/_u/beautiful_yet_godly"));
                    checkFeed();
                    startActivity(intent);
                }
            }
        });

        contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SendEmail.class);
                checkFeed();
                startActivity(intent);
            }
        });
        
    }

    

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        nightMode = AppCompatDelegate.getDefaultNightMode();

        sharedPreferences = getSharedPreferences("clite", MODE_PRIVATE);
        editor = sharedPreferences.edit();

        editor.putInt("Theme", nightMode);

        if (darkSwitch.isChecked()){

            editor.putBoolean("Switch", true);
        }
        else {
            editor.putBoolean("Switch", false);
        }

        editor.apply();

    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.exit){
            exitSnackbar();
        }

        if (item.getItemId() == R.id.about){
            Intent intent= new Intent(MainActivity.this, About.class);
            startActivity(intent);
        }

        if (item.getItemId() == R.id.info){
            Intent intent= new Intent(MainActivity.this, Info.class);
            startActivity(intent);
        }


        drawer.closeDrawer(GravityCompat.START);

        return false;
    }

    private void checkTheme(){

        if(darkSwitch.isChecked()){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

    }

    private void jsonParse() {

        String url = "http://www.ericx.tk/byg-feed/api/";

        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("feed");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);


                        String post = object.getString("post");
                        String date = object.getString("date");
                        String time = object.getString("time");


                        feedText.setText(post + "\n");
                        feedDateText.append(date + "\n" + time);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                
                
                feedText.setText(feedError);
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }



    public void exitSnackbar(){
        final Snackbar snackbar = Snackbar.make(findViewById(R.id.drawer), "Exit the app?", Snackbar.LENGTH_SHORT).setAction("Yes", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                System.exit(0);
            }
        }).setActionTextColor(Color.RED);
        snackbar.show();
    }

    @Override
    public void onBackPressed (){

        exitSnackbar();
    }


}
