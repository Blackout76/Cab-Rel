package Render;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;

public class RenderRestrictZone {
	private final int size = 10;
	
	public void render(int width, int height,Graphics2D g2d){
		g2d.setColor(Color.yellow);
        g2d.setStroke(new BasicStroke(5));
		g2d.drawLine(size, size, size, height-size*2);
		g2d.drawLine(size, size, width-size*2, size);
		g2d.drawLine(size, height-size*2, width-size*2, height-size*2);
		g2d.drawLine(width-size*2, size, width-size*2, height-size*2);
	}
	
	public int getSize(){
		return this.size;
	}
}
