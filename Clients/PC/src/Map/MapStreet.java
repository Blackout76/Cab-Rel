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
		// TO DO
		return 0;
	}
	
	public String getName(){
		return this.name;
	}
	public ArrayList<MapVertice> getPath(){
		return this.path;
	}
	public boolean isOneway(){
		return this.oneway;
	}
	
}
