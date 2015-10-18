package Render;

import java.awt.*;
import java.util.Observable;

import javax.swing.JFrame;

import Map.MapArea;

public class IHM extends JFrame {
    public static final int windowWidth = 600;
    public static final int windowHeight = 600;
    public static final int offset_limit = 15;
    public static final int offset_limit_x = 17;
    public static final int offset_limit_y = 35;
    public static final boolean drawingTools = true;
    public final String windowName = "Cab-Rel Client";
    private RenderArea area;
    
    
	public IHM() {
		init();
		area = new RenderArea();
		area.setSize(windowWidth, windowHeight);
		add(area, BorderLayout.CENTER);
		area.setVisible(true);
	}

	private void init() {
		setTitle(this.windowName);
		setSize(this.windowWidth,this.windowHeight);
		setLocation(0,0);
		setVisible(true);
		setLayout(new BorderLayout( )); 
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}

	public void destroy() {
		destroy();
	}

	public void generateArea(MapArea mapArea) {
		this.area.renderArea(mapArea);
	}
}
