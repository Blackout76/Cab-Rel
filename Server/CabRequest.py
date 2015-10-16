from MapManager import *
from Location import *
from server import *
from TestMapManager import *


class CabRequest:
	def __init__(self, requestInfo, mapManagerTaxi):
		self.areaRequest = mapLoaded.areasDict[requestInfo["area"]]
		self.locationRequest = Location(requestInfo["location"], mapManagerTaxi)
		print self.areaRequest
		print self.locationRequest

	def toDictFormat(self):
		print "to do"