package com.example.blackout.gne.Map;

import java.util.ArrayList;


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

	public String getOposedVerticeFromVertice(String name) {
		if(this.path.get(0).getName().equals(name))
			return this.getPath().get(this.getPath().size()-1).getName();
		return this.path.get(0).getName();

	}
}
