package com.example.blackout.gne.Network;

import org.json.JSONObject;

/**
 * Created by blackout on 15/10/2015.
 */
public interface AsyncWebSocket {
    void onWebSocketMessage(JSONObject json);
}