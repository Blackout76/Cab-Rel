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

	private Point position;


	public RenderTaxi (){
        position = new Point((int)(RenderView.width/2),(int)(RenderView.height/2));

	}
    //{"cabInfo":{"loc_prior":{"location":"b","area":"Quartier Nord","locationType":"vertex"}
    // ,"odometer":0,"loc_now":{"location":"b","area":"Quartier Nord","locationType":"vertex"},"destination":null}}

    //{"cabQueue":[{"location":{"location":{"to":"b","progression":0.13879001140594482,"name":"mb","from":"m"},
    //"area":"Quartier Nord","locationType":"street"},"area":"Quartier Nord"},
    //{"location":{"location":{"to":"b","progression":0.42704629898071284,"name":"mb","from":"m"},
    // "area":"Quartier Nord","locationType":"street"},"area":"Quartier Nord"},
    // {"location":{"location":{"to":"m","progression":0.24911034107208252,"name":"mb","from":"b"},
    // "area":"Quartier Nord","locationType":"street"},"area":"Quartier Nord"},
    // {"location":{"location":{"to":"h","progression":0.468566107749939,"name":"mh","from":"m"},
    // "area":"Quartier Sud","locationType":"street"},"area":"Quartier Sud"},
    // {"location":{"location":{"to":"h","progression":0.42052567005157465,"name":"mh","from":"m"},
    // "area":"Quartier Sud","locationType":"street"},"area":"Quartier Sud"},
    // {"location":{"location":{"to":"h","progression":0.4806469202041625,"name":"mh","from":"m"},
    // "area":"Quartier Sud","locationType":"street"},"area":"Quartier Sud"},
    // {"location":{"location":{"to":"h","progression":0.39263501167297366,"name":"mh","from":"m"},
    // "area":"Quartier Sud","locationType":"street"},"area":"Quartier Sud"},
    // {"location":{"location":{"to":"m","progression":0.4904903769493103,"name":"mh","from":"h"},
    // "area":"Quartier Sud","locationType":"street"},"area":"Quartier Sud"},
    // {"location":{"location":{"to":"m","progression":0.40247225761413574,"name":"mh","from":"h"},
    // "area":"Quartier Sud","locationType":"street"},"area":"Quartier Sud"},
    // {"location":{"location":{"to":"m","progression":0.4028104662895202,"name":"mh","from":"h"},
    // "area":"Quartier Sud","locationType":"street"},"area":"Quartier Sud"}]}
	
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
