from CabRequest import *
from MapManager import *
from Taxi import *
import NetworkWebSocket
import server
import TaxiMove
from math import sqrt

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
		
		loc_now["locationType"] = "vertex"
		#find the name of the first vertex in the vertices dictionary
		loc_now["location"] = self.mapManagerTaxi.areasDict.values()[0].verticesDict.values()[0].verticeName
		
		
		"""
		#Start taxi in middle street
		loc_now["locationType"] = "street"
		loacationInStreet = {}
		loacationInStreet["from"] = self.mapManagerTaxi.areasDict.values()[0].streetsDict.values()[0].streetVertices[0].verticeName
		loacationInStreet["to"] = self.mapManagerTaxi.areasDict.values()[0].streetsDict.values()[0].streetVertices[1].verticeName
		loacationInStreet["progression"] = 0.5
		
		loc_now["location"] = loacationInStreet
		"""
		
		
		
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
		cabRequest = CabRequest(newCabRequest, self.mapManagerTaxi)
		self.cabRequestList.append(cabRequest)
		print 'New cab request created!'
		# Olivier: Auto broadcast on new cab request
		cabQueueJson = self.toDictFormatCabRequest()
		NetworkWebSocket.server_WEBSOCKET.broadcastAll(cabQueueJson)

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

	##	On answer request received to the device
	#	@param answer answer return by the device on a request
	def onRequestAnswer(self, answer):
		if len(self.cabRequestList) > 0:
			if answer == "yes":
				print 'destination '
				print self.taxiList[0].destination
				if self.taxiList[0].destination == None:
					self.taxiList[0].destination = self.cabRequestList[0]
					cabInfoJson = self.toDictFormatCabRequest()
					NetworkWebSocket.server_WEBSOCKET.broadcastAll(cabInfoJson)
					print 'gne'
					self.startMove()
					self.cabRequestList.pop(0)
			else:
				self.cabRequestList.pop(0)
				NetworkWebSocket.server_WEBSOCKET.broadcastAll(self.toDictFormatCabRequest())
				print 'b'
		else:
			NetworkWebSocket.server_WEBSOCKET.broadcastAll(self.toDictFormatCabRequest())

	def onCabInfo(self):
		cabInfoJson = self.toDictFormatTaxiList()
		#print cabInfoJson
		NetworkWebSocket.server_WEBSOCKET.broadcastAll(cabInfoJson)

	def getCabInfo(self):
		return self.toDictFormatTaxiList()
	
	##	Find the lowest path between 2 point
	def startMove(self):
		#initialise the distance between the taxi and his two nerest point
		distTaxiToVertexFrom = 0.0
		distTaxiToVertexTo = 0.0
		#initialise the distance between the finish and his two nerest point
		distFinishToVertexFrom = 0.0
		distFinishToVertexTo = 0.0
		path = []
		#if the taxi is on a street, calculate the distance between the taxi and each street point
		if self.taxiList[0].destination.locationRequest.locationType == "street":
			#calculate the taxi x
			taxiX = ((1-self.taxiList[0].destination.locationRequest.progression) * self.taxiList[0].destination.locationRequest.vertexFrom.verticeX + self.taxiList[0].destination.locationRequest.progression * self.taxiList[0].destination.locationRequest.vertexTo.verticeX)
			#calculate the taxi y
			taxiY = ((1-self.taxiList[0].destination.locationRequest.progression) * self.taxiList[0].destination.locationRequest.vertexFrom.verticeY + self.taxiList[0].destination.locationRequest.progression * self.taxiList[0].destination.locationRequest.vertexTo.verticeY)
			#calulate the distance between the taxi and the from point from on the street
			distTaxiToVertexFrom = sqrt((taxiX-self.taxiList[0].destination.locationRequest.vertexFrom.verticeX)*(taxiX-self.taxiList[0].destination.locationRequest.vertexFrom.verticeX)+(taxiY-self.taxiList[0].destination.locationRequest.vertexFrom.verticeY)*(taxiY-self.taxiList[0].destination.locationRequest.vertexFrom.verticeY))
			#calulate the distance between the taxi and the from point to on the street
			distTaxiToVertexTo = sqrt((self.taxiList[0].destination.locationRequest.vertexTo.verticeX-taxiX)*(self.taxiList[0].destination.locationRequest.vertexTo.verticeX-taxiX)+(self.taxiList[0].destination.locationRequest.vertexTo.verticeY-taxiY)*(self.taxiList[0].destination.locationRequest.vertexTo.verticeY-taxiY))

		#if the finish is on a street, calculate the distance between the taxi and each street point
		if self.taxiList[0].loc_now.locationType == "street":
			#calculate the finish x
			finishX = ((1-self.taxiList[0].loc_now.progression) * self.taxiList[0].loc_now.vertexFrom.verticeX + self.taxiList[0].loc_now.progression * self.taxiList[0].loc_now.vertexTo.verticeX)
			#calculate the finish y
			finishY = ((1-self.taxiList[0].loc_now.progression) * self.taxiList[0].loc_now.vertexFrom.verticeY + self.taxiList[0].loc_now.progression * self.taxiList[0].loc_now.vertexTo.verticeY)
			#calulate the distance between the finish and the from point from on the street
			distFinishToVertexFrom = sqrt((self.taxiList[0].loc_now.vertexFrom.verticeX-finishX)*(self.taxiList[0].loc_now.vertexFrom.verticeX-finishX)+(self.taxiList[0].loc_now.vertexFrom.verticeY-finishY)*(self.taxiList[0].loc_now.vertexFrom.verticeY-finishY))
			#calulate the distance between the finish and the from point to on the street
			distFinishToVertexTo = sqrt((finishX-self.taxiList[0].loc_now.vertexTo.verticeX)*(finishX-self.taxiList[0].loc_now.vertexTo.verticeX)+(finishY-self.taxiList[0].loc_now.vertexTo.verticeY)*(finishY-self.taxiList[0].loc_now.vertexTo.verticeY))

		#if the taxi is on a street		
		if self.taxiList[0].destination.locationRequest.locationType == "street":
			#if the finish is on a street select the best dijkstra of four
			if self.taxiList[0].loc_now.locationType == "street":
				tmpPath = []
				endPoint = {}
				startPoint = {}
				endPoint["area"] = self.taxiList[0].destination.locationRequest.areaLocation.areaName
				endPoint["vertex"] = self.taxiList[0].destination.locationRequest.vertexFrom.verticeName
				startPoint["area"] = self.taxiList[0].loc_now.areaLocation.areaName
				startPoint["vertex"] = self.taxiList[0].loc_now.vertexFrom.verticeName
				path = self.mapManagerTaxi.dijkstraTree.findShortestPath(startPoint, endPoint)
				for item in path:
					tmpPath.append(item)
					tmpPath[0]["totalWeight"] = tmpPath[0]["totalWeight"] + distTaxiToVertexFrom + distFinishToVertexFrom

				tmpPath =[]
				endPoint["area"] = self.taxiList[0].destination.locationRequest.areaLocation.areaName
				endPoint["vertex"] = self.taxiList[0].destination.locationRequest.vertexFrom.verticeName
				startPoint["area"] = self.taxiList[0].loc_now.areaLocation.areaName
				startPoint["vertex"] = self.taxiList[0].loc_now.vertexTo.verticeName
				path = self.mapManagerTaxi.dijkstraTree.findShortestPath(startPoint, endPoint)
				path[0]["totalWeight"] = path[0]["totalWeight"] + distTaxiToVertexTo + distFinishToVertexFrom
				if path[0] < tmpPath[0]:
					for item in path:
						tmpPath.append(item)

				endPoint["area"] = self.taxiList[0].destination.locationRequest.areaLocation.areaName
				endPoint["vertex"] = self.taxiList[0].destination.locationRequest.vertexTo.verticeName
				startPoint["area"] = self.taxiList[0].loc_now.areaLocation.areaName
				startPoint["vertex"] = self.taxiList[0].loc_now.vertexFrom.verticeName
				path = self.mapManagerTaxi.dijkstraTree.findShortestPath(startPoint, endPoint)
				path[0]["totalWeight"] = path[0]["totalWeight"] + distTaxiToVertexFrom + distFinishToVertexTo
				if path[0] < tmpPath[0]:
					tmpPath =[]
					for item in path:
						tmpPath.append(item)

				endPoint["area"] = self.taxiList[0].destination.locationRequest.areaLocation.areaName
				endPoint["vertex"] = self.taxiList[0].destination.locationRequest.vertexTo.verticeName
				startPoint["area"] = self.taxiList[0].loc_now.areaLocation.areaName
				startPoint["vertex"] = self.taxiList[0].loc_now.vertexTo.verticeName
				path = self.mapManagerTaxi.dijkstraTree.findShortestPath(startPoint, endPoint)
				path[0]["totalWeight"] = path[0]["totalWeight"] + distTaxiToVertexTo + distFinishToVertexTo
				if path[0] < tmpPath[0]:
					tmpPath =[]
					for item in path:
						tmpPath.append(item)

				path = tmpPath
				path.pop(0)
			#if the finish is on a vertex select the best dijkstra of two
			else:
				tmpPath = []
				endPoint = {}
				startPoint = {}
				endPoint["area"] = self.taxiList[0].destination.locationRequest.areaLocation.areaName
				endPoint["vertex"] = self.taxiList[0].destination.locationRequest.vertexFrom.verticeName
				startPoint["area"] = self.taxiList[0].loc_now.areaLocation.areaName
				startPoint["vertex"] = self.taxiList[0].loc_now.location.verticeName
				path = self.mapManagerTaxi.dijkstraTree.findShortestPath(startPoint, endPoint)
				for item in path:
					tmpPath.append(item)
					tmpPath[0]["totalWeight"] = tmpPath[0]["totalWeight"] + distTaxiToVertexFrom + distFinishToVertexFrom

				endPoint["area"] = self.taxiList[0].destination.locationRequest.areaLocation.areaName
				endPoint["vertex"] = self.taxiList[0].destination.locationRequest.vertexTo.verticeName
				path = self.mapManagerTaxi.dijkstraTree.findShortestPath(startPoint, endPoint)
				path[0]["totalWeight"] = path[0]["totalWeight"] + distTaxiToVertexFrom + distFinishToVertexTo
				if path[0] < tmpPath[0]:
					tmpPath =[]
					for item in path:
						tmpPath.append(item)
				path = tmpPath
				path.pop(0)
		#if the taxi is on a vertex select the best dijkstra of two
		else:
			#if the finish is on a street
			if self.taxiList[0].loc_now.locationType == "street":
				tmpPath = []
				endPoint = {}
				startPoint = {}
				endPoint["area"] = self.taxiList[0].destination.locationRequest.areaLocation.areaName
				endPoint["vertex"] = self.taxiList[0].destination.locationRequest.location.verticeName
				startPoint["area"] = self.taxiList[0].loc_now.areaLocation.areaName
				startPoint["vertex"] = self.taxiList[0].loc_now.vertexFrom.verticeName
				path = self.mapManagerTaxi.dijkstraTree.findShortestPath(startPoint, endPoint)
				for item in path:
					tmpPath.append(item)
					tmpPath[0]["totalWeight"] = tmpPath[0]["totalWeight"] + distTaxiToVertexFrom + distFinishToVertexFrom

				startPoint["area"] = self.taxiList[0].loc_now.areaLocation.areaName
				startPoint["vertex"] = self.taxiList[0].loc_now.vertexTo.verticeName
				path = self.mapManagerTaxi.dijkstraTree.findShortestPath(startPoint, endPoint)
				path[0]["totalWeight"] = path[0]["totalWeight"] + distTaxiToVertexTo + distFinishToVertexFrom
				if path[0] < tmpPath[0]:
					tmpPath =[]
					for item in path:
						tmpPath.append(item)
				path = tmpPath
				path.pop(0)
			#if the finish is on a vertex
			else:
				endPoint = {}
				startPoint = {}
				endPoint["area"] = self.taxiList[0].destination.locationRequest.areaLocation.areaName
				endPoint["vertex"] = self.taxiList[0].destination.locationRequest.location.verticeName
				startPoint["area"] = self.taxiList[0].loc_now.areaLocation.areaName
				startPoint["vertex"] = self.taxiList[0].loc_now.location.verticeName
				path = self.mapManagerTaxi.dijkstraTree.findShortestPath(startPoint, endPoint)
				path.pop(0)
		#start the thread to start the taxi mouvement
		TaxiMove.taxiThread = TaxiMove.TaxiThread("0",path)
		#start the taxi mouvement on the client
		TaxiMove.taxiThread.start()
		return path