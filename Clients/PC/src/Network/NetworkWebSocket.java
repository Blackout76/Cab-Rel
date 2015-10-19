package Network;
import java.util.Observable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import General.Logger;
import General.Logger.Logger_type;
import General.Main;
 
@WebSocket(maxTextMessageSize = 64 * 1024)
public class NetworkWebSocket  {
 
    private final CountDownLatch closeLatch;
 
    private Session session;
 
    public NetworkWebSocket() {
        this.closeLatch = new CountDownLatch(100);
    }

    public boolean awaitClose(int duration, TimeUnit unit) throws InterruptedException {
        return this.closeLatch.await(duration, unit);
    }
 
    @OnWebSocketClose
    public void onClose(int statusCode, String reason) {
        Logger.log(Logger_type.ERROR, "WebSocket", "Connection closed !" + reason);
        this.session = null;
        this.closeLatch.countDown();
    }
 
    @OnWebSocketConnect
    public void onConnect(Session session) {
        this.session = session;
        Logger.log(Logger_type.SUCCESS, "WebSocket", "Connected !");
    }
 
    @OnWebSocketMessage
    public void onMessage(String msg) {
       System.out.println("Message recu:" + msg);
       JSONParser parser = new JSONParser();
       JSONObject json = null;
       try { json = (JSONObject) parser.parse(msg);} catch (ParseException e) {}
       executeMessage(json);
    }
    public void executeMessage(JSONObject json){
    	if(json.get("areas") != null)
    		Main.mapManager.loadMap(json);
    }
    
    public void sendMessage(String message){
    	if(session.isOpen())
    		try {session.getRemote().sendString(message);} catch (Exception e) {e.printStackTrace();}
    	else
    		Logger.log(Logger_type.ERROR, "[WEBSOCKET]", "Socket is closed!");
    }

	public boolean isConnected() {
		if(this.session.isOpen())
			return true;
		return false;
	}
}