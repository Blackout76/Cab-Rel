package com.example.blackout.gne.render;


import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.example.blackout.gne.General.CPoint;

import java.util.Observer;

public class RenderCabRequest {

    private CPoint position;

    public RenderCabRequest(CPoint position){
        this.position = new CPoint(position);

    }

    public void render(int scale_x, int scale_y, Canvas canvas){

        Paint p=new Paint();
        p.setColor(Color.CYAN);
        p.setTextSize(75);
        canvas.drawText("X", position.x*scale_x, position.y*scale_y, p);
    }

    //@Override
    public void update(Observer arg0, Object arg1) {
        //TODO
    }

    public CPoint getPoint() {
        // TODO Auto-generated method stub
        return this.position;
    }


}


