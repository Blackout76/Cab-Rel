import json
from pprint import pprint
from Area import *




class MapManager:
	def __init__(self, name):
		self.mapName = name
		self.areasDict = {}

	def LoadFileMap(self):
		jsonMap = open('map.json')
		data = json.load(jsonMap)
		self.LoadJsonMap(data)
		jsonMap.close()

	def LoadJsonMap(self, data):
		mapData = data["areas"]
		self.initArea(mapData)

	def initArea(self, Areas):
		for newAreaJson in Areas:
			newArea = Area(newAreaJson)
			self.areasDict[newAreaJson["name"]] = newArea

	def ToJsonFormat(self):
		areas = []
		for areasToJson in self.areasDict:
			 areas.append(self.areasDict[areasToJson].ToJsonFormat())
		print json.dumps(areas)
		return json.dumps(areas)
