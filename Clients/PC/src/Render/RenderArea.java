package Render;

import java.awt.Color;
import java.awt.Dimension;
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

import Map.MapArea;
import Map.MapStreet;
import Map.MapVertice;

public class RenderArea extends JPanel {
    private Point startPoint, endPoint;
    private List<Point[]> lines;
    private ArrayList<RenderStreet> renderStreets;
    private ArrayList<RenderVertice> renderVertices;
    private RenderRestrictZone renderRestrict;

    public RenderArea() {
        lines = new ArrayList<>();
        renderStreets = new ArrayList<>();
        renderVertices = new ArrayList<>();
        
        MouseAdapter ma = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                startPoint = e.getPoint();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                endPoint = e.getPoint();
                Point[] points = new Point[]{startPoint, endPoint};
                lines.add(points);
                startPoint = null;
                endPoint = null;
                repaint();
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                endPoint = e.getPoint();
                repaint();
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
        g2d.setColor(Color.BLACK);
        //System.err.println(lines.size());
        for (Point[] p : lines) {
            g2d.drawLine(p[0].x, p[0].y, p[1].x, p[1].y);
        }
        if (startPoint != null && endPoint != null) {
            g2d.setColor(Color.RED);
            g2d.drawLine(startPoint.x, startPoint.y, endPoint.x, endPoint.y);
        }
        //	renderRestrict.render(getWidth(), getHeight(), g2d);
        for(RenderStreet renderObject: renderStreets)
        	renderObject.render(g2d);
        for(RenderVertice renderObject: renderVertices)
        	renderObject.render(g2d);
        g2d.dispose();
    }

	public void renderArea(MapArea mapArea) {
	    int scale_x = (int)(this.getWidth() / mapArea.getWidth()); 
	    int scale_y = (int)(this.getHeight() / mapArea.getHeight()); 
	    renderStreets(scale_x,scale_y,mapArea.getStreets());
	    renderVertices(scale_x,scale_y,mapArea.getVertices());
        repaint();
	}
	
	private void renderVertices(int scale_x, int scale_y,HashMap<String, MapVertice> vertices) {
		this.renderVertices = new ArrayList<>();
		for(Entry<String, MapVertice> entry : vertices.entrySet()) {
	        this.renderVertices.add(new RenderVertice(scale_x,scale_y,entry));
	    }
	}

	private void renderStreets(int scale_x, int scale_y, HashMap<String, MapStreet> streets){
		this.renderStreets = new ArrayList<>();
	    for(Entry<String, MapStreet> entry : streets.entrySet()) {
	        this.renderStreets.add(new RenderStreet(scale_x,scale_y,entry));
	    }
	}
}