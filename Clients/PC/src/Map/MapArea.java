package Map;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.simple.JSONObject;

import General.Cab;
import General.Logger;
import General.Logger.Logger_type;

public class MapArea {
	private String name;
	private float width;
	private float height;
	private HashMap<String,MapVertice> vertices;
	private HashMap<String,MapStreet> streets;
	private ArrayList<Mapbridge> bridges;
	
	@SuppressWarnings("unchecked")
	public MapArea (JSONObject areaJson){
		this.name = (String) areaJson.get("name");
		this.width = Float.parseFloat(((JSONObject) ((JSONObject)areaJson.get("map")).get("weight")).get("w").toString());
		this.height = Float.parseFloat(((JSONObject) ((JSONObject)areaJson.get("map")).get("weight")).get("h").toString());
		generateVertices((ArrayList<JSONObject>) ((JSONObject)areaJson.get("map")).get("vertices"));
		generateStreets((ArrayList<JSONObject>)((JSONObject)areaJson.get("map")).get("streets"));
		generateBridge((ArrayList<JSONObject>)((JSONObject)areaJson.get("map")).get("bridges"));
		Logger.log(Logger_type.SUCCESS, "MAP", "New area (" + this.name + ") generated with "+ this.vertices.size() +" vertices, " +this.streets.size() + "streets and "+ this.bridges.size() +" bridges !");
	}

	private void generateVertices(ArrayList<JSONObject> verticesList) {
		this.vertices = new HashMap<>();
		for(JSONObject vertice: verticesList){
			float x = Float.parseFloat(vertice.get("x").toString());
			float y = Float.parseFloat(vertice.get("y").toString());
			this.vertices.put((String)vertice.get("name"),new MapVertice((String)vertice.get("name"),x,y));
		}
	}

	@SuppressWarnings("unchecked")
	private void generateStreets(ArrayList<JSONObject> streetsList) {
		this.streets = new HashMap<>();
		for(JSONObject street: streetsList){
			ArrayList<MapVertice> path = new ArrayList<>();
			for(String p: ((ArrayList<String>)street.get("path")))
				path.add(getVerticeByName(p));
			this.streets.put((String)street.get("name"),new MapStreet((String)street.get("name"),path,(boolean)street.get("oneway")));
		}
	}
	
	private MapVertice getVerticeByName(String name){
		return this.vertices.get(name);
	}

	private void generateBridge(ArrayList<JSONObject> bridgesList) {
		this.bridges = new ArrayList<>();
		for(JSONObject bridge: bridgesList){
			String startVertice = bridge.get("from").toString() ;
			String endArea = ( (JSONObject) bridge.get("to") ).get("area").toString() ;
			String endVertice = ( (JSONObject) bridge.get("to") ).get("vertex").toString() ;
			float weight = Float.parseFloat(bridge.get("weight").toString());
			this.bridges.add(new Mapbridge(startVertice, endArea, endVertice, weight));
		}
	}
}
