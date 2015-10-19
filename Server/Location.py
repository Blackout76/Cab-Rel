from MapManager import *
from server import *
from TestMapManager import *

class Location:
	def __init__(self, locationData, mapManagerTaxi):
		self.areaLocation = mapManagerTaxi.areasDict[locationData["area"]]
		self.locationType = locationData["locationType"]
		if self.locationType == "vertex":
			self.location = mapManagerTaxi.areasDict[locationData["area"]].verticesDict[locationData["location"]]
			self.vertexFrom = None
			self.vertexTo =  None
			self.progression = 0
		else:
			location = locationData["location"]
			self.vertexFrom = mapManagerTaxi.areasDict[locationData["area"]].verticesDict[location["from"]]
			self.vertexTo = mapManagerTaxi.areasDict[locationData["area"]].verticesDict[location["to"]]
			self.progression = location["progression"]

	def toDictFormat(self):
		location = {}
		location["area"] = self.areaLocation.areaName
		location["locationType"] = self.locationType
		if self.locationType == "vertex":
			location["location"] = self.location.verticeName
		else:
			locationSeg = {}
			locationSeg["from"] = self.vertexFrom.verticeName
			locationSeg["to"] = self.vertexTo.verticeTo.verticeName
			locationSeg["progression"] = self.progression
			location["location"] = locationSeg

		return location
