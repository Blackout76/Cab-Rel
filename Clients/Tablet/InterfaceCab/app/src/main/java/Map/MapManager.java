package Map;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;


public class MapManager extends Observable implements Observer{
	private HashMap<Object, MapArea> areas;
	
	@SuppressWarnings("unchecked")
	public void loadMap(JSONObject mapJson){
		this.areas = new HashMap<>();
		try {
			//JSONArray li=  (JSONArray)mapJson.get("areas");

			JSONArray li=mapJson.getJSONArray("areas");
			for(int i=0; i<li.length(); i++)
                this.areas.put(li.getJSONObject(i).get("name"), new MapArea(li.getJSONObject(i)));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		Log.i("MAP", "map generated !");
	}
	

	public MapArea getAreaByName(String name){
		return this.areas.get(name);
	}

	@Override
	public void update(Observable o, Object obj) {
	
		if(((HashMap) obj).get("areas") !=  null){
			this.loadMap(new JSONObject((HashMap) obj));
			//Cab.renderer.generateArea(getAreaByName("Quartier Sud"));
		}
	}
}
