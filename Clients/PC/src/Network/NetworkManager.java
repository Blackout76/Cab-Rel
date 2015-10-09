package Network;

import java.io.InputStream;
import java.lang.reflect.Array;
import java.net.URI;
import java.util.concurrent.TimeUnit;

import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.api.ContentResponse;
import org.eclipse.jetty.client.api.Response;
import org.eclipse.jetty.client.util.InputStreamResponseListener;
import org.eclipse.jetty.websocket.client.ClientUpgradeRequest;
import org.eclipse.jetty.websocket.client.WebSocketClient;

public class NetworkManager {
	private final String SERVER_URL = "http://37.187.127.119/clickn3D/WEB_services/request_client.php";
	private String SERVER_URI;
    private WebSocketClient client;
    private NetworkWebSocket webSocket;
    
    public NetworkManager() {
    	try {this.SERVER_URI = requestServer();} catch (Exception e) {System.err.println(e.getMessage());}
    	if(SERVER_URI != null)
    		ConnectToSocket(this.SERVER_URI);
    }
	 
    private String requestServer() throws Exception {
    	HttpClient httpClient = new HttpClient();
    	httpClient.setFollowRedirects(false);
    	httpClient.start();
    	ContentResponse response = httpClient.GET(this.SERVER_URL);
    	if(response.getContentAsString() != null)
    		return response.getContentAsString();
		return null;
	}

	public void ConnectToSocket(String uri){
		this.client = new WebSocketClient();
		this.webSocket = new NetworkWebSocket();
        try {
            client.start();
            URI echoUri = new URI(uri);
            ClientUpgradeRequest request = new ClientUpgradeRequest();
            client.connect(webSocket, echoUri, request);
            webSocket.awaitClose(5, TimeUnit.SECONDS);
            System.out.println("Connected to : " + echoUri);
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
}
