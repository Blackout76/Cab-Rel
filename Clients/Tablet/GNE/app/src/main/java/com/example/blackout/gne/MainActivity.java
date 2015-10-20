package com.example.blackout.gne;

import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.Touch;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.example.blackout.gne.General.CPoint;
import com.example.blackout.gne.Map.MapManager;
import com.example.blackout.gne.Network.AsyncResponse;
import com.example.blackout.gne.Network.HTTP;
import com.example.blackout.gne.Network.WebSocket;
import com.example.blackout.gne.Taxi.TaxiManager;
import com.example.blackout.gne.render.RenderView;

import org.json.JSONException;
import org.json.JSONObject;


public class MainActivity extends AppCompatActivity implements AsyncResponse  {

    public static WebSocket webSocket;
    public static MapManager mapManager;
    public static RenderView ihm;
    public  static TaxiManager taxiManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        // INIT PROGRAM
        init();
    }

    private void init() {
        taxiManager=new TaxiManager();
        mapManager = new MapManager();
        FrameLayout window=new FrameLayout(this);
        ihm=new RenderView(window.getContext());
        window.addView(ihm);
        setContentView(window);


        /*
        String JsonS = "{ \"areas\": [ { \"name\": \"Quartier Nord\", \"map\": { \"weight\": {\"w\": 1, \"h\": 1}, \"vertices\": [ {\"name\": \"m\", \"x\": 0.5, \"y\": 0.5}, {\"name\": \"b\", \"x\": 0.5, \"y\": 1} ], \"streets\": [ {\"name\": \"mb\", \"path\": [\"m\", \"b\"], \"oneway\": false} ], \"bridges\": [ { \"from\": \"b\", \"to\": { \"area\": \"Quartier Sud\", \"vertex\": \"h\"}, \"weight\": 2 } ] } }, { \"name\": \"Quartier Sud\", \"map\": { \"weight\": {\"w\": 1, \"h\": 1}, \"vertices\": [ {\"name\": \"a\", \"x\": 1, \"y\": 1}, {\"name\": \"m\", \"x\": 0, \"y\": 1}, {\"name\": \"h\", \"x\": 0.5, \"y\": 0} ], \"streets\": [ {\"name\": \"ah\", \"path\": [\"a\", \"h\"], \"oneway\": false}, {\"name\": \"mh\", \"path\": [\"m\", \"h\"], \"oneway\": false} ], \"bridges\": [ { \"from\": \"h\", \"to\": { \"area\": \"Quartier Nord\", \"vertex\": \"b\"}, \"weight\": 2 } ] } } ] }";
        try {
            JSONObject jo = new JSONObject(JsonS);
            mapManager.loadMap(jo);
        } catch (JSONException e) {
            e.printStackTrace();
        }
       // ihm.loadRender("Quartier Nord");
        ihm.loadRender("Quartier Sud");
        */
        // TEST HTTP

        HTTP request=new HTTP();
        request.delegate = this;
        request.execute("http://172.30.1.104:8080/clientConnect");

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
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void httpResponse(String output) {
        if(output != null){
            JSONObject msgJSON = null;
            try {msgJSON = new JSONObject(output);} catch (JSONException e) { }
            try {
                webSocket = new WebSocket(msgJSON.get("addr").toString());
                webSocket.delegate = mapManager;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean onTouchEvent(MotionEvent event){
        int action = event.getAction();
        float x=0;
        float y=0;
        MainActivity.ihm.invalidate();
        switch (event.getAction()) {
            case MotionEvent.ACTION_UP:
                x = event.getX();
                y = event.getY();
                Log.e("x",""+x);
                Log.e("y", "" + y);
                if(ihm.getNameOfActiveArea() != null && mapManager.getAreaByName(ihm.getNameOfActiveArea()) != null)
                    taxiManager.createRequestAtPoint(new CPoint(x,y));
        }

        return true;
    }
}
