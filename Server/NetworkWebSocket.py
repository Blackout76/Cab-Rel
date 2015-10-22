import sys
reload(sys)
sys.setdefaultencoding('utf-8')
import json
from threading import Thread
from SimpleWebSocketServer import WebSocket, SimpleWebSocketServer
import MapManager
import TaxiManager

clients = []
portWebSocket = 666
server_WEBSOCKET = None

class NetworkServerSocket(Thread):
	def __init__(self, lettre):
		Thread.__init__(self)
		self.server = SimpleWebSocketServer('', portWebSocket, ClientWebSocket)
		print 'WEBSOCKET listenning ...'

	def run(self):
		self.server.serveforever()
	
	def broadcastAll(self,jsonMessage):
		for c in clients:
			c.sendMessage(json.dumps(jsonMessage,ensure_ascii=False))

class ClientWebSocket(WebSocket):

	def handleMessage(self):
		# echo message back to client
		print self.data
		clientMessageHandle = json.loads(self.data)
		#print clientMessageHandle
		if clientMessageHandle.keys()[0] == "cabRequest":
			TaxiManager.taxiManager.addCabRequest(clientMessageHandle)
		elif clientMessageHandle["cmd"] == "requestAnswer":
			TaxiManager.taxiManager.onRequestAnswer(clientMessageHandle["answer"])
		elif clientMessageHandle["cmd"] == "cabInfo":
			TaxiManager.taxiManager.onCabInfo()
		

	def handleConnected(self):
		clients.append(self)
		#Send map to client
		self.sendMessage(json.dumps(MapManager.mapManager.toDictFormat(),ensure_ascii=False))
		self.sendMessage(json.dumps(TaxiManager.taxiManager.getCabInfo(),ensure_ascii=False))
		self.sendMessage(json.dumps(TaxiManager.taxiManager.toDictFormatCabRequest(),ensure_ascii=False))
		print self.address, 'connected'

	def handleClose(self):
		clients.remove(self)
		print self.address, 'closed'
