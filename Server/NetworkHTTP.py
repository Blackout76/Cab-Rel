#! /usr/bin/env python
from BaseHTTPServer import HTTPServer, BaseHTTPRequestHandler
from server import *
from threading import Thread
import NetworkWebSocket
import threading
import json
import urlparse
import sys
reload(sys)
sys.setdefaultencoding('utf-8')

#ip = "172.30.1.104" # prod IP
#ipLocal = "169.254.83.93" # prod IP
ip = "0.0.0.0" # dev IP
ipLocal = "0.0.0.0" # dev IP
portHTTP = 8080
server_http = None

# -------------------------------------
#              HTTP SERVER              
# -------------------------------------
class GetHandler(BaseHTTPRequestHandler):
	def do_GET(self):
		parsed_path = urlparse.urlparse(self.path)
		path = parsed_path.path[1:]
		jsonHash = {}
		
		if path == "deviceConnect":
			jsonHash["addr"] = ipLocal
			jsonHash["prt"] = NetworkWebSocket.portWebSocket
		elif path == "clientConnect":
			jsonHash["addr"] = 'ws://' + ip + ':' + `NetworkWebSocket.portWebSocket`
		
		self.send_response(200)
		self.end_headers()
		self.wfile.write(json.dumps(jsonHash))
		return

class ServerHTTP(Thread):
	def __init__(self, lettre):
		self.isRun = True
		Thread.__init__(self)
		self.server = HTTPServer(('', portHTTP), GetHandler)
		print 'HTTP listenning ...'

	def run(self):
		while self.isRun:
			self.server.serve_forever() 

