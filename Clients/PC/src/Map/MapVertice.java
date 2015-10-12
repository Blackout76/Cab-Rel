package Map;

import org.json.simple.JSONObject;

public class MapVertice {
	private String name;
	private float x;
	private float y;
	
	public MapVertice(String name, float x, float y) {
		this.name = name;
		this.x = x;
		this.y = y;
	}

	public String getName(){
		return this.name;
	}
	public float getX(){
		return this.x;
	}
	public float getY(){
		return this.y;
	}
}
