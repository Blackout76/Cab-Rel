import json
from threading import Thread
from SimpleWebSocketServer import WebSocket, SimpleWebSocketServer
import urlparse
import sys

clients = []

class NetworkServerSocketClient(Thread):
	def __init__(self, lettre, port):
		Thread.__init__(self)
		self.server = SimpleWebSocketServer('', port, ClientWebSocket)
		print 'WEBSOCKET clients listenning ...'

	def run(self):
		self.server.serveforever()

class ClientWebSocket(WebSocket):

    def handleMessage(self):
        # echo message back to client
        self.sendMessage(self.data)

    def handleConnected(self):
		clients.append(self)
        print self.address, 'connected'

    def handleClose(self):
		clients.remove(self)
        print self.address, 'closed'
