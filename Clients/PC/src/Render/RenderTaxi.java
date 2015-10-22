package Render;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

import javax.imageio.ImageIO;

public class RenderTaxi implements Observer{
	private final String fileURL = "images/taxi.png";
	private Image img;
	private Point position;
	private String areaName;
	
	public RenderTaxi (){
		position = new Point(RenderArea.width/2,RenderArea.height/2);
	    img = Toolkit.getDefaultToolkit().getImage(fileURL);
	}
	
	public void render(Graphics2D g2d){
		g2d.drawImage(img, position.x-img.getWidth(null)/2, position.y-img.getHeight(null)/2, null);
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		//TODO
	}
	
	public void setPosition(Point p){
		this.position.x = (int) p.getX();
		this.position.y = (int) p.getY();
	}

	public void setArea(String name) {
		this.areaName = name;
	}

	public String getName() {
		return this.areaName;
	}
}
