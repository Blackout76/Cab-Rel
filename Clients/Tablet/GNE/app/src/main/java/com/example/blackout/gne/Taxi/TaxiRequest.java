package com.example.blackout.gne.Taxi;

import com.example.blackout.gne.General.CPoint;
import com.example.blackout.gne.MainActivity;
import com.example.blackout.gne.Map.MapArea;
import com.example.blackout.gne.Map.MapStreet;
import com.example.blackout.gne.Map.MapVertice;
import com.example.blackout.gne.render.RenderView;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

public class TaxiRequest {
	private CPoint intersectedPoint;
	private double pourcentIntersect;
	private MapVertice originVertice;
	private MapStreet street;
	private MapArea area;

	public TaxiRequest(CPoint position, MapArea area) {
		this.intersectedPoint = new CPoint(position);
		this.area = area;
	}

	public TaxiRequest(HashMap<String, Object> infos) {
		this.area = MainActivity.mapManager.getAreaByName(MainActivity.ihm.getNameOfActiveArea());
		this.street = this.area.getStreetByName((String) infos.get("streetName"));
		this.originVertice = this.street.getPath().get((int) (((HashMap<String, Object>) infos.get("pointIntercept")).get("indexVertice")));
		this.pourcentIntersect = Float.parseFloat(infos.get("pourcentHeight").toString());
		this.intersectedPoint = new CPoint(Float.parseFloat(((HashMap<String, Object>) infos.get("pointIntercept")).get("x").toString()),
				Float.parseFloat(((HashMap<String, Object>) infos.get("pointIntercept")).get("y").toString()));
	}

	public JSONObject toJSON() {
		JSONObject json = new JSONObject();
		JSONObject locationMap = new JSONObject();
		JSONObject locationIntercept = new JSONObject();
		JSONObject infos = new JSONObject();

		if (this.pourcentIntersect == 0) {

			try {
				locationMap.put("locationType", "street");
				locationMap.put("area", this.area.getName());
				locationMap.put("location", this.originVertice.getName());
			} catch (JSONException e) {
				e.printStackTrace();
			}

		} else {
			try {
				locationIntercept.put("from", this.originVertice.getName());
				locationIntercept.put("to", this.street.getOposedVerticeFromVertice(this.originVertice.getName()));
				locationIntercept.put("progression", this.pourcentIntersect);
				locationIntercept.put("name", this.street.getName());

				locationMap.put("locationType", "street");
				locationMap.put("area", this.area.getName());
				locationMap.put("location", locationIntercept);
			} catch (JSONException e) {
				e.printStackTrace();
			}

		}
		try {
			infos.put("area", this.area.getName());
			infos.put("location", locationMap);
			json.put("cabRequest", infos);
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return json;
	}

	public CPoint getPosition() {
		return this.intersectedPoint;
	}

	public MapArea getArea() {
		return this.area;
	}
}
