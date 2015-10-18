package Map;

import java.awt.Point;
import java.util.ArrayList;

import org.json.simple.JSONObject;

public class MapStreet {
	private ArrayList<MapVertice> path;
	private String name;
	private boolean oneway;
	
	public MapStreet(String name, ArrayList<MapVertice> arrayList,boolean oneway){
		this.path = arrayList;
		this.name = name;
		this.oneway = oneway;
	}

	public float getWeight(){
		// TODO
		return 0;
	}
	
	public String getName(){
		return this.name;
	}
	public ArrayList<MapVertice> getPath(){
		return this.path;
	}
	public String getPathString(){
		return this.path.get(0).toString() + "" + this.path.get(1).toString();
	}
	public boolean isOneway(){
		return this.oneway;
	}
	
}
