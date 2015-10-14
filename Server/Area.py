import json
from Vertice import *
from Street import *
from Bridge import *


class Area:
	def __init__(self, areaJson):
		self.areaName = areaJson["name"]
		mapComposition = areaJson["map"]
		weight = mapComposition["weight"]
		self.weightWidth =  weight["w"]
		self.weightHeight = weight["h"]
		self.verticesDict = {}
		self.streetsDict = {}
		self.bridgesList = []
		self.initVertice(mapComposition["vertices"])
		self.initStreet(mapComposition["streets"])
		self.initBridge(mapComposition["bridges"])


	def initVertice(self, vertices):
		for newVerticeJson in vertices:
			newVertice = Vertice(newVerticeJson)
			self.verticesDict[newVerticeJson["name"]] = newVertice

	def initStreet(self, streets):
		print "initStreet"
		for newStreetJson in streets:
			verticeList = []
			for vertice in newStreetJson["path"]:
				print self.verticesDict
				verticeList.append(self.verticesDict[vertice])
				print vertice

			newStreet = Street(newStreetJson["name"], verticeList, newStreetJson["oneway"])
			self.streetsDict[newStreetJson["name"]] = newStreet

	def initBridge(self, bridges):
		for newBridgeJson in bridges:
			newBridge = Bridge(newBridgeJson["from"], newBridgeJson["weight"])
			self.bridgesList.append(newBridge)

