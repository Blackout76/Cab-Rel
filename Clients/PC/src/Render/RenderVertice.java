package Render;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.Map.Entry;

import Map.MapVertice;

public class RenderVertice {
	private final int size = 80;
	private final int font_size = 75;
	private Point center;
	private String name;
	
	public RenderVertice(int scale_x, int scale_y,Entry<String, MapVertice> entry) {
		center = new Point();
		center.x = (int)(scale_x * entry.getValue().getX());
		center.y = (int)(scale_y * entry.getValue().getY());
		name = entry.getValue().getName();
	}

	public void render(Graphics2D g2d) {
		g2d.setFont(new Font("TimesRoman", Font.PLAIN, font_size));
		g2d.setColor(Color.CYAN);
		g2d.fillOval(center.x - size/2, center.y - size/2, size, size);
		g2d.setColor(Color.BLACK);
		g2d.drawOval(center.x - size/2, center.y - size/2, size, size);
		g2d.drawString(name, center.x-font_size/4, center.y+font_size/4);
	}

}
