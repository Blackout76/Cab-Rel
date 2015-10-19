import json
from pprint import pprint
from Area import *

mapManager = None

##	Manage the map
#
#	More details.
class MapManager:
	##	The constructor
	#	@param name name of the map
	def __init__(self, name):
		#memeber initialisation
		self.mapName = name
		self.areasDict = {}

	##	Load a json file to make the map
	def loadFileMap(self):
		#open file map.json
		jsonMap = open('map.json')
		#convert the json map to a dictionary
		data = json.load(jsonMap)
		self.loadJsonMap(data)
		#close the map.json file
		jsonMap.close()

	##	Load a json to make the map
	#	@param data dictionary witch contain the map
	def loadJsonMap(self, data):
		#take the map in the data dictionary
		mapData = data["areas"]
		#initialise areas
		self.initArea(mapData)

	##	Initialise all Areas
	#	@param Areas
	def initArea(self, areas):
		#for all areas in the list areas
		for newAreaJson in areas:
			#initialise a new area
			newArea = Area(newAreaJson)
			self.areasDict[newAreaJson["name"]] = newArea

	##	Return the map to format json
	def toDictFormat(self):
		#the list of areas
		areas = []
		#for all areas in self.areasDict
		for areasToJson in self.areasDict:
			#format the areas to json
			areas.append(self.areasDict[areasToJson].toDictFormat())
		return areas
