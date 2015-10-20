package com.example.blackout.gne.render;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
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

import org.eclipse.jetty.util.resource.Resource;

import java.util.Observable;


public class RenderTaxi {

	private ImageView img;
	private Point position;


	public RenderTaxi (){
        position = new Point((int)(RenderView.width/2),(int)(RenderView.height/2));

	}
	
	public void render(Canvas canvas){

        Paint p=new Paint();
        p.setColor(Color.YELLOW);
        canvas.drawCircle(50, 50, 50, p);

	}



	//@Override
	public void update(Observable arg0, Object arg1) {
		//TODO
	}
}
