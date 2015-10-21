#! /usr/bin/env python
from BaseHTTPServer import HTTPServer, BaseHTTPRequestHandler
from threading import Thread
import NetworkWebSocket
import NetworkHTTP
import MapManager
import TaxiManager
import threading
import json
import TaxiMove
import urlparse
import sys
import time
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
	
	"""
	# TEST TAXI MOVE
	# Test request
	clientMessageHandle = {"cabRequest":{"location":{"location":{"to":"m","progression":0.8,"name":"mh","from":"h"},"area":"Quartier Sud","locationType":"street"},"area":"Quartier Sud"}}
	#clientMessageHandle = {"cabRequest":{"location":{"location":"m","area":"Quartier Sud","locationType":"vertex"},"area":"Quartier Sud"}}
	TaxiManager.taxiManager.addCabRequest(clientMessageHandle)
	TaxiManager.taxiManager.onRequestAnswer("yes")
	
	path = []
	p0 = {}
	p0["area"] = "Quartier Nord"
	p0["vertex"] = "m"
	path.append(p0)
	p1 = {}
	p1["area"] = "Quartier Nord"
	p1["vertex"] = "b"
	path.append(p1)
	p2 = {}
	p2["area"] = "Quartier Sud"
	p2["vertex"] = "h"
	path.append(p2)
	p3 = {}
	p3["area"] = "Quartier Sud"
	p3["vertex"] = "m"
	path.append(p3)
	
	
	TaxiMove.taxiThread = TaxiMove.TaxiThread("0",path)
	TaxiMove.taxiThread.start()
	"""
	
	
	NetworkHTTP.server_http = NetworkHTTP.ServerHTTP("1")
	NetworkHTTP.server_http.start()
	
	NetworkWebSocket.server_WEBSOCKET = NetworkWebSocket.NetworkServerSocket("2")
	NetworkWebSocket.server_WEBSOCKET.start()
	
	TaxiManager.taxiManager.onCabInfo()
	