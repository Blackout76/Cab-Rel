package com.example.blackout.gne.render;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Build;
import android.util.Log;

import com.example.blackout.gne.Map.MapVertice;

import java.util.Map.Entry;


public class RenderVertice {
	private final int size = 100;
	private final int font_size = 100;
	private Point center;
	private String name;
	private boolean isBridge;

	public RenderVertice(int scale_x, int scale_y,Entry<String, MapVertice> entry, boolean isBridge) {

		this.center = new Point();
		this.isBridge = isBridge;
		int offset_x = 0;
		int offset_y = 0;
		center.x = (int)(scale_x * entry.getValue().getX());
		center.y = (int)(scale_y * entry.getValue().getY());


		if(center.x >= RenderView.width-RenderView.offSetBorder)
			center.x = (int)(RenderView.width - RenderView.offSetBorder);

		else if(center.x <= RenderView.offSetBorder)
			center.x = RenderView.offSetBorder;

		if(center.y >= RenderView.height-RenderView.offSetBorder)
			center.y = (int)(RenderView.height-RenderView.offSetBorder);

		else if(center.y <= RenderView.offSetBorder)
			center.y = RenderView.offSetBorder;

		name = entry.getValue().getName();
	}

	//@TargetApi(Build.VERSION_CODES.LOLLIPOP)
	public void render(int iWidth, int iHeight,Canvas canvas
    ) {
		Paint p=new Paint();

        //bridge=blue and classic point=red
		if(isBridge) {
            p.setColor(Color.BLUE);
        }
		else {
            p.setColor(Color.RED);
        }

        canvas.drawCircle(center.x , center.y , size, p);

        //Color and size text
        p.setColor(Color.WHITE);
        p.setTextSize(font_size);

        //Text location in the point
        canvas.drawText(name, center.x-font_size/2, center.y+font_size/4, p );


	}

}
