package com.example.blackout.gne.General;

import android.graphics.Point;

import org.json.JSONException;
import org.json.JSONObject;


public class CPoint {
	public float x;
	public float y;

	public CPoint(float x,float y){
		this.x = x;
		this.y = y;
	}
	public CPoint(int x,int y){
		this.x = x;
		this.y = y;
	}
	public CPoint() {
		// TODO Auto-generated constructor stub
	}
	public CPoint(Point point) {
		this.x = (float) point.x;
		this.x = (float) point.y;
	}
	public CPoint(CPoint c) {
		this.x = c.getX();
		this.y = c.getY();
	}
	public float getX(){
		return this.x;
	}
	public float getY(){
		return this.y;
	}
	public String toString(){
		return "[" + this.x + ":" + this.y + "]";
	}
	public JSONObject toJSON() {
		JSONObject json = new JSONObject();
		try {
			json.put("x", this.x);
			json.put("y", this.y);
		} catch (JSONException e) {
			e.printStackTrace();
		}


		return json;
	}
}
