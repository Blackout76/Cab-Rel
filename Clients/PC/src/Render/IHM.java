package Render;

import java.awt.*;
import java.util.Observable;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import Map.MapArea;

public class IHM extends JFrame {
    public static final int offsetBorder = 15;
    private final int windowWidth = 600;
    private final int windowHeight = 600;
    public static final boolean drawingTools = false;
    public final String windowName = "Cab-Rel Client";
    private RenderArea area;
    private static String activeAreaName;
    
	public IHM() {
		initJFrame();
		area = new RenderArea((int)(windowWidth-getPreferredSize().getWidth()),(int)(windowHeight-getPreferredSize().getHeight()));
		area.setVisible(true);
		add(area,BorderLayout.CENTER);
	}

	private void initJFrame() {
		setTitle(this.windowName);
		setSize(this.windowWidth,this.windowHeight);
		setLocation(0,0);
		setVisible(true);
		setLayout(new BorderLayout());
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}

	public void destroy() {
		destroy();
	}

	public void generateArea(MapArea mapArea) {
		this.activeAreaName = mapArea.getName();
		this.area.renderArea(mapArea);
		this.area.repaint();
	}
	
	public static String getNameOfActiveArea(){
		return activeAreaName;
	}
}
