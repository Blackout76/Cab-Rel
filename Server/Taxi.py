from Location import *
from CabRequest import *


class Taxi:
	def __init__(self, taxiInfo, mapManagerTaxi):
		self.odometer = taxiInfo["odometer"]

		if taxiInfo["destination"] != None:
			self.destination = CabRequest(taxiInfo["destination"], mapManagerTaxi)
		else:
			self.destination = None
			
		self.loc_now = Location(taxiInfo["loc_now"], mapManagerTaxi)
		self.loc_prior = Location(taxiInfo["loc_prior"], mapManagerTaxi)

	def toJsonFormat(self):
		print "to do"