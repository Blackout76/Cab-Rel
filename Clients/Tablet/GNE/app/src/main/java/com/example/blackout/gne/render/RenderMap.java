package com.example.blackout.gne.render;

import android.graphics.Canvas;
import android.util.Log;

import com.example.blackout.gne.MainActivity;
import com.example.blackout.gne.Map.MapArea;
import com.example.blackout.gne.Taxi.TaxiRequest;

public class RenderMap {
    private RenderArea area;
	
    public RenderMap(){
    	area = new RenderArea();

    }

    public void render(int iWidth, int iHeight, Canvas canvas) {
        area.render(iWidth, iHeight, canvas);

        if(MainActivity.taxiManager != null && MainActivity.taxiManager.getTaxiRequest() != null)
            for(TaxiRequest taxiRequest : MainActivity.taxiManager.getTaxiRequest()){
                if(taxiRequest.getArea().getName().equals(MainActivity.ihm.getNameOfActiveArea())){
                    RenderCabRequest renderTaxiRequest  = new RenderCabRequest(taxiRequest.getPosition());
                    renderTaxiRequest.render(RenderArea.scale_x, RenderArea.scale_y, canvas);

                }
            }
    }


    public void loadRender(int width, int height, String areaName) {
        area.loadArea(width, height, MainActivity.mapManager.getAreaByName(areaName));
    }
}