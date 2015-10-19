package com.example.blackout.gne.render;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;

import com.example.blackout.gne.Map.MapStreet;

import java.util.Map.Entry;


public class RenderStreet{
	
	private Point[] pts;
	
	public RenderStreet(int scale_x, int scale_y,Entry<String, MapStreet> entry) {
        
        String streetName = entry.getKey();
        MapStreet street = entry.getValue();

        pts = new Point[street.getPath().size()];
        for(int i=0;i< street.getPath().size() ; i++){
        	pts[i] = new Point();
        	pts[i].x = (int) (scale_x * street.getPath().get(i).getX());
        	pts[i].y = (int) (scale_y * street.getPath().get(i).getY());
        }
	}

	public void render(Canvas canvas) {
		Paint paint=new Paint();
        //canvas.setStroke(new BasicStroke(5));
        canvas.drawLine(pts[0].x, pts[0].y, pts[1].x, pts[1].y,paint );

	}
}
