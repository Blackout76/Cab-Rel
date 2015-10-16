package com.example.blackout.gne.render;

import com.example.ameliepereira.interfacecab.*;

public class RenderMap {
    private RenderArea area;
	
    public RenderMap(){
    	area = new RenderArea();

        area=new RenderArea();
    }

    public void render(int iWidth, int iHeight, String areaName) {
        area.renderArea(iWidth,iHeight,MainActivity.map.getAreaByName(areaName));

    }
}