package com.example.blackout.gne.render;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.View;

import com.example.blackout.gne.Map.MapArea;

public class RenderView extends View {
    RenderMap renderMap;
    public static float height, width;
    String name;
    public static int offSetBorder=30;

    public RenderView(Context baseContext) {

        super(baseContext);
        renderMap=new RenderMap();
        this.height=getContext().getResources().getDisplayMetrics().heightPixels;
        this.width=getContext().getResources().getDisplayMetrics().widthPixels;
        Log.e("height", ""+height);
        Log.e("width", ""+width);

    }


    // Dessiner sur la totalité de l'écran
    public void onDraw(Canvas canvas) {

        Paint paint = new Paint();

        renderMap.render((int)this.width,(int)this.height, canvas);

    }
    public void loadRender(String areaName)
    {
        renderMap.loadRender((int) this.width, (int) this.height, areaName);

    }
}
