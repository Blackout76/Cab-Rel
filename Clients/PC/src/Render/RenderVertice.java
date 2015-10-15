package Render;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.Map.Entry;

import org.json.simple.JSONObject;

import Map.MapVertice;

public class RenderVertice {
	private final int size = 50;
	private final int font_size = 30;
	private Point center;
	private String name;
	private boolean isBridge; 
	public RenderVertice(int scale_x, int scale_y,Entry<String, MapVertice> entry, boolean isBridge) {
		this.center = new Point();
		this.isBridge = isBridge;
		int offset_x = 0;
		int offset_y = 0;
		center.x = (int)(scale_x * entry.getValue().getX());
		center.y = (int)(scale_y * entry.getValue().getY());
		
		if(center.x >= IHM.windowWidth-IHM.offset_limit_x)
			center.x = IHM.windowWidth-IHM.offset_limit_x - IHM.offset_limit;
		else if(center.x <= 0)
			center.x = IHM.offset_limit;
		if(center.y >= IHM.windowHeight-IHM.offset_limit_y)
			center.y = IHM.windowHeight-IHM.offset_limit_y - IHM.offset_limit;
		else if(center.y <= 0)
			center.y = IHM.offset_limit;
		name = entry.getValue().getName();
	}

	public void render(Graphics2D g2d) {
		g2d.setFont(new Font("TimesRoman", Font.PLAIN, font_size));
		if(isBridge)
			g2d.setColor(Color.CYAN);
		else
			g2d.setColor(Color.GREEN);
		g2d.fillOval(center.x - size/2, center.y - size/2, size, size);
		g2d.setColor(Color.BLACK);
		g2d.drawOval(center.x - size/2, center.y - size/2, size, size);
		g2d.drawString(name, center.x-font_size/4, center.y+font_size/4);
	
	}

}
