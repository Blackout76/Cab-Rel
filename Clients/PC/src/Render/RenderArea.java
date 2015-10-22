package Render;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import javax.swing.JPanel;

import org.json.simple.JSONObject;

import General.CPoint;
import General.Main;
import Map.MapArea;
import Map.MapBridge;
import Map.MapStreet;
import Map.MapVertice;
import Taxi.TaxiRequest;

public class RenderArea extends JPanel {
	public static int width = 0;
	public static int height = 0;
	private int scale_x = 0;
	private int scale_y = 0;
    private Point startPoint, endPoint;
    private List<Point[]> lines;
    private RenderTaxi renderTaxi;
    private ArrayList<RenderStreet> renderStreets;
    private ArrayList<RenderVertice> renderVertices;

    public RenderArea(int width, int height) {
    	this.width = width;
    	this.height = height;
    	
        lines = new ArrayList<>();
        renderStreets = new ArrayList<>();
        renderVertices = new ArrayList<>();
        renderTaxi = new RenderTaxi();
        
        MouseAdapter ma = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                startPoint = e.getPoint();
                Main.taxiManager.createRequestAtPoint(startPoint);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
            	if(IHM.drawingTools){
	                endPoint = e.getPoint();
	                Point[] points = new Point[]{startPoint, endPoint};
	                lines.add(points);
	                startPoint = null;
	                endPoint = null;
	                repaint();
            	}
            }

            @Override
            public void mouseDragged(MouseEvent e) {
            	if(IHM.drawingTools){
	                endPoint = e.getPoint();
	                repaint();
            	}
            }

        };

        addMouseListener(ma);
        addMouseMotionListener(ma);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(200, 200);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();
        
        // Draw Background
        GradientPaint gp = new GradientPaint(0, 0, new Color(119, 215, 255), 0, this.height, new Color(0, 60, 255));
        g2d.setPaint(gp);
        g2d.fillRect(0, 0, this.width, this.height);
        
        //all last line created for TEST
        g2d.setColor(Color.BLACK);
        for (Point[] p : lines) {
            g2d.drawLine(p[0].x, p[0].y, p[1].x, p[1].y);
        }
        
        //current line
        if (startPoint != null && endPoint != null) {
            g2d.setColor(Color.RED);
            g2d.drawLine(startPoint.x, startPoint.y, endPoint.x, endPoint.y);
        }
        //Render area
        for(RenderStreet renderObject: renderStreets)
        	renderObject.render(g2d);
        for(RenderVertice renderObject: renderVertices)
        	renderObject.render(g2d);
        
        //Render Taxi
        if(renderTaxi != null && renderTaxi.getName() != null && renderTaxi.getName().equals(Main.renderer.getNameOfActiveArea()))
        	renderTaxi.render(g2d);
        //Render Request
        if(Main.taxiManager != null && Main.taxiManager.getTaxiRequest() != null)

        	//System.err.println(Main.taxiManager.getTaxiRequest().size());
	        for(TaxiRequest taxiRequest : Main.taxiManager.getTaxiRequest()){
	        	if(taxiRequest.getArea().getName().equals(Main.renderer.getNameOfActiveArea())){
	        		RenderTaxiRequest renderTaxiRequest  = new RenderTaxiRequest(taxiRequest.getPosition());
	        		renderTaxiRequest.render(scale_x,scale_y,g2d);
	        	}
	        }
        	
        
        g2d.dispose();
    }

	public void renderArea(MapArea mapArea) {
		
	    scale_x = (int)(this.width / mapArea.getWidth()); 
	    scale_y = (int)(this.height / mapArea.getHeight()); 
	    //System.err.println(scale_x + "  " + scale_y);
	    renderStreets(scale_x,scale_y,mapArea.getStreets());
	    renderVertices(scale_x,scale_y,mapArea);
        repaint();
	}
	
	private void renderVertices(int scale_x, int scale_y,MapArea mapArea) {
		this.renderVertices = new ArrayList<>();
		for(Entry<String, MapVertice> entryVertice : mapArea.getVertices().entrySet()) {
			boolean isBridge = false;
			for(MapBridge entryBridge : mapArea.getBridges())
				if(entryBridge.getStartVertice().equals(entryVertice.getValue().getName()))
					isBridge = true;
	        this.renderVertices.add(new RenderVertice(scale_x,scale_y,entryVertice,isBridge));
	    }
	}

	private void renderStreets(int scale_x, int scale_y, HashMap<String, MapStreet> streets){
		this.renderStreets = new ArrayList<>();
	    for(Entry<String, MapStreet> entry : streets.entrySet()) {
	        this.renderStreets.add(new RenderStreet(scale_x,scale_y,entry));
	    }
	}
	
	public void updateTaxiRenderPosition(JSONObject json){
		//{"cabInfo": {"loc_now": {"locationType": "vertex", "location": "c", "area": "Quartier Nord"},
		MapArea area = null;
		Point position = new Point();
		
		if(((JSONObject) ((JSONObject) json.get("cabInfo")).get("loc_now")).get("locationType").toString().equals("vertex")){
			String name = ((JSONObject) ((JSONObject) json.get("cabInfo")).get("loc_now")).get("location").toString();
			area = Main.mapManager.getAreaByName(((JSONObject) ((JSONObject) json.get("cabInfo")).get("loc_now")).get("area").toString());
			position.x  = (int) (area.getVerticeByName(name).getX() * scale_x);
			position.y  = (int) (area.getVerticeByName(name).getY() * scale_y);
		}
		else{
			JSONObject jsonLocation = (JSONObject) ((JSONObject) ((JSONObject) json.get("cabInfo")).get("loc_now")).get("location");
			area = Main.mapManager.getAreaByName(((JSONObject) ((JSONObject) json.get("cabInfo")).get("loc_now")).get("area").toString());
			MapVertice A = area.getVerticeByName(jsonLocation.get("from").toString());
			MapVertice B = area.getVerticeByName(jsonLocation.get("to").toString());
			float progress = Float.parseFloat(jsonLocation.get("progression").toString());
			
			
			position.x = (int) (((1-progress) * A.getX() + progress * B.getX()) * scale_x);
			position.y = (int) (((1-progress) * A.getY() + progress * B.getY()) * scale_y);
			
		}
		this.renderTaxi.setArea(area.getName());
		this.renderTaxi.setPosition(position);
		Main.renderer.area.repaint();
		
	}
}