from CabRequest import *
from MapManager import *
from Taxi import *
import NetworkWebSocketDevice
import NetworkWebSocketClient
import server

taxiManager = None
##	Manage all Taxis
#
#	More details.
class TaxiManager:
	##	The constructor
	#	@param mapManagerTaxi map manager for know vexteces and streets
	def __init__(self, mapManagerTaxi):
		self.taxiList = []
		self.cabRequestList = []
		self.mapManagerTaxi = mapManagerTaxi

	##	Creat a new taxi
	def newTaxi(self):
		#initialise the new taxi
		newTaxi = {}
		newTaxi["odometer"] = 0
		newTaxi["destination"] = None
		#initialise newTaxi["loc_now"]
		loc_now = {}
		#find the name of the first area in the arelist
		loc_now["area"] = self.mapManagerTaxi.areasDict.values()[0].areaName
		loc_now["locationType"] = "vertex"
		#find the name of the first vertex in the vertices dictionary
		loc_now["location"] = self.mapManagerTaxi.areasDict.values()[0].verticesDict.values()[0].verticeName
		newTaxi["loc_now"] = loc_now
		#initialise newTaxi["loc_prior"]
		loc_prior = {}
		#find the name of the first area in the arelist
		loc_prior["area"] = self.mapManagerTaxi.areasDict.values()[0].areaName
		loc_prior["locationType"] = "vertex"
		#find the name of the first vertex in the vertices dictionary
		loc_prior["location"] = self.mapManagerTaxi.areasDict.values()[0].verticesDict.values()[0].verticeName
		newTaxi["loc_prior"] = loc_prior
		#creat the new taxi
		self.taxiList.append(Taxi(newTaxi, self.mapManagerTaxi))

	##	Find a cab with an id
	#	@param idList id of the cap in the list
	def findTaxiById(self, idList):
		return self.taxiList[idList]

	##	Creat a new cab request
	#	@param request cab request to add
	def addCabRequest(self, request):
		#take the cab request in the json
		newCabRequest = request["cabRequest"]
		#intance the new cab request
		newCabRequest = CabRequest(newCabRequest, self.mapManagerTaxi)
		self.cabRequestList.append(newCabRequest)

	##	Return the taxi list to dictionary format
	def toDictFormatTaxiList(self):
		#return juste the first taxi
		#because we need to finich with one taxi before
		taxis = {}
		taxis["cabInfo"] = self.taxiList[0].toDictFormat()
		return taxis

	##	Return the CabRequest list to dictionary format
	def toDictFormatCabRequest(self):
		#initialise the liste of cab request not accept
		cabRequests = []
		cabQueue = {}
		#for each request in the request list add the json of the request
		for request in self.cabRequestList:
			cabRequests.append(request.toDictFormat())
		cabQueue["cabQueue"] = cabRequests
		return cabQueue

	def onRequestAnswer(self, answer):
		if answer == "yes":
			print "mod taxi info and send it"
			if self.taxiList[0].destination == None:
				self.taxiList[0].destination = self.cabRequestList[0] 
				cabInfoJson = self.toDictFormatCabRequest()
				NetworkWebSocketClient.server_client.broadcastAll(cabInfoJson)
				NetworkWebSocketDevice.server_device.broadcastAll(cabInfoJson)
		else:
			print "del cab request and resend cabrequestslist"
			self.cabRequestList.pop(0)
			cabQueueJson = json.dumps(self.toDictFormatCabRequest)
			NetworkWebSocketClient.server_client.broadcastAll(cabQueueJson)
			NetworkWebSocketDevice.server_device.broadcastAll(cabQueueJson)

	def onCabInfo(self):
		cabInfoJson = self.toDictFormatTaxiList()
		NetworkWebSocketClient.server_client.broadcastAll(cabInfoJson)
		NetworkWebSocketDevice.server_device.broadcastAll(cabInfoJson)


