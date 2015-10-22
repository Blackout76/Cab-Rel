package Taxi;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import org.json.simple.JSONObject;

import General.Logger;
import General.Logger.Logger_type;
import General.Main;
import General.CPoint;
import General.Utils;
import Map.MapArea;
import Map.MapStreet;
import Map.MapVertice;
import Render.RenderArea;

public class TaxiManager {

	private ArrayList<TaxiRequest> taxiRequests = new ArrayList<>();
	
	public void loadRequests(JSONObject json){
		taxiRequests = new ArrayList<>();
		System.out.println(json);
		ArrayList<JSONObject> requests = (ArrayList<JSONObject>) json.get("cabQueue");
		for(JSONObject request : requests){
			MapArea area = Main.mapManager.getAreaByName(request.get("area").toString());
			if(((JSONObject)request.get("location")).get("locationType").toString().equals("street")){
				MapStreet street = area.getStreetByName( ((JSONObject)request.get("location")).get("name").toString() );
				float progression = Float.parseFloat( ((JSONObject) ((JSONObject)request.get("location")).get("location") ).get("progression").toString());
				MapVertice originPoint = area.getVerticeByName( ((JSONObject) ((JSONObject)request.get("location")).get("location") ).get("from").toString() );
				
				CPoint point = Utils.computePointToProgression(originPoint,street,progression);
				taxiRequests.add(new TaxiRequest(point,area));
			}
			else{
				MapVertice originPoint = area.getVerticeByName( ((JSONObject)request.get("location")).get("location").toString() );
				taxiRequests.add(new TaxiRequest(originPoint.toPoint(),area));
			}
		}
		Main.renderer.area.repaint();
	}
	// {"cabQueue": [{"location": {"locationType": "street", "location": {"to": "h", "from": "a", "name": "ah", "progression": 0.36903424263000484}, "area": "Quartier Sud"}, "area": "Quartier Sud"}, {"location": {"locationType": "street", "location": {"to": "a", "from": "h", "name": "ah", "progression": 0.47620534896850586}, "area": "Quartier Sud"}, "area": "Quartier Sud"}, {"location": {"locationType": "street", "location": {"to": "a", "from": "h", "name": "ah", "progression": 0.2872812509536744}, "area": "Quartier Sud"}, "area": "Quartier Sud"}]}
	// {"location": {"locationType": "vertex", "location": "m", "area": "Quartier Nord"}, "area": "Quartier Nord"}
	public ArrayList<TaxiRequest> getTaxiRequest(){
		return this.taxiRequests;
	}
	
	public void createRequestAtPoint(Point startPoint){
        TaxiRequest taxiRequest = new TaxiRequest(computeBestInterceptStreet(startPoint));
       	Logger.log(Logger_type.SUCCESS, "[tAXI]", "new taxi request created !");
       	//System.out.println(taxiRequest.toJSON());
       	Main.networkManager.sendTaxiRequest(taxiRequest.toJSON());
	}

	public HashMap<String, Object> computeBestInterceptStreet(Point ori){
		HashMap<String, Object> result = new HashMap<>();
		CPoint impactPoint = new CPoint();
		double distance = -1;
		HashMap<String, Object> streetInfos = null;
		MapStreet street = null;
        HashMap<String, MapStreet> streets = Main.mapManager.getAreaByName(Main.renderer.getNameOfActiveArea()).getStreets();
        float areaX = Main.mapManager.getAreaByName(Main.renderer.getNameOfActiveArea()).getWidth();
        float areaY = Main.mapManager.getAreaByName(Main.renderer.getNameOfActiveArea()).getHeight();
        float ori_x = ori.x * areaX/RenderArea.width;
        float ori_y = ori.y * areaY/RenderArea.height;

        for(Entry<String, MapStreet> streetEntry : streets.entrySet()) {
        	
			HashMap<String, Object> infos = Utils.computeInfoOfTriangle(
												streetEntry.getValue().getPath().get(0).toPoint() ,
												streetEntry.getValue().getPath().get(1).toPoint() ,
												new CPoint(ori_x, ori_y));
	    	if(distance == -1){
				streetInfos = (HashMap<String, Object>) infos.clone();
				street = streetEntry.getValue();
				distance = (double) streetInfos.get("height");
			}
			else
				if(distance >= ((double)infos.get("height"))){
					streetInfos = (HashMap<String, Object>) infos.clone();
					street = streetEntry.getValue();
					distance = (double) streetInfos.get("height");
				}
				else if(((HashMap<String, Double>)infos.get("pointIntercept")).get("distanceToPoint") != null && distance >= ((HashMap<String, Double>)infos.get("pointIntercept")).get("distanceToPoint")){
					streetInfos = (HashMap<String, Object>) infos.clone();
					street = streetEntry.getValue();
					distance = (double) ((HashMap<String, Double>)infos.get("pointIntercept")).get("distanceToPoint");
				}
	    }
        streetInfos.put("streetName", street.getName());
        if(((HashMap<String, Double>)streetInfos.get("pointIntercept")).get("distanceToPoint") != null)
        	streetInfos.put("pourcentHeight", 0);
		return streetInfos;
	}
}
