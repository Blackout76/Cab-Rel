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
		for newStreetJson in streets:
			verticeList = []
			for vertice in newStreetJson["path"]:
				verticeList.append(self.verticesDict[vertice])

			newStreet = Street(newStreetJson["name"], verticeList, newStreetJson["oneway"])
			self.streetsDict[newStreetJson["name"]] = newStreet

	def initBridge(self, bridges):
		for newBridgeJson in bridges:
			newBridge = Bridge(self.verticesDict[newBridgeJson["from"]],newBridgeJson)
			self.bridgesList.append(newBridge)

	def ToJsonFormat(self):
		area = {}
		area["name"] = self.areaName
		mapComposition = {}
		weight = {}
		weight["w"] = self.weightWidth
		weight["h"] = self.weightHeight
		mapComposition["weight"] = weight

		vertices = []
		for vertexToJson in self.verticesDict:
			vertices.append(self.verticesDict[vertexToJson].ToJsonFormat())
		mapComposition["vertices"] = vertices

		streets = []
		for streetsToJson in self.streetsDict:
			streets.append(self.streetsDict[streetsToJson].ToJsonFormat())
		mapComposition["streets"] = streets
		
		bridges = []
		for brigesToJson in self.bridgesList:
			bridges.append(brigesToJson.ToJsonFormat())
		mapComposition["bridges"] = bridges

		area["map"] = mapComposition
		return area


