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

import General.CPoint;

public class RenderTaxiRequest{
	private final String fileURL = "images/icon-map-pin.png";
	private Image img;
	private CPoint position;
	public RenderTaxiRequest (CPoint position){
		this.position = new CPoint(position);
	    img = Toolkit.getDefaultToolkit().getImage(fileURL);
	}
	
	public void render(int scale_x, int scale_y, Graphics2D g2d){
		g2d.drawImage(img,((int) (position.x*scale_x))-img.getWidth(null)/2, ((int) (position.y*scale_y))-img.getHeight(null)/2, null);
	}

	public CPoint getPoint() {
		// TODO Auto-generated method stub
		return this.position;
	}
}
