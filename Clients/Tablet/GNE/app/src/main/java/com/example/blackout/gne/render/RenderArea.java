package com.example.blackout.gne.render;

import android.graphics.Canvas;
import android.graphics.Point;
import android.util.Log;

import com.example.blackout.gne.General.CPoint;
import com.example.blackout.gne.MainActivity;
import com.example.blackout.gne.Map.MapArea;
import com.example.blackout.gne.Map.MapBridge;
import com.example.blackout.gne.Map.MapStreet;
import com.example.blackout.gne.Map.MapVertice;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;


public class RenderArea  {
    private Point startPoint, endPoint;
    private List<Point[]> lines;
    private CPoint position;
    private RenderTaxi renderTaxi;
    private RenderCabRequest renderCabRequest;
    private ArrayList<RenderStreet> renderStreets;
    private ArrayList<RenderVertice> renderVertices;
    public static int scale_x, scale_y;


	public void loadArea(int iWidth, int iHeight, MapArea mapArea) {
		
	    this.scale_x = (int)(iWidth/ mapArea.getWidth());
	    this.scale_y = (int)(iHeight/ mapArea.getHeight());
	    renderStreets(scale_x, scale_y, mapArea.getStreets());
	    renderVertices(scale_x, scale_y, mapArea);


	}
	
	private void renderVertices(int scale_x, int scale_y,MapArea mapArea) {
		this.renderVertices = new ArrayList<>();

		for(Entry<String, MapVertice> entryVertice : mapArea.getVertices().entrySet()) {
			boolean isBridge = false;
			for(MapBridge entryBridge : mapArea.getBridges())
				if(entryBridge.getStartVertice().equals(entryVertice.getValue().getName()))
					isBridge = true;

	        this.renderVertices.add(new RenderVertice(scale_x,scale_y,entryVertice,isBridge));
	    }
	}

	private void renderStreets(int scale_x, int scale_y, HashMap<String, MapStreet> streets){
		this.renderStreets = new ArrayList<>();
	    for(Entry<String, MapStreet> entry : streets.entrySet()) {
	        this.renderStreets.add(new RenderStreet(scale_x,scale_y,entry));
	    }
	}



	public void render(int iWidth, int iHeight, Canvas canvas) {
        if(this.renderStreets != null)
            //print streets
            for(RenderStreet rs: this.renderStreets)
            {
                rs.render(canvas);
            }
        if(this.renderVertices != null)
            //print vertices
            for(RenderVertice rv: this.renderVertices)
            {
                rv.render(iWidth, iHeight, canvas);
            }

		if(this.renderTaxi!=null&& this.renderTaxi.getName().equals(MainActivity.ihm.getNameOfActiveArea()))  {
           
			this.renderTaxi.render(canvas);
		}


	}

	public void updateTaxiRenderPosition(JSONObject json){
        boolean isfree=true;
        if(this.renderTaxi==null)
            this.renderTaxi=new RenderTaxi(isfree);
		//{"cabInfo": {"loc_now": {"locationType": "vertex", "location": "c", "area": "Quartier Nord"},
		MapArea area = null;
		Point position = new Point();

		try {
			if(((JSONObject) ((JSONObject) json.get("cabInfo")).get("loc_now")).get("locationType").toString().equals("vertex")){
                String name = ((JSONObject) ((JSONObject) json.get("cabInfo")).get("loc_now")).get("location").toString();
                area = MainActivity.mapManager.getAreaByName(((JSONObject) ((JSONObject) json.get("cabInfo")).get("loc_now")).get("area").toString());
                position.x  = (int) (area.getVerticeByName(name).getX() * scale_x);
                position.y  = (int) (area.getVerticeByName(name).getY() * scale_y);
            }
            else{
                JSONObject jsonLocation = (JSONObject) ((JSONObject) ((JSONObject) json.get("cabInfo")).get("loc_now")).get("location");
				area = MainActivity.mapManager.getAreaByName(((JSONObject) ((JSONObject) json.get("cabInfo")).get("loc_now")).get("area").toString());

                MapVertice A = area.getVerticeByName(jsonLocation.get("from").toString());
                MapVertice B = area.getVerticeByName(jsonLocation.get("to").toString());
                float progress = Float.parseFloat(jsonLocation.get("progression").toString());


                position.x = (int) (((1-progress) * A.getX() + progress * B.getX()) * scale_x);
                position.y = (int) (((1-progress) * A.getY() + progress * B.getY()) * scale_y);

            }
		} catch (JSONException e) {
			e.printStackTrace();
		}
		this.renderTaxi.setArea(area.getName());
		this.renderTaxi.setPosition(position);
		MainActivity.ihm.postInvalidate();

	}
}