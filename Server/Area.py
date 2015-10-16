import json
from Vertice import *
from Street import *
from Bridge import *

##	An area of the map
#
#	More details.
class Area:
	##	The constructor
	#	@param areaJson dictionary of an area
	def __init__(self, areaJson):
		#name of the area
		self.areaName = areaJson["name"]
		#
		mapComposition = areaJson["map"]
		#initialise the weight of the area
		weight = mapComposition["weight"]
		self.weightWidth =  weight["w"]
		self.weightHeight = weight["h"]
		#vertices in the area
		self.verticesDict = {}
		#street in the area
		self.streetsDict = {}
		#bridge in the area
		self.bridgesList = []
		self.initVertice(mapComposition["vertices"])
		self.initStreet(mapComposition["streets"])
		self.initBridge(mapComposition["bridges"])

	##	Initialise vertices class
	#	@param vertices list of vertices dictionarys
	def initVertice(self, vertices):
		#creat a new vertex for each vetex in the list
		for newVerticeJson in vertices:
			newVertice = Vertice(newVerticeJson)
			self.verticesDict[newVerticeJson["name"]] = newVertice

	##	Initialise streets class
	#	@param streets list of streets dictionarys
	def initStreet(self, streets):
		#creat a new street for each street in the list
		for newStreetJson in streets:
			verticeList = []
			#take street points to the point dictionary of the area
			for vertice in newStreetJson["path"]:
				verticeList.append(self.verticesDict[vertice])
			#creat a new street instance and save it in the street dictionary
			newStreet = Street(newStreetJson["name"], verticeList, newStreetJson["oneway"])
			self.streetsDict[newStreetJson["name"]] = newStreet

	##	Initialise bridges class
	#	@param bridges list of bridges dictionarys
	def initBridge(self, bridges):
		#creat a new bridge for each bridge in the list
		for newBridgeJson in bridges:
			newBridge = Bridge(self.verticesDict[newBridgeJson["from"]],newBridgeJson)
			self.bridgesList.append(newBridge)

	##	Return the area in a dictionary
	def toDictFormat(self):
		#initialise the area dictionary
		area = {}
		area["name"] = self.areaName
		#initialise area["map"]
		mapComposition = {}
		#initialise weight
		weight = {}
		weight["w"] = self.weightWidth
		weight["h"] = self.weightHeight
		mapComposition["weight"] = weight
		#initialise the list of vertices for the area dictionary
		vertices = []
		for vertexToJson in self.verticesDict:
			vertices.append(self.verticesDict[vertexToJson].toDictFormat())
		mapComposition["vertices"] = vertices
		#initialise the list of streets for the area dictionary
		streets = []
		for streetsToJson in self.streetsDict:
			streets.append(self.streetsDict[streetsToJson].toDictFormat())
		mapComposition["streets"] = streets
		#initialise the list of bridges for the area dictionary
		bridges = []
		for brigesToJson in self.bridgesList:
			bridges.append(brigesToJson.toDictFormat())
		mapComposition["bridges"] = bridges

		area["map"] = mapComposition
		return area


