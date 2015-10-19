#! /usr/bin/env python
from BaseHTTPServer import HTTPServer, BaseHTTPRequestHandler
from server import *
from threading import Thread
import threading
import json
import urlparse
import sys

# -------------------------------------
#              HTTP SERVER              
# -------------------------------------
class GetHandler(BaseHTTPRequestHandler):
	def do_GET(self):
		parsed_path = urlparse.urlparse(self.path)
		path = parsed_path.path[1:]
		jsonHash = {}

		if path == "deviceConnect":
			jsonHash['addr'] = 'ws://' + ip + ':' + `portWebSocketDevice`
		elif path == "clientConnect":
			jsonHash['addr'] = 'ws://' + ip + ':' + `portWebSocketClient`

		self.send_response(200)
		self.end_headers()
		self.wfile.write(json.dumps(jsonHash))
		return

class ServerHTTP(Thread):
	def __init__(self, lettre):
		self.isRun = True
		Thread.__init__(self)
		self.server = HTTPServer((ip, portHTTP), GetHandler)
		print 'HTTP listenning ...'

	def run(self):
		while self.isRun:
			self.server.serve_forever() 

