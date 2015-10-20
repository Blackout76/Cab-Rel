package Taxi;

import java.util.HashMap;

import org.json.simple.JSONObject;

import General.CPoint;
import General.Main;
import Map.MapArea;
import Map.MapStreet;
import Map.MapVertice;
import Render.IHM;

public class TaxiRequest {
	private CPoint intersectedPoint;
	private double pourcentIntersect;
	private MapVertice originVertice;
	private MapStreet street;
	private MapArea area;
	
	public TaxiRequest (HashMap<String, Object> infos){
		this.area = Main.mapManager.getAreaByName(IHM.getNameOfActiveArea());
		this.street = this.area.getStreetByName((String) infos.get("streetName"));
		this.originVertice = this.street.getPath().get( (int)( ((HashMap<String, Object>)infos.get("pointIntercept")).get("indexVertice") ) );
		this.pourcentIntersect = (double)infos.get("pourcentHeight");
		this.intersectedPoint = new CPoint( Float.parseFloat( ((HashMap<String, Object>)infos.get("pointIntercept")).get("x").toString() ),  
				 							Float.parseFloat(((HashMap<String, Object>)infos.get("pointIntercept")).get("y").toString()	));
	}
	
	public JSONObject toJSON(){
		JSONObject json = new JSONObject();
		JSONObject locationMap = new JSONObject();
		JSONObject locationIntercept = new JSONObject();
		JSONObject infos = new JSONObject();
		
		if(this.pourcentIntersect == 0){

			locationMap.put("locationType", "street");
			locationMap.put("area", this.area.getName());
			locationMap.put("location", this.originVertice.getName());
		}
		else{
			locationIntercept.put("from", this.originVertice.getName());
			locationIntercept.put("to", this.street.getOposedVerticeFromVertice(this.originVertice.getName()));
			locationIntercept.put("progression", this.pourcentIntersect);
			locationIntercept.put("name", this.street.getName());
			
			locationMap.put("locationType", "street");
			locationMap.put("area", this.area.getName());
			locationMap.put("location", locationIntercept);
		}
		infos.put("area", this.area.getName());
		infos.put("location", locationMap);
		json.put("cabRequest", infos);
		return json;
	}
}
