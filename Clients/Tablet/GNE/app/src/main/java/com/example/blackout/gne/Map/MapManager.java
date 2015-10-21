package com.example.blackout.gne.Map;

import android.util.Log;

import com.example.blackout.gne.MainActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Observable;


public class MapManager extends Observable {
	private HashMap<Object, MapArea> areas;
	public static String areaToLoad = "";

	@SuppressWarnings("unchecked")
	public void loadMap(JSONObject mapJson) {
		this.areas = new HashMap<>();
		try {
			//JSONArray li=  (JSONArray)mapJson.get("areas");
			JSONArray li = mapJson.getJSONArray("areas");

			for (int i = 0; i < li.length(); i++) {
				this.areas.put(li.getJSONObject(i).get("name"), new MapArea(li.getJSONObject(i)));
				areaToLoad=li.getJSONObject(i).getString("name");
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		Log.i("MAP", "map generated !");
		MainActivity.ihm.loadRender(areaToLoad);

	}


	public MapArea getAreaByName(String name) {
		return this.areas.get(name);
	}

	public void loadNextArea(){
		String first = "";
		String nameNext = "";
		boolean found = false;
		for(Object areaName : areas.keySet()){
			if(found){
				nameNext = areaName.toString();
				break;
			}
			if(!found){
				if(first.equals(""))
					first = areaName.toString();
				if(areaName.equals(MainActivity.ihm.getNameOfActiveArea()))
					found = true;
			}

		}

		if(nameNext.equals(""))
			nameNext = first;
		if(nameNext != null && !nameNext.equals("")){
			MainActivity.ihm.loadRender(nameNext);

			MainActivity.ihm.postInvalidate();
		}
	}
}