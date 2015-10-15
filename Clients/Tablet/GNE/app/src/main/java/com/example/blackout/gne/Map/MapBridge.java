package com.example.blackout.gne.Map;

public class MapBridge {
	private String startVertice;
	private String endArea;
	private String endVertice;
	private float weight;

	public MapBridge(String startVertice2,String endArea2, String endVertice,float weight) {
		this.startVertice = startVertice2;
		this.endArea = endArea2;
		this.endVertice = endVertice;
		this.weight = weight;
	}
	
	public String getStartVertice(){
		return this.startVertice;
	}
}
