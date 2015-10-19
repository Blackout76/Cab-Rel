from MapManager import *
from Location import *
from server import *
from TestMapManager import *


##	Manage the map
#
#	More details.
class CabRequest:
	##	The constructor
	#	@param requestInfo dictinary with datas about the request
	#	@param mapManagerTaxi map manager for know vexteces and streets
	def __init__(self, requestInfo, mapManagerTaxi):
		self.areaRequest = mapLoaded.areasDict[requestInfo["area"]]
		self.locationRequest = Location(requestInfo["location"], mapManagerTaxi)

	##	Return the CabRequest to dictionary format
	def toDictFormat(self):
		#initialise the cab request dictionary
		cabRequest = {}
		cabRequest["area"] = self.areaRequest
		cabRequest["location"] = self.locationRequest.toDictFormat()
		return cabRequest