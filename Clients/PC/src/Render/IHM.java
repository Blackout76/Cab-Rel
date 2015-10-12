package Render;

import java.awt.*;
import java.util.Observable;

import javax.swing.JFrame;

import Map.MapArea;

public class IHM extends JFrame {
    public final int windowWidth = 800;
    public final int windowHeight = 600;
    public final String windowName = "Cab-Rel Client";
    private RenderArea area;
    
	public IHM() {
		init();
		area = new RenderArea();
		area.setSize(getWidth(), getHeight());
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
