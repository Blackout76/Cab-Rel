package General;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import Map.MapManager;
import Network.NetworkManager;
import Render.IHM;

public class Cab implements Runnable{
	private NetworkManager networkManager;
	private MapManager mapManager;
	public static IHM renderer;
	
	
	public Cab(){
		renderer = new IHM();
		mapManager = new MapManager();
		Thread t = new Thread(networkManager = new NetworkManager());t.start();
		try {Thread.sleep(500);} catch (InterruptedException e) {} //Waiting ready webSocket
		networkManager.addObserver(mapManager);
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
		Thread t = new Thread(new Cab());
		t.start();
	}
}
