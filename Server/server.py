#! /usr/bin/env python
from BaseHTTPServer import HTTPServer, BaseHTTPRequestHandler
from threading import Thread
import NetworkWebSocketClient
import NetworkWebSocketDevice
import NetworkHTTP
import MapManager
import TaxiManager
import threading
import json
import urlparse
import sys
reload(sys)
sys.setdefaultencoding('utf-8')

# -------------------------------------
#              MAIN               
# -------------------------------------
if __name__ == '__main__':
	MapManager.mapManager = MapManager.MapManager("NomMap")
	MapManager.mapManager.loadFileMap()
	TaxiManager.taxiManager = TaxiManager.TaxiManager(MapManager.mapManager)
	TaxiManager.taxiManager.newTaxi()

	#NetworkHTTP.server_http = NetworkHTTP.ServerHTTP("1")
	#NetworkHTTP.server_http.start()
	NetworkWebSocketClient.server_client = NetworkWebSocketClient.NetworkServerSocketClient("2")
	NetworkWebSocketClient.server_client.start()
	NetworkWebSocketDevice.server_device = NetworkWebSocketDevice.NetworkServerSocketDevice("3")
	NetworkWebSocketDevice.server_device.start()
	
	TaxiManager.taxiManager.onCabInfo()
	
	