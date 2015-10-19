from CabRequest import *
from MapManager import *
from Taxi import *



class TaxiManager:
	def __init__(self, mapManagerTaxi):
		self.taxiList = []
		self.cabRequestDict = {}
		self.mapManagerTaxi = mapManagerTaxi

	def loadFileTaxi(self):
		jsonTaxi = open('map.json')
		data = json.load(jsonTaxi)
		self.LoadJsonTaxi(data)
		jsonTaxi.close()

	def loadJsonTaxi(self, data):
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
		loc_now["location"] = self.mapManagerTaxi.areasDict.values()[0].verticesDict.values()[0].verticeName
		newTaxi["loc_now"] = loc_now

		loc_prior = {}
		loc_prior["area"] = self.mapManagerTaxi.areasDict.values()[0].areaName
		loc_prior["locationType"] = "vertex"
		loc_prior["location"] = self.mapManagerTaxi.areasDict.values()[0].verticesDict.values()[0].verticeName
		newTaxi["loc_prior"] = loc_prior
		
		self.taxiList.append(Taxi(newTaxi, self.mapManagerTaxi))

	def clearfindCabById(self, idList):
		return self.taxiList[idList]

	def addCabRequest(self, requestQueue):
		print "to do"