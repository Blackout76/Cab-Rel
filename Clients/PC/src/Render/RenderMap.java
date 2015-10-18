package Render;

import javax.swing.*;

public class RenderMap extends JFrame{
    private RenderArea area;
	
    public RenderMap(){
    	area = new RenderArea(0,0);
		add(area);
    }
}