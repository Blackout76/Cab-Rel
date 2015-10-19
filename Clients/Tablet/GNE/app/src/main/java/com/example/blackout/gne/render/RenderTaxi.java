package com.example.blackout.gne.render;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Picture;
import android.graphics.Point;
import android.media.Image;
import android.widget.ImageView;
import java.util.Observable;


public class RenderTaxi {
	private final String fileURL = "images/taxi.png";
	private ImageView img;
	private Point position;
	public RenderTaxi (){
		//position = new Point(IHM.windowWidth/2,IHM.windowHeight/2);
	    //img = Toolkit.getDefaultToolkit().getImage(fileURL);
	}
	
	public void render(Canvas g2d){

		//g2d.drawImage(img, position.x-img.getWidth(null)/2, position.y-img.getHeight(null)/2, null);
		//Bitmap b = Bitmap.createBitmap(128, 128, Bitmap.Config.ARGB_8888);
		//g2d= new Canvas(b);
       // ImageView iv = (ImageView)findViewById(R.id.my_imageView);
        /*AbsoluteLayout.LayoutParams absParams = (AbsoluteLayout.LayoutParams)img.getLayoutParams();
        absParams.x = position.x-img.getWidth()/2;
        absParams.y = position.y-img.getHeight()/2;
        img.setLayoutParams(absParams);*/
	}

	//@Override
	public void update(Observable arg0, Object arg1) {
		//TODO
	}
}
