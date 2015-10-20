import sys
reload(sys)
sys.setdefaultencoding('utf-8')
import json
from threading import Thread
from SimpleWebSocketServer import WebSocket, SimpleWebSocketServer
import urlparse
import TaxiManager

devices = []
portWebSocketDevice = 2589
server_device = None

class NetworkServerSocketDevice(Thread):
	def __init__(self, lettre):
		Thread.__init__(self)
		self.server = SimpleWebSocketServer('', portWebSocketDevice, DeviceWebSocket)
		print 'WEBSOCKET devices listenning ...'

	def run(self):
		self.server.serveforever()

	def broadcastAll(self,jsonMessage):
		for d in devices:
			d.sendMessage(json.dumps(jsonMessage,ensure_ascii=False))

class DeviceWebSocket(WebSocket):

	def handleMessage(self):
		# echo message back to client
		deviceMessageHandle = json.loads(self.data)
		if deviceMessageHandle["cmd"] == "requestAnswer":
			print "requestAnswer"
			TaxiManager.taxiManager.onRequestAnswer(deviceMessageHandle["answer"])
		elif deviceMessageHandle["cmd"] == "cabInfo":
			print "cabInfo"
			TaxiManager.taxiManager.onCabInfo()
			

	def handleConnected(self):
		devices.append(self)
		TaxiManager.taxiManager.newTaxi()
		print self.address, 'connected'

	def handleClose(self):
		devices.remove(self)
		print self.address, 'closed'
