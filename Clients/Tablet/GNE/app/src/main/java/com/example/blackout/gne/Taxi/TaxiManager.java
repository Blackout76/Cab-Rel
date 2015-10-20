package com.example.blackout.gne.Taxi;

import android.graphics.Point;
import android.util.Log;

import com.example.blackout.gne.General.CPoint;
import com.example.blackout.gne.General.Utils;
import com.example.blackout.gne.MainActivity;
import com.example.blackout.gne.Map.MapStreet;
import com.example.blackout.gne.render.RenderArea;
import com.example.blackout.gne.render.RenderView;

import java.util.HashMap;
import java.util.Map.Entry;


public class TaxiManager {

	public void createRequestAtPoint(CPoint startPoint){
        TaxiRequest taxiRequest = new TaxiRequest(computeBestInterceptStreet(startPoint));
      Log.i("[tAXI]", "new taxi request created !");
       	//System.out.println(taxiRequest.toJSON());
       	MainActivity.webSocket.sendTaxiRequest(taxiRequest);
	}

	public HashMap<String, Object> computeBestInterceptStreet(CPoint ori){
		HashMap<String, Object> result = new HashMap<>();
		CPoint impactPoint = new CPoint();
		double distance = -1;
		HashMap<String, Object> streetInfos = null;
		MapStreet street = null;
        HashMap<String, MapStreet> streets = MainActivity.mapManager.getAreaByName(MainActivity.ihm.getNameOfActiveArea()).getStreets();
        float areaX = MainActivity.mapManager.getAreaByName(MainActivity.ihm.getNameOfActiveArea()).getWidth();
        float areaY = MainActivity.mapManager.getAreaByName(MainActivity.ihm.getNameOfActiveArea()).getHeight();
        float ori_x = ori.x * areaX/ RenderView.width;
        float ori_y = ori.y * areaY/RenderView.height;

        for(Entry<String, MapStreet> streetEntry : streets.entrySet()) {
        	
			HashMap<String, Object> infos = Utils.computeInfoOfTriangle(
					streetEntry.getValue().getPath().get(0).toPoint(),
					streetEntry.getValue().getPath().get(1).toPoint(),
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
