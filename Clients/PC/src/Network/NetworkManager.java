package Network;

import java.net.URI;
import java.net.URL;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.TimeUnit;

import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.api.ContentResponse;
import org.eclipse.jetty.websocket.client.WebSocketClient;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import General.Logger;
import General.Logger.Logger_type;
import Map.MapManager;

public class NetworkManager implements Runnable{
	//private final String SERVER_URL = "http://37.187.127.119/clickn3D/WEB_services/request_client.php";
	//private final String SERVER_URL = "http://172.30.1.184:8080/deviceConnect";
	private final String SERVER_URL = "http://0.0.0.0:8080/clientConnect";
	private String SERVER_URI;
    private WebSocketClient client;
    private NetworkWebSocket webSocket;
    
    public NetworkManager() {
    	try {this.SERVER_URI = requestServer();} catch (Exception e) {System.err.println(e.getMessage());}
    }
	 
    private String requestServer() throws Exception {
    	HttpClient httpClient = new HttpClient();
    	httpClient.setFollowRedirects(false);
    	httpClient.start();
    	ContentResponse response = httpClient.GET(this.SERVER_URL);
    	if(response.getContentAsString() != null){
           JSONParser parser = new JSONParser();
	       JSONObject json = null;
	       try { json = (JSONObject) parser.parse(response.getContentAsString());} catch (ParseException e) {}
            Logger.log(Logger_type.SUCCESS, "NetWork HTTP", "URI: "+ json.get("addr").toString() + " !");
    		return json.get("addr").toString();
    	}
		return null;
	}

	public void ConnectToSocket(String uri){
		this.client = new WebSocketClient();
		this.webSocket = new NetworkWebSocket();
		
        try {
            client.start();
            System.err.println(uri.toString());
            URI echoUri = new URI(String.format(uri, null));
            client.connect(webSocket, echoUri);
            System.out.println("je suis ici !!!" + uri);
            webSocket.awaitClose(1, TimeUnit.HOURS);
        } catch (Throwable t) {
            t.printStackTrace();
        } finally {
            try {
                client.stop();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
	
	public boolean isConnected(){
		if(this.SERVER_URI != null){
			if(this.webSocket.isConnected())
				return true;
		}
			return false;
	}

	@SuppressWarnings("unchecked")
	public void requestArea() {
		JSONObject json = new JSONObject();
		json.put("cmd", "areas");
		this.webSocket.sendMessage(json.toJSONString());
	}

	@Override
	public void run() {
    	if(SERVER_URI == null) return;
    	ConnectToSocket(this.SERVER_URI);
	}

	public void addObserver(Observer mapManager) {
		if(this.webSocket != null)
			this.webSocket.addObserver(mapManager);
	}
}
