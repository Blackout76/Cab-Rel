package com.example.blackout.gne.render;

import android.graphics.Canvas;
import android.graphics.Point;
import android.media.Image;

import java.util.Observable;


public class RenderTaxi {
	private final String fileURL = "images/taxi.png";
	private Image img;
	private Point position;
	public RenderTaxi (){
		//position = new Point(IHM.windowWidth/2,IHM.windowHeight/2);
	    //img = Toolkit.getDefaultToolkit().getImage(fileURL);
	}
	
	public void render(Canvas g2d){
		//g2d.drawImage(img, position.x-img.getWidth(null)/2, position.y-img.getHeight(null)/2, null);
	}

	//@Override
	public void update(Observable arg0, Object arg1) {
		//TODO
	}
}
