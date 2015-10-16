package com.example.blackout.gne.render;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.View;

import Map.MapArea;


public class RenderView extends View {
    RenderMap renderMap;

    float height, width;
    String name;

    public RenderView(Context context, MapArea area) {

        super(context);
        renderMap=new RenderMap();


    }


    // Dessiner sur la totalité de l'écran
    public void onDraw(Canvas canvas) {
        canvas.drawRGB(0, 0, 0);

        //instance pour donner une taille de l'écran
        Paint paint = new Paint();
        int iWidth = canvas.getWidth();
        int iHeight = canvas.getHeight();
       renderMap.render(iWidth, iHeight, "Quartier Nord");
        //for()
        //canvas.drawLine(streets.get(0).getX(),streets.get(1).getY(),streets.get(2).getX(),streets.get(3).getY());
    }

}
