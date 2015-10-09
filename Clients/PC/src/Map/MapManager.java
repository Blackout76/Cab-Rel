package Map;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.simple.JSONObject;

import General.Logger;
import General.Logger.Logger_type;

public class MapManager {
	private HashMap<String,MapArea> areas;

	public MapManager (){
		
	}
	
	@SuppressWarnings("unchecked")
	public void loadMap(JSONObject mapJson){
		this.areas = new HashMap<String, MapArea>();
		for(JSONObject area: (ArrayList<JSONObject>)mapJson.get("areas"))
			this.areas.put((String)area.get("name"),new MapArea(area));
		Logger.log(Logger_type.SUCCESS, "MAP", "map generated !");
	}
	

	public MapArea getAreaByName(String name){
		return this.areas.get(name);
	}
}
