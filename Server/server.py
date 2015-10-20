#! /usr/bin/env python
from BaseHTTPServer import HTTPServer, BaseHTTPRequestHandler
from threading import Thread
import NetworkWebSocket
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

	NetworkHTTP.server_http = NetworkHTTP.ServerHTTP("1")
	NetworkHTTP.server_http.start()
	
	NetworkWebSocket.server_WEBSOCKET = NetworkWebSocket.NetworkServerSocket("2")
	NetworkWebSocket.server_WEBSOCKET.start()
	
	TaxiManager.taxiManager.onCabInfo()
	
	