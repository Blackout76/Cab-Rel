package com.example.blackout.gne.render;

import android.graphics.Canvas;

import com.example.blackout.gne.MainActivity;
import com.example.blackout.gne.Map.MapArea;

public class RenderMap {
    private RenderArea area;
	
    public RenderMap(){
    	area = new RenderArea();

    }

    public void render(int iWidth, int iHeight, Canvas canvas) {
        area.render(iWidth, iHeight,canvas);

    }


    public void loadRender(int width, int height, String areaName) {
        area.loadArea(width, height, MainActivity.mapManager.getAreaByName(areaName));
    }
}