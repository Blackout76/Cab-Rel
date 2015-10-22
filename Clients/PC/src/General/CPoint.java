package General;

import java.awt.Point;

import org.json.simple.JSONObject;

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
		this.x = (float) point.getX();
		this.x = (float) point.getY();
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
		json.put("x", this.x);
		json.put("y", this.y);
		return json;
	}
}
