import json
from threading import Thread
from SimpleWebSocketServer import WebSocket, SimpleWebSocketServer
import urlparse
import sys

devices = []

class NetworkServerSocketDevice(Thread):
	def __init__(self, lettre, port):
		Thread.__init__(self)
		self.server = SimpleWebSocketServer('', port, DeviceWebSocket)
		print 'WEBSOCKET devices listenning ...'

	def run(self):
		self.server.serveforever() 

class DeviceWebSocket(WebSocket):

    def handleMessage(self):
        # echo message back to client
        self.sendMessage(self.data)

    def handleConnected(self):
		devices.append(self)
        print self.address, 'connected'

    def handleClose(self):
		devices.remove(self)
        print self.address, 'closed'
