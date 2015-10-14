#! /usr/bin/env python


import json
from pprint import pprint




class MapManager:
	def __init__(self, name):
		self.mapName = name



	def LoadFileMap(self):
		jsonMap = open('map.json')
		data = json.load(jsonMap)
		print type(data)
		pprint(data)
		self.LoadJsonMap(data)
		jsonMap.close()

	def LoadJsonMap(self, data):
		print "LoadJsonMap"