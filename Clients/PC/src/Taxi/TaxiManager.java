package Taxi;

import java.awt.Point;
import java.util.HashMap;
import java.util.Map.Entry;

import General.Logger;
import General.Logger.Logger_type;
import General.Main;
import General.CPoint;
import General.Utils;
import Map.MapStreet;
import Render.RenderArea;

public class TaxiManager {

	public void createRequestAtPoint(Point startPoint){
        TaxiRequest taxiRequest = new TaxiRequest(computeBestInterceptStreet(startPoint));
       	Logger.log(Logger_type.SUCCESS, "[tAXI]", "new taxi request created !");
       	System.out.println(taxiRequest.toJSON());
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
		return streetInfos;
	}
}
