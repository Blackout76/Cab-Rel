from threading import Thread
import NetworkWebSocket
import NetworkHTTP
import MapManager
import TaxiManager
import threading
import json
import urlparse
import sys
import time
reload(sys)
sys.setdefaultencoding('utf-8')

taxiThread = None

class TaxiThread(Thread):
	def __init__(self, lettre, path):
		Thread.__init__(self)
		self.startLocation = TaxiManager.taxiManager.toDictFormatTaxiList()["cabInfo"]["loc_now"]
		self.endlocation = TaxiManager.taxiManager.toDictFormatTaxiList()["cabInfo"]["destination"]
		self.arrived = False
		self.path = path
		self.progression = 0
		self.increment = 0.05
		
		print self.startLocation
		if self.startLocation["locationType"] == "street":
			self.startLocationPath = {}
			print self.startLocation["location"]
			if self.startLocation["location"]["from"] == self.path[0]["vertex"]:
				self.startLocationPath["vertex"] = self.startLocation["location"]["to"] 
			else:
				self.startLocationPath["vertex"] = self.startLocation["location"]["from"] 
			self.startLocationPath["area"] = self.startLocation["area"] 
			self.progression = self.startLocation["location"]["progression"]
			path.insert(0,self.startLocationPath)
			
		print path
		print 'Thread cab move runing ...'

	def run(self):
		while not self.arrived:
			time.sleep(0.1)
			if len(self.path) == 1 :
				if self.endlocation["location"]["locationType"] == "vertex" and self.endlocation["location"]["location"] == self.path[len(self.path)-1]["vertex"]:
					print 'Cab arrived !'
					self.arrived = True
				elif self.endlocation["location"]["locationType"] == "street" and self.endlocation["location"]["location"]["progression"] <= self.progression:
					print 'Cab arrived !'
					self.arrived = True
			self.executeTick()
			
	def executeTick (self):
		
		if self.progression >= 1:
			self.progression = 0
			self.path.pop(0)
			print 'Cab changed street !'
			
		self.computeCabPosition()
		self.updateCabLocNow()
		self.progression += self.increment
		
	def updateCabLocNow (self):
		loc_now = {}
		location = {}
		loc_now["area"] = self.path[0]["area"]
		if self.progression >= 1 or self.progression == 0 :
			loc_now["locationType"] = "vertex"
			loc_now["location"] = self.path[0]["vertex"]
		else :
			loc_now["locationType"] = "street"
			location["from"] = self.path[0]["vertex"]
			if len(self.path) == 1 :
				location["to"] = self.endlocation["location"]["location"]["to"]
			else:
				location["to"] = self.path[1]["vertex"]
			location["progression"] = self.progression
			loc_now["location"] = location
			
		
		#print loc_now
		TaxiManager.taxiManager.taxiList[0].loc_now.onUpdateLocation(loc_now, MapManager.mapManager)
		TaxiManager.taxiManager.onCabInfo()
	
	def computeCabPosition (self):
		areaA = MapManager.mapManager.areasDict[self.path[0]["area"]]
		A = areaA.verticesDict[self.path[0]["vertex"]]
		if len(self.path) == 1 :
			if self.endlocation["location"]["locationType"] == "vertex":
				return
			areaB = MapManager.mapManager.areasDict[self.endlocation["location"]["area"]]
			B = areaB.verticesDict[self.endlocation["location"]["location"]["to"]]
		else:
			areaB = MapManager.mapManager.areasDict[self.path[1]["area"]]
			B = areaB.verticesDict[self.path[1]["vertex"]]
			
		if areaA.areaName == areaB.areaName:
			x = ((1-self.progression) * A.verticeX + self.progression * B.verticeX)
			y = ((1-self.progression) * A.verticeY + self.progression * B.verticeY)
		else:
			x = B.verticeX
			y = B.verticeY
			self.progression = 1
			print 'PONTTTTTTTTTTT'
			

		#print `self.progression` + " from: " + A.verticeName + "  to: " + B.verticeName
		#print " x: " + `x` + "  y: " + `y`
		