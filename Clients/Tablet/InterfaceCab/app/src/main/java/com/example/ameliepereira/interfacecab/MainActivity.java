package com.example.ameliepereira.interfacecab;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.JsonReader;

import org.json.JSONException;
import org.json.JSONObject;

import Map.MapManager;

public class MainActivity extends ActionBarActivity {
    private MapManager map;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        map=new MapManager();

        String JsonS="{ \"areas\": [ { \"name\": \"Quartier Nord\", \"map\": { \"weight\": {\"w\": 1, \"h\": 1}, \"vertices\": [ {\"name\": \"m\", \"x\": 0.5, \"y\": 0.5}, {\"name\": \"b\", \"x\": 0.5, \"y\": 1} ], \"streets\": [ {\"name\": \"mb\", \"path\": [\"m\", \"b\"], \"oneway\": false} ], \"bridges\": [ { \"from\": \"b\", \"to\": { \"area\": \"Quartier Sud\", \"vertex\": \"h\"}, \"weight\": 2 } ] } }, { \"name\": \"Quartier Sud\", \"map\": { \"weight\": {\"w\": 1, \"h\": 1}, \"vertices\": [ {\"name\": \"a\", \"x\": 1, \"y\": 1}, {\"name\": \"m\", \"x\": 0, \"y\": 1}, {\"name\": \"h\", \"x\": 0.5, \"y\": 0} ], \"streets\": [ {\"name\": \"ah\", \"path\": [\"a\", \"h\"], \"oneway\": false}, {\"name\": \"mh\", \"path\": [\"m\", \"h\"], \"oneway\": false} ], \"bridges\": [ { \"from\": \"h\", \"to\": { \"area\": \"Quartier Nord\", \"vertex\": \"b\"}, \"weight\": 2 } ] } } ] }";
        try {
            JSONObject jo=new JSONObject(JsonS);
            map.loadMap(jo);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
