package Map;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;

import org.json.simple.JSONObject;

import General.Main;
import General.Logger;
import General.Logger.Logger_type;

public class MapManager extends Observable {
	private HashMap<String,MapArea> areas;
	
	@SuppressWarnings("unchecked")
	public void loadMap(JSONObject mapJson){
		this.areas = new HashMap<String, MapArea>();
		String areaToLoad = null;
		for(JSONObject area: (ArrayList<JSONObject>)mapJson.get("areas")){
			this.areas.put((String)area.get("name"),new MapArea(area));
			areaToLoad = (String)area.get("name");
		}
		Logger.log(Logger_type.SUCCESS, "MAP", "map generated !");
		
		/* Draw last area loaded */
		if(areaToLoad != null)
			Main.renderer.generateArea(areas.get(areaToLoad));
	}
	

	public MapArea getAreaByName(String name){
		return this.areas.get(name);
	}

}
