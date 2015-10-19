from MapManager import *
from Location import *
from server import *
from TestMapManager import *


class CabRequest:
	def __init__(self, requestInfo, mapManagerTaxi):
		self.areaRequest = mapLoaded.areasDict[requestInfo["area"]]
		self.locationRequest = Location(requestInfo["location"], mapManagerTaxi)

	def toDictFormat(self):
		cabRequest = {}
		cabRequest["area"] = self.areaRequest
		cabRequest["location"] = self.location