package IHM;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class IHM extends JFrame{
    public final int windowWidth = 800;
    public final int windowHeight = 800;
    public final String windowName = "Cab-Rel Client";
    
	public IHM() {
		init();
		createMenu();
		createView();
	}

	private void createView() {
		// TODO Auto-generated method stub
		
	}

	private void createMenu() {
		// TODO Auto-generated method stub
		
	}

	private void init() {
		setTitle(this.windowName);
		setSize(this.windowWidth,this.windowHeight);
		setLocation(0,0);
		setVisible(true);
		setLayout(new BorderLayout( )); 
	}

	private Container make_view() {
		Container view = new JPanel();
		Container view_legs_left = make_view_legs();
		Container view_legs_right = make_view_legs();
		
		view.setLayout(new BorderLayout());
		view.add(new JButton(), BorderLayout.CENTER);
		view.add(view_legs_left, BorderLayout.WEST);
		view.add(view_legs_right, BorderLayout.EAST);
		
		return view;
	}

	private Container make_view_legs() {
		Container view_legs = new JPanel();
		view_legs.setLayout(new GridLayout(3, 1));
		view_legs.add(new JLabel("Leg view"));
		view_legs.add(new JLabel("Leg view"));
		view_legs.add(new JLabel("Leg view"));
		return view_legs;
	}

	public void menu(){
	    MenuBar mbar = new MenuBar( );
	    Menu meprinc1 = new Menu("Fichier");
	    Menu meprinc2 = new Menu("Affichage");
	    Menu meprinc3 = new Menu("Option");
	    Menu meprinc4 = new Menu("?");
	    MenuItem item1 = new MenuItem("Connection");
	    MenuItem item2 = new MenuItem("Deconnection");
	    MenuItem item3 = new MenuItem("Quitter");
	    
	    setMenuBar(mbar);
	    meprinc1.add(item1);
	    meprinc1.add(item2);
	    meprinc1.add(item3);
	    mbar.add(meprinc1);
	    mbar.add(meprinc2);
	    mbar.add(meprinc3);
	    mbar.add(meprinc4); 
	    

	    item3.addActionListener(new ActionListener() {
	        public void actionPerformed(ActionEvent arg0) {
	        	//
	        }
	    });
	}

	public void destroy() {
		destroy();
		
	}
}
