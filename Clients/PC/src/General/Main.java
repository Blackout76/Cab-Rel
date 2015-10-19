package General;

import java.awt.Point;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import Map.MapManager;
import Network.NetworkManager;
import Render.IHM;
import Taxi.TaxiManager;

public class Main implements Runnable{
	public static NetworkManager networkManager;
	public static MapManager mapManager;
	public static TaxiManager taxiManager;
	public static IHM renderer;
	
	
	public Main(){
		renderer = new IHM();
		mapManager = new MapManager();
		taxiManager = new TaxiManager();
		
		Thread t = new Thread(networkManager = new NetworkManager());
		try {Thread.sleep(500);} catch (InterruptedException e) {} //Waiting ready webSocket
		t.start();
	}

	@Override
	public void run() {
		while(true){
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			System.out.println("Command Request Test: (areas, ) ?");
			String line = null;
			try {line = br.readLine();} catch (IOException e) {e.printStackTrace();}
			switch (line) {
			case "areas":
				networkManager.requestArea();
				break;
			default:
				break;
			}
		}
	}

	public static void main(String[] args){
		Thread t = new Thread(new Main());
		t.start();
	}
}
