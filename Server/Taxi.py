from Location import *
from CabRequest import *


class Taxi:
	def __init__(self, taxiInfo):
		self.odometer = taxiInfo["odometer"]
		self.destination = CabRequest(taxiInfo["destination"])
		self.loc_now = Location(taxiInfo["loc_now"])
		self.loc_prior = Location(taxiInfo["loc_prior"])

	def toJsonFormat(self):
		print "to do"