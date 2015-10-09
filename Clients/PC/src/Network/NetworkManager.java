package Network;

import java.net.URI;
import java.util.concurrent.TimeUnit;

import org.eclipse.jetty.websocket.client.ClientUpgradeRequest;
import org.eclipse.jetty.websocket.client.WebSocketClient;

public class NetworkManager {

    private WebSocketClient client = new WebSocketClient();
    private NetworkWebSocket webSocket = new NetworkWebSocket();
    
    public NetworkManager() {
    	ConnectToSocket("ws://0.0.0.0:2009");
    }
	 
    public void ConnectToSocket(String uri){
        try {
            client.start();
            URI echoUri = new URI(uri);
            ClientUpgradeRequest request = new ClientUpgradeRequest();
            client.connect(webSocket, echoUri, request);
            System.out.println("Connecting to : " + echoUri);
            webSocket.awaitClose(5, TimeUnit.SECONDS);
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
}
