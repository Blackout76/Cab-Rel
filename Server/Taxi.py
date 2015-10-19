from Location import *
from CabRequest import *


class Taxi:
	##	The constructor
	#	@param taxiInfo dictinary with the Taxi datas
	#	@param mapManagerTaxi map manager for know vexteces and streets
	def __init__(self, taxiInfo, mapManagerTaxi):
		self.odometer = taxiInfo["odometer"]
		#determine if the taxi have alreay accept a request
		if taxiInfo["destination"] != None:
			self.destination = CabRequest(taxiInfo["destination"], mapManagerTaxi)
		else:
			self.destination = None
		#initialise locations
		self.loc_now = Location(taxiInfo["loc_now"], mapManagerTaxi)
		self.loc_prior = Location(taxiInfo["loc_prior"], mapManagerTaxi)

	##	Return the Taxi list to dictionary format
	def toDictFormat(self):
		#initialise the taxi dictionary
		taxi = {}
		taxi["odometer"] = self.odometer
		#determine if the taxi have alreay accept a request
		if self.destination == None:
			taxi["destination"] = None
		else:
			taxi["destination"] = self.destination.toDictFormat()
		taxi["loc_now"] = self.loc_now.toDictFormat()
		taxi["loc_prior"] = self.loc_prior.toDictFormat()
		return taxi