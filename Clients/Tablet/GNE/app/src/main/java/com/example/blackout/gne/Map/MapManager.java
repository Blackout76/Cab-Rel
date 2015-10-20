package com.example.blackout.gne.Map;

import android.util.Log;

import com.example.blackout.gne.MainActivity;
import com.example.blackout.gne.Network.AsyncWebSocket;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;


public class MapManager extends Observable implements AsyncWebSocket {
	private HashMap<Object, MapArea> areas;

	@SuppressWarnings("unchecked")
	public void loadMap(JSONObject mapJson) {
		this.areas = new HashMap<>();
		try {
			//JSONArray li=  (JSONArray)mapJson.get("areas");
			JSONArray li = mapJson.getJSONArray("areas");
			for (int i = 0; i < li.length(); i++)
				this.areas.put(li.getJSONObject(i).get("name"), new MapArea(li.getJSONObject(i)));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		Log.i("MAP", "map generated !");
	}


	public MapArea getAreaByName(String name) {
		return this.areas.get(name);
	}


	@Override
	public void onWebSocketMessage(JSONObject json) {
		try {
			if (json.getJSONArray("areas") != null) {
				this.loadMap(json);
				JSONObject msgJSON = null;
				//MainActivity.ihm.loadRender("Quartier Nord");
				MainActivity.ihm.loadRender("Quartier Sud");
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
}