from CabRequest import *
from MapManager import *
from Taxi import *



class TaxiManager:
	def __init__(self, mapManagerTaxi):
		self.taxiList = []
		self.cabRequestDict = {}
		self.mapManagerTaxi = mapManagerTaxi

	def LoadFileTaxi(self):
		jsonTaxi = open('map.json')
		data = json.load(jsonTaxi)
		self.LoadJsonTaxi(data)
		jsonTaxi.close()

	def LoadJsonTaxi(self, data):
		taxiData = data["cabInfo"]
		for taxi in taxiData["cabs"]:
			addTaxi(taxi)
		initCabRequest(taxiData["cabQueue"])

	def newTaxi(self):
		newTaxi = {}
		newTaxi["odometer"] = 0
		newTaxi["destination"] = None

		loc_now = {}
		loc_now["area"] = self.mapManagerTaxi.areasDict.values()[0].areaName
		loc_now["locationType"] = "vertex"
		loc_now["area"] = self.mapManagerTaxi.areasDict.values()[0].verticesDict.values()[0].verticeName
		newTaxi["loc_now"] = loc_now

		loc_prior = {}
		loc_now["area"] = self.mapManagerTaxi.areasDict["Quartier Nord"].areaName
		loc_prior["locationType"] = "vertex"
		newTaxi["loc_prior"] = loc_prior

		print newTaxi
		self.taxiList.append(Taxi(newTaxi))

	def findCabById(self):
		print "to do"


	def addCabRequest(self, requestQueue):
		print "to do"