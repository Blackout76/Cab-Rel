from MapManager import *
from server import *
from TestMapManager import *



class Location:
	def __init__(self, locationData, mapManagerTaxi):
		self.areaLocation = mapManagerTaxi.areasDict[locationData["area"]]
		if locationData["locationType"] == "vertex":
			self.location = mapManagerTaxi.areasDict[locationData["area"]].verticesDict[locationData["location"]]
		else:
			location = locationData["location"]
			self.vertexFrom = mapManagerTaxi.areasDict[locationData["area"]].verticesDict[location["from"]]
			self.vertexTo = mapManagerTaxi.areasDict[locationData["area"]].verticesDict[location["to"]]
			self.progression = location["progression"]

	def toDictFormat(self):
		print "TODO"