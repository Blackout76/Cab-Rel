package Map;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;

import org.json.simple.JSONObject;

import General.Main;
import General.Logger;
import General.Logger.Logger_type;

public class MapManager extends Observable implements Observer{
	private HashMap<String,MapArea> areas;
	
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

	@Override
	public void update(Observable o, Object obj) {
	
		if(((HashMap) obj).get("areas") !=  null){
			this.loadMap(new JSONObject((HashMap) obj));
			Main.renderer.generateArea(getAreaByName("Quartier Sud"));
		}
	}
}
