from Vertice import *




class Bridge:
	def __init__(self, vertex, bridgeJson):
		self.bridgeVertice = vertex
		bridgeTo = bridgeJson["to"]
		self.bridgeToArea = bridgeTo["area"]
		self.bridgeToVertex = bridgeTo["vertex"]
		self.bridgeWeight = bridgeJson["weight"]

	def ToJsonFormat(self):
		bridge = {}
		bridge["from"] = self.bridgeVertice.verticeName
		bridgeTo = {}
		bridgeTo["area"] = self.bridgeToArea
		bridgeTo["vertex"] = self.bridgeToVertex
		bridge["to"] = bridgeTo
		bridge["weight"] = self.bridgeWeight
		return bridge
