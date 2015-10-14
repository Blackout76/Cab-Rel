import json
from pprint import pprint
from Area import *




class MapManager:
	def __init__(self, name):
		self.mapName = name
		self.dictArea = {}



	def LoadFileMap(self):
		jsonMap = open('map.json')
		data = json.load(jsonMap)
		self.LoadJsonMap(data)
		jsonMap.close()

	def LoadJsonMap(self, data):
		print "LoadJsonMap"
		self.initArea(data)



	def initArea(self, Areas):
		for newAreaJson in Areas["areas"]:
			newArea = Area(newAreaJson)
			self.dictArea[newAreaJson["name"]] = newArea