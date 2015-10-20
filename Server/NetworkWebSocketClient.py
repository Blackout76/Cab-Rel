import sys
reload(sys)
sys.setdefaultencoding('utf-8')
import json
from threading import Thread
from SimpleWebSocketServer import WebSocket, SimpleWebSocketServer
import MapManager
import TaxiManager

clients = []
portWebSocketClient = 2639
server_client = None

class NetworkServerSocketClient(Thread):
	def __init__(self, lettre):
		Thread.__init__(self)
		self.server = SimpleWebSocketServer('', portWebSocketClient, ClientWebSocket)
		print 'WEBSOCKET clients listenning ...'

	def run(self):
		self.server.serveforever()
	
	def broadcastAll(self,json):
		for c in clients:
			c.sendMessage(json.dumps(json,ensure_ascii=False))

class ClientWebSocket(WebSocket):

	def handleMessage(self):
		# echo message back to client
		self.sendMessage(self.data)
		clientMessageHandle = json.loads(self.data)
		print messageHandle
		if messageHandle.keys()[0] == "cabRequest":
			TaxiManager.taxiManager.addCabRequest(messageHandle)
		else:
			print "not a cab requets"
		

	def handleConnected(self):
		clients.append(self)
		#Send map to client
		self.sendMessage(json.dumps(MapManager.mapManager.toDictFormat(),ensure_ascii=False))
		print self.address, 'connected'

	def handleClose(self):
		clients.remove(self)
		print self.address, 'closed'
