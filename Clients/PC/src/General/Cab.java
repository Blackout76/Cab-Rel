package General;

import Network.NetworkManager;

public class Cab {
	public static NetworkManager networkManager;
	
	public Cab(){
		networkManager = new NetworkManager();
		System.exit(0);
	}
	
	public static void main(String[] args){
		new Cab();
	}
}
