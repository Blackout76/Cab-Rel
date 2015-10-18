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
		JSONObject infos = new JSONObject();
		infos.put("origin", this.intersectedPoint.toJSON());
		infos.put("area", this.area.getName());
		infos.put("street", this.street.getName());
		infos.put("pourcent", this.pourcentIntersect);
		infos.put("vertice", this.originVertice.getName());
		json.put("cabRequest", infos);
		return json;
		
	}
}
