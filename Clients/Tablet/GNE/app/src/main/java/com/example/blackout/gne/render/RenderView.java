package com.example.blackout.gne.render;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.Layout;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.util.AttributeSet;

import com.example.blackout.gne.General.CPoint;
import com.example.blackout.gne.MainActivity;
import com.example.blackout.gne.Map.MapArea;

public class RenderView extends View  {
    RenderMap renderMap;
    public static float height, width;
    String name;
    public static int offSetBorder=30;
    private String nameOfActiveArea;

    public RenderView(Context context, AttributeSet attrs) {

        super(context, attrs);
        renderMap=new RenderMap();
        this.height=getContext().getResources().getDisplayMetrics().heightPixels-150;
        this.width=getContext().getResources().getDisplayMetrics().widthPixels;

    }



    public void onDraw(Canvas canvas) {
        renderMap.render((int) this.width, (int) this.height, canvas);

    }

    public void loadRender(String areaName) {
        this.nameOfActiveArea = areaName;
        renderMap.loadRender((int) this.width, (int) this.height, areaName);
        MainActivity.ihm.postInvalidate();

    }

    public String getNameOfActiveArea() {
        return nameOfActiveArea;
    }
    @Override
    public boolean onTouchEvent(MotionEvent event){

        int action = event.getAction();
        float x=0;
        float y=0;
        MainActivity.ihm.invalidate();
        switch (event.getAction()) {
            case MotionEvent.ACTION_UP:
                x = event.getX();
                y = event.getY();
                Log.e("x", "" + x);
                Log.e("y", "" + y);
                if(MainActivity.ihm.getNameOfActiveArea() != null && MainActivity.mapManager.getAreaByName(MainActivity.ihm.getNameOfActiveArea()) != null)
                    MainActivity.taxiManager.createRequestAtPoint(new CPoint(x,y));
        }
        return true;
    }
}
