#! /usr/bin/env python
from BaseHTTPServer import HTTPServer, BaseHTTPRequestHandler
from NetworkWebSocketClient import *
from NetworkWebSocketDevice import *
from NetworkHTTP import *
from MapManager import *
from threading import Thread
import threading
import json
import urlparse
import sys

ip = "0.0.0.0"
portHTTP = 8080
portWebSocketClient = 2639
portWebSocketDevice = 2589

# -------------------------------------
#              MAIN               
# -------------------------------------
if __name__ == '__main__':
	mapManager = MapManager("NomMap")
	mapManager.LoadFileMap()
	print mapManager.toDictFormat()
	
	#thread_server_http = ServerHTTP("1")
	#thread_server_http.start()
	#thread_server_client = NetworkServerSocketClient("2",portWebSocketClient)
	#thread_server_client.start()
	#thread_server_device = NetworkServerSocketDevice("3",portWebSocketDevice)
	#thread_server_device.start()


	
	