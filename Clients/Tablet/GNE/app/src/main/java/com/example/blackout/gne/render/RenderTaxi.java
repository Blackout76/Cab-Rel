package com.example.blackout.gne.render;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Picture;
import android.graphics.Point;
import android.media.Image;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.AbsoluteLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.blackout.gne.General.CPoint;
import com.example.blackout.gne.MainActivity;
import com.example.blackout.gne.R;

import java.util.Observable;


public class RenderTaxi {

	private ImageView img;
	private Point position;

	public RenderTaxi (){
        position = new Point((int)(RenderView.width/2),(int)(RenderView.height/2));

	}
	
	public void render(Canvas canvas){


           /* img.setScaleX(position.x-img.getWidth()/2);
            img.setScaleY(position.y - img.getHeight() / 2);*/
        //img.setImageResource(R.mipmap.taxi);
       // img.setX(position.x - img.getWidth() / 2);
       // img.setY(position.y - img.getHeight() / 2);

        //img.draw(canvas);


		//canvas.drawImage(img, position.x-img.getWidth(null)/2, position.y-img.getHeight(null)/2, null);
		//Bitmap b = Bitmap.createBitmap(128, 128, Bitmap.Config.ARGB_8888);
		//canvas= new Canvas(b);
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
