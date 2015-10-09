package Network;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.StatusCode;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
 
/**
 * Basic Echo Client Socket
 */
@WebSocket(maxTextMessageSize = 64 * 1024)
public class NetworkWebSocket {
 
    private final CountDownLatch closeLatch;
 
    @SuppressWarnings("unused")
    private Session session;
 
    public NetworkWebSocket() {
        this.closeLatch = new CountDownLatch(1);
    }
 
    public boolean awaitClose(int duration, TimeUnit unit) throws InterruptedException {
        return this.closeLatch.await(duration, unit);
    }
 
    @OnWebSocketClose
    public void onClose(int statusCode, String reason) {
        System.out.printf("Connection closed: %d - %s%n", statusCode, reason);
        this.session = null;
        this.closeLatch.countDown();
    }
 
    @OnWebSocketConnect
    public void onConnect(Session session) {
        this.session = session;
    }
 
    @OnWebSocketMessage
    public void onMessage(String msg) {
       System.out.println("Message recu:" + msg);
    }
    
    public void sendMessage(String message){
        session.getRemote().sendStringByFuture(message);
    }

	public boolean isConnected() {
		if(this.session.isOpen())
			return true;
		return false;
	}
}