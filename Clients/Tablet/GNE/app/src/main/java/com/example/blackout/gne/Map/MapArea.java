package com.example.blackout.gne.Map;

//import java.awt.Point;
import android.graphics.Point;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.logging.Logger;

//import org.json;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class MapArea {
	private String name;
	private float width;
	private float height;
	private HashMap<String,MapVertice> vertices;
	private HashMap<String,MapStreet> streets;
	private ArrayList<MapBridge> bridges;
	
	@SuppressWarnings("unchecked")
	public MapArea (JSONObject areaJson){
		try {
			this.name = (String) areaJson.get("name");
			this.width = Float.parseFloat(((JSONObject) ((JSONObject)areaJson.get("map")).get("weight")).get("w").toString());
			this.height = Float.parseFloat(((JSONObject) ((JSONObject)areaJson.get("map")).get("weight")).get("h").toString());
			generateVertices(((JSONObject) areaJson.get("map")).getJSONArray("vertices"));
			generateStreets(((JSONObject) areaJson.get("map")).getJSONArray("streets"));
			generateBridge(((JSONObject) areaJson.get("map")).getJSONArray("bridges"));
		} catch (JSONException e) {
			e.printStackTrace();
		}



		Log.i("MAP", "New area (" + this.name + ") generated with " + this.vertices.size() + " vertices, " + this.streets.size() + "streets and " + this.bridges.size() + " bridges !");
	}

	private void generateVertices(JSONArray verticesList) {
		this.vertices = new HashMap<>();
		for(int i=0; i<verticesList.length(); i++){
			float x = 0;
			try {
				JSONObject vertice = verticesList.getJSONObject(i);
				x = Float.parseFloat(vertice.get("x").toString());
				float y = Float.parseFloat(vertice.get("y").toString());
				this.vertices.put((String)vertice.get("name"),new MapVertice((String)vertice.get("name"),x,y));
			} catch (JSONException e) {
				e.printStackTrace();
			}

		}
	}

	@SuppressWarnings("unchecked")
	private void generateStreets(JSONArray streetsList) {
		this.streets = new HashMap<>();
		for(int i=0; i<streetsList.length(); i++){

				ArrayList<MapVertice> path = new ArrayList<>();
				try {
					JSONObject street = streetsList.getJSONObject(i);
					JSONArray pathList = street.getJSONArray("path");
					for(int j=0; j<pathList.length(); j++)
						path.add(getVerticeByName(pathList.getString(j)));
					this.streets.put((String)street.get("name"),new MapStreet((String)street.get("name"),path,(boolean)street.get("oneway")));
				} catch (JSONException e) {
					e.printStackTrace();
				}


		}
	}
	
	private MapVertice getVerticeByName(String name){
		return this.vertices.get(name);
	}

	private void generateBridge(JSONArray bridgesList) {
		this.bridges = new ArrayList<>();
		for(int i=0; i<bridgesList.length(); i++){
			String startVertice = null;
			try {
				JSONObject bridge = bridgesList.getJSONObject(i);
				startVertice = bridge.get("from").toString();
				String endArea = ( (JSONObject) bridge.get("to") ).get("area").toString() ;
				String endVertice = ( (JSONObject) bridge.get("to") ).get("vertex").toString() ;
				float weight = Float.parseFloat(bridge.get("weight").toString());
				this.bridges.add(new MapBridge(startVertice, endArea, endVertice, weight));
			} catch (JSONException e) {
				e.printStackTrace();
			}


		}
	}

	public HashMap<String, MapStreet> getStreets() {
		return this.streets;
	}
	public HashMap<String, MapVertice> getVertices() {
		return this.vertices;
	}
	public ArrayList<MapBridge> getBridges() {
		return this.bridges;
	}
	public float getHeight(){
		return this.height;
	}
	public float getWidth(){
		return this.width;
	}
	public String getName(){
		return this.name;
	}
	public HashMap<String, Object> computeBestWayInterceptStreet(Point ori){
		HashMap<String, Object> map = new HashMap<>();
		Point impactPoint = new Point();
		float distance = -1;

	    for(Entry<String, MapStreet> street : streets.entrySet()) {
			
		}
	    
		map.put("distance", impactPoint);
		map.put("impactPoint", impactPoint);
		return null;
	}
	
}
