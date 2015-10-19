from MapManager import *
from server import *
from TestMapManager import *

class Location:
	##	The constructor
	#	@param locationData dictionary with the location datas
	#	@param mapManagerTaxi map manager for know vexteces and streets
	def __init__(self, locationData, mapManagerTaxi):
		#take the instance of the area with the name
		self.areaLocation = mapManagerTaxi.areasDict[locationData["area"]]
		self.locationType = locationData["locationType"]
		#if the type of the location is a vertex
		if self.locationType == "vertex":
			#take the instance of the vertex in the json in the vertices list
			self.location = mapManagerTaxi.areasDict[locationData["area"]].verticesDict[locationData["location"]]
			self.vertexFrom = None
			self.vertexTo =  None
			self.progression = 0
			self.street = None
		else:
			location = locationData["location"]
			self.location = None
			#take the instance of the vertex "from" in the json in the vertices list
			self.vertexFrom = mapManagerTaxi.areasDict[locationData["area"]].verticesDict[location["from"]]
			#take the instance of the vertex "to" in the json in the vertices list
			self.vertexTo = mapManagerTaxi.areasDict[locationData["area"]].verticesDict[location["to"]]
			self.progression = location["progression"]
			#take the instance of the street in the json in the streets list
			self.street = mapManagerTaxi.areasDict[locationData["area"]].streetsDict[location["name"]]

	##	Return the Location to dictionary format
	def toDictFormat(self):
		#initialise the locationn dictionary format
		location = {}
		#take the name of the area
		location["area"] = self.areaLocation.areaName
		location["locationType"] = self.locationType
		#if the location is a vertex esle it's a street
		if self.locationType == "vertex":
			#take the name of the vertex
			location["location"] = self.location.verticeName
		else:
			#initialisation of location["location"]
			locationSeg = {}
			#take the name of the vertex "from"
			locationSeg["from"] = self.vertexFrom.verticeName
			#take the name of the vertex "to"
			locationSeg["to"] = self.vertexTo.verticeTo.verticeName
			locationSeg["progression"] = self.progression
			#take the name of the street
			locationSeg["name"] = self.street.streetName
			location["location"] = locationSeg
		return location
