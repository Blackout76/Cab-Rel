package com.example.blackout.gne.render;

import android.graphics.Canvas;
import android.graphics.Point;
import android.util.Log;

import com.example.blackout.gne.Map.MapArea;
import com.example.blackout.gne.Map.MapBridge;
import com.example.blackout.gne.Map.MapStreet;
import com.example.blackout.gne.Map.MapVertice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;


public class RenderArea  {
    private Point startPoint, endPoint;
    private List<Point[]> lines;
    private RenderTaxi renderTaxi;
    private ArrayList<RenderStreet> renderStreets;
    private ArrayList<RenderVertice> renderVertices;



	public void loadArea(int iWidth, int iHeight, MapArea mapArea) {
		
	    int scale_x = (int)(iWidth/ mapArea.getWidth());
        //Log.e("iwidth", ""+iWidth);
	    int scale_y = (int)(iHeight/ mapArea.getHeight());
        //Log.e("iheight", ""+iHeight);
	    renderStreets(scale_x, scale_y, mapArea.getStreets());
	    renderVertices(scale_x, scale_y, mapArea);
        renderTaxis(scale_x, scale_y, mapArea);


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

    private void renderTaxis(int scale_x, int scale_y, MapArea mapArea)
    {


    }

	public void render(int iWidth, int iHeight, Canvas canvas) {

        //print streets
        for(RenderStreet rs: this.renderStreets)
        {
            rs.render(canvas);
        }
        //print vertices
		for(RenderVertice rv: this.renderVertices)
		{
			rv.render(iWidth, iHeight, canvas);
		}

	}
}