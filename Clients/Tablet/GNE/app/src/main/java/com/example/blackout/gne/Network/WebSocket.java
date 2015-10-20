package com.example.blackout.gne.Network;

import android.util.Log;

import com.example.blackout.gne.Taxi.TaxiRequest;

import org.java_websocket.WebSocketListener;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_17;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * Created by blackout on 15/10/2015.
 */
public class WebSocket {
    private URI uri;
    private WebSocketClient websocket;
    public AsyncWebSocket delegate=null;

    public WebSocket(String url) {
        Log.e("URL",""+url);
        try {
            uri = new URI(url);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        connect();
    }

    private void connect() {

        websocket = new WebSocketClient(uri, new Draft_17()) {
            @Override
            public void onOpen(ServerHandshake serverHandshake) {
                Log.i("WEBSOCKET", " Opened");
            }

            @Override
            public void onMessage(String msg) {
                JSONObject msgJSON = null;
                try {msgJSON = new JSONObject(msg);} catch (JSONException e) { }
                delegate.onWebSocketMessage(msgJSON);
                Log.i("WEBSOCKET", "Recepted: " + msgJSON.toString());
            }

            @Override
            public void onClose(int i, String s, boolean b) {
                Log.i("WEBSOCKET", "Closed " + s);
            }

            @Override
            public void onError(Exception e) {
                Log.i("WEBSOCKET", "Error " + e.getMessage());
            }
        };
        websocket.connect();
    }

    public void send(JSONObject json){
        websocket.send(json.toString());
    }
    public void send(String message){
        websocket.send(message);
    }


    public void sendTaxiRequest(TaxiRequest taxiRequest) {
        JSONObject taxi =taxiRequest.toJSON();
        this.websocket.send(taxi.toString());
    }
}
