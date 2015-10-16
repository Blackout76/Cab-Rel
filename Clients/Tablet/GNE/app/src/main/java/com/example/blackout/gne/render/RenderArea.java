package com.example.blackout.gne.render;

import android.graphics.Point;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import Map.MapArea;
import Map.MapBridge;
import Map.MapStreet;
import Map.MapVertice;

public class RenderArea  {
    private Point startPoint, endPoint;
    private List<Point[]> lines;
    private RenderTaxi renderTaxi;
    private ArrayList<RenderStreet> renderStreets;
    private ArrayList<RenderVertice> renderVertices;


	public void renderArea(int iWidth, int iHeight, MapArea mapArea) {
		
	    int scale_x = (int)(iWidth/ mapArea.getWidth());
	    int scale_y = (int)(iHeight/ mapArea.getHeight());
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

}